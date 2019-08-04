package models

import play.api.libs.json._

case class GitHubCommitAuthor(name: String,
                              email: String,
                              date: String)

case class GitHubCommitCommitter(name: String,
                              email: String,
                              date: String)

case class GitHubCommitInfos(author: GitHubCommitAuthor,
                             committer: GitHubCommitCommitter)

case class GitHubCommit(commit: GitHubCommitInfos,
                        url: String,
                        comments_url: String)

object GitHubCommitFormatter {
  implicit val gitHubCommitAuthorFormat: OFormat[GitHubCommitAuthor] = Json.format[GitHubCommitAuthor]
  implicit val gitHubCommitCommitterFormat: OFormat[GitHubCommitCommitter] = Json.format[GitHubCommitCommitter]
  implicit val gitHubCommitInfosFormat: OFormat[GitHubCommitInfos] = Json.format[GitHubCommitInfos]
  implicit val gitHubCommitFormat: OFormat[GitHubCommit] = Json.format[GitHubCommit]
}
