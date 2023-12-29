package com.practice.controller;

import com.google.gson.Gson;
import com.practice.configure.authenticate.PrincipalDetails;
import com.practice.domain.dto.AttachmentDto;
import com.practice.service.AttachmentService;
import com.practice.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/attachment/")
public class AttachmentController {

    private final AttachmentService service;

    @GetMapping("upload")
    public String upload() {
        return "attachment/upload";
    }

    @PostMapping("s3-upload")
    public @ResponseBody void s3Upload(HttpServletResponse response,
                       @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                       @RequestParam(value = "files[]", required = false) MultipartFile[] multipartFileList) throws Exception {

        Map<String, Object> result = new HashMap<>();
        PrintWriter writer = null;

        try {
            writer = response.getWriter();
        } catch (IOException exception) {
            log.info(exception.getMessage());
        }

        if (multipartFileList != null && multipartFileList.length > 0) {

            List<AttachmentDto> list = new ArrayList<>();

            for (MultipartFile multipleFile : multipartFileList) {

                String originalFileName = multipartFile.getOriginalFilename();
                boolean isPermit = FileUtils.permitExtensionCheck(originalFileName);

                if (!isPermit) {
                    result.put(FileUtils.RS_MESSAGE, "해당 파일은 업로드하실 수 없습니다.<br />확인 후 다시 업로드해주시기 바랍니다.");
                    break;
                }

                if (isPermit) list.add(service.s3Upload(multipleFile));
            }

            result.put(FileUtils.RS_DATA, list);

            log.info("===== S3 multipartFileList Successfully Uploaded. =====");
        } else {

            String originalFileName = multipartFile.getOriginalFilename();
            boolean isPermit = FileUtils.permitExtensionCheck(originalFileName);

            result.put(FileUtils.RS_MESSAGE, isPermit ? "" : "해당 파일은 업로드하실 수 없습니다.<br />확인 후 다시 업로드해주시기 바랍니다.");

            if (isPermit) {
                result.put(FileUtils.RS_DATA, service.s3Upload(multipartFile));

                log.info("===== S3 File Successfully Uploaded. =====");
            }
        }

        result.put(FileUtils.RS_STATUS, FileUtils.RS_SUCC);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");


        writer.print(new Gson().toJson(result));
        writer.flush();
        writer.close();
    }

    @PostMapping("upload")
    public @ResponseBody void upload(HttpServletResponse response,
                                     @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                                     @RequestParam(value = "files[]", required = false) MultipartFile[] multipartFileList,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {

        Map<String, Object> result = new HashMap<>();
        PrintWriter writer = null;

        try {
            writer = response.getWriter();
        } catch (IOException exception) {
            log.info(exception.getMessage());
        }

        if (multipartFileList != null && multipartFileList.length > 0) {

            List<AttachmentDto> list = new ArrayList<>();

            for (MultipartFile multipleFile : multipartFileList) {
                String originalFileName = multipartFile.getOriginalFilename();
                boolean isPermit = FileUtils.permitExtensionCheck(originalFileName);

                if (!isPermit) {
                    result.put(FileUtils.RS_MESSAGE, "해당 파일은 업로드하실 수 없습니다.<br />확인 후 다시 업로드해주시기 바랍니다.");
                    break;
                }

                if (isPermit) list.add(service.upload(multipleFile, principalDetails.getUser()));
            }

            result.put(FileUtils.RS_DATA, list);

            log.info("===== multipartFileList Successfully Uploaded. =====");
        } else {
            String originalFileName = multipartFile.getOriginalFilename();
            boolean isPermit = FileUtils.permitExtensionCheck(originalFileName);

            result.put(FileUtils.RS_MESSAGE, isPermit ? "" : "해당 파일은 업로드하실 수 없습니다.<br />확인 후 다시 업로드해주시기 바랍니다.");

            if (isPermit) {
                result.put(FileUtils.RS_DATA, service.upload(multipartFile, principalDetails.getUser()));

                log.info("===== File Successfully Uploaded. =====");
            }
        }

        result.put(FileUtils.RS_STATUS, FileUtils.RS_SUCC);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.sendRedirect("/user/profile/" + principalDetails.getUser().getId());

        writer.print(new Gson().toJson(result));
        writer.flush();
        writer.close();
    }
}
