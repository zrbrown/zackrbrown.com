package com.zackrbrown.site.controller;

import com.zackrbrown.site.config.BaseConfig;
import com.zackrbrown.site.dao.Post;
import com.zackrbrown.site.dao.Tag;
import com.zackrbrown.site.model.FormBlogPost;
import com.zackrbrown.site.model.FormBlogPostUpdate;
import com.zackrbrown.site.service.PostService;
import com.zackrbrown.site.service.PostUpdateService;
import com.zackrbrown.site.service.TagService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final BaseConfig config;
    private final PostService postService;
    private final PostUpdateService postUpdateService;
    private final TagService tagService;

    public BlogController(BaseConfig config, PostService postService, PostUpdateService postUpdateService, TagService tagService) {
        this.config = config;
        this.postService = postService;
        this.postUpdateService = postUpdateService;
        this.tagService = tagService;
    }

    @GetMapping
    public String latestBlog(Model model) {
        Optional<Post> latestPost = postService.getLatestPost();
        if (latestPost.isPresent()) {
            model.addAttribute("postUpdates", postUpdateService.getFormattedPostUpdates(latestPost.get()));

            List<String> tags = tagService.getTags(latestPost.get()).stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            renderPost(model, latestPost.get(), tags);

            Optional<Post> previousPost = postService.getPreviousPost(latestPost.get());
            model.addAttribute("showPrevious", previousPost.isPresent());
            previousPost.ifPresent(p -> model.addAttribute("previousPost", p.getUrlName()));
            model.addAttribute("showNext", false);
        } else {
            model.addAttribute("postTitle", "");
            model.addAttribute("postDate", "");
            model.addAttribute("postContent", "");
            model.addAttribute("showPrevious", false);
            model.addAttribute("showNext", false);
            model.addAttribute("tags", Collections.emptyList());
            model.addAttribute("postUpdates", Collections.emptyList());
        }

        return "blog";
    }

    @GetMapping("/{postUrlName}")
    public String blog(@PathVariable String postUrlName, Model model) {
        Optional<Post> requestedPost = postService.getPostByUrlName(postUrlName);

        if (requestedPost.isPresent()) {
            model.addAttribute("postUpdates", postUpdateService.getFormattedPostUpdates(requestedPost.get()));

            List<String> tags = tagService.getTags(requestedPost.get()).stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());
            renderPost(model, requestedPost.get(), tags);

            Optional<Post> previousPost = postService.getPreviousPost(requestedPost.get());
            model.addAttribute("showPrevious", previousPost.isPresent());
            previousPost.ifPresent(p -> model.addAttribute("previousPost", p.getUrlName()));

            Optional<Post> nextPost = postService.getNextPost(requestedPost.get());
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

        Optional<Post> post = postService.getPostByUrlName(postUrlName);

        if (!post.isPresent()) {
            return "redirect:/blog";
        }

        model.addAttribute("postTitle", post.get().getTitle());
        model.addAttribute("postContent", post.get().getContent());
        model.addAttribute("submitPath", "/blog/" + postUrlName + "/edit");

        List<String> tags = tagService.getTags(post.get()).stream()
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

        Optional<Post> postOptional = postService.getPostByUrlName(postUrlName);

        if (!postOptional.isPresent()) {
            return "redirect:/blog/{postUrlName}";
        }

        postService.editPost(postOptional.get(), blogPost.getPostTitle(), blogPost.getPostContent(),
                blogPost.getAddedTags());

        return "redirect:/blog/{postUrlName}";
    }

    @GetMapping("/{postUrlName}/update")
    public String updatePost(@PathVariable String postUrlName, Principal principal, Model model) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        Optional<Post> post = postService.getPostByUrlName(postUrlName);

        if (!post.isPresent()) {
            return "redirect:/blog";
        }

        List<String> tags = tagService.getTags(post.get()).stream()
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

        Optional<Post> postOptional = postService.getPostByUrlName(postUrlName);

        if (!postOptional.isPresent()) {
            return "redirect:/blog/{postUrlName}";
        }

        postUpdateService.addPostUpdate(postOptional.get(), blogPostUpdate.getPostContent(), LocalDateTime.now());

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
        model.addAttribute("ajaxBaseUrl", config.getUrl());

        return "admin/blog_edit";
    }

    // TODO use RETHROW in production
    @PostMapping("/add")
    public String submitPost(FormBlogPost blogPost, Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        postService.addPost(UUID.randomUUID(), blogPost.getPostTitle(), blogPost.getPostContent(), LocalDateTime.now(),
                blogPost.getAddedTags());

        return "redirect:/blog";
    }
}
