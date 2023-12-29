package com.practice.domain.entity;

import com.practice.domain.compositekeys.MemberFollowId;
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
public class MemberFollow {

    @EmbeddedId
    @Column(name = "MEMBER_FOLLOW_ID")
    private MemberFollowId memberFollowId;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private User member;

    @MapsId("followerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followerId")
    private User follower;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void beforePersist() {
        LocalDateTime dateTime = LocalDateTime.now();
        this.createdAt = dateTime;
    }
}
