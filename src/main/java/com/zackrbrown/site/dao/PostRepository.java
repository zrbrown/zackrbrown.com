package com.zackrbrown.site.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    Page<Post> findByCreatedDateTimeBefore(LocalDateTime createdDateTime, Pageable pageable);

    Page<Post> findByCreatedDateTimeAfter(LocalDateTime createdDateTime, Pageable pageable);
}
