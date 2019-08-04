package models

import org.joda.time.DateTime
import play.api.libs.json._

case class GitHubIssues(id: Int,
                        created_at: String,
                        title: String,
                        number: Int)

object GitHubIssuesFormatter {
  implicit val gitHubIssuesFormat: OFormat[GitHubIssues] = Json.format[GitHubIssues]
}
