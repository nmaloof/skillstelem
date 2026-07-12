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
       "-explain"
     ),
     libraryDependencies ++= Seq(
       // Cats Libraries
       "org.typelevel" %% "cats-core"   % "2.13.0",
       "org.typelevel" %% "cats-effect" % "3.7.0",

       //  Logging Libraries
       "org.typelevel" %% "log4cats-core"   % "2.8.0",
       "org.typelevel" %% "log4cats-slf4j"  % "2.8.0",
       "ch.qos.logback" % "logback-classic" % "1.5.32",

       // Http Libraries
       "org.http4s" %% "http4s-dsl"          % "0.23.34",
       "org.http4s" %% "http4s-ember-server" % "0.23.34",
       // "org.http4s" %% "http4s-ember-client" % "0.23.34",
       "org.http4s" %% "http4s-circe" % "0.23.34",

       // JSON Parsing
       "io.circe" %% "circe-core"   % "0.14.15",
       "io.circe" %% "circe-parser" % "0.14.15",
       //    "io.circe" %% "circe-generic" % "0.14.15",

       // Database Libraries
       "org.tpolecat" %% "doobie-core"   % "1.0.0-RC12",
       "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC12",
       "org.xerial"    % "sqlite-jdbc"   % "3.49.1.0",
       "com.github.geirolz" %% "fly4s" % "2.0.0",

       // Misc
       "is.cir" %% "ciris"        % "3.14.1",
       "is.cir" %% "ciris-http4s" % "3.14.1",

       "org.scalameta" %% "munit" % "1.3.3" % Test
     )
   )
