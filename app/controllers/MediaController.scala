package controllers

import akka.actor.ActorSystem
import javax.inject._
import play.api.mvc.{AnyContent, _}
import services.MediaService

import scala.concurrent.ExecutionContext

@Singleton
class MediaController @Inject()(cc: ControllerComponents,
                                mediaService: MediaService,
                                actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def getMedia: Action[AnyContent] = Action.async {
    mediaService.getMedia.map { msg => Ok(msg) }
  }

  def putMedia: Action[AnyContent] = Action.async { request =>
    mediaService.putMedia(request.body.asJson).map( {
        case Right(ok) => Ok(ok)
        case Left(ko) => NotFound(ko)
      })
  }
}
