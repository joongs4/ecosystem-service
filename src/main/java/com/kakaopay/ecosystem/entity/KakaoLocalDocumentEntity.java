package com.kakaopay.ecosystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLocalDocumentEntity {

	private String address_name;
	private String category_group_code;
	private String category_group_name;
	private String category_name;
	private String distance;
	private String id;
	private String phone;
	private String place_name;
	private String place_url;
	private String rolad_address_name;
	private String x;
	private String y;

	/*
	 * "address_name": "강원 원주시 소초면 학곡리 900-1", "category_group_code": "",
	 * "category_group_name": "", "category_name":
	 * "서비스,산업 \u003e 관리,운영 \u003e 공원관리운영", "distance": "", "id": "15831744",
	 * "phone": "033-740-9900", "place_name": "치악산국립공원사무소", "place_url":
	 * "http://place.map.kakao.com/15831744", "road_address_name":
	 * "강원 원주시 소초면 무쇠점2길 26", "x": "128.050276029765", "y": "37.4140873530471"
	 */

}
