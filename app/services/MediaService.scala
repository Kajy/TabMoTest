package services


import models.Media.MediaTypeFormatter._
import javax.inject.Inject
import models.Media.MediaType
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString, JsValue, Json}
import utils.MediaDAO

import scala.concurrent.{ExecutionContext, Future}

case class MediaService @Inject()(mediaDAO: MediaDAO)(implicit ec: ExecutionContext){

  def getAllMedias: Future[JsValue] = {
    Future { JsArray(mediaDAO.get.map(media => Json.toJson(media))) }
  }

  def getMediasByGenre(genre: String): Future[JsValue] = {
    Future { JsArray(mediaDAO.get.filter(_.genre.contains(genre)).map(media => Json.toJson(media))) }
  }

  def getMediaByYear: Future[JsValue] = {
    Future { JsObject(mediaDAO.get.groupBy(_.year).map(year => (year._1.toString, JsNumber(year._2.size))))}
  }

  def putMedia(maybeMedia: Option[JsValue]): Future[Either[JsValue, JsValue]] = {
    maybeMedia match {
      case Some(media) => {
        try {
          val newMedia = media.as[MediaType]
          mediaDAO.put(newMedia)
          Future {
            Right(JsString("Media uploaded !"))
          }
        }
        catch {
          case ex: IllegalArgumentException => Future { Left(JsString(ex.getMessage)) }
        }
      }
      case None => Future { Left(JsString("Error no body")) }
    }
  }
}
