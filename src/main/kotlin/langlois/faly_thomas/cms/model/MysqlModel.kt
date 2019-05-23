package langlois.faly_thomas.cms.model

import langlois.faly_thomas.cms.ConnectionPool

class MysqlModel (private val pool : ConnectionPool): Model {

    override fun getArticleList(): List<Article> {
        val list = ArrayList<Article>()

        pool.useConnection { connection ->
            connection.prepareStatement("SELECT id, title FROM articles").use {stmt ->
                stmt.executeQuery().use() {result ->
                    while (result.next()) {
                        list += Article(
                            result.getInt("id"),
                            result.getString("title"),
                            null
                        )
                    }
                }
            }
            return list
        }


        return list
    }

    override fun getArticle(id: Int): Article? {
        pool.useConnection { connection ->
            connection.prepareStatement("SELECT * FROM articles where id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use {result ->
                    if (result.next()) {
                        return Article(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("content")
                        )
                    }
                }
            }
        }
        return null
    }

    override fun getCommentairesArticle(articleId: Int) : List<Commentaire> {
        val list = ArrayList<Commentaire>()

        pool.useConnection { connection ->
            connection.prepareStatement("SELECT id, text FROM commentaires where idArticle = ?").use {stmt ->
                stmt.setInt(1, articleId)
                stmt.executeQuery().use() {result ->
                    while (result.next()) {
                        list += Commentaire(
                            result.getInt("id"),
                            result.getString("text")
                        )
                    }
                }
            }
            return list
        }
        return list
    }

    override fun postCommentaireArticle(text: String?, articleId: Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("INSERT INTO commentaires (idArticle, text) VALUES (?, ?)").use {stmt ->
                stmt.setInt(1, articleId)
                stmt.setString(2, text)

                stmt.executeUpdate()
            }
        }
    }

    override fun postArticle(title: String, content: String) {
        pool.useConnection { connection ->
            connection.prepareStatement("INSERT INTO articles (title, content) VALUES (?, ?)").use {stmt ->
                stmt.setString(1, title)
                stmt.setString(2, content)

                stmt.executeUpdate()
            }
        }
    }

    override fun deleteArticle(id: Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("DELETE FROM articles WHERE id = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
            connection.prepareStatement("DELETE FROM commentaires WHERE idArticle = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }

    override fun deleteCommentaire(id : Int) {
        pool.useConnection { connection ->
            connection.prepareStatement("DELETE FROM commentaires WHERE id = ?").use {stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }

}