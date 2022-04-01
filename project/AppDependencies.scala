import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "5.21.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "5.21.0"             % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play"       % "5.1.0"             % "it,test",
    "org.mockito"             % "mockito-core"             % "4.0.0"             % "it, test",
    "org.scalatestplus"      %% "mockito-3-12"             % "3.2.10.0"          % "it, test",
    "com.vladsch.flexmark"    %  "flexmark-all"               % "0.36.8"            % "test, it"
  )
}
