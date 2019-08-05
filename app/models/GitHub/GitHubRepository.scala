package models.GitHub

import play.api.libs.json._

case class GitHubRepository(id: Int,
                            name: String,
                            languages_url: String,
                            url: String,
                            language: String)

object GitHubRepositoryFormatter {
  implicit val gitHubRepositoryFormat: OFormat[GitHubRepository] = Json.format[GitHubRepository]
}
