package langlois.faly_thomas.cms

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.sessions.*
import kotlinx.coroutines.launch
import langlois.faly_thomas.cms.model.*
import langlois.faly_thomas.cms.tpl.ArticleContext
import langlois.faly_thomas.cms.tpl.IndexContext
import java.time.Duration

data class AuthSession(val user: String)
class App

fun main(){

    val appComponents = AppComponents(
        "jdbc:mysql://localhost/iim_cms_kotlin?serverTimezone=UTC",
        "root",
        "root"
    )
    embeddedServer(Netty, 8080){

        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(App::class.java.classLoader, "templates")
        }

        install(Sessions) {
            cookie<AuthSession> ("Auth_SESSION", SessionStorageMemory()){
                cookie.duration = Duration.ofMinutes(30)
            }
        }

        install(Authentication) {
            form("equal-auth") {
                userParamName = "user"
                passwordParamName = "password"
                challenge = FormAuthChallenge.Redirect { "/login" }
                validate { credentials ->
                    if (credentials.name == "admin" && credentials.password == "admin") {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
                skipWhen { call -> call.sessions.get<AuthSession>() != null }
            }
        }

        routing {
            static ("static") {
                resources("static")
            }
            get("article/{id}") {
                val controller = appComponents.getArticlePresenter(object : ArticlePresenter.View {
                    override fun displayNotFound() {
                        launch {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                    override fun displayArticle(article: Article, commentaires : List<Commentaire>) {
                        val context = ArticleContext(article, commentaires)
                        launch {
                            val session = call.sessions.get<AuthSession>()
                            if (session != null && session.user == "admin"){
                                call.respond(FreeMarkerContent("admin/article.ftl", context, "e"))
                            }
                            else{
                                call.respond(FreeMarkerContent("article.ftl", context, "e"))
                            }
                        }
                    }

                })

                val id = call.parameters["id"]!!.toIntOrNull()
                if (id == null){
                    call.respond(HttpStatusCode.NotFound)
                } else{
                    controller.start(id)
                }
            }

            post("article/{id}") {
                val controller = appComponents.getArticlePresenter(object : ArticlePresenter.View {
                    override fun displayNotFound() {
                        launch {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                    override fun displayArticle(article: Article, commentaires : List<Commentaire>) {
                        val context = ArticleContext(article, commentaires)
                        launch {
                            val session = call.sessions.get<AuthSession>()
                            if (session != null && session.user == "admin"){
                                call.respond(FreeMarkerContent("admin/article.ftl", context, "e"))
                            }
                            else{
                                call.respond(FreeMarkerContent("article.ftl", context, "e"))
                            }
                        }
                    }
                })

                val postParameters : Parameters = call.receiveParameters()
                val text: String? = postParameters["text"]
                val id = call.parameters["id"]!!.toIntOrNull()
                if (id != null){
                    controller.postCommentaire(text, id)
                }
            }

            get( "/"){
                val controller = appComponents.getArticleListPresenter(object:ArticleListPresenter.View {
                    override fun displayArticleList(list: List<Article>) {
                        val context = IndexContext(list)
                        launch{
                            val session = call.sessions.get<AuthSession>()
                            if (session != null && session.user == "admin"){
                                call.respond(FreeMarkerContent("admin/index.ftl", context, "e"))
                            }
                            else{
                                call.respond(FreeMarkerContent("index.ftl", context, "e"))
                            }
                        }
                    }
                })
                controller.start()
            }

            get("/login") {
                val session = call.sessions.get<AuthSession>()
                if (session != null){
                    call.respondRedirect("/", permanent = true)
                }
                else{
                    call.respond(FreeMarkerContent("login.ftl", null, "e"))
                }
            }

            route("/logout"){
                get("") {
                    call.sessions.clear<AuthSession>()
                    launch {
                        call.respondRedirect("/", permanent = false)
                    }
                }
            }

            authenticate("equal-auth") {
                post("/login") {
                    val principal = call.authentication.principal<UserIdPrincipal>()
                    call.sessions.set(AuthSession(principal!!.name))
                    call.respondRedirect("/")
                }
                route("/deleteArticle/{id}") {
                    get("") {
                        val controller = appComponents.getArticleListPresenter(object : ArticleListPresenter.View {
                            override fun displayArticleList(list: List<Article>) {
                                launch {
                                    call.respondRedirect("/")
                                }
                            }
                        })
                        controller.start()

                        val id = call.parameters["id"]!!.toIntOrNull()
                        if (id != null) {
                            controller.deleteArticle(id)
                        }
                    }
                }
                route("deleteCommentaire/{articleid}/{commentaireid}") {
                    get("") {
                        val articleid = call.parameters["articleid"]!!.toIntOrNull()
                        val commentaireid = call.parameters["commentaireid"]!!.toIntOrNull()
                        val controller = appComponents.getArticlePresenter(object : ArticlePresenter.View {
                            override fun displayNotFound() {
                                launch {
                                    call.respond(HttpStatusCode.NotFound)
                                }
                            }
                            override fun displayArticle(article: Article, commentaires : List<Commentaire>) {
                                val context = ArticleContext(article, commentaires)
                                launch {
                                    call.respondRedirect("/article/${articleid}")
                                }
                            }
                        })

                        if (commentaireid != null && articleid != null) {
                            controller.deleteCommentaire(commentaireid, articleid)
                        }
                    }
                }
                post("/") {
                    val controller = appComponents.getArticleListPresenter(object : ArticleListPresenter.View {
                        override fun displayArticleList(list: List<Article>) {
                            val context = IndexContext(list)
                            launch{
                                val session = call.sessions.get<AuthSession>()
                                if (session != null && session.user == "admin"){
                                    call.respond(FreeMarkerContent("admin/index.ftl", context, "e"))
                                }
                                else{
                                    call.respond(FreeMarkerContent("index.ftl", context, "e"))
                                }
                            }
                        }
                    })

                    val postParameters : Parameters = call.receiveParameters()
                    val title: String? = postParameters["title"]
                    val content: String? = postParameters["content"]
                    if (title != null && content != null){
                        controller.postArticle(title, content)
                    }
                }
            }
        }
    }.start(wait = true)



}