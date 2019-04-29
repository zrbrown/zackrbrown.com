package com.zackrbrown.site.controller;

import com.zackrbrown.site.dao.*;
import com.zackrbrown.site.model.FormBlogPost;
import com.zackrbrown.site.model.FormBlogPostUpdate;
import com.zackrbrown.site.model.FormattedPostUpdate;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final PostRepository postRepository;
    private final PostUpdateRepository postUpdateRepository;
    private final TagRepository tagRepository;

    public BlogController(PostRepository postRepository,
                          PostUpdateRepository postUpdateRepository,
                          TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postUpdateRepository = postUpdateRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public String latestBlog(Model model) {
        Optional<Post> latestPost = postRepository.findAll(
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                .get().findFirst();
        if (latestPost.isPresent()) {
            Set<FormattedPostUpdate> formattedPostUpdates = latestPost.get().getPostUpdates().stream()
                    .map(postUpdate -> {
                        FormattedPostUpdate formattedPostUpdate = new FormattedPostUpdate();

                        Parser parser = Parser.builder().build();
                        Node document = parser.parse(postUpdate.getContent());
                        HtmlRenderer renderer = HtmlRenderer.builder().build();
                        String renderedContent = renderer.render(document);
                        formattedPostUpdate.setContent(renderedContent);

                        formattedPostUpdate.setDate(postUpdate.getUpdatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));

                        return formattedPostUpdate;
                    }).collect(Collectors.toSet());
            model.addAttribute("postUpdates", formattedPostUpdates);

            List<String> tags = tagRepository.getAllByPosts(Collections.singleton(latestPost.get())).stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            renderPost(model, latestPost.get(), tags);

            Optional<Post> previousPost = postRepository.findByCreatedDateTimeBefore(
                    latestPost.get().getCreatedDateTime(),
                    PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                    .get().findFirst();
            model.addAttribute("showPrevious", previousPost.isPresent());
            model.addAttribute("showPrevious", previousPost.isPresent());
            previousPost.ifPresent(p -> model.addAttribute("previousPost", p.getUrlName()));
            model.addAttribute("showNext", false);
        } else {
            model.addAttribute("postTitle", "");
            model.addAttribute("postDate", "");
            model.addAttribute("postContent", "");
            model.addAttribute("showPrevious", false);
            model.addAttribute("showNext", false);
        }

        return "blog";
    }

    @GetMapping("/{postUrlName}")
    public String blog(@PathVariable String postUrlName, Model model) {
        Optional<Post> requestedPost = postRepository.getByUrlName(postUrlName);

        if (requestedPost.isPresent()) {
            Set<FormattedPostUpdate> formattedPostUpdates = requestedPost.get().getPostUpdates().stream()
                    .map(postUpdate -> {
                        FormattedPostUpdate formattedPostUpdate = new FormattedPostUpdate();

                        Parser parser = Parser.builder().build();
                        Node document = parser.parse(postUpdate.getContent());
                        HtmlRenderer renderer = HtmlRenderer.builder().build();
                        String renderedContent = renderer.render(document);
                        formattedPostUpdate.setContent(renderedContent);

                        formattedPostUpdate.setDate(postUpdate.getUpdatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));

                        return formattedPostUpdate;
                    }).collect(Collectors.toSet());
            model.addAttribute("postUpdates", formattedPostUpdates);

            List<String> tags = tagRepository.getAllByPosts(Collections.singleton(requestedPost.get())).stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());

            renderPost(model, requestedPost.get(), tags);

            Optional<Post> previousPost = postRepository.findByCreatedDateTimeBefore(
                    requestedPost.get().getCreatedDateTime(),
                    PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                    .get().findFirst();
            model.addAttribute("showPrevious", previousPost.isPresent());
            previousPost.ifPresent(p -> model.addAttribute("previousPost", p.getUrlName()));

            Optional<Post> nextPost = postRepository.findByCreatedDateTimeAfter(
                    requestedPost.get().getCreatedDateTime(),
                    PageRequest.of(0, 1, Sort.Direction.ASC, "createdDateTime"))
                    .get().findFirst();
            model.addAttribute("showNext", nextPost.isPresent());
            nextPost.ifPresent(p -> model.addAttribute("nextPost", p.getUrlName()));
        } else {
            return "redirect:/blog";
        }

        return "blog";
    }

    private void renderPost(Model model, Post post, List<String> tags) {
        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("postDate", post.getCreatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));

        Parser parser = Parser.builder().build();
        Node document = parser.parse(post.getContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedContent = renderer.render(document);
        model.addAttribute("postContent", renderedContent);

        model.addAttribute("tags", tags);
    }

    @GetMapping("/{postUrlName}/edit")
    public String editPost(@PathVariable String postUrlName, Principal principal, Model model) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.getByUrlName(postUrlName);

        if (!post.isPresent()) {
            return "redirect:/blog";
        }

        model.addAttribute("postTitle", post.get().getTitle());
        model.addAttribute("postContent", post.get().getContent());
        model.addAttribute("submitPath", "/blog/" + postUrlName + "/edit");

        List<String> tags = tagRepository.getAllByPosts(Collections.singleton(post.get())).stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        model.addAttribute("tags", tags);

        return "admin/blog_edit";
    }

    @PostMapping("/{postUrlName}/edit")
    public String submitPostEdit(@PathVariable String postUrlName, FormBlogPost blogPost, Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog/{postUrlName}";
        }

        Optional<Post> postOptional = postRepository.getByUrlName(postUrlName);

        if (!postOptional.isPresent()) {
            return "redirect:/blog/{postUrlName}";
        }

        Post post = postOptional.get();
        post.setTitle(blogPost.getPostTitle());
        post.setContent(blogPost.getPostContent());
        post.getTags().addAll(blogPost.getAddedTags().stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);

            Optional<Tag> existingTag = tagRepository.findOne(Example.of(tag));
            return existingTag.orElseGet(() -> tagRepository.save(tag));
        }).collect(Collectors.toSet()));
        postRepository.save(post);

        return "redirect:/blog/{postUrlName}";
    }

    @GetMapping("/{postUrlName}/update")
    public String updatePost(@PathVariable String postUrlName, Principal principal, Model model) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        Optional<Post> post = postRepository.getByUrlName(postUrlName);

        if (!post.isPresent()) {
            return "redirect:/blog";
        }

        List<String> tags = tagRepository.getAllByPosts(Collections.singleton(post.get())).stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        renderPost(model, post.get(), tags);

        model.addAttribute("submitPath", "/blog/" + postUrlName + "/update");

        return "admin/blog_update";
    }

    @PostMapping("/{postUrlName}/update")
    public String submitPostUpdate(@PathVariable String postUrlName, FormBlogPostUpdate blogPostUpdate, Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog/{postUrlName}";
        }

        Optional<Post> postOptional = postRepository.getByUrlName(postUrlName);

        if (!postOptional.isPresent()) {
            return "redirect:/blog/{postUrlName}";
        }

        Post post = postOptional.get();

        PostUpdate postUpdate = new PostUpdate();
        postUpdate.setPost(post);
        postUpdate.setContent(blogPostUpdate.getPostContent());
        postUpdate.setUpdatedDateTime(LocalDateTime.now());
        postUpdateRepository.save(postUpdate);

        post.getPostUpdates().add(postUpdate);
        postRepository.save(post);

        return "redirect:/blog/{postUrlName}";
    }

    @GetMapping("/add")
    public String addPost(Principal principal, Model model) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        model.addAttribute("postTitle", "");
        model.addAttribute("postContent", "");
        model.addAttribute("submitPath", "/blog/add");
        model.addAttribute("tags", Collections.emptyList());

        return "admin/blog_edit";
    }

    // TODO use RETHROW in production
    @PostMapping("/add")
    public String submitPost(FormBlogPost blogPost, Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        Set<Tag> tags = blogPost.getAddedTags().stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);

            Optional<Tag> existingTag = tagRepository.findOne(Example.of(tag));
            return existingTag.orElseGet(() -> tagRepository.save(tag));
        }).collect(Collectors.toSet());

        Post post = new Post(
                UUID.randomUUID(),
                "test-post",
                blogPost.getPostTitle(),
                blogPost.getPostContent(),
                LocalDateTime.now(),
                tags);
        postRepository.save(post);

        return "redirect:/blog";
    }
}
