package langlois.faly_thomas.cms.tpl

import langlois.faly_thomas.cms.model.Article
import langlois.faly_thomas.cms.model.Commentaire

data class ArticleContext (val article: Article, val commentaires : List<Commentaire>)
