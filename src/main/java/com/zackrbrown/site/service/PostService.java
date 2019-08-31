package com.zackrbrown.site.service;

import com.zackrbrown.site.dao.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public Optional<Post> getPostByUrlName(String urlName) {
        return postRepository.getByUrlName(urlName);
    }

    public Optional<Post> getLatestPost() {
        return postRepository.findAll(
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                .get().findFirst();
    }

    public Optional<Post> getPreviousPost(Post post) {
        return postRepository.findByCreatedDateTimeBefore(
                post.getCreatedDateTime(),
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                .get().findFirst();
    }

    public Optional<Post> getNextPost(Post post) {
        return postRepository.findByCreatedDateTimeAfter(
                post.getCreatedDateTime(),
                PageRequest.of(0, 1, Sort.Direction.ASC, "createdDateTime"))
                .get().findFirst();
    }

    public void addPost(UUID uuid, String title, String content, LocalDateTime addedDateTime, List<String> tags) {
        Set<Tag> addedTags = tags.stream().map(this::getOrAddTag).collect(Collectors.toSet());

        Post post = new Post(
                UUID.randomUUID(),
                title.replaceAll("\\s", "-"),
                title,
                content,
                addedDateTime,
                addedTags);
        postRepository.save(post);
    }

    public void editPost(Post post, String title, String content, List<String> tags) {
        post.setTitle(title);
        post.setContent(content);
        post.getTags().addAll(tags.stream().map(this::getOrAddTag).collect(Collectors.toSet()));
        postRepository.save(post);
    }

    private Tag getOrAddTag(String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);

        Optional<Tag> existingTag = tagRepository.findOne(Example.of(tag));
        return existingTag.orElseGet(() -> tagRepository.save(tag));
    }
}
