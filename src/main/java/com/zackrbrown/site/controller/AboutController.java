package com.zackrbrown.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController extends StaticContentController {

    @Override
    public String getTitle() {
        return "About";
    }

    @Override
    public String getMarkdown() {
        return "about";
    }
}