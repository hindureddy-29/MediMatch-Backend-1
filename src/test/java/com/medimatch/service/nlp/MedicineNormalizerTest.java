package com.medimatch.service.nlp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicineNormalizerTest {

    private MedicineNormalizer normalizer;

    @BeforeEach
    void setUp() {
        normalizer = new MedicineNormalizer();
    }

    @Test
    void testNormalizeExtractsStrengthAndDosageForm() {
        MedicineNormalizer.NormalizedResult res = normalizer.normalize("Tylenol 500mg Tablet");
        assertEquals("tylenol", res.getCleanName());
        assertEquals("500mg", res.getExtractedStrength());
        assertEquals("Tablet", res.getExtractedDosageForm());
    }

    @Test
    void testNormalizeRemovesTrademarksAndPunctuation() {
        MedicineNormalizer.NormalizedResult res = normalizer.normalize("Lipitor® 20 mg Tab.");
        assertEquals("lipitor", res.getCleanName());
        assertEquals("20mg", res.getExtractedStrength());
        assertEquals("Tablet", res.getExtractedDosageForm());
    }

    @Test
    void testCanonicalizeIngredient() {
        assertEquals("paracetamol", normalizer.canonicalizeIngredient("Acetaminophen"));
        assertEquals("paracetamol", normalizer.canonicalizeIngredient("Paracetamol BP"));
        assertEquals("albuterol", normalizer.canonicalizeIngredient("Salbutamol Sulphate"));
    }
}
