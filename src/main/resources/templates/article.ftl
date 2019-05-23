<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Test</title>
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body>

<h1>${article.title}</h1>
${article.content}
<br> <br>

<#list commentaires as commentaire>
    ${commentaire.text} <br>
</#list>

<br>
<form action="" method="post">
    <div>
        <label for="commentaire">Ajouter un commentaire: </label>
        <input type="text" name="text" id="commentaire" required>
    </div>
    <div>
        <input type="submit" value="Envoyer">
    </div>
</form>


</html>
</body>
</html>