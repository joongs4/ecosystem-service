package com.kakaopay.ecosystem.http;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class HttpKakaoAuthInterceptor implements ClientHttpRequestInterceptor {

	private final String KAKAOAPP_KEY = "KakaoAK";
	private final String KAKAOAPP_KEY_VALUE = "5834f9bca07da037c8442cec7eb6467d";

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpRequest wrapper = new HttpRequestWrapper(request);
//		wrapper.getHeaders().setBasicAuth(KAKAOAPP_KEY + " " + KAKAOAPP_KEY_VALUE);
//		wrapper.getHeaders().set(KAKAOAPP_KEY, KAKAOAPP_KEY_VALUE);
		wrapper.getHeaders().set("Authorization", KAKAOAPP_KEY + " " + KAKAOAPP_KEY_VALUE);
		return execution.execute(wrapper, body);
	}

}
