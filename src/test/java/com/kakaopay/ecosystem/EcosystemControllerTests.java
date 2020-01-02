package com.kakaopay.ecosystem;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ecosystem.entity.EcosystemServiceEntity;
import com.kakaopay.ecosystem.filter.JwtRequestFilter;
import com.kakaopay.ecosystem.jwt.JwtAuthenticationEntryPoint;
import com.kakaopay.ecosystem.jwt.JwtTokenUtil;
import com.kakaopay.ecosystem.resource.EcosystemServiceResource;
import com.kakaopay.ecosystem.resource.RegionResource;
import com.kakaopay.ecosystem.service.EcosystemService;
import com.kakaopay.ecosystem.service.RegionService;
import com.kakaopay.ecosystem.service.UserService;
import com.kakaopay.ecosystem.store.EcosystemServiceStore;
import com.kakaopay.ecosystem.store.RegionStore;
import com.kakaopay.ecosystem.util.AddressManager;

@WebMvcTest(controllers = { EcosystemServiceResource.class, RegionResource.class })
public class EcosystemControllerTests {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@MockBean
	private FilterChainProxy springSecurityFilterChain;

	@MockBean
	private EcosystemService ecosystemService;

	@MockBean
	private RegionService regionService;

	@MockBean
	private EcosystemServiceStore ecosystemServiceStore;

	@MockBean
	private RegionStore regionStore;

	@MockBean
	private AddressManager addressManager;

	@MockBean
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//	@MockBean
//	private WebSecurityConfig webSecurityConfig;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@MockBean
	private JwtRequestFilter jwtRequestFilter;

	@MockBean
	private MockMvcResultMatchers mockMvcResultMatchers;

	@BeforeEach
	void beforeTest() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void testFindAllEcosystem() throws Exception {
		mockMvc.perform(get("/ecosystem")).andExpect(status().isOk());
	}

	@Test
	void testFindByIdEcosystem() throws Exception {
		mockMvc.perform(get("/ecosystem/prg1")).andExpect(status().isOk());
	}

	@Test
	void testFindRegionByKeyword() throws Exception {
		mockMvc.perform(get("/ecosystem/region?keyword=세계문화유산")).andExpect(status().isOk());
	}

	@Test
	void testFindKeywordCount() throws Exception {
		mockMvc.perform(get("/ecosystem/count?keyword=문화")).andExpect(status().isOk());
	}

	@Test
	void testFindRecommendation() throws Exception {
		mockMvc.perform(get("/ecosystem/recommendation?region=평창&keyword=국립공원")).andExpect(status().isOk());
	}

	@Test
	void testSaveEcosystem() throws Exception {

		EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
		entityToSave.setTheme("testTheme");
		entityToSave.setRegionName("강원도 속초시");
		entityToSave.setProgramName("testProgram");
		entityToSave.setProgramIntroduction("testProgramIntroduction");
		entityToSave.setProgramDetailedIntroduction("testProgramDetailedIntroduction");

		ObjectMapper objectMapper = new ObjectMapper();

		mockMvc.perform(post("/ecosystem").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entityToSave))).andExpect(status().isOk());
	}

	@Test
	void testUpdateEcosystem() throws Exception {

		EcosystemServiceEntity entityToSave = new EcosystemServiceEntity();
		entityToSave.setId("prg1");
		entityToSave.setTheme("testTheme");
		entityToSave.setRegionName("강원도 속초시");
		entityToSave.setProgramName("testProgram");
		entityToSave.setProgramIntroduction("testProgramIntroduction");
		entityToSave.setProgramDetailedIntroduction("testProgramDetailedIntroduction");

		ObjectMapper objectMapper = new ObjectMapper();

		mockMvc.perform(put("/ecosystem/prg1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entityToSave))).andExpect(status().isOk());
	}

	@Test
	void testFindProgramsByRegion() throws Exception {
		mockMvc.perform(get("/region/평창군")).andExpect(status().isOk());
	}

}
