package services

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

import javax.inject.Inject
import play.api.libs.json._
import utils.GitHubDAO

import scala.concurrent.{ExecutionContext, Future}

case class GitHubService @Inject()(gitHubDAO: GitHubDAO)(implicit ec: ExecutionContext){

  def getAllLanguagesByRepos(user: String): Future[JsValue] = {
    gitHubDAO.getUserRepos(user).map(repos => {
      repos.map(repo => gitHubDAO.callGet(repo.languages_url).map(_.json)
      )
    }).flatMap(gitHubDAO.parallel).map(languages => JsObject(JsArray(languages
      .flatMap(languages => languages.as[JsObject].value.keySet
        .map(JsString))).as[Seq[String]].groupBy(identity).mapValues(size => JsNumber(size.size)).toSeq.sortWith(_._2.as[Int] >_._2.as[Int]))
    )

  }

  def getMostImportantContributionByRepos(user: String, repos: String): Future[JsValue] = {
    gitHubDAO.getContributorsByRepo(user, repos).map (res => JsArray{
      if (res.status == 404)
        throw new Exception("Error User")
      res.json.asInstanceOf[JsArray].value.foldLeft(Json.arr())((arr, value) => {
        val obj = value.asInstanceOf[JsObject].value
        arr.+:(JsObject(Map("user" -> obj.getOrElse("login", JsString("NULL")), "contributions" -> obj.getOrElse("contributions", JsString("NULL")))))
        }).as[Seq[JsObject]].sortWith(_.value.getOrElse("contributions", JsNumber(0)).as[Int] > _.value.getOrElse("contributions", JsNumber(0)).as[Int]).take(10)
      })
  }

  def getMostImportantCommitterByRepos(user: String, repos: String): Future[JsValue] = {
    gitHubDAO.getCommitsByRepo(user, repos).map(res => {
      JsObject(res.takeRight(100).map(commit => commit.commit.author.name).groupBy(l => l).map(t => (t._1, JsNumber(t._2.length))).toSeq.sortWith(_._2.as[Int] > _._2.as[Int]))
    })
  }


  def getIssuesByRepos(user: String, repos: String, until: Int): Future[JsValue] = {
    val formatterDate = new SimpleDateFormat("yyyy-MM-dd")
    val dateFrom =  LocalDate.parse(formatterDate.format(Calendar.getInstance.getTime)).minusDays(until)
    val dateUntil =  LocalDate.parse(formatterDate.format(Calendar.getInstance.getTime))
    val days =      dateFrom.toEpochDay.until(dateUntil.toEpochDay).map(LocalDate.ofEpochDay)

    gitHubDAO.getIssuesByRepo(user, repos, dateFrom).map(res => {
      JsObject(days.map(day => (day.toString, JsNumber(res.count(issue => LocalDate.parse(issue.created_at.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString == day.toString)))))
    })
  }
}
