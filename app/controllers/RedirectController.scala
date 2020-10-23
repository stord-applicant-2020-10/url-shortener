package controllers

import java.net.URI

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

class RedirectController @Inject()(repo: RedirectRepository,
                                   cc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext,
                                    assetsFinder: AssetsFinder
                                  )
  extends MessagesAbstractController(cc) {

  val urlRegex: Regex = raw"https?://([a-zA-Z_]+\.)+[a-zA-Z]+(/.*)?(\?.*)?".r
  val redirectForm: Form[CreateRedirectForm] = Form {
    mapping(
      "url" -> nonEmptyText.verifying(pattern(urlRegex, "constraint.url", "error.url"))
    )(CreateRedirectForm.apply)(CreateRedirectForm.unapply)
  }

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.index(redirectForm))
  }

  def createRedirect: Action[AnyContent] = Action.async { implicit request =>
    redirectForm.bindFromRequest().fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      createRedirectForm => {
        repo.create(createRedirectForm.url).map { result =>
          Redirect(routes.RedirectController.index()).flashing(
            "success" -> "redirect.created",
            "result" ->  s"${extractUri(request)}/${result.slug}"
          )
        }.recover { _ =>
          Redirect(routes.RedirectController.index()).flashing(
            "failure" -> "redirect.failed"
          )
        }
      }
    )
  }

  def extractUri(request: MessagesRequest[AnyContent]): String = {
    val uri = request.headers.toSimpleMap.getOrElse("Referer", request.uri)
    val parsed = new URI(uri)
    new URI(parsed.getScheme, parsed.getUserInfo, parsed.getHost,
      parsed.getPort, null, null, null).toString
  }

  def followRedirect(slug: String): Action[AnyContent] = Action.async { implicit request =>
    repo.find(slug).map {
      case Some(url) => Redirect(url.toString, TEMPORARY_REDIRECT)
      case None => NotFound
    }
  }

  def getRedirects: Action[AnyContent] = Action.async { implicit request =>
    repo.list().map { redirects =>
      Ok(Json.toJson(redirects))
    }
  }
}

case class CreateRedirectForm(url: String)
