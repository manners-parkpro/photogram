package com.practice.repository;

import com.practice.domain.compositekeys.MemberLikeId;
import com.practice.domain.entity.MemberLike;
import com.practice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberLikeRepository extends JpaRepository<MemberLike, MemberLikeId> {
    List<MemberLike> findByMember(User member);
}
