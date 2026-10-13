package com.medimatch.service.nlp;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MedicineNormalizer {

    // Regex for dosage strengths like 500mg, 10 mg, 2.5mcg, 100iu, 500/125 mg, 5ml
    private static final Pattern STRENGTH_PATTERN = Pattern.compile(
            "\\b(\\d+(?:\\.\\d+)?(?:\\s*(?:/|-)\\s*\\d+(?:\\.\\d+)?)?\\s*(?:mg|mcg|g|ml|iu|meq|%))\\b",
            Pattern.CASE_INSENSITIVE
    );

    // Dosage forms keywords
    private static final Map<String, String> DOSAGE_FORMS = new LinkedHashMap<>();
    static {
        DOSAGE_FORMS.put("tablet", "Tablet");
        DOSAGE_FORMS.put("tablets", "Tablet");
        DOSAGE_FORMS.put("tab", "Tablet");
        DOSAGE_FORMS.put("capsule", "Capsule");
        DOSAGE_FORMS.put("capsules", "Capsule");
        DOSAGE_FORMS.put("cap", "Capsule");
        DOSAGE_FORMS.put("syrup", "Syrup");
        DOSAGE_FORMS.put("syr", "Syrup");
        DOSAGE_FORMS.put("suspension", "Suspension");
        DOSAGE_FORMS.put("susp", "Suspension");
        DOSAGE_FORMS.put("injection", "Injection");
        DOSAGE_FORMS.put("inj", "Injection");
        DOSAGE_FORMS.put("ointment", "Ointment");
        DOSAGE_FORMS.put("oint", "Ointment");
        DOSAGE_FORMS.put("cream", "Cream");
        DOSAGE_FORMS.put("inhaler", "Inhaler");
        DOSAGE_FORMS.put("drops", "Drops");
        DOSAGE_FORMS.put("drop", "Drops");
        DOSAGE_FORMS.put("gel", "Gel");
        DOSAGE_FORMS.put("solution", "Solution");
        DOSAGE_FORMS.put("infusion", "Infusion");
        DOSAGE_FORMS.put("patch", "Patch");
    }

    // Common pharmaceutical synonyms / aliases
    private static final Map<String, String> INGREDIENT_SYNONYMS = new HashMap<>();
    static {
        INGREDIENT_SYNONYMS.put("acetaminophen", "paracetamol");
        INGREDIENT_SYNONYMS.put("tylenol", "paracetamol");
        INGREDIENT_SYNONYMS.put("salbutamol", "albuterol");
        INGREDIENT_SYNONYMS.put("frusemide", "furosemide");
        INGREDIENT_SYNONYMS.put("amoxycillin", "amoxicillin");
        INGREDIENT_SYNONYMS.put("clavulanate", "clavulanic acid");
        INGREDIENT_SYNONYMS.put("potassium clavulanate", "clavulanic acid");
    }

    public static class NormalizedResult {
        private String original;
        private String cleanName;
        private String extractedStrength;
        private String extractedDosageForm;
        private Set<String> tokens;

        public NormalizedResult(String original, String cleanName, String extractedStrength,
                                String extractedDosageForm, Set<String> tokens) {
            this.original = original;
            this.cleanName = cleanName;
            this.extractedStrength = extractedStrength;
            this.extractedDosageForm = extractedDosageForm;
            this.tokens = tokens;
        }

        public String getOriginal() { return original; }
        public String getCleanName() { return cleanName; }
        public String getExtractedStrength() { return extractedStrength; }
        public String getExtractedDosageForm() { return extractedDosageForm; }
        public Set<String> getTokens() { return tokens; }
    }

    public NormalizedResult normalize(String rawMedicineName) {
        if (rawMedicineName == null || rawMedicineName.trim().isEmpty()) {
            return new NormalizedResult("", "", "", "", Collections.emptySet());
        }

        String original = rawMedicineName.trim();
        String text = original;

        // 1. Remove trademarks and special symbols
        text = text.replaceAll("[®™©@#$%^*+=\\[\\]{}|;:\"]", " ");

        // 2. Extract strength
        String extractedStrength = "";
        Matcher strengthMatcher = STRENGTH_PATTERN.matcher(text);
        if (strengthMatcher.find()) {
            extractedStrength = strengthMatcher.group(1).trim().toLowerCase().replaceAll("\\s+", "");
            // Replace strength in text with space to isolate drug name
            text = strengthMatcher.replaceFirst(" ");
        }

        // 3. Extract dosage form
        String extractedDosageForm = "";
        String[] words = text.toLowerCase().split("[^a-zA-Z0-9]+");
        StringBuilder nameWithoutForm = new StringBuilder();

        for (String w : words) {
            if (w.isEmpty()) continue;
            if (DOSAGE_FORMS.containsKey(w) && extractedDosageForm.isEmpty()) {
                extractedDosageForm = DOSAGE_FORMS.get(w);
            } else if (w.equals("sr") || w.equals("er") || w.equals("xr") || w.equals("cr") || w.equals("ds") || w.equals("forte")) {
                // Release mechanisms or suffixes
                if (extractedDosageForm.isEmpty()) {
                    extractedDosageForm = w.toUpperCase() + " Form";
                }
            } else {
                if (nameWithoutForm.length() > 0) nameWithoutForm.append(" ");
                nameWithoutForm.append(w);
            }
        }

        String cleanName = nameWithoutForm.toString().trim();
        if (cleanName.isEmpty()) {
            // fallback if entire string was dosage or form
            cleanName = original.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").trim();
        }

        // Token set for token-level comparison
        Set<String> tokens = new HashSet<>(Arrays.asList(cleanName.split("\\s+")));

        return new NormalizedResult(original, cleanName, extractedStrength, extractedDosageForm, tokens);
    }

    public String canonicalizeIngredient(String ingredient) {
        if (ingredient == null) return "";
        String lower = ingredient.toLowerCase().trim();
        // remove salts and pharmacopeia standards (e.g. "bp", "usp", "ip", "ep", "sulphate", "sulfate")
        lower = lower.replaceAll("\\b(hydrochloride|hcl|sodium|potassium|maleate|succinate|mesylate|tartrate|besylate|calcium|hydrate|sulphate|sulfate|bp|usp|ip|ep)\\b", "")
                     .replaceAll("[^a-zA-Z0-9+ /]", " ")
                     .replaceAll("\\s+", " ")
                     .trim();

        // check known synonyms
        for (Map.Entry<String, String> entry : INGREDIENT_SYNONYMS.entrySet()) {
            if (lower.contains(entry.getKey())) {
                lower = lower.replace(entry.getKey(), entry.getValue());
            }
        }
        return lower;
    }
}
