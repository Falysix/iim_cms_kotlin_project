package langlois.faly_thomas.cms.model

interface Model {
    fun getArticleList() : List<Article>
    fun getArticle(id: Int) : Article?
    fun getCommentairesArticle(articleId: Int) : List<Commentaire>
    fun postCommentaireArticle(text: String?, articleId: Int)
    fun deleteArticle(id: Int)
    fun postArticle(title: String, content: String)
    fun deleteCommentaire(id : Int)
}