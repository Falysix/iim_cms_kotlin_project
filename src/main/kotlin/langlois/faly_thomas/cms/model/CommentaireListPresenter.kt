package langlois.faly_thomas.cms.model

interface CommentaireListPresenter {

    fun start(articleId : Int)

    interface View{
        fun displayCommentairesList(list: List<Commentaire>)
    }

}