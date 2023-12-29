package com.practice.domain.entity;

import com.practice.domain.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class User extends BaseEntity {

    //@Transient 객체에 해당옵션을 사용하면 DB에 컬럼이 생성되지 않는다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 100)
    private String username;
    @Column(length = 100)
    private String email;
    @Column(length = 50)
    private String phone;
    private String password;
    @Column(columnDefinition = "TEXT")
    private String introDuction;
    private LocalDateTime lastLoginAt;
    @Enumerated(EnumType.STRING)
    @Column(length = 1, columnDefinition = "char(1) default 'Y'")
    private YNType activeYn;
    @Enumerated(EnumType.STRING)
    @Column(length = 1, columnDefinition = "char(1) default 'N'")
    private YNType dormantYn;
    @Enumerated(EnumType.STRING)
    @Column(length = 1, columnDefinition = "char(1) default 'N'")
    private YNType deleteYn;

    // 실제 저장 되는 객체
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberFollow> memberFollowings = new ArrayList<>();

    // follower를 찾기 위한 객체
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<MemberFollow> memberFollowers = new ArrayList<>();

    /** memberLikes / comments -> Attachment Model로 가는게 맞는 설계 **/
    // 실제 저장 되는 객체
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberLike> memberLikes = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_ATTACHE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ATTACHE_ID")
    )
    private List<Attachment> attachments = new ArrayList<>();
}
