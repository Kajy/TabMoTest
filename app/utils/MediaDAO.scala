package utils

import javax.inject.Inject
import models.Media.MediaType
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class MediaDAO @Inject()(implicit ec: ExecutionContext) {

  var mediaData: Seq[MediaType] =  Seq.empty

  def get: Future[Option[MediaType]] = {
    Future { mediaData.headOption }
  }

  def put(newMedia: MediaType) = {
    mediaData = mediaData ++ Seq(newMedia)
  }
}
