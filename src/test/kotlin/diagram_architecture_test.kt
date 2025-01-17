import architecture.diagram_inter_process
import architecture.diagram_intra_process
import org.junit.Test

internal class diagram_architecture_test {
    @Test
    fun create_inter_process_diagram() {
        diagram_inter_process {
            service("应用服务", "#LightSeaGreen") {
                process("租赁信息应用服务")
                process("推广服务应用服务")
                process("后台管理应用服务")
            }

            service("核心业务能力", "#HotPink") {
                process("信息推广服务") {
                    component("推广报价引擎", "#orange")
                }
                process("预充值服务")
            }

            service("领域服务", "#orange") {
                process("房屋信息管理系统")
                process("用户账户管理系统")
            }

            service("第三方系统", "#gray") {
                process("微信支付")
                process("支付宝支付")
                process("银联支付")
                process("ADX数据监测系统")
                process("发票代开服务")
                process("短信发送服务")
            }
        } export "./diagrams/inter_process_diagram.png"
    }

    @Test
    fun create_tw_renting_inter_process_diagram() {
        diagram_inter_process {
            service("前端", "#Cyan") {
                process("思沃租房通用版Web端")
                process("思沃租房App个人版Android端")
                process("思沃租房App个人版IOS端")
                process("思沃租房App经纪人版Android端")
                process("思沃租房App经纪人版IOS端")
                process("后台管理系统Web端")
            }

            service("BFF", "#RoyalBlue") {
                process("思沃租房WebBFF")
                process("思沃租房MobileBFF")
            }

            service("技术组件", "#RoyalBlue") {
                process("支付网关")
                process("三方服务网关")
            }

            service("应用服务", "#LightSeaGreen") {
                process("租赁信息应用服务")
                process("推广服务应用服务")
                process("后台管理应用服务")
            }

            service("核心业务能力", "#HotPink") {
                process("信息推广服务") {
                    component("推广报价引擎", "#orange")
                }
                process("预充值服务")
            }

            service("领域服务", "#orange") {
                process("房屋信息管理系统")
                process("用户账户管理系统")
                process("鉴权认证服务")
            }

            service("第三方系统", "#gray") {
                process("微信支付")
                process("支付宝支付")
                process("银联支付")
                process("ADX数据监测系统")
                process("发票代开服务")
                process("短信发送服务")
            }
        } export "./diagrams/tw_renting_inter_process_diagram.png"
    }

    @Test
    fun create_tw_renting_inter_process_communication_diagram() {
        diagram_inter_process {
            service("前端", "#Cyan") {
                process("思沃租房通用版Web端").call("思沃租房WebBFF", "1. GET /web-bff/ads")
                process("思沃租房通用版Web端").call("ADX数据监测系统", "5. GET /adx/xxx")
            }

            service("BFF", "#RoyalBlue") {
                process("思沃租房WebBFF").call("租赁信息应用服务", "2. GET /rental/ads")
            }

            service("应用服务", "#LightSeaGreen") {
                process("租赁信息应用服务").call("鉴权认证服务", "3. GET /auth/check")
                process("租赁信息应用服务").call("房屋信息管理系统", "4. GET /listings/ads")
            }

            service("技术组件", "#RoyalBlue") {
                process("三方服务网关").call("信息推广服务", "7. POST /promotion-contracts/{id}/promotion/confirmation")
            }

            service("核心业务能力", "#HotPink") {
                process("信息推广服务")
            }

            service("领域服务", "#orange") {
                process("房屋信息管理系统")
                process("鉴权认证服务")
            }

            service("第三方系统", "#gray") {
                process("ADX数据监测系统").call("三方服务网关", "6. POST /3rd-party-gateway/ad-data")
            }
        } export "./diagrams/tw_renting_inter_process_communication_diagram.png"
    }

    @Test
    fun create_intra_process_diagram_diagram() {
        diagram_intra_process {
            layer("应用层", "#HotPink") {
                component("Activity").call("ViewModel", "方法调用")
                component("ViewModel").call("Presenter", "方法调用")
                component("Service").call("Presenter", "方法调用")
            }

            layer("业务层", "#orange") {
                component("Presenter").call("Repository", "方法调用")
            }

            layer("数据层", "#LightSeaGreen") {
                val repo = component("Repository")
                repo.call("DBDataSource", "方法调用")
                repo.call("RemoteDataSource", "方法调用")

                component("DBDataSource").call("SqliteDB", "读写")
                component("RemoteDataSource").call("MobileBFF", "Http请求")
            }

            process("PushService").call("Service", "消息推送")
            process("MobileBFF")
            process("SqliteDB")

        } export "./diagrams/intra_process_diagram.png"
    }
}