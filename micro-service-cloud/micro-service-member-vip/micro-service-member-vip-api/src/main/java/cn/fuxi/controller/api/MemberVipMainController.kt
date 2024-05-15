package cn.fuxi.controller.api

import cn.fuxi.common.response.ServiceResponse
import cn.fuxi.service.MemberVipDomainService
import com.olympus.logger.event.annotation.EventTrace
import com.olympus.logger.event.model.LoggerType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 会员主控制器
 *
 * @author eddie.lys
 * @since 2024/5/15
 */
@RestController
class MemberVipMainController (
    private val memberVipDomainService: MemberVipDomainService
) {

    @PostMapping("/member-vip/define")
    @EventTrace(event = "用户注册", loggerType = LoggerType.FORMAT)
    fun userRegister(): ServiceResponse<String> {
        val defineUserAsVIP = memberVipDomainService.defineUserAsVIP()

        return if (defineUserAsVIP) {
            ServiceResponse.ofSuccess("")
        }else {
            ServiceResponse.ofError("")
        }
    }

}