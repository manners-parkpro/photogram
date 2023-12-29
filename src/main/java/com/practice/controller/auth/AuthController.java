package com.practice.controller.auth;

import com.practice.domain.dto.ApiResult;
import com.practice.domain.dto.UserDto;
import com.practice.exception.AlreadyEntity;
import com.practice.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.InvalidParameterException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/")
public class AuthController {

    private final AuthService service;

    @GetMapping("sign-in")
    public String signin() throws InvalidParameterException {
        return "login";
    }

    @GetMapping("sign-up")
    public String signup() throws InvalidParameterException {
        return "join";
    }

    @PostMapping("sign-up")
    public String register(@Valid UserDto dto) throws InvalidParameterException {

        ApiResult<Long> result = new ApiResult<>(ApiResult.RESULT_CODE_NOT_FOUND);

        try {
            Long id = service.save(dto);
            result.setCode(ApiResult.RESULT_CODE_OK);
            result.setData(id);
        } catch (AlreadyEntity | InvalidParameterException e) {
            result.setMessage(e.getMessage());
        }

        return "login";
    }
}
