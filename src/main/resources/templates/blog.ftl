<!DOCTYPE html>

<html>
<head>
    <meta charset="utf-8">
    <title>Zack Brown</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="/css/zackrbrown.css">
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">
</head>

<body>
<#include "/common/header.ftl"/>

<div class="content-container">
    <div class="post-title">${postTitle}</div>
    <span class="post-date">${postDate}</span>
    <p class="post-content">${postContent}</p>
    <div class="navigation-buttons">
        <#if showPrevious>
            <div class="previous-post"><a href="/blog?post=${previousPost}">&lt;&lt; Older</a></div>
        </#if>
        <#if showNext>
            <div class="next-post"><a href="/blog?post=${nextPost}">Newer &gt;&gt;</a></div>
        </#if>
    </div>
</div>

</body>
</html>