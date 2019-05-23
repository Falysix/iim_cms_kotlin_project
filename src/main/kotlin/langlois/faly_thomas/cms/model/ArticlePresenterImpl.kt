package langlois.faly_thomas.cms.model

class ArticlePresenterImpl(private val model: Model, private val view : ArticlePresenter.View) :ArticlePresenter {
    override fun start(id: Int) {
        val article = model.getArticle(id)
        val commentaires = model.getCommentairesArticle(id)
        if (article != null){
            view.displayArticle(article, commentaires)
            // view.displayCommentairesList(commentaires)
        } else{
            view.displayNotFound()
        }
    }
    override fun postCommentaire(text : String?, articleId: Int) {
        if (text != null || text!!.length > 0){
            model.postCommentaireArticle(text, articleId)
            start(articleId)
        }
    }

    override fun deleteCommentaire(id : Int, idArticle : Int){
        model.deleteCommentaire(id)
        start(idArticle)
    }
}