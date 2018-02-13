package com.polianskyi.ahr

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import com.polianskyi.ahr.model.UserHandler
import com.polianskyi.ahr.model.UserHandler.{User, UserDeleted, UserNotFound}
import spray.json.DefaultJsonProtocol

case class UserPwd(pwd: String)
case class UpsertRequest(username: String, password: String)

trait Protocols extends DefaultJsonProtocol {
  implicit val delUserFormat = jsonFormat1(UserDeleted.apply)
  implicit val uNotFoundFormat = jsonFormat1(UserNotFound.apply)
  implicit val idFormat = jsonFormat1(UserPwd.apply)
  implicit val usrFormat = jsonFormat2(User.apply)
  implicit val userFormat = jsonFormat2(UpsertRequest.apply)
}

trait Service extends Protocols {
  def userHandler: ActorRef

  val unsecuredRoutes: Route = {
    pathPrefix("api") {
      pathPrefix("user") {
        path("register") {
          put {
            entity(as[UpsertRequest]) {u =>
              complete {
                (userHandler ? UserHandler.Register(u.username, u.password)).map {
                  case true => OK -> s"Thank you ${u.username}"
                  case _ => InternalServerError -> "Failed to complete your request. Please try later!"
                }
              }
            }
          }
        }
      }
    }
  }
}

object AkkaHttpRedisService {

}
