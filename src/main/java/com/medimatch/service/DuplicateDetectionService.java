package com.medimatch.service;

import com.medimatch.dto.AnalysisRequest;
import com.medimatch.dto.AnalysisResponse;
import com.medimatch.dto.DuplicateMatchDto;
import com.medimatch.dto.MedicineInputItem;
import com.medimatch.entity.AnalysisMatchResult;
import com.medimatch.entity.AnalysisSession;
import com.medimatch.entity.Medicine;
import com.medimatch.repository.AnalysisSessionRepository;
import com.medimatch.repository.MedicineRepository;
import com.medimatch.service.nlp.DuplicateClassifier;
import com.medimatch.service.nlp.MedicineNormalizer;
import com.medimatch.service.nlp.StringSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DuplicateDetectionService {

    private final MedicineRepository medicineRepository;
    private final AnalysisSessionRepository sessionRepository;
    private final MedicineNormalizer normalizer;
    private final StringSimilarity stringSimilarity;
    private final DuplicateClassifier classifier;

    @Autowired
    public DuplicateDetectionService(MedicineRepository medicineRepository,
                                     AnalysisSessionRepository sessionRepository,
                                     MedicineNormalizer normalizer,
                                     StringSimilarity stringSimilarity,
                                     DuplicateClassifier classifier) {
        this.medicineRepository = medicineRepository;
        this.sessionRepository = sessionRepository;
        this.normalizer = normalizer;
        this.stringSimilarity = stringSimilarity;
        this.classifier = classifier;
    }

    public AnalysisResponse analyzeMedicines(AnalysisRequest request, Long userId, String username) {
        long startTime = System.currentTimeMillis();

        List<MedicineInputItem> parsedItems = parseAndNormalizeInputs(request);
        if (parsedItems.isEmpty()) {
            AnalysisResponse emptyResponse = new AnalysisResponse();
            emptyResponse.setSessionTitle("Empty Analysis");
            emptyResponse.setExecutionTimeMs(System.currentTimeMillis() - startTime);
            return emptyResponse;
        }

        // Load catalog medicines for verification and ingredient lookup
        List<Medicine> catalog = medicineRepository.findAll();

        // Enrich parsed inputs with catalog knowledge where available
        enrichInputsWithCatalog(parsedItems, catalog);

        List<DuplicateMatchDto> allMatches = new ArrayList<>();
        Set<String> processedPairs = new HashSet<>();

        // 1. Inter-list pairwise comparison (detect duplicate therapies in patient's list)
        if (parsedItems.size() > 1) {
            for (int i = 0; i < parsedItems.size(); i++) {
                for (int j = i + 1; j < parsedItems.size(); j++) {
                    MedicineInputItem itemA = parsedItems.get(i);
                    MedicineInputItem itemB = parsedItems.get(j);

                    MedicineNormalizer.NormalizedResult normA = normalizer.normalize(itemA.getName());
                    MedicineNormalizer.NormalizedResult normB = normalizer.normalize(itemB.getName());

                    DuplicateClassifier.MatchEvaluation eval = new DuplicateClassifier.MatchEvaluation();
                    eval.setOriginalName(itemA.getName());
                    eval.setOriginalCleanName(normA.getCleanName());
                    eval.setOriginalStrength(!normA.getExtractedStrength().isEmpty() ? normA.getExtractedStrength() : itemA.getStrength());
                    eval.setOriginalDosageForm(!normA.getExtractedDosageForm().isEmpty() ? normA.getExtractedDosageForm() : itemA.getDosageForm());
                    eval.setOriginalIngredient(itemA.getActiveIngredient());
                    eval.setOriginalBrand(itemA.getBrandName());

                    eval.setTargetName(itemB.getName());
                    eval.setTargetCleanName(normB.getCleanName());
                    eval.setTargetStrength(!normB.getExtractedStrength().isEmpty() ? normB.getExtractedStrength() : itemB.getStrength());
                    eval.setTargetDosageForm(!normB.getExtractedDosageForm().isEmpty() ? normB.getExtractedDosageForm() : itemB.getDosageForm());
                    eval.setTargetIngredient(itemB.getActiveIngredient());
                    eval.setTargetBrand(itemB.getBrandName());
                    eval.setTargetVerified(itemB.getActiveIngredient() != null && !itemB.getActiveIngredient().isEmpty());
                    eval.setSource("LIST_INTERNAL");

                    DuplicateMatchDto match = classifier.evaluate(eval);
                    if (isRelevantMatch(match)) {
                        String pairKey = generatePairKey(itemA.getName(), itemB.getName());
                        if (processedPairs.add(pairKey)) {
                            allMatches.add(match);
                        }
                    }
                }
            }
        }

        // 2. Catalog / Database lookup (detect brand-to-generic & known equivalents in verified database)
        if (Boolean.TRUE.equals(request.getCompareWithDatabase())) {
            for (MedicineInputItem input : parsedItems) {
                MedicineNormalizer.NormalizedResult normInput = normalizer.normalize(input.getName());

                for (Medicine dbMed : catalog) {
                    // Skip if names are identical and source is self
                    if (input.getName().equalsIgnoreCase(dbMed.getBrandName()) && parsedItems.size() > 1) {
                        // Still check for verified info
                    }

                    DuplicateClassifier.MatchEvaluation eval = new DuplicateClassifier.MatchEvaluation();
                    eval.setOriginalName(input.getName());
                    eval.setOriginalCleanName(normInput.getCleanName());
                    eval.setOriginalStrength(!normInput.getExtractedStrength().isEmpty() ? normInput.getExtractedStrength() : input.getStrength());
                    eval.setOriginalDosageForm(!normInput.getExtractedDosageForm().isEmpty() ? normInput.getExtractedDosageForm() : input.getDosageForm());
                    eval.setOriginalIngredient(input.getActiveIngredient());
                    eval.setOriginalBrand(input.getBrandName());

                    MedicineNormalizer.NormalizedResult normDb = normalizer.normalize(dbMed.getBrandName());
                    eval.setTargetName(dbMed.getBrandName() + " (" + dbMed.getGenericName() + ")");
                    eval.setTargetCleanName(normDb.getCleanName());
                    eval.setTargetStrength(dbMed.getStrength());
                    eval.setTargetDosageForm(dbMed.getDosageForm());
                    eval.setTargetIngredient(dbMed.getActiveIngredient());
                    eval.setTargetBrand(dbMed.getBrandName());
                    eval.setTargetVerified(dbMed.getVerified());
                    eval.setSource("DATABASE_CATALOG");

                    DuplicateMatchDto match = classifier.evaluate(eval);
                    if (isRelevantMatch(match)) {
                        String pairKey = generatePairKey(input.getName(), dbMed.getBrandName());
                        if (processedPairs.add(pairKey)) {
                            allMatches.add(match);
                        }
                    }
                }
            }
        }

        // Sort matches by highest similarity score first
        allMatches.sort((m1, m2) -> Double.compare(m2.getSimilarityScore(), m1.getSimilarityScore()));

        // Count duplicates and uncertain matches
        int duplicatesCount = 0;
        int uncertainCount = 0;
        for (DuplicateMatchDto m : allMatches) {
            String cls = m.getMatchClassification();
            if ("Exact name match".equalsIgnoreCase(cls) || "Possible ingredient duplicate".equalsIgnoreCase(cls) || "Possible name match".equalsIgnoreCase(cls)) {
                duplicatesCount++;
            } else if ("Uncertain match".equalsIgnoreCase(cls)) {
                uncertainCount++;
            }
        }

        // Build Response
        AnalysisResponse response = new AnalysisResponse();
        response.setTotalInputMedicines(parsedItems.size());
        response.setTotalDuplicatesFound(duplicatesCount);
        response.setTotalUncertainMatches(uncertainCount);
        response.setMatches(allMatches);
        response.setAnalyzedItems(parsedItems);

        String title = request.getSessionTitle() != null && !request.getSessionTitle().trim().isEmpty()
                ? request.getSessionTitle().trim()
                : "Analysis Session (" + parsedItems.size() + " medicines) - " + LocalDateTime.now().toLocalDate();
        response.setSessionTitle(title);

        // Save to Database History if requested
        if (Boolean.TRUE.equals(request.getSaveToHistory())) {
            AnalysisSession session = new AnalysisSession(
                    title,
                    userId,
                    username != null ? username : "Guest User",
                    parsedItems.size(),
                    duplicatesCount,
                    uncertainCount
            );

            for (DuplicateMatchDto dto : allMatches) {
                AnalysisMatchResult entity = new AnalysisMatchResult(
                        session,
                        dto.getOriginalMedicineName(),
                        dto.getMatchedMedicineName(),
                        dto.getBrandName(),
                        dto.getActiveIngredient(),
                        dto.getStrength(),
                        dto.getDosageForm(),
                        dto.getSimilarityScore(),
                        dto.getMatchClassification(),
                        dto.getReason(),
                        dto.getVerificationStatus(),
                        dto.getClinicalNotice()
                );
                session.addResult(entity);
            }

            AnalysisSession savedSession = sessionRepository.save(session);
            response.setSessionId(savedSession.getId());

            // map back generated IDs
            List<AnalysisMatchResult> savedResults = savedSession.getResults();
            for (int k = 0; k < Math.min(allMatches.size(), savedResults.size()); k++) {
                allMatches.get(k).setId(savedResults.get(k).getId());
            }
        }

        response.setExecutionTimeMs(System.currentTimeMillis() - startTime);
        return response;
    }

    private boolean isRelevantMatch(DuplicateMatchDto match) {
        if (match == null) return false;
        String cls = match.getMatchClassification();
        if ("Exact name match".equals(cls) || "Possible ingredient duplicate".equals(cls)) {
            return true;
        }
        if ("Possible name match".equals(cls) && match.getSimilarityScore() >= 80.0) {
            return true;
        }
        if ("Uncertain match".equals(cls) && match.getSimilarityScore() >= 65.0) {
            return true;
        }
        return false;
    }

    private String generatePairKey(String nameA, String nameB) {
        String a = nameA.toLowerCase().trim();
        String b = nameB.toLowerCase().trim();
        return a.compareTo(b) < 0 ? (a + ":::" + b) : (b + ":::" + a);
    }

    private List<MedicineInputItem> parseAndNormalizeInputs(AnalysisRequest request) {
        List<MedicineInputItem> list = new ArrayList<>();

        // Structured items
        if (request.getItems() != null) {
            for (MedicineInputItem item : request.getItems()) {
                if (item.getName() != null && !item.getName().trim().isEmpty()) {
                    list.add(item);
                }
            }
        }

        // Raw text lines
        if (request.getRawText() != null && !request.getRawText().trim().isEmpty()) {
            String[] lines = request.getRawText().split("[\\r\\n;,]+");
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    // Check if already in list
                    boolean alreadyPresent = list.stream().anyMatch(i -> i.getName().equalsIgnoreCase(trimmed));
                    if (!alreadyPresent) {
                        list.add(new MedicineInputItem(trimmed));
                    }
                }
            }
        }

        return list;
    }

    private void enrichInputsWithCatalog(List<MedicineInputItem> items, List<Medicine> catalog) {
        for (MedicineInputItem item : items) {
            MedicineNormalizer.NormalizedResult norm = normalizer.normalize(item.getName());

            // If item doesn't have ingredient, try finding exact or very close catalog match
            if (item.getActiveIngredient() == null || item.getActiveIngredient().trim().isEmpty()) {
                Medicine bestMatch = null;
                double bestSim = 0.0;

                for (Medicine dbMed : catalog) {
                    MedicineNormalizer.NormalizedResult dbNorm = normalizer.normalize(dbMed.getBrandName());
                    double sim = stringSimilarity.hybridSimilarity(norm.getCleanName(), dbNorm.getCleanName());
                    if (sim > bestSim && sim >= 85.0) {
                        bestSim = sim;
                        bestMatch = dbMed;
                    }

                    // Check generic name match
                    MedicineNormalizer.NormalizedResult dbGenNorm = normalizer.normalize(dbMed.getGenericName());
                    double genSim = stringSimilarity.hybridSimilarity(norm.getCleanName(), dbGenNorm.getCleanName());
                    if (genSim > bestSim && genSim >= 85.0) {
                        bestSim = genSim;
                        bestMatch = dbMed;
                    }
                }

                if (bestMatch != null) {
                    item.setActiveIngredient(bestMatch.getActiveIngredient());
                    if (item.getBrandName() == null || item.getBrandName().trim().isEmpty()) {
                        item.setBrandName(bestMatch.getBrandName());
                    }
                    if ((item.getStrength() == null || item.getStrength().isEmpty()) && bestMatch.getStrength() != null) {
                        item.setStrength(bestMatch.getStrength());
                    }
                    if ((item.getDosageForm() == null || item.getDosageForm().isEmpty()) && bestMatch.getDosageForm() != null) {
                        item.setDosageForm(bestMatch.getDosageForm());
                    }
                }
            }
        }
    }
}
