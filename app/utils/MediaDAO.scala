package utils

import javax.inject.Inject
import models.Media.MediaType
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class MediaDAO @Inject()(implicit ec: ExecutionContext) {

  var mediaData: Seq[MediaType] =  Seq.empty

  def get: Seq[MediaType] = {
    mediaData
  }

  def put(newMedia: MediaType) = {
    mediaData = mediaData ++ Seq(newMedia)
  }
}
