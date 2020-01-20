package basic
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestGetAppToken extends Simulation{

  val feeder = csv("C:\\Users\\Administrator\\IdeaProjects\\ScalaDemon\\src\\test\\resources\\data\\jdy_user.csv").random

  val httpConf = http.baseURL("http://172.20.182.50:8093")
  val headers_default = Map("Content-Type"->"application/json","tenantAlias"->"7916594396")

  val get_app_token = {exec(http("get_app_token")
      .get("/")
        .check(status.is(200))
        .check(bodyString.saveAs("get_header")))}
//把csv文件加载进来
  val get_xx = feed(feeder).exec {
    http("get_apptoken").post("/ierp/api/getAppToken.do")
      .headers(headers_default)
      .body(StringBody(
                         """{
                          "appId": "openapi",
                          "appSecuret": "kingdee",
                          "tenantId": "${tenantId}",
                          "accountId": "${accountId}",
                          "language": "zh_CN"
                         }""")
                       ).asJSON
      .check(bodyString.saveAs("tips_body"))
      .check(jsonPath("$.data.app_token").exists.saveAs("app_token"))

  }
    .exec{session=>
                println(session("tips_body").as[String])
                println(session.get("app_token").as[String])
                session}
    val get_accseeToken = {
      exec {
        http("get_accesstoken").post("/ierp/api/login.do")
          .headers(headers_default)
          .body(StringBody(
            """{
                 "user": "13686805851",
                 "apptoken": "${app_token}" ,
                 "tenantid": "7916594396",
                 "accountId": "1577175774505592107",
                 "language": "zh_CN",
                 "usertype": "Mobile"
              }""")
          )
          .asJSON
          .check(bodyString.saveAs("tips_body2"))
      }

        .exec { session =>
          println(session("tips_body2").as[String])
          session}
    }


  val get_xx1 = scenario("get_token")
    .exec(get_xx)
    .exec(get_accseeToken)



  setUp(
    //执行单场景
  get_xx1.inject(constantUsersPerSec(1).during(1)).protocols(httpConf)
  )  //每秒运行1个虚拟用户，持续运行10s

}
