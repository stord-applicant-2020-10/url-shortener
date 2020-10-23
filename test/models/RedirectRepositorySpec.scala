package models

import org.scalatest.Inside
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.db.{DBApi, Database}
import play.api.db.evolutions.Evolutions

class RedirectRepositorySpec extends PlaySpec with GuiceOneAppPerSuite with ScalaFutures with Inside {
  val goodUrl = "https://www.stord.com"
  val dbApi: DBApi = app.injector.instanceOf[DBApi]
  val db: Database = dbApi.database("default")
  val repo: RedirectRepository = app.injector.instanceOf[RedirectRepository]

  "create" should {
    "update id" in {
      Evolutions.cleanupEvolutions(db)
      Evolutions.applyEvolutions(db)
      whenReady(repo.create(goodUrl)) {
        inside(_) {
          case Redirect(id, _, url) =>
            id must not be 0
            url.toString must be (goodUrl)
        }
      }
    }

    "be findable" in {
      Evolutions.cleanupEvolutions(db)
      Evolutions.applyEvolutions(db)
      val model = repo.create(goodUrl).futureValue
      whenReady(repo.find(model.slug)) {
        inside(_) {
          case Some(url) =>
            url.toString must be (goodUrl)
        }
      }
    }
  }
}
