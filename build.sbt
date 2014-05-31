name := "mednetwork"


version := "1.0-SNAPSHOT"


libraryDependencies ++= Seq(
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  javaJdbc,
  javaEbean,
  cache,
  "com.jolbox" % "bonecp" % "0.8.0.RELEASE",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc4",
  "commons-io" % "commons-io" % "2.3",
  "org.elasticsearch" % "elasticsearch" % "1.1.1",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "joda-time" % "joda-time" % "2.3"
)     
 
play.Project.playJavaSettings
