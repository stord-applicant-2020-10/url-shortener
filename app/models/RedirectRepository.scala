package models

import java.net.URL

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedirectRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import CustomColumnTypes._
  import dbConfig._
  import profile.api._

  private class RedirectTable(tag: Tag) extends Table[Redirect](tag, "redirect") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def slug: Rep[String] = column[String]("slug")

    def url: Rep[URL] = column[URL]("url")

    def * : ProvenShape[Redirect] = (id, slug, url).<>((Redirect.apply _).tupled, Redirect.unapply)
  }

  private val redirect = TableQuery[RedirectTable]
  private val redirectInsert = {
    redirect.returning(redirect.map(_.id))
      .into((row, id) => row.copy(id = id))
  }

  def create(url: String): Future[Redirect] = {
    val tryRedirect = Redirect.safe(url)

    Future.fromTry(tryRedirect).flatMap { redirect =>
      db.run {
        redirectInsert += redirect
      }
    }
  }

  def find(slug: String): Future[Option[URL]] = {
    db.run {
      redirect.filter(_.slug === slug).map(_.url).result.headOption
    }
  }

  def list(): Future[Seq[Redirect]] = db.run {
    redirect.result
  }
}
