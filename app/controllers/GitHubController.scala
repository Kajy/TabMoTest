package controllers

import javax.inject._
import akka.actor.ActorSystem
import play.api.mvc._
import services.GitHubService

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class GitHubController @Inject()(cc: ControllerComponents,
                                gitHubService: GitHubService,
                                actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def getAllLanguagesByRepos(user: String): Action[AnyContent] = Action.async {
    gitHubService.getAllLanguagesByRepos(user).map { msg => Ok(msg) }
  }

  def getMostImportantCommiterByRepos(user: String, repos: String): Action[AnyContent] = Action.async {
    gitHubService.getMostImportantCommitterByRepos(user, repos).map (res => Ok(res))
  }

  def getIssuesStats(user: String, repos: String, until: Int = 30): Action[AnyContent] = Action.async {
    gitHubService.getIssuesByRepos(user, repos, until).map(res => Ok(res))
  }
}
