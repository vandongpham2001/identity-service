/*
package com.dongpv.sns.identity.controller;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@TestPropertySource("/test.properties")
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	private ObjectMapper objectMapper;
	private CreateUserRequestDto validRequest;
	private UserResponseDto userResponse;
	private LocalDate dob;

	@BeforeEach
	void initData() {
		dob = LocalDate.of(1990, 1, 1);

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

		userResponse = UserResponseDto.builder()
				.id("cf0600f538b3")
				.email("john.doe@example.com")
				.username("johndoe")
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
		Mockito.when(userService.create(ArgumentMatchers.any(CreateUserRequestDto.class)))
				.thenReturn(userResponse);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.username").value("johndoe"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.email").value("john.doe@example.com"));

		Mockito.verify(userService).create(ArgumentMatchers.any(CreateUserRequestDto.class));
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

		Mockito.verifyNoInteractions(userService);
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

		Mockito.verifyNoInteractions(userService);
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

		Mockito.verifyNoInteractions(userService);
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

		Mockito.verifyNoInteractions(userService);
	}

	// ========== SECURITY TESTS ==========

	@Test
	void createUser_withoutAdminRole_forbidden() throws Exception {

		assertCreateUserForbidden();
	}

	@Test
	@WithMockUser(roles = "USER")
	void createUser_withUserRole_forbidden() throws Exception {

		assertCreateUserForbidden();
	}

	// ========== GET USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void getUserById_validId_success() throws Exception {
		// GIVEN
		String userId = "cf0600f538b3";
		Mockito.when(userService.findOneById(userId)).thenReturn(userResponse);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"))
				.andExpect(MockMvcResultMatchers.jsonPath("result.username").value("johndoe"));

		Mockito.verify(userService).findOneById(userId);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getUserById_invalidId_notFound() throws Exception {
		// GIVEN
		String userId = "invalid-id";
		Mockito.when(userService.findOneById(userId)).thenThrow(new RuntimeException("User not found"));

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());

		Mockito.verify(userService).findOneById(userId);
	}

	@Test
	void getUserById_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/some-id"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());

		Mockito.verifyNoInteractions(userService);
	}

	// ========== UPDATE USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateUser_validRequest_success() throws Exception {
		// GIVEN
		String userId = "cf0600f538b3";
		UpdateUserRequestDto updateRequest = UpdateUserRequestDto.builder()
				.firstName("Updated John")
				.lastName("Updated Doe")
				.build();
		String content = objectMapper.writeValueAsString(updateRequest);

		UserResponseDto updatedResponse = UserResponseDto.builder()
				.id(userId)
				.email("john.doe@example.com")
				.username("johndoe")
				.gender(Gender.MALE)
				.build();

		Mockito.when(userService.update(userId, updateRequest)).thenReturn(updatedResponse);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/" + userId)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result.id").value(userId));

		Mockito.verify(userService).update(userId, updateRequest);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateUser_invalidId_notFound() throws Exception {
		// GIVEN
		String userId = "invalid-id";
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().firstName("Updated John").build();
		String content = objectMapper.writeValueAsString(updateRequest);

		Mockito.when(userService.update(userId, updateRequest)).thenThrow(new RuntimeException("User not found"));

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/" + userId)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());

		Mockito.verify(userService).update(userId, updateRequest);
	}

	@Test
	void updateUser_withoutAdminRole_forbidden() throws Exception {
		// GIVEN
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().firstName("Updated John").build();
		String content = objectMapper.writeValueAsString(updateRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.put("/admin/user/some-id")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isForbidden());

		Mockito.verifyNoInteractions(userService);
	}

	// ========== DELETE USER TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteUser_validId_success() throws Exception {
		// GIVEN
		String userId = "cf0600f538b3";
		Mockito.doNothing().when(userService).delete(userId);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000));

		Mockito.verify(userService).delete(userId);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteUser_invalidId_notFound() throws Exception {
		// GIVEN
		String userId = "invalid-id";
		Mockito.doThrow(new RuntimeException("User not found"))
				.when(userService)
				.delete(userId);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/" + userId))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());

		Mockito.verify(userService).delete(userId);
	}

	@Test
	void deleteUser_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.delete("/admin/user/some-id"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());

		Mockito.verifyNoInteractions(userService);
	}

	// ========== GET ALL USERS TESTS ==========

	@Test
	@WithMockUser(roles = "ADMIN")
	void getAllUsers_success() throws Exception {
		// GIVEN
		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<UserResponseDto> pageResponse = new PageImpl<>(List.of(userResponse), pageRequest, 1);

		Mockito.when(userService.filter(
						ArgumentMatchers.any(PageRequest.class), ArgumentMatchers.any(BaseFilterRequestDto.class)))
				.thenReturn(pageResponse);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
				.andExpect(MockMvcResultMatchers.jsonPath("result").exists());

		Mockito.verify(userService)
				.filter(ArgumentMatchers.any(PageRequest.class), ArgumentMatchers.any(BaseFilterRequestDto.class));
	}

	@Test
	void getAllUsers_withoutAdminRole_forbidden() throws Exception {
		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
				.andExpect(MockMvcResultMatchers.status().isForbidden());

		Mockito.verifyNoInteractions(userService);
	}

	private void assertCreateUserForbidden() throws Exception {
		// GIVEN
		String content = objectMapper.writeValueAsString(validRequest);

		// WHEN, THEN
		mockMvc.perform(MockMvcRequestBuilders.post("/admin/user")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(content))
				.andExpect(MockMvcResultMatchers.status().isForbidden());

		Mockito.verifyNoInteractions(userService);
	}
}
*/
