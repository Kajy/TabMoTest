package models.Media

import akka.http.scaladsl.model.DateTime
import play.api.libs.json.{Json, OFormat}

case class MediaType(title: String,
                     country: String,
                     year: Int,
                     genre: Seq[String],
                     ranking: Int,
                     original_title: Option[String] = None,
                     french_release: Option[String] = None,
                     synopsis: Option[String] = None) {
  require(title.length < 250)
  require(country.matches("[A-Z]{3}"))
}

object MediaTypeFormatter {
  implicit val mediaTypeFormatter: OFormat[MediaType] = Json.format[MediaType]
}