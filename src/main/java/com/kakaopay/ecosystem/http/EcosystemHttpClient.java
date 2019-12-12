package com.kakaopay.ecosystem.http;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class EcosystemHttpClient {

	public static <T> T get(String url, Class<T> responseType, Map<String, ?> variables,
			ClientHttpRequestInterceptor... interceptors) {

		RestTemplate restTemplate = new RestTemplate();

		if (interceptors != null) {
			restTemplate.setInterceptors(Arrays.asList(interceptors));
		}

		
		ResponseEntity<T> retVal = restTemplate.getForEntity(url, responseType);
		if (retVal.getStatusCodeValue() < 200 || retVal.getStatusCodeValue() >= 400) {
			throw new RuntimeException(
					"Http Get Request Exception : " + retVal.getStatusCode() + ", targetUrl : " + url);
		}

		return retVal.getBody();
	}
}
