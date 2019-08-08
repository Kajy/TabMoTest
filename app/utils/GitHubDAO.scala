package utils

import java.time.LocalDate

import javax.inject._
import models.GitHub.{GitHubCommit, GitHubIssues, GitHubRepository, GitHubUser}
import models.GitHub.GitHubRepositoryFormatter._
import models.GitHub.GitHubCommitFormatter._
import models.GitHub.GitHubUserFormatter._
import models.GitHub.GitHubIssuesFormatter._
import models.GitHub.{GitHubIssues, GitHubRepository, GitHubUser}
import play.api.Configuration
import play.api.libs.json.{JsArray, JsValue}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

case class GitHubDAO @Inject()(wsClient: WSClient,
                               conf: Configuration)(implicit ec: ExecutionContext){

  def urlBase: String = conf.getOptional[String]("url.github").getOrElse("https://api.github.com")

  def parallel(fSeq: Seq[Future[JsValue]]): Future[Seq[JsValue]] = {
     Future.sequence(fSeq).map(value => value)
  }

  def callGet(url: String): Future[WSResponse] = {
    wsClient.url(url).get
  }

  def getUserInfos(user: String): Future[GitHubUser] = {
    wsClient.url(s"$urlBase/users/$user").get.map( res => {
      if (res.status == 404)
        throw new Exception(res.json.toString)
      res.json.as[GitHubUser]
    })
  }

  def getUserRepos(user: String): Future[Seq[GitHubRepository]] = {
    wsClient.url(s"$urlBase/users/$user/repos").get.map( res => {
      if (res.status == 404)
        throw new Exception(res.json.toString)
      res.json.as[JsArray].value.map(repo => repo.as[GitHubRepository])
    })
  }

  def getContributorsByRepo(user: String, repos: String): Future[WSResponse] = {
    wsClient.url(s"$urlBase/repos/$user/$repos/contributors").get
  }

  def getCommitsByRepo(user: String, repos: String): Future[Seq[GitHubCommit]] = {
    wsClient.url(s"$urlBase/repos/$user/$repos/commits").get.map( res => {
      if (res.status == 404)
        throw new Exception(res.json.toString)
      res.json.as[JsArray].value.map(commit => commit.as[GitHubCommit])
    })
  }

  def getIssuesByRepo(user: String, repos: String, until: LocalDate): Future[Seq[GitHubIssues]] = {
    wsClient.url(s"$urlBase/repos/$user/$repos/issues?state=all&since=${until.toString}").get.map( res => {
      if (res.status == 404)
        throw new Exception(res.json.toString)
      res.json.as[JsArray].value.map(commit => commit.as[GitHubIssues])
    })
  }
}