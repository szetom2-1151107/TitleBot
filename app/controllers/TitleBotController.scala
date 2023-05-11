package controllers

import model.{TitleResult, TitleResultCacheManager}
import org.jsoup.Jsoup
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import play.api.libs.ws._
import play.api.mvc._

import java.net.URL
import java.util.Base64
import scala.concurrent.{Await, ExecutionContext, Future}
import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success, Try}

@Singleton
class TitleBotController @Inject()(implicit ec: ExecutionContext, ws: WSClient, val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Get request handler to retrieve website information
   *
   * curl localhost:9000/getWebsiteInfo&url=cnn.com
   */
  def getWebsiteInfo(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val urlOpt = getUrl(request.getQueryString("url"))

    urlOpt match {
      case Some(url) => doGetWebsiteInfo(url)
      case None => Future(BadRequest("Invalid Url"))
    }
  }

  // Attempts to retrieve website information for the given url
  def doGetWebsiteInfo(url: URL): Future[Result] = {
    val urlStr = url.toString
    // Check if the url's website information is already in the cache.
    // If so, return a Future with an Ok response containing the cached information
    TitleResultCacheManager.get(urlStr) match {
      case Some(result) => Future(Ok(Json.toJson(result)))

      // If the information is not in the cache, attempt to retrieve it from the website
      case _ => {
        for {
          faviconTry <- doGetData(getFaviconUrl(url))(getFavicon)
          titleTry <- doGetData(urlStr)(getTitle)
        } yield {
          (faviconTry, titleTry) match {

            // If the website information is successfully retrieved,
            // cache it and return a Future with an Ok response containing the information
            case (Success(favicon), Success(title)) =>
              val res = TitleResult(title, favicon)
              TitleResultCacheManager.put(urlStr, res)
              Ok(Json.toJson(res))
            case _ => BadRequest("Invalid Url")
          }
        }
      }
    }
  }

  // Sends an HTTP GET request to the given url and applies the given function to the response body to extract data
  def doGetData(url: String)(onSuccess: (WSResponse) => String): Future[Try[String]] = {
    val request: WSRequest = ws.url(url)
    request.get().map { response =>
      if (response.status == 200) {
        Success(onSuccess(response))
      } else {
        Failure(new Throwable("Unable to retrieve Data from website"))
      }
    }
  }

  // Extracts the title of the website from the response body
  private [controllers] def getTitle(response: WSResponse): String = {
    Jsoup.parse(response.body).title()
  }

  // Extracts the favicon of the website from the response body and encodes it in Base64
  private [controllers] def getFavicon(response: WSResponse): String = {
    val faviconBytes = response.bodyAsBytes.toArray
    Base64.getEncoder.encodeToString(faviconBytes)
  }


  // Prepares the pased in urlString for consumption
  // If returns None, that means the url passed in was malformed
  private [controllers] def getUrl(urlString: Option[String]): Option[URL] = {
    val urlTry = Try {
      val urlWithProtocal = addProtocal(urlString.getOrElse(""))
      new URL(urlWithProtocal)
    }

    urlTry match {
      case Success(url) => Some(url)
      case Failure(e) => None
    }
  }

  // adds https:// to url if it doesn't have a protocal
  private [controllers] def addProtocal(url: String): String = {
    if (!url.matches("^https://.*")) {
      "https://" + url
    } else {
      url
    }
  }

  // prepares the URL to fetch a website's favicon
  private [controllers] def getFaviconUrl(url: URL): String = {
    s"${url.getProtocol}://${url.getHost}/favicon.ico"
  }

}
