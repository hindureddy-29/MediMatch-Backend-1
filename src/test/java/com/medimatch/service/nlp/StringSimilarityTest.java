package com.medimatch.service.nlp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringSimilarityTest {

    private StringSimilarity similarity;

    @BeforeEach
    void setUp() {
        similarity = new StringSimilarity();
    }

    @Test
    void testExactMatchGives100Percent() {
        assertEquals(100.0, similarity.levenshteinSimilarity("paracetamol", "paracetamol"));
        assertEquals(100.0, similarity.jaroWinklerSimilarity("paracetamol", "paracetamol"));
        assertEquals(100.0, similarity.hybridSimilarity("paracetamol", "paracetamol"));
    }

    @Test
    void testTypoLevenshteinSimilarity() {
        // "paracetmol" vs "paracetamol" (1 omission)
        double score = similarity.levenshteinSimilarity("paracetmol", "paracetamol");
        assertTrue(score > 85.0, "Score should be high for 1 letter typo: " + score);
    }

    @Test
    void testPrefixSimilarityWithJaroWinkler() {
        // "atorvastatin" vs "atorva"
        double score = similarity.jaroWinklerSimilarity("atorvastatin", "atorva");
        assertTrue(score > 75.0, "Prefix matching should be strongly captured by Jaro-Winkler: " + score);
    }
}
