package utils

import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalTime}
import java.time.format.DateTimeFormatter
import java.util.Calendar

import akka.actor.Status.Success
import javax.inject._
import models.{GitHubCommit, GitHubIssues, GitHubRepository, GitHubUser}
import models.GitHubRepositoryFormatter._
import models.GitHubCommitFormatter._
import models.GitHubUserFormatter._
import models.GitHubIssuesFormatter._
import play.api.Configuration
import play.api.libs.json.{JsArray, JsValue}
import play.api.libs.ws.{WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

case class GitHubDAO @Inject()(wsClient: WSClient,
                               conf: Configuration)(implicit ec: ExecutionContext){

  def urlBase: String = conf.get[String]("url.github")

  def parallel(fSeq: Seq[Future[JsValue]]): Future[Seq[JsValue]] = {
     Future.sequence(fSeq).map(value => value)
  }

  def callGet(url: String): Future[WSResponse] = {
    wsClient.url(url).get
  }

  def getUserInfos(user: String): Future[GitHubUser] = {
    wsClient.url(s"$urlBase/users/$user").get.map( res => {
      res.json.as[GitHubUser]
    })
  }

  def getUserRepos(user: String): Future[Seq[GitHubRepository]] = {
    wsClient.url(s"$urlBase/users/$user/repos").get.map( res => {
      res.json.as[JsArray].value.map(repo => repo.as[GitHubRepository])
    })
  }

  def getContributorsByRepo(user: String, repos: String): Future[WSResponse] = {
    wsClient.url(s"$urlBase/repos/$user/$repos/contributors").get
  }

  def getCommitsByRepo(user: String, repos: String): Future[Seq[GitHubCommit]] = {
    wsClient.url(s"$urlBase/repos/$user/$repos/commits").get.map( res => {
      res.json.as[JsArray].value.map(commit => commit.as[GitHubCommit])
    })
  }

  def getIssuesByRepo(user: String, repos: String): Future[Seq[GitHubIssues]] = {
    val formatterDate = new SimpleDateFormat("yyyy-MM-dd")
    val date =  LocalDate.parse(formatterDate.format(Calendar.getInstance.getTime)).minusDays(90).toString
    println(date)
    wsClient.url(s"$urlBase/repos/$user/$repos/issues?state=all&since=$date").get.map( res => {
      res.json.as[JsArray].value.map(commit => commit.as[GitHubIssues])
    })
  }
}