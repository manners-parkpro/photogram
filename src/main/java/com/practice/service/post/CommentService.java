package com.practice.service.post;

import com.practice.domain.dto.CommentDto;
import com.practice.domain.entity.Attachment;
import com.practice.domain.entity.Comment;
import com.practice.domain.entity.User;
import com.practice.exception.NotFoundException;
import com.practice.repository.AuthRepository;
import com.practice.repository.CommentRepository;
import com.practice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final AuthRepository authRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public CommentDto save(User user, CommentDto dto) throws InvalidParameterException, NotFoundException {
        if (user == null || dto == null)
            throw new InvalidParameterException("InvalidParameterException");

        Attachment attachment = attachmentService.findById(dto.getAttachmentId());
        if (attachment == null)
            throw new NotFoundException("NotFoundException");

        Comment comment;
        if (dto.getId() != null) {
            Comment optComment = repository.findById(dto.getId()).orElseThrow(() -> new NotFoundException("NotFoundException"));
            comment = optComment;
        } else
            comment = new Comment();

        if (StringUtils.isNotBlank(dto.getComment()))
            comment.setComment(dto.getComment());

        comment.setAttachment(attachment);
        comment.setWriter(user);

        repository.save(comment);

        return new CommentDto(comment);
    }

    @Transactional
    public void remove(User user, Long[] ids) throws InvalidParameterException, NotFoundException {
        if (user == null || ids == null)
            throw new InvalidParameterException("InvalidParameterException");

        List<Comment> comments = repository.findAllById(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(comments))
            throw new NotFoundException("NotFoundException");

        repository.deleteAllByIdInBatch(Arrays.asList(ids));
    }
}
