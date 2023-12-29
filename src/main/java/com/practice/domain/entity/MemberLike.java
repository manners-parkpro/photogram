package com.practice.domain.entity;

import com.practice.domain.compositekeys.MemberFollowId;
import com.practice.domain.compositekeys.MemberLikeId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberLike {

    @EmbeddedId
    @Column(name = "MEMBER_LIKE_ID")
    private MemberLikeId memberLikeId;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private User member;

    @MapsId("attachmentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachmentId")
    private Attachment attachment;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void beforePersist() {
        LocalDateTime dateTime = LocalDateTime.now();
        this.createdAt = dateTime;
    }
}
