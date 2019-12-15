package com.kakaopay.ecosystem.util;

import java.util.Collection;

public class EcosystemUtil {

	public static boolean isNullOrEmpty(String value) {

		return value == null || value.isEmpty();
	}

	public static boolean isNullOrEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

}
