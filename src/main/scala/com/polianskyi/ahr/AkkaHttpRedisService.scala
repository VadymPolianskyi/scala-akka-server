package com.polianskyi.ahr

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

object AkkaHttpRedisService {

}
