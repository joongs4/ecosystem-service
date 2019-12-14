package com.kakaopay.ecosystem.search;

public interface SearchStrategy {

	boolean containsPattern(String str, String pattern);

	int countPattern(String str, String pattern);

}
