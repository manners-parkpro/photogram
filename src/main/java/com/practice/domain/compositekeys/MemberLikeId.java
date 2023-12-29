package com.practice.domain.compositekeys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class MemberLikeId implements Serializable {

    @Column
    private Long memberId;

    @Column
    private Long attachmentId;
}
