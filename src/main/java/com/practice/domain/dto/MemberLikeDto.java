package com.practice.domain.dto;

import com.practice.domain.entity.MemberLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberLikeDto {
    private Long memberId;
    private Long attachmentId;
    private UserDto member;
    private AttachmentDto attachment;
    private LocalDateTime createdAt;

    public MemberLikeDto lightDto(MemberLike ml) {
        MemberLikeDto dto = new MemberLikeDto();
        dto.setMemberId(ml.getMember().getId());
        dto.setAttachmentId(ml.getAttachment().getId());

        return dto;
    }

    public MemberLikeDto(MemberLike ml) {
        this.memberId = ml.getMember().getId();
        this.member = new UserDto().lightUserDto(ml.getMember());
        this.attachmentId = ml.getAttachment().getId();
        this.attachment = new AttachmentDto(ml.getAttachment());
        this.createdAt = ml.getCreatedAt();
    }
}
