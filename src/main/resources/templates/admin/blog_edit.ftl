<!DOCTYPE html>

<html>
<head>
    <meta charset="utf-8">
    <title>Zack Brown</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="/css/zackrbrown.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.css">

    <script src="https://cdn.jsdelivr.net/simplemde/latest/simplemde.min.js"></script>
</head>

<body>
<#include "/common/header.ftl"/>

<div class="content-container">
    <form action="${submitPath}" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="content-title-edit-container">
            <input title="Post title" type="text" class="content-title-edit" name="postTitle" value="${postTitle}"/>
        </div>
        <br/>
        <textarea title="Post content" id="editor" name="postContent">${postContent}</textarea>
        <input title="Tag name entry" type="text" class="add-tag-text" name="addTag" id="addTagInput"/>
        <button title="Add tag" type="button" onclick="addTagToTags()">Add tag</button>
        <div class="tag-container" id="tagContainer">
            <#list tags>
                <#items as tag>
                    <div class="tag-wrapper">
                        <span class="tag">${tag}</span><a href="" class="tag tag-remove">&#9447;</a>
                    </div>
                </#items>
            </#list>
        </div>
        <div class="post-action-container">
            <button type="submit" class="post-action">Post</button>
        </div>
    </form>
</div>

<script>
    var simplemde = new SimpleMDE({element: document.getElementById("editor")});

    var tagContainer = document.getElementById("tagContainer");

    function addTagToTags() {
        let tagName = document.getElementById("addTagInput").value;

        let tagSpan = document.createElement("span");
        tagSpan.setAttribute("class", "tag");
        tagSpan.appendChild(document.createTextNode(tagName));

        let tagRemoveLink = document.createElement("a");
        tagRemoveLink.setAttribute("href", "");
        tagRemoveLink.setAttribute("class", "tag tag-remove");
        tagRemoveLink.appendChild(document.createTextNode("ⓧ"));

        let tagWrapper = document.createElement("div");
        tagWrapper.setAttribute("class", "tag-wrapper");
        tagWrapper.appendChild(tagSpan);
        tagWrapper.appendChild(tagRemoveLink);

        tagContainer.appendChild(tagWrapper);

        let tagSubmitInput = document.createElement("input");
        tagSubmitInput.setAttribute("type", "hidden");
        tagSubmitInput.setAttribute("name", "addedTags");
        tagSubmitInput.setAttribute("value", tagName);

        tagContainer.appendChild(tagSubmitInput);
    }
</script>

</body>
</html>