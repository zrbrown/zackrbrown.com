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
<div class="header">
    <ul class="headerLinkContainer">
        <li><a href="" title="Blog">Blog</a></li>
        <li><a href="" title="Projects">Projects</a></li>
        <li><a href="" title="About">About</a></li>
    </ul>
    <div class="headerIconContainer">
        <a href="//github.com/zrbrown" class="headerIcon"><span class="fa fa-github fa-2x"></span></a>
        <a href="//twitter.com/zackrbrown" class="headerIcon"><span class="fa fa-twitter fa-2x"></span></a>
        <a href="//www.linkedin.com/profile/view?id=104289701" class="headerIcon"><span
                    class="fa fa-linkedin fa-2x"></span></a>
        <a href="//www.facebook.com/zackrbrown" class="headerIcon"><span class="fa fa-facebook fa-2x"></span></a>
    </div>
</div>

<div class="contentContainer">
    <h1 class="contentTitle">Hello ${name}!</h1>
    <p class="content">I'm Zack Brown, a Software Engineer from Kansas City.</p>
</div>
<div class="contentContainer">
    <form action="">
        <label for="title">Title</label>
        <input type="text" id="title">
        <br/>
        <label for="editor">Content</label>
        <textarea id="editor"></textarea>
    </form>
</div>

<script>
    var simplemde = new SimpleMDE({element: document.getElementById("editor")});
</script>

</body>
</html>