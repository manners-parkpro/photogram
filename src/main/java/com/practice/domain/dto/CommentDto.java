package com.practice.domain.dto;

import com.practice.domain.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long[] ids;
    private String comment;
    private AttachmentDto attachment;
    private Long attachmentId;
    private UserDto writer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(Comment c) {
        this.id = c.getId();
        this.comment = c.getComment();
        this.attachment = new AttachmentDto(c.getAttachment());
        this.attachmentId = c.getAttachment().getId();
        this.writer = new UserDto().lightUserDto(c.getWriter());
        this.createdAt = c.getCreatedAt();
        this.updatedAt = c.getUpdatedAt();
    }
}
