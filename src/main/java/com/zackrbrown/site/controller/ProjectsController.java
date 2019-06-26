package com.zackrbrown.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectsController extends StaticContentController {

    @Override
    public String getTitle() {
        return "Projects";
    }

    @Override
    public String getMarkdown() {
        return "projects";
    }
}