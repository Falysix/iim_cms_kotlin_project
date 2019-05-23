package langlois.faly_thomas.cms.model

interface CommentairePresenter {
    fun start(id: Int)

    interface View {
        fun displayCommentaire(id : Int)
    }
}