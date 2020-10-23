package models

import java.net.{MalformedURLException, URL}
import java.util.UUID

import org.scalatest.{Inside, OptionValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import play.libs.Json

import scala.util.{Failure, Success}

class RedirectSpec extends AnyFlatSpec with OptionValues with Inside with Matchers {
  val goodUrl = new URL("https://www.stord.com/")
  val badUrl = "http//www.stord.com"
  val zeroUUID = new UUID(0, 0)

  "safe" should "create a model for well-formed inputs" in {
    inside(Redirect.safe(goodUrl.toString, zeroUUID)) {
      case Success(Redirect(0, slug, url)) =>
        url must be (goodUrl)
        slug must be (Redirect.slugFor(goodUrl.toString, zeroUUID))
    }
  }

  it should "yield a failure for ill-formed inputs" in {
    inside(Redirect.safe(badUrl, zeroUUID)) {
      case Failure(exception) =>
        exception mustBe a [MalformedURLException]
        exception.getMessage must include (badUrl)
    }
  }

  "model" should "be json serializable" in {
    val givenModel = Redirect(0, "slug", goodUrl)
    val json = Json.toJson(givenModel)
    val resultingModel = Json.fromJson(json, classOf[Redirect])
    resultingModel must be (givenModel)
  }
}
