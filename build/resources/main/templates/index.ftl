<html lang="en">
<head></head>
<body>
<h2>Home</h2>
<button><a href="/login">Connexion</a></button>
<br><br>
<#list list as article>
<a href="article/${article.id}"> ${article.title}<a/>
    <br>
    </#list>
</body>

</html>