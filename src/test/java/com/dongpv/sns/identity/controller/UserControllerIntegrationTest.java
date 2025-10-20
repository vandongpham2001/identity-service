/*
package com.dongpv.sns.identity.controller;



@Slf4j
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
	@Container
	static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");

	@DynamicPropertySource
	static void configureDatasource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
		registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
	}

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;
	private CreateUserRequestDto validRequest;

	@BeforeEach
	void initData() {
		LocalDate dob = LocalDate.of(2001, 1, 1);

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		validRequest = CreateUserRequestDto.builder()
				.email("john.doe@example.com")
				.username("johndoe")
				.firstName("John")
				.lastName("Doe")
				.password("Password123!")
				.dob(dob)
				.gender(Gender.MALE)
				.build();
	}

	private CreateUserRequestDto createValidRequest() {
		return CreateUserRequestDto.builder()
				.email("test@example.com")
				.username("testuser")
				.firstName("Test")
				.lastName("User")
				.password("Password123!")
				.dob(LocalDate.of(1995, 5, 15))
				.gender(Gender.FEMALE)
				.build();
	}

	// ========== CREATE USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_validRequest_success() throws Exception {
		// GIVEN
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.username").value("johndoe"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.email").value("john.doe@example.com"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_missingEmail_badRequest() throws Exception {
		// GIVEN
		validRequest.setEmail(null);
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_invalidEmail_badRequest() throws Exception {
		// GIVEN
		validRequest.setEmail("invalid-email");
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_usernameTooShort_badRequest() throws Exception {
		// GIVEN
		validRequest.setUsername("jo");
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_passwordTooShort_badRequest() throws Exception {
		// GIVEN
		validRequest.setPassword("123");
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_passwordWeak_badRequest() throws Exception {
		// GIVEN
		validRequest.setPassword("password123");
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_missingFirstName_badRequest() throws Exception {
		// GIVEN
		validRequest.setFirstName(null);
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_missingLastName_badRequest() throws Exception {
		// GIVEN
		validRequest.setLastName(null);
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_missingDob_badRequest() throws Exception {
		// GIVEN
		validRequest.setDob(null);
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_missingGender_badRequest() throws Exception {
		// GIVEN
		validRequest.setGender(null);
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_duplicateUsername_badRequest() throws Exception {
		// GIVEN - Create first user
		String firstUserContent = objectMapper.writeValueAsString(validRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(firstUserContent))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Create second user with same username
		CreateUserRequestDto duplicateRequest = createValidRequest();
		duplicateRequest.setUsername(validRequest.getUsername());
		String duplicateContent = objectMapper.writeValueAsString(duplicateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(duplicateContent))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createUser_duplicateEmail_badRequest() throws Exception {
		// GIVEN - Create first user
		String firstUserContent = objectMapper.writeValueAsString(validRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(firstUserContent))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Create second user with same email
		CreateUserRequestDto duplicateRequest = createValidRequest();
		duplicateRequest.setEmail(validRequest.getEmail());
		String duplicateContent = objectMapper.writeValueAsString(duplicateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(duplicateContent))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	// ========== SECURITY TESTS ==========

	@Test
	void createUser_withoutAdminRole_forbidden() throws Exception {
		performCreateUser_andExpectForbidden();
	}

	@Test
	@WithMockUser(roles = "USER")
	void createUser_withUserRole_forbidden() throws Exception {
		performCreateUser_andExpectForbidden();
	}

	// ========== GET USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void getUserById_validId_success() throws Exception {
		// GIVEN - Create user first
		String createContent = objectMapper.writeValueAsString(validRequest);
		var createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseBody = createResponse.getResponse().getContentAsString();
		String userId = extractUserIdFromResponse(responseBody);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.username").value("johndoe"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.email").value("john.doe@example.com"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getUserById_invalidId_notFound() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/invalid-id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void getUserById_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/some-id"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// ========== UPDATE USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateUser_validRequest_success() throws Exception {
		// GIVEN - Create user first
		String createContent = objectMapper.writeValueAsString(validRequest);
		var createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseBody = createResponse.getResponse().getContentAsString();
		String userId = extractUserIdFromResponse(responseBody);

		// Prepare update request
		UpdateUserRequestDto updateRequest = UpdateUserRequestDto.builder()
				.firstName("Updated John")
				.lastName("Updated Doe")
				.build();
		String updateContent = objectMapper.writeValueAsString(updateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/" + userId)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(updateContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Updated John"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Updated Doe"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateUser_invalidId_notFound() throws Exception {
		// GIVEN
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().firstName("Updated John").build();
		String updateContent = objectMapper.writeValueAsString(updateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/invalid-id")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(updateContent))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void updateUser_withoutAdminRole_forbidden() throws Exception {
		// GIVEN
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().firstName("Updated John").build();
		String updateContent = objectMapper.writeValueAsString(updateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/some-id")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(updateContent))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// ========== DELETE USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteUser_validId_success() throws Exception {
		// GIVEN - Create user first
		String createContent = objectMapper.writeValueAsString(validRequest);
		var createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseBody = createResponse.getResponse().getContentAsString();
		String userId = extractUserIdFromResponse(responseBody);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteUser_invalidId_notFound() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/invalid-id"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void deleteUser_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/some-id"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// ========== GET ALL USERS TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void getAllUsers_success() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result").exists());
	}

	@Test
	void getAllUsers_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	// ========== HELPER METHODS ==========

	private String extractUserIdFromResponse(String responseBody) {
		try {
			// Simple extraction - in real scenario you might want to use JSONPath
			int startIndex = responseBody.indexOf("\"id\":\"") + 6;
			int endIndex = responseBody.indexOf("\"", startIndex);
			return responseBody.substring(startIndex, endIndex);
		} catch (Exception e) {
			throw new RuntimeException("Failed to extract user ID from response", e);
		}
	}

	private void performCreateUser_andExpectForbidden() throws Exception {
		// GIVEN
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
}
*/
