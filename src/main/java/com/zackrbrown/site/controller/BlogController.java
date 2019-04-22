package com.zackrbrown.site.controller;

import com.zackrbrown.site.dao.Post;
import com.zackrbrown.site.dao.PostRepository;
import com.zackrbrown.site.model.FormBlogPost;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BlogController {

    private final PostRepository postRepository;

    public BlogController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        Optional<Post> latestPost = postRepository.findAll(
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdDateTime"))
                .get().findFirst();
        if (latestPost.isPresent()) {
            Parser parser = Parser.builder().build();
            Node document = parser.parse(latestPost.get().getContent());
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String renderedContent = renderer.render(document);

            model.addAttribute("postTitle", latestPost.get().getTitle());
            model.addAttribute("postContent", renderedContent);
        } else {
            model.addAttribute("postTitle", "");
            model.addAttribute("postContent", "");
        }

        return "blog";
    }

    // TODO use RETHROW in production
    @PostMapping("/blog")
    public String postBlogEntry(FormBlogPost blogPost, Model model) {
        Post post = new Post(
                UUID.randomUUID(),
                "test-post",
                blogPost.getPostTitle(),
                blogPost.getPostContent(),
                LocalDateTime.now());
        postRepository.save(post);

        Parser parser = Parser.builder().build();
        Node document = parser.parse(blogPost.getPostContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedContent = renderer.render(document);

        model.addAttribute("postTitle", blogPost.getPostTitle());
        model.addAttribute("postContent", renderedContent);

        return "blog";
    }
}
