package com.dongpv.sns.identity.controller.v1;

import java.util.List;
import java.util.Map;

import com.dongpv.sns.identity.dto.request.admin.BaseFilterRequestDto;
import com.dongpv.sns.identity.entity.UserEntity;
import com.dongpv.sns.identity.util.PaginationUtils;
import com.dongpv.sns.identity.util.StringUtils;
import com.dongpv.sns.identity.wrapper.ResponseWrapper;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.dongpv.sns.identity.dto.ApiResponse;
import com.dongpv.sns.identity.dto.request.admin.user.CreateUserRequestDto;
import com.dongpv.sns.identity.dto.request.admin.user.UpdateUserRequestDto;
import com.dongpv.sns.identity.dto.response.UserResponseDto;
import com.dongpv.sns.identity.service.impl.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/filter")
    ResponseEntity<?> filterUsers(@RequestParam(required = false) final Map<String, String> requestParams) {
        PageRequest pageRequest = PaginationUtils.generatePageRequest(requestParams);

        var filter = new BaseFilterRequestDto();
        filter.setKeyword(StringUtils.getValue(requestParams.get("keyword")));

        if (!StringUtils.getValue(requestParams.get("sortColumn")).isEmpty()) {
            filter.setSortColumn(
                    StringUtils.camelToSnake(StringUtils.getValue(requestParams.get("sortColumn"))));
        } else {
            filter.setSortColumn(
                    StringUtils.camelToSnake(filter.getSortColumn()));
        }

        if (!StringUtils.getValue(requestParams.get("sortType")).isEmpty()) {
            filter.setSortType(StringUtils.getValue(requestParams.get("sortType")));
        } else {
            filter.setSortType(
                    StringUtils.camelToSnake(filter.getSortType()));
        }

        Page<UserResponseDto> pageResponseDto = userService.filter(pageRequest, filter);

        return ResponseEntity.ok(
                new ResponseWrapper<>(PaginationUtils.buildPageRes(pageResponseDto)));
    }

    @PostMapping
    ApiResponse<UserResponseDto> createUser(@RequestBody @Valid CreateUserRequestDto request) {
        return ApiResponse.ok(userService.create(request));
    }

//    @GetMapping
//    ApiResponse<List<UserResponseDto>> getUsers() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        LOGGER.info("Username {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> LOGGER.info(grantedAuthority.getAuthority()));
//
//        return ApiResponse.ok(userService.findAll());
//    }

//    @GetMapping("/{userId}")
//    ApiResponse<UserResponseDto> getUser(@PathVariable("userId") String userId) {
//        return ApiResponse.<UserResponseDto>builder()
//                .result(userService.getById(userId))
//                .build();
//    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(new ResponseWrapper<>(userService.findOneById(userId)));
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponseDto> updateUser(@PathVariable String userId, @RequestBody UpdateUserRequestDto request) {
        return ApiResponse.ok(userService.update(userId, request));
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.delete(userId);
        return ApiResponse.ok("User has been deleted");
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponseDto> getMyInfo() {
        return ApiResponse.ok(userService.getMyInfo());
    }

    @GetMapping("/filter-test")
    public ResponseEntity<Page<UserEntity>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<UserEntity> entities = userService.filterUsersByUsername(page, size, sortBy);
        return ResponseEntity.ok(entities);
    }
}
