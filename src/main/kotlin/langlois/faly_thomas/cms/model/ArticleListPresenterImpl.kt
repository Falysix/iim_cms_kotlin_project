package langlois.faly_thomas.cms.model;

class ArticleListPresenterImpl(private val model: Model, private val view: ArticleListPresenter.View): ArticleListPresenter {
    override fun start() {
        val list = model.getArticleList()
        view.displayArticleList(list)
    }

    override fun deleteArticle(id: Int) {
        model.deleteArticle(id)
    }

    override fun postArticle(title: String, content: String) {
        model.postArticle(title, content)
        start()
    }
}
