ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion      := "3.8.3"
ThisBuild / semanticdbEnabled := true

lazy val root = project
   .in(file("."))
   .enablePlugins(JavaAppPackaging, DockerPlugin, AshScriptPlugin)
   .settings(
     name       := "skills-telem",
     run / fork := true,
     scalacOptions ++= Seq(
       "-deprecation",
       "-Wunused:all",
       "-no-indent",
     ),
     libraryDependencies ++= Seq(
       "org.typelevel" %% "cats-effect"         % "3.7.0",
       "org.http4s"    %% "http4s-dsl"          % "0.23.34",
       "org.http4s"    %% "http4s-ember-server" % "0.23.34",
       // "org.http4s" %% "http4s-ember-client" % "0.23.34",
       "org.http4s" %% "http4s-circe" % "0.23.34",
       "io.circe"   %% "circe-core"   % "0.14.15",
       "io.circe"   %% "circe-parser" % "0.14.15",
       //    "io.circe" %% "circe-generic" % "0.14.15",
       "org.scalameta" %% "munit" % "1.3.3" % Test
     )
   )
