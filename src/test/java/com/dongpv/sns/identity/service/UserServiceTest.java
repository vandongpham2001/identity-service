/*
package com.dongpv.sns.identity.service;


@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceTest {
	@Autowired
	private UserServiceImpl userService;

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private RoleRepository roleRepository;

	@MockitoBean
	private UserMapper userMapper;

	private CreateUserRequestDto request;
	private UserResponseDto userResponse;
	private UserEntity user;
	private RoleEntity userRole;

	@BeforeEach
	void initData() {
		LocalDate dob = LocalDate.of(1990, 1, 1);

		request = CreateUserRequestDto.builder()
				.email("john.doe@example.com")
				.username("johndoe")
				.password("Password123!")
				.build();

		userRole = RoleEntity.builder()
				.name(PredefinedRole.USER_ROLE)
				.description("User role")
				.build();

		user = UserEntity.builder()
				.email("john.doe@example.com")
				.username("johndoe")
				.emailVerified(false)
				.roles(new HashSet<>())
				.build();
		user.setId("cf0600f538b3");

		userResponse = UserResponseDto.builder()
				.id("cf0600f538b3")
				.email("john.doe@example.com")
				.username("johndoe")
				.build();
	}

	// ========== CREATE USER TESTS ==========

	@Test
	void createUser_validRequest_success() {
		// GIVEN
		when(roleRepository.findById(PredefinedRole.USER_ROLE)).thenReturn(Optional.of(userRole));
		when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(user);
		when(userMapper.toResponseDto(any(UserEntity.class))).thenReturn(userResponse);

		// WHEN
		var response = userService.create(request);

		// THEN
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		Assertions.assertThat(response.getUsername()).isEqualTo("johndoe");
		Assertions.assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
		Assertions.assertThat(response.getGender()).isEqualTo(Gender.MALE);

		verify(roleRepository).findById(PredefinedRole.USER_ROLE);
		verify(userRepository).saveAndFlush(any(UserEntity.class));
	}

	@Test
	void createUser_withCustomRoles_success() {
		// GIVEN
		request.setRoles(java.util.List.of("ADMIN_ROLE", "USER_ROLE"));
		RoleEntity adminRole = RoleEntity.builder()
				.name("ADMIN_ROLE")
				.description("Admin role")
				.build();

		when(roleRepository.findAllById(request.getRoles())).thenReturn(java.util.List.of(adminRole, userRole));
		when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(user);

		// WHEN
		var response = userService.create(request);

		// THEN
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		verify(roleRepository).findAllById(request.getRoles());
		verify(userRepository).saveAndFlush(any(UserEntity.class));
	}

	@Test
	void createUser_duplicateUser_throwsException() {
		// GIVEN
		when(roleRepository.findById(PredefinedRole.USER_ROLE)).thenReturn(Optional.of(userRole));
		when(userRepository.saveAndFlush(any(UserEntity.class)))
				.thenThrow(new DataIntegrityViolationException("Duplicate user"));

		// WHEN & THEN
		assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));

		verify(roleRepository).findById(PredefinedRole.USER_ROLE);
		verify(userRepository).saveAndFlush(any(UserEntity.class));
	}

	// ========== UPDATE USER TESTS ==========

	@Test
	void updateUser_validRequest_success() {
		// GIVEN
		String userId = "cf0600f538b3";
		UpdateUserRequestDto updateRequest = UpdateUserRequestDto.builder().build();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(user);
		when(userMapper.toResponseDto(any(UserEntity.class))).thenReturn(userResponse);

		// WHEN
		var response = userService.update(userId, updateRequest);

		// THEN
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		verify(userRepository).findById(userId);
		verify(userRepository).saveAndFlush(any(UserEntity.class));
		verify(userMapper).toResponseDto(any(UserEntity.class));
	}

	@Test
	void updateUser_userNotFound_throwsException() {
		// GIVEN
		String userId = "invalid-id";
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().firstName("Updated John").build();

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// WHEN & THEN
		assertThrows(UserNotFoundException.class, () -> userService.update(userId, updateRequest));

		verify(userRepository).findById(userId);
		verify(userRepository, times(0)).saveAndFlush(any(UserEntity.class));
	}

	@Test
	void updateUser_withPassword_success() {
		// GIVEN
		String userId = "cf0600f538b3";
		UpdateUserRequestDto updateRequest =
				UpdateUserRequestDto.builder().password("NewPassword123!").build();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(user);
		when(userMapper.toResponseDto(any(UserEntity.class))).thenReturn(userResponse);

		// WHEN
		var response = userService.update(userId, updateRequest);

		// THEN
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		verify(userRepository).findById(userId);
		verify(userRepository).saveAndFlush(any(UserEntity.class));
		verify(userMapper).toResponseDto(any(UserEntity.class));
	}

	// ========== DELETE USER TESTS ==========

	@Test
	void deleteUser_validId_success() {
		// GIVEN
		String userId = "cf0600f538b3";
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.save(any(UserEntity.class))).thenReturn(user);

		// WHEN
		userService.delete(userId);

		// THEN
		verify(userRepository).findById(userId);
		verify(userRepository).save(any(UserEntity.class));
	}

	@Test
	void deleteUser_userNotFound_throwsException() {
		// GIVEN
		String userId = "invalid-id";
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// WHEN & THEN
		assertThrows(UserNotFoundException.class, () -> userService.delete(userId));

		verify(userRepository).findById(userId);
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	// ========== FIND USER TESTS ==========

	@Test
	void findOneById_validId_success() {
		// GIVEN
		String userId = "cf0600f538b3";
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// WHEN
		var response = userService.findOneById(userId);

		// THEN
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		Assertions.assertThat(response.getUsername()).isEqualTo("johndoe");
		verify(userRepository).findById(userId);
	}

	@Test
	void findOneById_userNotFound_throwsException() {
		// GIVEN
		String userId = "invalid-id";
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// WHEN & THEN
		assertThrows(UserNotFoundException.class, () -> userService.findOneById(userId));

		verify(userRepository).findById(userId);
	}

	// ========== ME TESTS ==========

	@Test
	@WithMockUser(username = "john.doe@example.com")
	void getMyInfo_valid_success() {
		// GIVEN
		when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

		// WHEN
		var response = userService.me();

		// THEN
		Assertions.assertThat(response.getUsername()).isEqualTo("johndoe");
		Assertions.assertThat(response.getId()).isEqualTo("cf0600f538b3");
		verify(userRepository).findByEmail("john.doe@example.com");
	}

	@Test
	@WithMockUser(username = "john.doe@example.com")
	void getMyInfo_userNotFound_throwsException() {
		// GIVEN
		when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

		// WHEN & THEN
		assertThrows(UserNotFoundException.class, () -> userService.me());

		verify(userRepository).findByEmail("john.doe@example.com");
	}
}
*/
