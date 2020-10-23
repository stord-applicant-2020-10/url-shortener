package models

import java.net.URL
import java.util.{Base64, UUID}

import play.api.libs.json._

import scala.util.Try

final case class Redirect(id: Long, slug: String, url: URL)

object Redirect {
  implicit val urlReads: Reads[URL] = (json: JsValue) => {
    json.validate[String] match {
      case s: JsSuccess[String] => s.map(new URL(_))
      case e: JsError => e
    }
  }

  implicit val urlWrites: Writes[URL] = (o: URL) => {
    JsString(o.toString)
  }
  implicit val urlFormat: Format[URL] = Format(urlReads, urlWrites)
  implicit val redirectFormat: OFormat[Redirect] = Json.format[Redirect]

  def slugFor(url: String, uuid: UUID): String = {
    val bigInt = url.map(_.toLong).foldLeft(BigInt(uuid.getLeastSignificantBits))(_ + _)
    val bytes = bigInt.toByteArray
    val b64 = Base64.getUrlEncoder.withoutPadding().encodeToString(bytes)
    b64
  }

  def safe(url: String, uuid: UUID = UUID.randomUUID()): Try[Redirect] = {
    Try {
      new URL(url)
    }.map { converted =>
      val slug = slugFor(url, uuid)
      Redirect(0, slug, converted)
    }
  }
}
