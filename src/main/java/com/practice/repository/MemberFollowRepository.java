package com.practice.repository;

import com.practice.domain.compositekeys.MemberFollowId;
import com.practice.domain.entity.MemberFollow;
import com.practice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberFollowRepository extends JpaRepository<MemberFollow, MemberFollowId> {
    Optional<MemberFollow> findByFollowerAndMember(User follower, User member);
    Optional<MemberFollow> findByMemberFollowId(MemberFollowId memberFollowId);
    List<MemberFollow> findByMember(User member);
    boolean existsByMemberAndFollower(User member, User follower);
    long countByMember(User member);
}
