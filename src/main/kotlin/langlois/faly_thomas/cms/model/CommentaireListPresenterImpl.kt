package langlois.faly_thomas.cms.model;

class CommentaireListPresenterImpl(private val model: Model, private val view: CommentaireListPresenter.View): CommentaireListPresenter {
    override fun start(articleId: Int) {
        val list = model.getCommentairesArticle(articleId)
        view.displayCommentairesList(list)
    }
}
