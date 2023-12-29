package com.practice.controller.api;

import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.ApiResult;
import com.practice.exception.InvalidRequiredParameter;
import com.practice.exception.NotFoundException;
import com.practice.exception.UserNotFoundException;
import com.practice.repository.MemberLikeRepository;
import com.practice.service.memberLike.MemberLikeService;
import com.practice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/memberLike/")
public class MemberLikeApiController {

    private final MemberLikeService service;
    private final UserService userService;

    @PostMapping("save/{id}")
    public ApiResult createOrRemove(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws UserPrincipalNotFoundException, InvalidRequiredParameter, UserNotFoundException {

        if (id == null)
            throw new InvalidRequiredParameter();

        if (principalDetails == null)
            throw new UserPrincipalNotFoundException("UserPrincipalNotFoundException");

        ApiResult<Long> result = new ApiResult<>(ApiResult.RESULT_CODE_NOT_FOUND);

        try {
            service.save(principalDetails.getUser(), id);
            result.setCode(ApiResult.RESULT_CODE_OK);
        } catch (RuntimeException e) {
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("cancel/{id}")
    public ApiResult cancel(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws UserPrincipalNotFoundException, InvalidRequiredParameter, NotFoundException {

        if (id == null)
            throw new InvalidRequiredParameter();

        if (principalDetails == null)
            throw new UserPrincipalNotFoundException("UserPrincipalNotFoundException");

        ApiResult<Long> result = new ApiResult<>(ApiResult.RESULT_CODE_NOT_FOUND);

        try {
            service.cancel(principalDetails.getUser(), id);
            result.setCode(ApiResult.RESULT_CODE_OK);
        } catch (RuntimeException e) {
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
