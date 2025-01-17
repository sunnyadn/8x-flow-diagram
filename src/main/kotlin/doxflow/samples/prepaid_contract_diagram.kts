package doxflow.samples

import doxflow.diagram_8x_flow
import doxflow.dsl.fulfillment

diagram_8x_flow {
    lateinit var refundInPrepaidContext: fulfillment
    lateinit var prepaidInPrepaidContext: fulfillment
    lateinit var invoiceInPrepaidContext: fulfillment

    context("预充值协议上下文") {
        val houseAgent = participant_party("房产经纪人")
        val prepaidUser = role_party("预充值用户") played houseAgent
        val rentingPlatform = role_party("思沃租房")

        contract("预充值协议", prepaidUser, rentingPlatform) {
            key_timestamps("签订时间")
            participant_place("预充值账户") associate this

            prepaidInPrepaidContext = fulfillment("预充值") {
                request(rentingPlatform) {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation(prepaidUser) {
                    key_timestamps("创建时间")
                    key_data("金额")
                }
            }

            refundInPrepaidContext = fulfillment("余额退款") {
                request(prepaidUser) {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation(rentingPlatform) {
                    key_timestamps("创建时间")
                    key_data("金额")
                }
            }

            invoiceInPrepaidContext = fulfillment("发票开具") {
                request(prepaidUser) {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation(rentingPlatform) {
                    key_timestamps("创建时间")
                    key_data("金额")
                }
            }

            fulfillment("账单发送") {
                request(prepaidUser) {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation(rentingPlatform) {
                    key_timestamps("发布时间")
                    key_data("金额")

                    evidence("账单") {
                        key_timestamps("创建时间")
                        key_data("账单期数, 总金额")

                        detail("账单明细") {
                            key_timestamps("创建时间")
                            key_data("账单期数, 总金额")
                        }
                    }
                }
            }

            fulfillment("支付推广费用") {
                request(rentingPlatform) {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation(prepaidUser) {
                    key_timestamps("创建时间")
                    key_data("金额")
                }
            }
        }
    }

    context("三方支付上下文") {
        contract("XXX支付协议") {
            key_timestamps("签订时间")
            fulfillment("代付") {
                request {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation {
                    key_timestamps("创建时间")
                    key_data("金额")

                    val evidence = evidence("支付凭证") {
                        key_timestamps("支付时间")
                        key_data("金额")
                    }
                    evidence role refundInPrepaidContext.confirmation
                    evidence role prepaidInPrepaidContext.confirmation


                }
            }
        }
    }

    context("发票代开服务上下文") {
        contract("发票代开协议") {
            key_timestamps("签订时间")
            fulfillment("发票代开") {
                request {
                    key_timestamps("创建时间", "过期时间")
                    key_data("金额")
                }

                confirmation {
                    key_timestamps("创建时间")
                    key_data("金额")

                    val evidence = evidence("发票") {
                        key_timestamps("开具时间")
                        key_data("金额")
                    }
                    evidence role invoiceInPrepaidContext.confirmation
                }
            }
        }
    }

} export "./diagrams/prepaid_contract_diagram.png"
