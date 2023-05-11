package controllers
//
//import org.scalatestplus.play.PlaySpec
//import org.scalatestplus.play.guice.GuiceOneAppPerTest
//import play.api.test.Helpers.{GET, route, stubControllerComponents, writeableOf_AnyContentAsEmpty}
//import play.api.test.{FakeRequest, Injecting}
//import play.api.libs.ws.WSResponse
//import org.scalatestplus.mockito.MockitoSugar
//import play.api.mvc.Results.BadRequest
//
//import java.net.URL
//import java.util.concurrent.TimeUnit
//import scala.concurrent.{Await, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.{Duration, FiniteDuration}
//
//class TitleBotControllerTest extends PlaySpec with GuiceOneAppPerTest with Injecting {
//  trait Fixture {
//    val controller = inject[TitleBotController]
//  }
//
//  "getWebsiteInfo should return BadRequest" in new Fixture {
//    val future = controller.getWebsiteInfo().apply(FakeRequest(GET, "/getWebsiteInfo?url=cnn.com"))
//    val response = Await.result(future, Duration(100, TimeUnit.SECONDS))
//
//    response.header.status mustBe 200
//  }
//
//  "getUrl should do return prepared url" in new Fixture {
//    val url = "cnn.com"
//    controller.getUrl(Some(url)) mustBe Some("https://cnn.com")
//  }
//
//  "addProtocal should append https://" in new Fixture {
//    val url = "cnn.com"
//    controller.addProtocal(url) mustBe "https://cnn.com"
//  }
//
//  "addProtocal should not append additional https://" in new Fixture {
//    val url = "https://cnn.com"
//    controller.addProtocal(url) mustBe "https://cnn.com"
//  }
//}

import org.mockito.Mockito._
import akka.util.ByteString
import model.{TitleResult, TitleResultCacheManager}
import org.mockito.Mockito
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

class TitleBotControllerSpec extends PlaySpec with MockitoSugar with Results with ScalaFutures {

  val mockWSClient: WSClient = mock[WSClient]
  val mockWSRequest: WSResponse = mock[WSResponse]

  val controllerComponents: ControllerComponents = Helpers.stubControllerComponents()

  val controller = new TitleBotController()(global, mockWSClient, controllerComponents)

  "TitleBotController#getUrl" should {

    "return a URL for a valid url string" in {
      val url = controller.getUrl(Some("https://www.example.com")).get
      url.getProtocol mustBe "https"
      url.getHost mustBe "www.example.com"
    }
  }

  "TitleBotController#getFaviconUrl" should {

    "return the correct favicon URL for a given URL" in {
      val url = new java.net.URL("https://www.example.com")
      controller.getFaviconUrl(url) mustBe "https://www.example.com/favicon.ico"
    }

  }

  "TitleBotController#getTitle" should {

    "return the correct title for a given website" in {
      val response = mock[WSResponse]
      when(response.body).thenReturn("<html><head><title>Example Title</title></head><body></body></html>")
      controller.getTitle(response) mustBe "Example Title"
    }

  }

  "TitleBotController#getFavicon" should {

    "return the Base64-encoded favicon for a given website" in {
      val response = mock[WSResponse]
      when(response.bodyAsBytes).thenReturn(ByteString(Array[Byte](0, 1, 2, 3)))
      controller.getFavicon(response) mustBe "AAECAw=="
    }

  }

  "TitleBotController#getWebsiteInfo" should {

    "return the cached website info if it exists" in {
      val url = new java.net.URL("https://www.example.com")
      val result = TitleResult("Example Title", "AAECAw==")
      TitleResultCacheManager.put(url.toString, result)

      val request = FakeRequest(GET, s"/getWebsiteInfo?url=${url.toString}")
      val response = controller.getWebsiteInfo().apply(request)

      status(response) mustBe OK
      contentAsJson(response) mustBe Json.toJson(result)
    }
  }
}