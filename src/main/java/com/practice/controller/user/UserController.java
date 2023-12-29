package com.practice.controller.user;

import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.UserDto;
import com.practice.domain.entity.User;
import com.practice.exception.UserNotFoundException;
import com.practice.repository.MemberFollowRepository;
import com.practice.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/user/")
public class UserController {

    private final UserService service;
    private final MemberFollowRepository memberFollowRepository;

    @GetMapping("profile")
    public ModelAndView profile() {
        ModelAndView modelAndView = new ModelAndView("user/profile");
        return modelAndView;
    }

    @GetMapping("profile/{id}")
    public ModelAndView detail(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws UserNotFoundException {
        ModelAndView modelAndView = new ModelAndView("user/profile");
        log.info("id : " + id);

        User user = service.findById(id);

        modelAndView.addObject("user", new UserDto(user, true));
        modelAndView.addObject("principalId", principalDetails.getUser().getId());
        modelAndView.addObject("isMe", id.equals(principalDetails.getUser().getId()));
        modelAndView.addObject("hasSubscribe", memberFollowRepository.existsByMemberAndFollower(principalDetails.getUser(), user));
        modelAndView.addObject("subscribes", memberFollowRepository.findByMember(user));
        return modelAndView;
    }

    @GetMapping("modify")
    public ModelAndView modify(@AuthenticationPrincipal PrincipalDetails principalDetails) throws UserPrincipalNotFoundException {
        if (principalDetails == null)
            throw new UserPrincipalNotFoundException("UserPrincipalNotFoundException");

        log.info("Session : " + principalDetails.getUser());

        ModelAndView modelAndView = new ModelAndView("user/profileDetail");
        modelAndView.addObject("user", new UserDto().lightUserDto(principalDetails.getUser()));
        return modelAndView;
    }

    @PostMapping("save")
    public @ResponseBody String save(@RequestBody UserDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws UserPrincipalNotFoundException {
        if (principalDetails == null)
            throw new UserPrincipalNotFoundException("UserPrincipalNotFoundException");

        try {
            service.modifyUser(dto);
        } catch (UserPrincipalNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage());
        }

        return "success";
    }
}
