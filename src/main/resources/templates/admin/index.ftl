<html lang="en">
<head></head>
<body>
<h2>Home</h2>
Vous êtes connecté en tant qu'admin !
<button><a href="/logout">Déconnexion</a></button>
<br><br>
<#list list as article>
<a href="article/${article.id}"> ${article.id}  ${article.title}<a/> <button><a href="/deleteArticle/${article.id}">Supprimer</a></button>
    <br>
    </#list>
    <br>
    <form action="" method="post">
        <div>
            <label for="new">Ajouter un commentaire: </label><br>
            Titre : <input type="text" name="title" id="title" required><br>
            Contenu : <input type="text" name="content" id="content" required>
        </div>
        <div>
            <input type="submit" value="Envoyer">
        </div>
    </form>
</body>

</html>