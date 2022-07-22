import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-28"    % "5.21.0",
    "uk.gov.hmrc"                   %% "bootstrap-backend-play-28"    % "5.21.0",
    "uk.gov.hmrc"                   %% "internal-auth-client-play-28" % "1.2.0",
    "com.googlecode.libphonenumber" % "libphonenumber"                % "8.9.9"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"   % "5.21.0"            % "test, it",
    "org.mockito"             %% "mockito-scala-scalatest"  % "1.16.46"           % "test, it"
  )
}
