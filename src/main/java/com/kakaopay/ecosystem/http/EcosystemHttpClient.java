package com.kakaopay.ecosystem.http;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.kakaopay.ecosystem.exception.InternalServerException;

public class EcosystemHttpClient {

	private static final RestTemplate restTemplate = new RestTemplate();

	public static <T> T get(String url, Class<T> responseType, Map<String, ?> variables,
			ClientHttpRequestInterceptor... interceptors) {

//		RestTemplate restTemplate = new RestTemplate();

		if (interceptors != null) {
			restTemplate.setInterceptors(Arrays.asList(interceptors));
		}

		ResponseEntity<T> retVal = restTemplate.getForEntity(url, responseType);
		if (retVal.getStatusCodeValue() < 200 || retVal.getStatusCodeValue() >= 400) {
			throw new InternalServerException(
					"Http Get Request Exception : " + retVal.getStatusCode() + ", targetUrl : " + url);
		}

		return retVal.getBody();
	}
}
