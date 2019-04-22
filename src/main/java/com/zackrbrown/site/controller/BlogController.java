package com.zackrbrown.site.controller;

import com.zackrbrown.site.model.FormBlogPost;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlogController {

    @GetMapping("/blog")
    public String blog(Model model) {
        String testString = "# Testing\nI'm Zack Brown, a software engineer from **Kansas City, MO**.";

        Parser parser = Parser.builder().build();
        Node document = parser.parse(testString);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedContent = renderer.render(document);

        model.addAttribute("postTitle", "Hello World!");
        model.addAttribute("postContent", renderedContent);

        return "blog";
    }

    // TODO use RETHROW in production
    @PostMapping("/blog")
    public String postBlogEntry(FormBlogPost blogPost, Model model) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(blogPost.getPostContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String renderedContent = renderer.render(document);

        model.addAttribute("postTitle", blogPost.getPostTitle());
        model.addAttribute("postContent", renderedContent);

        return "blog";
    }
}
