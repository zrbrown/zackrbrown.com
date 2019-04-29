package com.zackrbrown.site.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostUpdateRepository extends JpaRepository<PostUpdate, UUID> {

    List<PostUpdate> findAllByPost(Post post, Sort sort);
}
