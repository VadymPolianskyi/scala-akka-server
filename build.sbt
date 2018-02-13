name := "scala-server"
version := "0.1"
scalaVersion := "2.12.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
    val akkaV       = "2.4.3"
    val scalaTestV  = "2.2.6"
    Seq(
        "com.typesafe.akka" %% "akka-actor" % akkaV,
        "com.typesafe.akka" %% "akka-stream" % akkaV,
        "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
        "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaV,
        "com.github.etaty" %% "rediscala" % "1.7.0",
        "org.scalatest"     %% "scalatest" % scalaTestV % "test",
        "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test"
    )
}