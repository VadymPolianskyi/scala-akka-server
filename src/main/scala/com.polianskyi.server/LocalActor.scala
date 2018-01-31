package com.polianskyi.server

import akka.actor.Actor


class LocalActor extends Actor {

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")
    println("That's remote:" + remoteActor)
    remoteActor ! "hi"
  }

  override def receive: Receive = {
    case msg: String => println("got message -> " + msg)
  }
}
