package com.medimatch.service.nlp;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class StringSimilarity {

    /**
     * Compute Levenshtein distance between two strings.
     */
    public int levenshteinDistance(String s1, String s2) {
        if (s1 == null || s2 == null) return 0;
        int len1 = s1.length();
        int len2 = s2.length();

        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) dp[i][0] = i;
        for (int j = 0; j <= len2; j++) dp[0][j] = j;

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[len1][len2];
    }

    /**
     * Normalized Levenshtein similarity as percentage (0.0 to 100.0).
     */
    public double levenshteinSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 100.0;
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 100.0;
        int dist = levenshteinDistance(s1, s2);
        double sim = (1.0 - ((double) dist / maxLen)) * 100.0;
        return Math.max(0.0, Math.min(100.0, sim));
    }

    /**
     * Jaro-Winkler similarity (0.0 to 100.0).
     * Highly effective for medical and brand names sharing prefixes.
     */
    public double jaroWinklerSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 100.0;

        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == 0 || len2 == 0) return 0.0;

        int matchDistance = Math.max(len1, len2) / 2 - 1;
        if (matchDistance < 0) matchDistance = 0;

        boolean[] s1Matches = new boolean[len1];
        boolean[] s2Matches = new boolean[len2];

        int matches = 0;
        for (int i = 0; i < len1; i++) {
            int start = Math.max(0, i - matchDistance);
            int end = Math.min(i + matchDistance + 1, len2);
            for (int j = start; j < end; j++) {
                if (s2Matches[j]) continue;
                if (s1.charAt(i) != s2.charAt(j)) continue;
                s1Matches[i] = true;
                s2Matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0) return 0.0;

        double transpositions = 0;
        int k = 0;
        for (int i = 0; i < len1; i++) {
            if (!s1Matches[i]) continue;
            while (!s2Matches[k]) k++;
            if (s1.charAt(i) != s2.charAt(k)) {
                transpositions++;
            }
            k++;
        }
        transpositions /= 2.0;

        double jaro = ((matches / (double) len1) +
                       (matches / (double) len2) +
                       ((matches - transpositions) / (double) matches)) / 3.0;

        // Winkler prefix scaling (up to 4 chars)
        int prefix = 0;
        int maxPrefix = Math.min(4, Math.min(len1, len2));
        for (int i = 0; i < maxPrefix; i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                prefix++;
            } else {
                break;
            }
        }

        double jaroWinkler = jaro + (prefix * 0.1 * (1.0 - jaro));
        return Math.max(0.0, Math.min(100.0, jaroWinkler * 100.0));
    }

    /**
     * Jaccard similarity over word tokens (0.0 to 100.0).
     */
    public double jaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1 == null || set2 == null || (set1.isEmpty() && set2.isEmpty())) return 0.0;
        if (set1.isEmpty() || set2.isEmpty()) return 0.0;

        int intersection = 0;
        for (String s : set1) {
            if (set2.contains(s)) intersection++;
        }

        int union = set1.size() + set2.size() - intersection;
        if (union == 0) return 100.0;
        return ((double) intersection / union) * 100.0;
    }

    /**
     * Hybrid similarity combining Jaro-Winkler and Levenshtein with high precision.
     */
    public double hybridSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        String a = s1.trim().toLowerCase();
        String b = s2.trim().toLowerCase();
        if (a.isEmpty() && b.isEmpty()) return 100.0;
        if (a.isEmpty() || b.isEmpty()) return 0.0;
        if (a.equals(b)) return 100.0;

        double jw = jaroWinklerSimilarity(a, b);
        double lev = levenshteinSimilarity(a, b);

        // 60% Jaro-Winkler (better prefix/stem sensitivity for medical names), 40% Levenshtein
        double hybrid = (0.60 * jw) + (0.40 * lev);
        return Math.round(hybrid * 10.0) / 10.0;
    }
}
