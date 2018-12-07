package evoke


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import util.SecureUtil

import scala.concurrent.duration._
import scala.util.Random

/**
  * Created by sky
  * Date on 2018/12/7
  * Time at 下午2:20
  */
class EvokeApiTest extends Simulation {

  import io.circe.parser.decode
  import io.circe.syntax._
  import io.circe._
  import io.circe.generic.auto._

  val random = new Random(System.currentTimeMillis())

  def randomElement[T](array: Array[T]): T = {
    array(random.nextInt(array.length))
  }


  val appClients = Array(
    "111" -> "aaa",
    "222" -> "bbb",
    "333" -> "ccc"
  )

  val mpAppIds = Array(
    "wx916f2f265696f987",
    "wxbc68c7afad6c342f"
  )

  val accessKey = ""

  case class CollectData4h5(
                             openId: String, //微信用户open_id
                             url: String, //h5链接
                             pageName: String, //h5页面英文名称
                             createTime: Long, //用户访问事件
                             accessKey: String //校验
                           )

  val userMap = List(
    "ogTrSw4x0lqqg3imzQCe-yiVGAZs",
    "ogTrSwz_sYVKvs9xfeFda2wfqYw0",
    "ogTrSwwiZtaOrLHw_5XEqsr2IvGY",
    "ogTrSw8JrPXSzRcAGMQ1ygP0Q0gQ",
    "ogTrSwyiwMr12wit8-vaSQ3i1T1A",
    "ogTrSwz1w5CyQUbKcrgDUfPg93Vc"
  )

  val pageList = List(
    ("page1", "http://aggjoa/page1"),
    ("page2", "http://aggjoa/page2"),
    ("page3", "http://aggjoa/page3"),
    ("page4", "http://aggjoa/page4"),
    ("page5", "http://aggjoa/page5"),
    ("page6", "http://aggjoa/page6")
  )

  def generateMap() = Iterator.continually {
    val a = random.nextInt(6)
    Map(
      "o" -> userMap(random.nextInt(6)),
      "p" -> pageList(a)._1,
      "u" -> pageList(a)._2
    )
  }


  val httpConf = http
    .baseUrl("http://flowdev.neoap.com/evoke")
    //    .baseUrl("http://localhost:30377/evoke")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .connectionHeader("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn =
    scenario("EvokeTest").exec(
      repeat(100) {
        feed(generateMap())
          .exec(
            http("look")
              .post("/api/event/h5visit")
              .body(StringBody(CollectData4h5("${o}", "${u}", "${p}", System.currentTimeMillis()/1000, accessKey).asJson.noSpaces)).asJson
          )
      }
    )

  /*  val scn =
      scenario("MpTest").exec(
        PlatAppIdApi.shot,
        MpTokenApi.shot,
        MpInfoApi.shot,
        JsTicketApi.shot
      )*/


  //  rampUsersPerSec
  //    rampUsers(300) over (5 seconds)
  //    constantUsersPerSec(30) during(1 minutes)
  //    constantUsersPerSec(30) during(1 minutes) randomized
  //    rampUsersPerSec(5) to (50) during(1 minutes)
  //    rampUsersPerSec(5) to (50) during(1 minutes) randomized
  //    heavisideUsers(5000) over(120 seconds)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
