package com.practice.repository;

import com.practice.domain.entity.Attachment;
import com.practice.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAttachment_Id(Long attachmentId);
}
