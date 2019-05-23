package langlois.faly_thomas.cms.model

import langlois.faly_thomas.cms.ConnectionPool

class AppComponents(val mySqlUrl : String, val mySqlUser: String, val mySqlPasword : String) {
    fun getPool():ConnectionPool{
        return ConnectionPool(mySqlUrl, mySqlUser, mySqlPasword)
    }

    fun getModel(): Model{
        return MysqlModel(getPool())
    }

    fun getArticleListPresenter(view: ArticleListPresenter.View): ArticleListPresenter {
        return ArticleListPresenterImpl(getModel(), view)
    }

    fun getArticlePresenter(view: ArticlePresenter.View): ArticlePresenterImpl {
        return ArticlePresenterImpl(getModel(), view)
    }

    fun getCommentaireListPresenter(view: CommentaireListPresenter.View): CommentaireListPresenterImpl {
        return CommentaireListPresenterImpl(getModel(), view)
    }
}