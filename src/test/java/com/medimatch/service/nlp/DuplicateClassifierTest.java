package com.medimatch.service.nlp;

import com.medimatch.dto.DuplicateMatchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DuplicateClassifierTest {

    private DuplicateClassifier classifier;

    @BeforeEach
    void setUp() {
        MedicineNormalizer normalizer = new MedicineNormalizer();
        StringSimilarity stringSimilarity = new StringSimilarity();
        classifier = new DuplicateClassifier(normalizer, stringSimilarity);
    }

    @Test
    void testIngredientDuplicateClassification() {
        // Tylenol vs Panadol (both Paracetamol)
        DuplicateClassifier.MatchEvaluation eval = new DuplicateClassifier.MatchEvaluation();
        eval.setOriginalName("Tylenol");
        eval.setOriginalCleanName("tylenol");
        eval.setOriginalIngredient("Paracetamol");
        eval.setOriginalStrength("500 mg");
        eval.setOriginalDosageForm("Tablet");

        eval.setTargetName("Panadol");
        eval.setTargetCleanName("panadol");
        eval.setTargetIngredient("Paracetamol");
        eval.setTargetStrength("500 mg");
        eval.setTargetDosageForm("Tablet");
        eval.setTargetVerified(true);

        DuplicateMatchDto result = classifier.evaluate(eval);
        assertEquals("Possible ingredient duplicate", result.getMatchClassification());
        assertEquals("VERIFIED", result.getVerificationStatus());
        assertTrue(result.getSimilarityScore() >= 85.0);
    }

    @Test
    void testExactNameMatch() {
        DuplicateClassifier.MatchEvaluation eval = new DuplicateClassifier.MatchEvaluation();
        eval.setOriginalName("Dolo 650");
        eval.setOriginalCleanName("dolo");
        eval.setOriginalIngredient("Paracetamol");
        eval.setOriginalStrength("650 mg");

        eval.setTargetName("Dolo 650");
        eval.setTargetCleanName("dolo");
        eval.setTargetIngredient("Paracetamol");
        eval.setTargetStrength("650 mg");
        eval.setTargetVerified(true);

        DuplicateMatchDto result = classifier.evaluate(eval);
        assertEquals("Exact name match", result.getMatchClassification());
        assertEquals(100.0, result.getSimilarityScore());
    }

    @Test
    void testUncertainMatchForLookAlikeDifferentIngredients() {
        // Similar sounding names but different active ingredients
        DuplicateClassifier.MatchEvaluation eval = new DuplicateClassifier.MatchEvaluation();
        eval.setOriginalName("Celebrex");
        eval.setOriginalCleanName("celebrex");
        eval.setOriginalIngredient("Celecoxib");

        eval.setTargetName("Celexa");
        eval.setTargetCleanName("celexa");
        eval.setTargetIngredient("Citalopram");
        eval.setTargetVerified(true);

        DuplicateMatchDto result = classifier.evaluate(eval);
        assertEquals("Uncertain match", result.getMatchClassification());
        assertEquals("POTENTIAL_RISK", result.getVerificationStatus());
    }
}
