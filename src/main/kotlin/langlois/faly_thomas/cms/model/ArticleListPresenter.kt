package langlois.faly_thomas.cms.model

interface ArticleListPresenter {

    fun start()
    fun deleteArticle(id:Int)
    fun postArticle(title: String, content: String)

    interface View{
        fun displayArticleList(list: List<Article>)
    }

}