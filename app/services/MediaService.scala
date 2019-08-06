package services


import models.Media.MediaTypeFormatter._
import javax.inject.Inject
import models.Media.MediaType
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue, Json}
import utils.MediaDAO

import scala.concurrent.{ExecutionContext, Future}

case class MediaService @Inject()(mediaDAO: MediaDAO)(implicit ec: ExecutionContext){

  def getAllMedias: Future[JsValue] = {
    Future { JsArray(mediaDAO.get.map(media => Json.toJson(media))) }
  }

  def getMediasByGenre(genre: String): Future[JsValue] = {
    Future { JsArray(mediaDAO.get.filter(_.genre.contains(genre)).map(media => Json.toJson(media))) }
  }

  def putMedia(maybeMedia: Option[JsValue]): Future[Either[JsValue, JsValue]] = {
    maybeMedia match {
      case Some(media) => {
        val newMedia = media.as[MediaType]
        mediaDAO.put(newMedia)
        Future { Right(JsString("Media uploaded !"))}
      }
      case None => Future { Left(JsString("Error no body")) }
    }
  }
}
