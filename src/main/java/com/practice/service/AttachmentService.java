package com.practice.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.practice.configure.component.S3Properties;
import com.practice.domain.dto.AttachmentDto;
import com.practice.domain.dto.CommentDto;
import com.practice.domain.dto.MemberLikeDto;
import com.practice.domain.dto.SearchDto;
import com.practice.domain.entity.Attachment;
import com.practice.domain.entity.Comment;
import com.practice.domain.entity.User;
import com.practice.domain.types.FileType;
import com.practice.repository.AttachmentRepository;
import com.practice.repository.AuthRepository;
import com.practice.repository.CommentRepository;
import com.practice.repository.MemberLikeRepository;
import com.practice.utils.AmazonS3Utils;
import com.practice.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository repository;
    private final AuthRepository authRepository;
    private final MemberLikeRepository memberLikeRepository;
    private final CommentRepository commentRepository;
    private final S3Properties s3Properties;
    @PersistenceContext
    private EntityManager em;

    @Value("${spring.profiles.active}")
    String profiles;

    @Value("${attachment.path}")
    String path;

    @Transactional
    public AttachmentDto s3Upload(MultipartFile multipartFile) throws Exception {

        if (multipartFile == null || multipartFile.getOriginalFilename() == null) return null;

        try {
            log.info("===== S3 File Upload start. =====");

            /**
             * AWS
             */
            String bucketName = s3Properties.getBucketName();
            String originalFileName = multipartFile.getOriginalFilename();

            int startIndex = originalFileName.replaceAll("\\\\", "/").lastIndexOf("/");

            originalFileName = originalFileName.substring(startIndex + 1);
            String fileExtension = FilenameUtils.getExtension(originalFileName);
            String randomFileName =  RandomStringUtils.randomAlphanumeric(128);
            String s3FileName = randomFileName + "." + fileExtension;
            FileType fileType = FileUtils.defineMediaType(fileExtension);

            AmazonS3 s3Client = AmazonS3Utils.amazonS3(profiles, s3Properties);

            if (s3Client != null) {
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentType(multipartFile.getContentType());
                meta.setContentLength(multipartFile.getBytes().length);

                PutObjectRequest request = new PutObjectRequest(bucketName, s3FileName, multipartFile.getInputStream(), meta);
                if ("local".equals(profiles))
                    request.withCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(request);

                URL url = s3Client.getUrl(bucketName, s3FileName);
                String fileFullPath = "local".equals(profiles) ? url.toString() : "/" + s3FileName;

                // AttachmentDto SET.
                AttachmentDto dto = new AttachmentDto();
                dto.setOrgFilename(originalFileName);
                dto.setSavedFilename(s3FileName);
                dto.setFullPath(fileFullPath);
                dto.setFileSize((int) multipartFile.getSize());
                dto.setFileType(fileType);

                return dto;
            }
        } catch (AmazonServiceException e) {
            /**
             * The call was transmitted successfully, but Amazon S3 couldn't process
             * it, so it returned an error response.
             */
            e.printStackTrace();
            log.info("===== S3 File Upload exception !!! =====");
        }

        return null;
    }

    @Transactional
    public AttachmentDto upload(MultipartFile multipartFile, User user) throws Exception {

        if (multipartFile == null || multipartFile.getOriginalFilename() == null) return null;

        try {
            log.info("===== File Upload start. =====");

            String originalFileName = multipartFile.getOriginalFilename();

            int startIndex = originalFileName.replaceAll("\\\\", "/").lastIndexOf("/");

            originalFileName = originalFileName.substring(startIndex + 1);
            String fileExtension = FilenameUtils.getExtension(originalFileName);
            String randomFileName =  RandomStringUtils.randomAlphanumeric(128);
            String savedFileName = randomFileName + "." + fileExtension;
            FileType fileType = FileUtils.defineMediaType(fileExtension);

            File file = new File(path);
            if (!file.exists())
                file.mkdir();

            String fullPath = path + savedFileName;

            // upload file
            multipartFile.transferTo(new File(fullPath));

            Attachment attachment = new Attachment();
            attachment.setOrgFilename(originalFileName);
            attachment.setSavedFilename(savedFileName);
            attachment.setFullPath(fullPath);
            attachment.setFileSize((int) multipartFile.getSize());
            attachment.setFileType(fileType);
            attachment.setRegister(user);

            // User Attachment
            user.getAttachments().add(attachment);
            authRepository.save(user);

            return new AttachmentDto(attachment);

        } catch (Exception e) {
            /**
             * The call was transmitted successfully, but Amazon S3 couldn't process
             * it, so it returned an error response.
             */
            e.printStackTrace();
            log.info("===== File Upload exception !!! =====");
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<AttachmentDto> findAll() {
        return repository.findAll().stream().map(AttachmentDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttachmentDto> findByAttachmentWithOutMe(User user) {
        return repository.findAll().stream().filter(a -> !a.getRegister().equals(user)).map(AttachmentDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttachmentDto> getAttachment(List<Attachment> attachments, User user) {
        List<AttachmentDto> dtos = attachments.stream().map(AttachmentDto::new).collect(Collectors.toList());
        setDatas(dtos, user);
        return dtos;
    }

    @Transactional(readOnly = true)
    public Page<AttachmentDto> getAttachmentWithOutMe(User user, SearchDto search) {
        int page = search.getPage();
        int size = search.getSize();

        TypedQuery<Attachment> tq = search(user);
        long total = tq.getResultList().size();
        tq.setFirstResult(page * size).setMaxResults(size);
        List<Attachment> results = tq.getResultList();

        return new PageImpl<>(getAttachment(results, user), PageRequest.of(page, size), total);
    }

    private TypedQuery<Attachment> search(User user) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Attachment> criteriaQuery = builder.createQuery(Attachment.class);
        Root<Attachment> root = criteriaQuery.from(Attachment.class);

        List<Predicate> likeOr = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.notEqual(root.get("register"), user));

        if (likeOr.size() > 0 && predicates.size() > 0)
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])), builder.or((likeOr.toArray(new Predicate[likeOr.size()]))));
        else if (likeOr.size() > 0)
            criteriaQuery.where(builder.or((likeOr.toArray(new Predicate[likeOr.size()]))));
        else if (predicates.size() > 0)
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        return em.createQuery(criteriaQuery);
    }

    public List<AttachmentDto> findByIdIn(List<Long> ids) {
        if (ids == null)
            throw new InvalidParameterException("InvalidParameterException");

        return repository.findByIdIn(ids).stream().map(AttachmentDto::new).collect(Collectors.toList());
    }

    public Attachment findById(Long id) {
        if (id == null)
            throw new InvalidParameterException("InvalidParameterException");

        return repository.findById(id).orElseThrow(() -> new NotFoundException("NotFoundException"));
    }

    public AttachmentDto findByIdToDto(Long id) {
        if (id == null)
            throw new InvalidParameterException("InvalidParameterException");

        Attachment attachment = repository.findById(id).orElseThrow(() -> new NotFoundException("NotFoundException"));

        return new AttachmentDto(attachment);
    }

    private List<CommentDto> getAttachmentComments(Long attachmentId) {
        return commentRepository.findByAttachment_Id(attachmentId).stream().map(CommentDto::new).collect(Collectors.toList());
    }

    private List<MemberLikeDto> getAttachmentLikes(User user) {
        return memberLikeRepository.findByMember(user).stream().map(MemberLikeDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    void setDatas(List<AttachmentDto> dtos, User user) {
        if (!CollectionUtils.isEmpty(dtos)) {
            List<MemberLikeDto> memberLikes = getAttachmentLikes(user);
            if (!CollectionUtils.isEmpty(memberLikes)) {
                for (MemberLikeDto memberLike : memberLikes) {
                    for (AttachmentDto dto : dtos) {
                        if (dto.getId().equals(memberLike.getAttachmentId()))
                            dto.setPostLike(memberLike);
                    }
                }
            }

            for (AttachmentDto dto : dtos) {
                List<CommentDto> comments = getAttachmentComments(dto.getId());

                if (CollectionUtils.isEmpty(comments)) continue;

                dto.setComments(comments);
            }
        }
    }
}
