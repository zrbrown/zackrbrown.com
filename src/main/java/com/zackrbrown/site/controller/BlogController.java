package com.zackrbrown.site.controller;

import com.zackrbrown.site.dao.Post;
import com.zackrbrown.site.dao.PostRepository;
import com.zackrbrown.site.model.FormBlogPost;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
public class BlogController {

    private final PostRepository postRepository;

    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public String blog(@RequestParam(defaultValue = "") String post, Model model) {
        if (post.isEmpty()) {
            Optional<Post> latestPost = postRepository.findAll(
                    PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                    .get().findFirst();
            if (latestPost.isPresent()) {
                renderPost(model, latestPost.get());
                model.addAttribute("previousPost", "x");
                model.addAttribute("nextPost", "y");
            } else {
                model.addAttribute("postTitle", "");
                model.addAttribute("postDate", "");
                model.addAttribute("postContent", "");
                model.addAttribute("showPrevious", false);
                model.addAttribute("showNext", false);
            }
        } else {
            Optional<Post> requestedPost = postRepository.findOne(
                    Example.of(new Post(null, post, null, null, null)));
            if (requestedPost.isPresent()) {
                renderPost(model, requestedPost.get());

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
        }

        return "blog";
    }

    private void renderPost(Model model, Post post) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(post.getContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedContent = renderer.render(document);

        model.addAttribute("postTitle", post.getTitle());
        model.addAttribute("postDate", post.getCreatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("postContent", renderedContent);
    }

    @GetMapping("/edit")
    public String edit(Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        return "admin/blog_edit";
    }

    // TODO use RETHROW in production
    @PostMapping("/edit")
    public String postBlogEntry(FormBlogPost blogPost, Principal principal) {
        // TODO pull from config
        if (!"zrbrown".equals(principal.getName())) {
            return "redirect:/blog";
        }

        Post post = new Post(
                UUID.randomUUID(),
                "test-post",
                blogPost.getPostTitle(),
                blogPost.getPostContent(),
                LocalDateTime.now());
        postRepository.save(post);

        return "redirect:/blog";
    }
}
