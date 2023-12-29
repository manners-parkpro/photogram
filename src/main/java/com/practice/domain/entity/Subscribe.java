package com.practice.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

//@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
// 2개 이상의 유니크 Key를 잡을때 사용한다.
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                       name = "subscribe_unique",
                        columnNames = {"follower", "register"}
                )
        }
)
@Deprecated
public class Subscribe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "follower")
    private User follower;
    @ManyToOne @JoinColumn(name = "register")
    private User register;
}
