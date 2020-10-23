import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._
import play.api.test._

class IntegrationWordSpec extends PlaySpec with ScalaFutures with OptionValues with GuiceOneAppPerSuite {

  "Application" should {

    "send 404 on a bad request" in {
      val bad = route(app, FakeRequest(GET, "/bad")).value
      status(bad) must be(NOT_FOUND)
    }

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).value

      status(home) must be(OK)
      contentType(home) must be(Some("text/html"))
    }
  }
}
