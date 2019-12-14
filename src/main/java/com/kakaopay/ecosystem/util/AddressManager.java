package com.kakaopay.ecosystem.util;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

import com.kakaopay.ecosystem.entity.KakaoLocalDocumentEntity;
import com.kakaopay.ecosystem.entity.KakaoLocalDocumentsEntity;
import com.kakaopay.ecosystem.service.KakaoLocalService;

@Component
public class AddressManager {

	private final KakaoLocalService kakaoLocalService;

	private final String[] provinces = { "경기", "강원", "경남", "경북", "충북", "충남", "전남", "전북", "제주" };
	private final String[] trashWords = { "일대", "번지" };

	public AddressManager(KakaoLocalService kakaoLocalService) {
		this.kakaoLocalService = kakaoLocalService;
	}

	public String getStandardAddress(String region) {

		String standardAddress = null;

		region = removeDetailsFromAddress(region);

		KakaoLocalDocumentsEntity kakaoLocalDocumentsEntity = kakaoLocalService.searchBykeyword(region);
		if (kakaoLocalDocumentsEntity != null && !kakaoLocalDocumentsEntity.getDocuments().isEmpty()) {
			KakaoLocalDocumentEntity kakaoLocalDocumentEntity = kakaoLocalDocumentsEntity.getDocuments().get(0);
			String addressName = kakaoLocalDocumentEntity.getAddress_name();

			standardAddress = removeExceptProvinceAndCity(addressName);
		}

		if (standardAddress == null) {
			standardAddress = region;
		}

		return standardAddress;

	}

	private String removeDetailsFromAddress(String address) {

		final String regEx = "[0-9,-]";

		String retVal = null;
		retVal = address.replaceAll(regEx, "");

		for (String trashWord : trashWords) {
			if (retVal.contains(trashWord)) {
				retVal.replace(trashWord, "");
			}
		}

		retVal = removeExceptProvinceAndCity(retVal);

		if (retVal.endsWith(" ")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}

		return retVal;
	}

	private String removeExceptProvinceAndCity(String region) {
		StringBuilder provinceAndCity = null;

		StringTokenizer tokenizer = new StringTokenizer(region, " ");

		while (tokenizer.hasMoreTokens()) {

			if (provinceAndCity == null) {
				provinceAndCity = new StringBuilder();
			}

			String tokenValue = tokenizer.nextToken();
			if (Arrays.stream(provinces).anyMatch(tokenValue::equals) || tokenValue.endsWith("도")) {

				if (tokenValue.length() >= 4) {
					tokenValue = tokenValue.substring(0, 1) + tokenValue.substring(2, 3);
				}

				provinceAndCity.append(tokenValue);
				provinceAndCity.append(" ");
				continue;
			}

			provinceAndCity.append(tokenValue);
			break;
		}

		return provinceAndCity != null ? provinceAndCity.toString() : region;
	}

}
