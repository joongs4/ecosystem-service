package com.kakaopay.ecosystem.util;

import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ecosystem.exception.ExceptionResponse;

public class EcosystemUtil {

	public static boolean isNullOrEmpty(String value) {

		return value == null || value.isEmpty();
	}

	public static boolean isNullOrEmpty(Collection collection) {
		return collection == null || collection.isEmpty();
	}

	public static String generateGuid() {
		String retVal = java.util.UUID.randomUUID().toString();

		return retVal;
	}

	public static String convertExceptionToJson(ExceptionResponse exceptionResponse) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(exceptionResponse);
		} catch (JsonProcessingException e) {
		}
		return null;
	}

}
