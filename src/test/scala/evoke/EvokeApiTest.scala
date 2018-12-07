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
class EvokeApiTest extends Simulation{
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
                             openId:String,//微信用户open_id
                             url:String,//h5链接
                             pageName:String,//h5页面英文名称
                             createTime:Long,  //用户访问事件
                             accessKey:String  //校验
                           )



  val httpConf = http
//    .baseUrl("http://flowdev.neoap.com/evoke")
    .baseUrl("http://localhost:30377/evoke")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .connectionHeader("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn =
    scenario("EvokeTest").exec(
      repeat(10){
        exec(
          http("look")
            .post("/api/event/h5visit")
            .body(StringBody(CollectData4h5("o6_bmjrPTlm6_2sgVt7hMZOPfL2M","http://h5.eqxiu.com/ls/Ezs0mcEK","xin_shou_fu_li",123456789l,accessKey).asJson.noSpaces)).asJson
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
