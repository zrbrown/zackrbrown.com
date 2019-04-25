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
import org.springframework.web.bind.annotation.RequestMapping;

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
            model.addAttribute("postDate", latestPost.get().getCreatedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
            model.addAttribute("postContent", renderedContent);
        } else {
            model.addAttribute("postTitle", "");
            model.addAttribute("postDate", "");
            model.addAttribute("postContent", "");
        }

        return "blog";
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
