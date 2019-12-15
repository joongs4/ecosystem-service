#사전과제 2. 생테 정보 서비스 API개발

작성자: 이중건

1. 개발 프레임워크
	1) 언어: Java 8
	2) 프레임워크: SpringBoot, Hibernate, Spring Data Jpa
	3) DB: H2
	4) Log: Logback

2. 기능명세 구현여부
	1) 필수(구현) - 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API를 개발하세요
	2) 필수(구현) - 생태 관광정보 데이터를 조회/추가/수정할 수 있는 API를 개발하세요. 
		A. 조회 : GET /ecosystem, /ecosystem/{id}
		B. 추가 : POST /ecosystem , BODY : 생태 관광정보
		C. 수정 : PUT /ecosystem/{id} , BODY : 생태 관광정보
		
	3) 필수(구현) - 생태 관광지 중에 서비스 지역 컬럼에서 특정 지역에서 진행되는 프로그램명과 테마를 출력하는 API를 개발하세요
		A. GET /region/{region}
		
	4) 필수(구현) - 생태 정보 데이터에 "프로그램 소개” 컬럼에서 특정 문자열이 포함된 레코드에서 서비스 지역 개수를 세어 출력하는 API를 개발하세요.
		A. GET /ecosystem/region?keyword={keyword}
		
		
	5) 필수(구현) - 모든 레코드에 프로그램 상세 정보를 읽어와서 입력 단어의 출현빈도수를 계산하여 출력 하는 API를 개발하세요. 
		A. GET /ecosystem/count?keyword={keyword}
	
	6) 선택(구현) - 생태관광 정보를 기반으로 생태관광 프로그램을 추천해주려고 합니다. 지역명과 관광 키 워드를 입력받아 프로그램 코드를 출력하는 API를 개발하세요.
		A. GET /ecosystem/recommendation?region={region}&keyword={keyword}
		
	7) 추가 제약사항(부분구현) - API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 호출 기능을 개발하세요. 
		A. signup : POST /user/signup, @BODY : UserId & UserPassword
		B. signin : PUT /user/signin, @BODY : UserId & UserPassword
		C. Refresh Token 발급 : 미구현
	
3. 문제해결 전략
	1) 2.1 데이터 파일에서 각 레코드를 데이터베이스에 저장하는 API를 개발하세요
		A) 생태관광 정보(EcosystemServiceJpo)와 지역 정보(RegionJpo) 두 개의 Entity는 N:1 관계 구성
		B) 생태관광 정보중 서비스 지역 항목은 표준화되지 않은 이유로 해당 정보를 바탕으로 KakaoLocal 서비스를 이용해서 표준화된 지역정보를 저장(도, 시까지만 분류)
		C) Data의 Primary key는 prefix + sequence를 기반으로 자동생성
	
	2) 2.2 생태 관광정보 데이터를 조회/추가/수정할 수 있는 API를 개발하세요.
		A) 생태 관광정보 데이터의 ID를 바탕으로 조회, 수정 가능
		
	3) 2.3 생태 관광지 중에 서비스 지역 컬럼에서 특정 지역에서 진행되는 프로그램명과 테마를 출력하는 API를 개발하세요
		A) 지역명을 입력받아 표준화된 지역정보를 바탕으로 RegionJpo를 검색한 뒤 이에 관계가 맺어진 EcosystemService Entity 조회 및 출력
		
	4) 2.4 생태 정보 데이터에 "프로그램 소개” 컬럼에서 특정 문자열이 포함된 레코드에서 서비스 지역 개수를 세어 출력하는 API를 개발하세요.
		A) 모든 생태관광 정보를 조회하고 "프로그램 소개" 컬럼에 특정 문자열의 포함여부를 바탕으로 Filtering을 진행한 뒤 관계가 맺어진 Region 항목의 수를 세어 출력
		
	5) 2.5 모든 레코드에 프로그램 상세 정보를 읽어와서 입력 단어의 출현빈도수를 계산하여 출력 하는 API를 개발하세요.
		A) 모든 생태관광 정보를 조회한 뒤 "프로그램 상세 소개" 컬럼에서 입력 단어의 출현 빈도수를 계산하여 출력
		B) 문자열 검색 알고리즘 중 KMP를 이용 - 참고 https://mygumi.tistory.com/61
		
	6) 2.6 - 생태관광 정보를 기반으로 생태관광 프로그램을 추천해주려고 합니다. 지역명과 관광 키워드를 입력받아 프로그램 코드를 출력하는 API를 개발하세요.
		A) 지역명을 입력받아 표준화된 지역정보를 바탕으로 RegionJpo를 검색한 뒤 이에 관계가 맺어진 EcosystemService Entity 조회
		B) A)에서 조회된 EcosystemService에서 "테마" 컬럼, "프로그램 소개" 컬럼, "프로그램 상세 소개" 컬럼을 입력 키워드로 조회
		C) 각 컬럼들에서 입력된 키워드가 사용된 빈도수를 체크하여 가중치를 바탕으로 합산 점수를 내고 최고 점수의 Entity를 출력
		4) 각 항목들의 가중치는 테마(0.5), 프로그램 소개(0.3), 프로그램 상세 소개(0.2)로 정하였으며 아래의 예시처럼 각 항목의 빈도수를 통해 합산 점수 계산
			ex) keyword : "국립공원"
			
			테마 0번, 프로그램 소개 1번, 프로그램 상세소개 1번 => (0.5 * 0) + (0.3 * 1) + (0.2 * 1) = 0.5점
			{
				programName: "오감만족! 오대산 에코 어드벤처 투어",
				theme: "아동·청소년 체험학습",
				regionName: "강원도 평창군 일대",
				programIntroduction: "1일차: 어름치마을 인근 탐방, 2일차: 오대산국립공원 탐방, 3일차: 봉평마을 탐방",
				programDetailedIntroduction: " 1일차: 백룡동굴, 민물고기생태관 체험, 칠족령 트레킹 2일차: 대관령 양떼목장, 신재생 에너지전시관, 오대산국립공원 3일차: 이효석 문학관, 봉평마을"
			}
			
	7) 2.7 - API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 호출 기능을 개발하세요.
		A) signup 계정 생성 API : ID 중복체크를 수행한 후 Password를 암호화하여 저장하고 토큰은 secret으로 서명하여 생성한다.		
		B) signin 로그인 API : IP, PASSWORD를 체크한 후 토큰을 발급한다.		
		C) secret은 설정파일(application.yml)에서 jwt.secret으로 설정할 수 있다.
		D) refresh 토큰 재발급 API : 미구현
		참고 - https://medium.com/swlh/spring-boot-security-jwt-hello-world-example-b479e457664c
		





