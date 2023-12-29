package com.practice.domain.dto;

import com.practice.domain.entity.MemberFollow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberFollowDto {
    private Long memberId;
    private Long followerId;
    private UserDto member;
    private UserDto follower;
    private LocalDateTime createdAt;

    public MemberFollowDto(MemberFollow mf) {
        this.memberId = mf.getMember().getId();
        this.followerId = mf.getFollower().getId();
        this.member = new UserDto().lightUserDto(mf.getMember());
        this.follower = new UserDto().lightUserDto(mf.getFollower());
        this.createdAt = mf.getCreatedAt();
    }
}
