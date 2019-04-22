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
    <h1 class="post-title">${postTitle}</h1>
    <p class="post-content">${postContent}</p>
</div>
<div class="content-container">
    <form action="/blog" method="post">
        <div class="content-title-edit-container">
            <input title="Post title" type="text" class="content-title-edit" name="postTitle"/>
        </div>
        <br/>
        <textarea title="Post content" id="editor" name="postContent"></textarea>
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