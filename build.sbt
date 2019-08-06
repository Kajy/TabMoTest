name := "TabMoTest"
 
version := "1.0"

lazy val `untitled` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.2" % "test"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"


unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      