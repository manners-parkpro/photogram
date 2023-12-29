package com.practice.domain.entity;

import com.practice.domain.types.FileType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicInsert
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private FileType fileType;

    private String orgFilename;

    private String savedFilename;

    @Column(length = 1024)
    private String fullPath;

    @Column(length = 1024)
    private String thumbnailPath;

    /**
     * 파일 사이즈(kb)
     */
    private int fileSize = 0;

    /**
     * 다운로드 수
     */
    private int downloads = 0;

    @Column(length = 1024)
    private String description;

    @ManyToOne
    private User register;

    /**
     * 이미지 정렬 순서
     */
    @Column
    private int sortOrder = 0;

    /**
     * 파일 구분 위치
     * 0 : 첨부파일 / 1 : 썸네일 / 2 : 팝업 mobile / 3 : 팝업 web
     */
    @Column
    private int position = 0;
}
