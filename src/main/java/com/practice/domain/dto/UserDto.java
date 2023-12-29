package com.practice.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.domain.entity.User;
import com.practice.domain.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank @Pattern(regexp = "[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$")
    private String email;
    private String phone;
    private String introDuction; // 자기소개
    @NotBlank
    private String password;
    private YNType activeYn;
    private YNType dormantYn;
    private YNType deleteYn;
    // 실제 저장되는 객체
    private List<MemberFollowDto> memberFollowings = new ArrayList<>();
    // follower를 찾기 위한 객체
    private List<MemberFollowDto> memberFollowers = new ArrayList<>();
    // 실제 저장되는 객체
    private List<MemberLikeDto> memberLikes = new ArrayList<>();
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime lastLoginAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
    private List<AttachmentDto> attachments = new ArrayList<>();

    public UserDto(User u) { this(u, false); }

    public UserDto lightUserDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());

        return dto;
    }

    public UserDto(User u, boolean isDetail) {
        this.id = u.getId();
        this.name = u.getName();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.phone = u.getPhone();

        if (isDetail) {
            this.attachments = u.getAttachments().stream().map(a -> new AttachmentDto(a)).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(u.getMemberFollowings()))
                this.memberFollowings = u.getMemberFollowings().stream().map(MemberFollowDto::new).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(u.getMemberFollowers()))
                this.memberFollowers = u.getMemberFollowers().stream().map(MemberFollowDto::new).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(u.getMemberLikes()))
                this.memberLikes = u.getMemberLikes().stream().map(MemberLikeDto::new).collect(Collectors.toList());

            this.introDuction = u.getIntroDuction();
            this.lastLoginAt = u.getLastLoginAt();
            this.createdAt = u.getCreatedAt();
            this.updatedAt = u.getUpdatedAt();
        }
    }
}
