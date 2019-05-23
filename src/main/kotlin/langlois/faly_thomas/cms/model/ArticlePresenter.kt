package langlois.faly_thomas.cms.model

interface ArticlePresenter {
    fun start(id: Int)
    fun postCommentaire(text: String?, articleId: Int)
    fun deleteCommentaire(id : Int, idArticle : Int)

    interface View {
        fun displayArticle(article : Article, commentaires: List<Commentaire>)
        fun displayNotFound()
    }
}