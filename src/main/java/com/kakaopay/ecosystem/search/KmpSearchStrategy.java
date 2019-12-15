package com.kakaopay.ecosystem.search;

public class KmpSearchStrategy implements SearchStrategy {

	@Override
	public boolean containsPattern(String str, String pattern) {
		return contains(str, pattern);
	}

	@Override
	public int countPattern(String str, String pattern) {
		return count(str, pattern);
	}

	private boolean contains(String str, String pattern) {

		int[] pi = getPi(pattern);
		int LenOfStr = str.length();
		int LenOfPattern = pattern.length();
		int j = 0;

		for (int i = 0; i < LenOfStr; i++) {
			while (j > 0 && str.charAt(i) != pattern.charAt(j)) {
				j = pi[j - 1];
			}
			if (str.charAt(i) == pattern.charAt(j)) {
				if (j == LenOfPattern - 1) {
					return true;
				} else {
					j++;
				}
			}
		}
		return false;
	}

	private int count(String str, String pattern) {

		int count = 0;
		int[] pi = getPi(pattern);
		int LenOfStr = str.length();
		int LenOfPattern = pattern.length();
		int j = 0;

		for (int i = 0; i < LenOfStr; i++) {
			while (j > 0 && str.charAt(i) != pattern.charAt(j)) {
				j = pi[j - 1];
			}
			if (str.charAt(i) == pattern.charAt(j)) {
				if (j == LenOfPattern - 1) {
					count++;
				} else {
					j++;
				}
			}
		}
		return count;
	}

	private int[] getPi(String pattern) {
		int LenOfPattern = pattern.length();
		int[] pi = new int[LenOfPattern];
		int j = 0;

		for (int i = 1; i < LenOfPattern; i++) {
			while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
				j = pi[j - 1];
			}
			if (pattern.charAt(i) == pattern.charAt(j)) {
				pi[i] = ++j;
			}
		}
		return pi;
	}

}
