package com.kakaopay.ecosystem.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kakaopay.ecosystem.entity.KakaoLocalDocumentEntity;
import com.kakaopay.ecosystem.entity.KakaoLocalDocumentsEntity;
import com.kakaopay.ecosystem.http.EcosystemHttpClient;
import com.kakaopay.ecosystem.http.HttpKakaoAuthInterceptor;

@Service
public class KakaoLocalService {

	private final String KAKAO_LOCAL_SEARCH_URL_BY_KEYWORD = "https://dapi.kakao.com/v2/local/search/keyword.json";
	private final String KAKAO_LOCAL_SEARCH_KEY_BY_KEYWORD = "query";

	private final HttpKakaoAuthInterceptor httpKakaoAuthInterceptor = new HttpKakaoAuthInterceptor();

	public KakaoLocalDocumentsEntity searchBykeyword(String keyword) {

		Map<String, String> requestVariables = new HashMap<>();
		requestVariables.put(KAKAO_LOCAL_SEARCH_KEY_BY_KEYWORD, keyword);

		String url = String.format("%s?%s=%s", KAKAO_LOCAL_SEARCH_URL_BY_KEYWORD, KAKAO_LOCAL_SEARCH_KEY_BY_KEYWORD,
				keyword);

		KakaoLocalDocumentsEntity kakaoLocalDocumentsEntity = EcosystemHttpClient.get(url,
				KakaoLocalDocumentsEntity.class, null, httpKakaoAuthInterceptor);

		return kakaoLocalDocumentsEntity;

	}

}
