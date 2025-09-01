package com.proyectcine.cinestark.api.controller;

import com.proyectcine.cinestark.api.dto.request.RegisterRequest;
import com.proyectcine.cinestark.api.dto.request.UpdateUserRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/users")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(@RequestBody RegisterRequest request) {
        log.info("begin service register -> {}", request);
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping("")
    public ResponseEntity<GenericResponse> getUsers(@RequestHeader("Authorization") String token,
                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("begin service getUsers");
        return ResponseEntity.ok(userService.getUsers(token, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(token, request, id));
    }
}
