package com.practice.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024)
    private String comment;
    @ManyToOne
    @JoinColumn(name = "attachmentId")
    private Attachment attachment;
    @ManyToOne
    @JoinColumn(name = "writerId")
    private User writer;
}
