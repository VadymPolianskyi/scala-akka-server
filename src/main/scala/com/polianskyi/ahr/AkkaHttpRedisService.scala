package com.polianskyi.ahr

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes.{InternalServerError, OK}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.Credentials
import akka.pattern.ask
import akka.util.Timeout
import com.polianskyi.ahr.model.UserHandler
import com.polianskyi.ahr.model.UserHandler.{GetUser, User, UserDeleted, UserNotFound}
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future

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

  implicit def requestTimeout: Timeout = Timeout(5, TimeUnit.SECONDS)

  def userHandler: ActorRef

  def userAuthenticate(credentials: Credentials): Future[Option[UserPwd]] = {
    credentials match {
      case p@Credentials.Provided(userName) =>
        fetchUserId(userName).map {
          case Some(UserPwd(id)) if p.verify(id) => Some(UserPwd(id))
          case _ => None
        }
      case _ => Future.successful(None)
    }
  }

  def fetchUserId(userName: String): Future[Option[UserPwd]] ={
    (userHandler ? GetUser(userName)).map {
      case User(_, p) => Some(UserPwd(p))
      case _ => None
    }
  }


//  registration
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
