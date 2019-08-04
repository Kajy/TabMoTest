package models

import play.api.libs.json._

case class GitHubUser(id: Int,
                      login: String)

object GitHubUserFormatter {
  implicit val gitHubUserFormat: OFormat[GitHubUser] = Json.format[GitHubUser]
}
