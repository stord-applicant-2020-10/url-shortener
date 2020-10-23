import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class BrowserWordSpec extends PlaySpec
  with GuiceOneServerPerSuite
  with OneBrowserPerSuite
  with HtmlUnitFactory {

  "Application" should {

    "post data" in {
      go to s"http://localhost:$port"
      textField(id("url")).value = "https://www.stord.com/"
      click.on(find(name("submit")).value)
    }
  }
}
