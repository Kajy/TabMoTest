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
  require(title.length < 250, "Media title length must be < 250")
  require(country.matches("[A-Z]{3}"), "Media country format require 3 letters (ex: [FRA], [GER], [SPN] ...")
  require(!genre.exists(_.length > 50), "Media genre length must be < 50")
  require(ranking <= 10 && ranking >= 0, "Media ranking must be between 0 and 10")
  require(french_release.forall(_.matches("([12]\\d{3}/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01]))")), "Media date format require (YYYY/MM/DD)")
  require({
    if (country != "FRA") original_title.exists(_.length < 250)
    else original_title.isEmpty
  }, "Original title require only if country != [FRA] and length < 250")
}

object MediaTypeFormatter {
  implicit val mediaTypeFormatter: OFormat[MediaType] = Json.format[MediaType]
}