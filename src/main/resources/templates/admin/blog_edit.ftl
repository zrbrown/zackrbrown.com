<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="../../static/css/zackrbrown.css"/>
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
        <input title="Add tag" type="text" class="add-tag" name="addTag">
        <#list tags>
            <div class="tag-container">
                <#items as tag>
                    <div class="tag-wrapper">
                        <a href="" class="tag">${tag}</a>
                        <a href="" class="tag tag-remove">&#9447;</a>
                    </div>
                </#items>
            </div>
        </#list>
        <div class="post-action-container">
            <button type="submit" class="post-action">Post</button>
        </div>
    </form>
</div>

<script>
    var simplemde = new SimpleMDE({element: document.getElementById("editor")});
</script>

</body>
</html>