package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BaiduSimulation extends Simulation{
  // 设置请求的根路径
  val httpConf = http.baseURL("https://www.baidu.com")
  /*
  运行10秒 during 默认单位秒,如果要用微秒 during(100 millisecond)
  */
  val scn = scenario("BaiduSimulation").during(10){
    exec(http("baidu_home").get("/"))
  }
  // 设置线程数
//  setUp(scn.inject(atOnceUsers(10)).protocols(httpConf))

}
