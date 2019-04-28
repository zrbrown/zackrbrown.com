package com.zackrbrown.site.model;

import java.util.List;

public class FormBlogPost {

    private String postTitle;
    private String postContent;
    private List<String> addedTags;

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public List<String> getAddedTags() {
        return addedTags;
    }

    public void setAddedTags(List<String> addedTags) {
        this.addedTags = addedTags;
    }
}
