package com.practice.controller;

import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.ApiResult;
import com.practice.domain.dto.AttachmentDto;
import com.practice.domain.dto.SearchDto;
import com.practice.domain.dto.UserDto;
import com.practice.exception.AlreadyEntity;
import com.practice.exception.UserNotFoundException;
import com.practice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class IndexController {

    private final AttachmentService attachmentService;

    @GetMapping
    public ModelAndView index(@AuthenticationPrincipal PrincipalDetails principalDetails, SearchDto search) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("user", new UserDto().lightUserDto(principalDetails.getUser()));
        modelAndView.addObject("pages", attachmentService.getAttachmentWithOutMe(principalDetails.getUser(), search));
        return modelAndView;
    }

    @GetMapping("api/index")
    public @ResponseBody ApiResult<Page<AttachmentDto>> indexPaging(@AuthenticationPrincipal PrincipalDetails principalDetails, SearchDto search) {
        ApiResult<Page<AttachmentDto>> result = new ApiResult<>(ApiResult.RESULT_CODE_NOT_FOUND);

        try {
            Page<AttachmentDto> data = attachmentService.getAttachmentWithOutMe(principalDetails.getUser(), search);
            result.setCode(ApiResult.RESULT_CODE_OK);
            result.setData(data);
        } catch (RuntimeException e) {
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @GetMapping("explor")
    public ModelAndView explor(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ModelAndView modelAndView = new ModelAndView("explor");
        modelAndView.addObject("user", new UserDto().lightUserDto(principalDetails.getUser()));
        modelAndView.addObject("attachments", attachmentService.findByAttachmentWithOutMe(principalDetails.getUser()));
        return modelAndView;
    }
}
