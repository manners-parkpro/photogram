package com.practice.domain.dto;

import com.practice.domain.entity.MemberLike;
import com.practice.domain.entity.User;
import com.practice.domain.entity.Attachment;
import com.practice.domain.types.FileType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentDto {

    private Long id;

    private FileType fileType;

    private String orgFilename;

    private String savedFilename;

    private String fullPath;

    private String thumbnailPath;

    private int fileSize;

    private int position;
    /**
     * 실제 파일
     */
    private File rawFile;
    private UserDto register;
    private MemberLikeDto postLike;
    private List<CommentDto> comments = new ArrayList<>();

    public AttachmentDto(Attachment a) {
        this.id = a.getId();
        this.fileType = a.getFileType();
        this.orgFilename = a.getOrgFilename();
        this.savedFilename = a.getSavedFilename();
        this.fullPath = a.getFullPath();
        this.thumbnailPath = a.getThumbnailPath();
        this.fileSize = a.getFileSize();
        this.position = a.getPosition();
        this.register = new UserDto().lightUserDto(a.getRegister());
    }

    public Attachment convertAttachDtoToModel(AttachmentDto a, User register) {
        Attachment attachment = new Attachment();
        attachment.setFileType(a.getFileType());
        attachment.setOrgFilename(a.getOrgFilename());
        attachment.setSavedFilename(a.getSavedFilename());
        attachment.setFullPath(a.getFullPath());
        attachment.setThumbnailPath(a.getThumbnailPath());
        attachment.setRegister(register);
        attachment.setPosition(a.getPosition());
        return attachment;
    }
}
