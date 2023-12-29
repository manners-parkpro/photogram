package com.practice.domain.compositekeys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class MemberFollowId implements Serializable {

    @Column
    private Long memberId;

    @Column
    private Long followerId;
}
