import java.util.concurrent.TimeUnit

import play.api.inject.Injector
import play.api.{Application, Environment, Mode}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import services.MediaService
import utils.{GitHubDAO, MediaDAO}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

trait BaseTest extends PlaySpec with GuiceOneServerPerSuite {
  implicit lazy override val app: Application = new GuiceApplicationBuilder()
    .configure("url.github" -> "https://api.github.com")
    .build()

  implicit lazy val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  lazy val injector: Injector = app.injector.instanceOf[Injector]
  implicit lazy val wsClient: WSClient = app.injector.instanceOf[WSClient]
  implicit lazy val githubDAO: GitHubDAO = injector.instanceOf[GitHubDAO]
  implicit lazy val mediaDAO: MediaDAO = injector.instanceOf[MediaDAO]
  implicit lazy val mediaService: MediaService = injector.instanceOf[MediaService]

  def await[T](future: Future[T], duration: Duration = Duration(10, TimeUnit.SECONDS)): T = Await.result(future, duration)
}
