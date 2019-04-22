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

<div class="contentContainer">
    <h1 class="contentTitle">Hello ${name}!</h1>
    <p class="content">I'm Zack Brown, a Software Engineer from Kansas City.

        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus tincidunt rhoncus mauris, eget pulvinar diam condimentum quis. Quisque et lorem sed dui lacinia mattis. Sed sed malesuada ex. Donec ligula urna, lobortis ac placerat eget, laoreet ut odio. Pellentesque sit amet ultricies mi. Aliquam condimentum porttitor fermentum. Maecenas et orci a ligula faucibus volutpat non eget enim. Suspendisse tincidunt, metus eu pellentesque ullamcorper, dolor sapien venenatis turpis, at lobortis ligula felis et quam.

        Nullam porta mauris vel purus ullamcorper, et laoreet risus laoreet. Praesent ac feugiat ante. Maecenas dolor mi, consequat ac ultricies sit amet, dictum eu magna. Sed luctus nibh libero, eu hendrerit leo hendrerit eget. Ut euismod urna eu est ullamcorper pellentesque. In id enim neque. Mauris efficitur leo nibh, non euismod lectus sodales ac. Vivamus tellus mi, euismod nec metus ac, convallis euismod massa. Phasellus malesuada dui massa. Aenean dictum est eu nunc efficitur lobortis. Quisque interdum libero arcu, in suscipit nisl congue nec. Nam nisi massa, iaculis id ligula ultricies, blandit malesuada tellus.

        Quisque sit amet leo in metus interdum sodales. Nulla vulputate dui nisi, vitae mattis enim lobortis id. Duis pulvinar, nibh luctus convallis vulputate, sapien tellus consequat justo, ac egestas odio nisl et risus. Vivamus placerat egestas dolor, id porttitor massa. Phasellus interdum eros eget vehicula blandit. Proin id metus ut sapien faucibus aliquet. Aliquam erat volutpat. Fusce tempus dolor consectetur tortor ultricies, sit amet interdum dui pretium. Morbi consectetur facilisis tempus. Curabitur quam magna, euismod nec dolor eu, dictum dictum elit. Interdum et malesuada fames ac ante ipsum primis in faucibus.

        In nec dui quis lorem blandit molestie. Maecenas sed risus tempor, pulvinar leo vel, sodales nisl. Cras eget arcu ligula. Suspendisse ac tempus sem, non tincidunt ante. Nunc non arcu ullamcorper, dignissim velit ut, eleifend enim. Nulla vestibulum porttitor tortor nec auctor. Proin sed euismod arcu. Donec feugiat sed diam non dignissim. Vivamus pellentesque vitae odio quis egestas. Fusce tristique nibh felis, laoreet iaculis lectus commodo ac.

        Phasellus sollicitudin metus venenatis, fringilla diam sit amet, tincidunt velit. Cras gravida erat et elementum cursus. Donec in est quam. Donec consequat sem dictum venenatis venenatis. Curabitur sit amet ultricies neque. Nulla nec rutrum felis. Phasellus tellus odio, facilisis sed viverra quis, placerat et ante. Vestibulum nec erat ipsum. Nullam a lorem sit amet urna rhoncus efficitur.
    </p>
</div>
<div class="contentContainer">
    <form action="">
        <div class="contentTitleEditContainer"><input title="Post title" type="text" class="contentTitleEdit"/></div>
        <br/>
        <textarea title="Post content" id="editor"></textarea>
        <div class="postActionContainer">
            <button type="submit" class="postAction">Post</button>
        </div>
    </form>
</div>

<script>
    var simplemde = new SimpleMDE({element: document.getElementById("editor")});
</script>

</body>
</html>