package services


import models.Media.MediaTypeFormatter._
import javax.inject.Inject
import models.Media.MediaType
import play.api.libs.json.{JsObject, JsString, JsValue, Json}
import utils.MediaDAO

import scala.concurrent.{ExecutionContext, Future}

case class MediaService @Inject()(mediaDAO: MediaDAO)(implicit ec: ExecutionContext){

  def getMedia: Future[JsValue] = {
    mediaDAO.get.map {
      case Some(media) => Json.toJson(media)
      case None => JsObject(Nil)
    }
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
