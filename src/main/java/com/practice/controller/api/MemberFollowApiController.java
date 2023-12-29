package com.practice.controller.api;

import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.ApiResult;
import com.practice.domain.dto.MemberFollowDto;
import com.practice.domain.entity.User;
import com.practice.exception.InvalidRequiredParameter;
import com.practice.exception.NotFoundException;
import com.practice.exception.UserNotFoundException;
import com.practice.service.memberFollow.MemberFollowService;
import com.practice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/memberFollow/")
public class MemberFollowApiController {

    private final MemberFollowService service;
    private final UserService userService;

    @PostMapping("save/{id}")
    public ApiResult save(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws UserPrincipalNotFoundException, InvalidRequiredParameter, UserNotFoundException {

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

    @PostMapping("{id}")
    public ApiResult<List<MemberFollowDto>> getSubscribes(@PathVariable Long id) throws UserNotFoundException {

        ApiResult<List<MemberFollowDto>> result = new ApiResult<>(ApiResult.RESULT_CODE_NOT_FOUND);

        try {
            User user = userService.findById(id);
            List<MemberFollowDto> datas = service.getMemberFollows(user);
            result.setData(datas);
            result.setCode(ApiResult.RESULT_CODE_OK);
        } catch (RuntimeException e) {
            result.setMessage(e.getMessage());
        }

        return result;
    }

    /**
    @PostMapping("{id}")
    public String getSubscribeByThymleaf(@PathVariable Long id, Model model) throws UserNotFoundException {

        User user = userService.findById(id);

        model.addAttribute("data", service.findByRegister(user));
        return "user/profile :: #follower-list";
    }
    **/
}
