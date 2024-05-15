package cn.fuxi.service

/**
 * 会员主服务
 * @since 2024/5/14
 * @author eddie.lys
 */
interface MemberVipDomainService {

    /**
     * 定义一个用户为VIP
     */
    fun defineUserAsVIP() : Boolean
}
