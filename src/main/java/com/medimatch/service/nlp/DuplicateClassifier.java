package com.medimatch.service.nlp;

import com.medimatch.dto.DuplicateMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DuplicateClassifier {

    private final MedicineNormalizer normalizer;
    private final StringSimilarity stringSimilarity;

    @Autowired
    public DuplicateClassifier(MedicineNormalizer normalizer, StringSimilarity stringSimilarity) {
        this.normalizer = normalizer;
        this.stringSimilarity = stringSimilarity;
    }

    public static class MatchEvaluation {
        private String originalName;
        private String originalCleanName;
        private String originalStrength;
        private String originalDosageForm;
        private String originalIngredient;
        private String originalBrand;

        private String targetName;
        private String targetCleanName;
        private String targetStrength;
        private String targetDosageForm;
        private String targetIngredient;
        private String targetBrand;
        private boolean targetVerified = true;
        private String source = "DATABASE_CATALOG";

        public MatchEvaluation() {}

        public String getOriginalName() { return originalName; }
        public void setOriginalName(String originalName) { this.originalName = originalName; }
        public String getOriginalCleanName() { return originalCleanName; }
        public void setOriginalCleanName(String originalCleanName) { this.originalCleanName = originalCleanName; }
        public String getOriginalStrength() { return originalStrength; }
        public void setOriginalStrength(String originalStrength) { this.originalStrength = originalStrength; }
        public String getOriginalDosageForm() { return originalDosageForm; }
        public void setOriginalDosageForm(String originalDosageForm) { this.originalDosageForm = originalDosageForm; }
        public String getOriginalIngredient() { return originalIngredient; }
        public void setOriginalIngredient(String originalIngredient) { this.originalIngredient = originalIngredient; }
        public String getOriginalBrand() { return originalBrand; }
        public void setOriginalBrand(String originalBrand) { this.originalBrand = originalBrand; }

        public String getTargetName() { return targetName; }
        public void setTargetName(String targetName) { this.targetName = targetName; }
        public String getTargetCleanName() { return targetCleanName; }
        public void setTargetCleanName(String targetCleanName) { this.targetCleanName = targetCleanName; }
        public String getTargetStrength() { return targetStrength; }
        public void setTargetStrength(String targetStrength) { this.targetStrength = targetStrength; }
        public String getTargetDosageForm() { return targetDosageForm; }
        public void setTargetDosageForm(String targetDosageForm) { this.targetDosageForm = targetDosageForm; }
        public String getTargetIngredient() { return targetIngredient; }
        public void setTargetIngredient(String targetIngredient) { this.targetIngredient = targetIngredient; }
        public String getTargetBrand() { return targetBrand; }
        public void setTargetBrand(String targetBrand) { this.targetBrand = targetBrand; }
        public boolean isTargetVerified() { return targetVerified; }
        public void setTargetVerified(boolean targetVerified) { this.targetVerified = targetVerified; }
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }

    public DuplicateMatchDto evaluate(MatchEvaluation eval) {
        DuplicateMatchDto dto = new DuplicateMatchDto();
        dto.setOriginalMedicineName(eval.getOriginalName());
        dto.setStandardizedMedicineName(eval.getOriginalCleanName());
        dto.setMatchedMedicineName(eval.getTargetName());
        dto.setStandardizedMatchedName(eval.getTargetCleanName());
        dto.setBrandName(eval.getTargetBrand() != null && !eval.getTargetBrand().isEmpty() ? eval.getTargetBrand() : eval.getTargetName());
        dto.setMatchSource(eval.getSource());

        // Resolve strengths and forms
        String str1 = (eval.getOriginalStrength() != null && !eval.getOriginalStrength().isEmpty())
                ? eval.getOriginalStrength() : "";
        String str2 = (eval.getTargetStrength() != null && !eval.getTargetStrength().isEmpty())
                ? eval.getTargetStrength() : "";
        String effectiveStrength = !str2.isEmpty() ? str2 : str1;
        dto.setStrength(effectiveStrength.isEmpty() ? "Unspecified" : effectiveStrength);

        String form1 = (eval.getOriginalDosageForm() != null && !eval.getOriginalDosageForm().isEmpty())
                ? eval.getOriginalDosageForm() : "";
        String form2 = (eval.getTargetDosageForm() != null && !eval.getTargetDosageForm().isEmpty())
                ? eval.getTargetDosageForm() : "";
        String effectiveForm = !form2.isEmpty() ? form2 : form1;
        dto.setDosageForm(effectiveForm.isEmpty() ? "Oral" : effectiveForm);

        // Calculate lexical similarity between standardized names
        double nameSim = stringSimilarity.hybridSimilarity(eval.getOriginalCleanName(), eval.getTargetCleanName());

        String canonIng1 = normalizer.canonicalizeIngredient(eval.getOriginalIngredient());
        String canonIng2 = normalizer.canonicalizeIngredient(eval.getTargetIngredient());

        boolean hasIng1 = !canonIng1.isEmpty();
        boolean hasIng2 = !canonIng2.isEmpty();

        dto.setActiveIngredient(hasIng2 ? eval.getTargetIngredient() : (hasIng1 ? eval.getOriginalIngredient() : "Unconfirmed"));

        // Case 1: Both active ingredients are known
        if (hasIng1 && hasIng2) {
            boolean ingredientMatch = canonIng1.equals(canonIng2) ||
                    canonIng1.contains(canonIng2) || canonIng2.contains(canonIng1);

            if (ingredientMatch) {
                // Verified duplicate active ingredient!
                dto.setVerificationStatus("VERIFIED");

                boolean exactName = eval.getOriginalCleanName().equalsIgnoreCase(eval.getTargetCleanName());
                if (exactName) {
                    dto.setMatchClassification("Exact name match");
                    dto.setSimilarityScore(100.0);
                    dto.setReason("Identical medicine name and verified active ingredient ('" + eval.getTargetIngredient() + "').");
                } else {
                    dto.setMatchClassification("Possible ingredient duplicate");
                    // Score based on ingredient match and name similarity
                    double score = Math.max(88.0, Math.min(99.0, 85.0 + (nameSim * 0.14)));
                    dto.setSimilarityScore(Math.round(score * 10.0) / 10.0);
                    dto.setReason("Different brand or commercial names sharing the same verified active ingredient ('" + eval.getTargetIngredient() + "').");
                }

                // Append clinical strength / form analysis
                StringBuilder clinicalNotice = new StringBuilder();
                if (!str1.isEmpty() && !str2.isEmpty() && !str1.equalsIgnoreCase(str2)) {
                    clinicalNotice.append("Dosage Alert: Identified differing strengths (").append(str1).append(" vs ").append(str2)
                            .append("). Concurrent administration may cause active ingredient overdose. ");
                } else {
                    clinicalNotice.append("High duplicate risk: Taking both products concurrently may result in toxic duplicate dosage. ");
                }

                if (!form1.isEmpty() && !form2.isEmpty() && !form1.equalsIgnoreCase(form2)) {
                    clinicalNotice.append("Form variation (").append(form1).append(" vs ").append(form2).append("): Differing pharmacokinetic delivery mechanisms. ");
                }
                clinicalNotice.append("Pharmacist review recommended before concurrent therapy.");
                dto.setClinicalNotice(clinicalNotice.toString());
                return dto;
            } else {
                // Ingredients differ, but names might be LASA (Look-Alike / Sound-Alike)
                if (nameSim >= 70.0) {
                    dto.setMatchClassification("Uncertain match");
                    dto.setSimilarityScore(nameSim);
                    dto.setVerificationStatus("POTENTIAL_RISK");
                    dto.setReason("High acoustic or spelling similarity (" + nameSim + "%), but active ingredients differ ('"
                            + eval.getOriginalIngredient() + "' vs '" + eval.getTargetIngredient() + "'). Potential Look-Alike / Sound-Alike (LASA) safety hazard.");
                    dto.setClinicalNotice("Dispensing/Prescription caution: These medications possess similar names but treat different medical conditions.");
                    return dto;
                }
            }
        }

        // Case 2: One or both ingredients unknown, rely on string similarity & catalog verification
        if (nameSim >= 95.0) {
            dto.setSimilarityScore(nameSim);
            if (eval.isTargetVerified()) {
                dto.setMatchClassification("Exact name match");
                dto.setVerificationStatus("VERIFIED");
                dto.setReason("Normalized name matches verified pharmaceutical entry ('" + eval.getTargetName() + "') with 95%+ confidence.");
                dto.setClinicalNotice("High probability of duplicate therapy. Verify current prescription record.");
            } else {
                dto.setMatchClassification("Possible name match");
                dto.setVerificationStatus("UNCONFIRMED");
                dto.setReason("Normalized names are nearly identical, but ingredient mapping requires professional verification.");
                dto.setClinicalNotice("Ingredients unconfirmed in local catalog. Professional verification required.");
            }
            return dto;
        }

        if (nameSim >= 80.0) {
            dto.setSimilarityScore(nameSim);
            dto.setMatchClassification("Possible name match");
            dto.setVerificationStatus(eval.isTargetVerified() ? "VERIFIED" : "UNCONFIRMED");
            dto.setReason("Significant spelling or formatting similarity (" + nameSim + "%). May represent brand misspelling, abbreviation, or formulation variant.");
            dto.setClinicalNotice("Potential duplicate medication due to typographical or phonetic proximity. Review clinical packaging.");
            return dto;
        }

        if (nameSim >= 60.0) {
            dto.setSimilarityScore(nameSim);
            dto.setMatchClassification("Uncertain match");
            dto.setVerificationStatus("UNCONFIRMED");
            dto.setReason("Moderate textual overlap (" + nameSim + "%). Formulation, active chemical, and clinical equivalency remain unconfirmed.");
            dto.setClinicalNotice("Uncertain duplicate. Manual inspection by qualified medical staff required.");
            return dto;
        }

        // Below 60% similarity and no matching ingredient
        dto.setSimilarityScore(nameSim);
        dto.setMatchClassification("Insufficient medicine information");
        dto.setVerificationStatus("UNCONFIRMED");
        dto.setReason("Low similarity (" + nameSim + "%) and insufficient verifiable pharmacological records to confirm duplicate status.");
        dto.setClinicalNotice("Unverified medicine identity. Consult medical provider or drug index.");
        return dto;
    }
}
