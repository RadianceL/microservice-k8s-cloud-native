package cn.fuxi.controller.api

import cn.fuxi.service.MemberVipDomainService
import org.springframework.web.bind.annotation.RestController

/**
 * 积分控制器
 *
 * @since 2024/5/15
 * @author eddie.lys
 */
@RestController
class MemberPointsController (
    private val memberVipDomainService: MemberVipDomainService
)

