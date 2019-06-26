package com.zackrbrown.site.controller;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StaticContentController {

    @GetMapping
    public String content(Model model) {
        try {
            Path path = Paths.get(getClass().getResource("/static/markdown/" + getMarkdown() + ".md").toURI());

            try (Stream<String> fileLines = Files.lines(path)) {
                String content = fileLines.collect(Collectors.joining("\n"));

                Parser parser = Parser.builder().build();
                Node document = parser.parse(content);
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String renderedContent = renderer.render(document);

                model.addAttribute("content", renderedContent);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("title", getTitle());

        return "static_content";
    }

    public abstract String getTitle();

    public abstract String getMarkdown();
}
