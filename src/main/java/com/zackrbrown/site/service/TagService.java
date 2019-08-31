package com.zackrbrown.site.service;

import com.zackrbrown.site.dao.Post;
import com.zackrbrown.site.dao.Tag;
import com.zackrbrown.site.dao.TagRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> getTags(Post post) {
        return tagRepository.getAllByPosts(Collections.singleton(post));
    }
}
