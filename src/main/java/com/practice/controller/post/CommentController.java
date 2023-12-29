package com.practice.controller.post;

import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.ApiResult;
import com.practice.domain.dto.CommentDto;
import com.practice.exception.AlreadyEntity;
import com.practice.exception.NotFoundException;
import com.practice.service.post.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.InvalidParameterException;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment/")
public class CommentController {

    private final CommentService service;

    @PostMapping("ajax/save")
    public @ResponseBody ApiResult save(@RequestBody CommentDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApiResult<CommentDto> result = new ApiResult<>(ApiResult.RESULT_CODE_ERROR);

        try {
            CommentDto data = service.save(principalDetails.getUser(), dto);
            result.setCode(ApiResult.RESULT_CODE_OK);
            result.setData(data);
        } catch (RuntimeException | NotFoundException e) {
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @PostMapping("ajax/remove")
    public @ResponseBody ApiResult remove(@RequestBody CommentDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws NotFoundException {
        ApiResult<CommentDto> result = new ApiResult<>(ApiResult.RESULT_CODE_OK);

        if (dto.getId() != null) {
            Long[] ids = {dto.getId()};
            dto.setIds(ids);
        }

        service.remove(principalDetails.getUser(), dto.getIds());
        return result;
    }
}
