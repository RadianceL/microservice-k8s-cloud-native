package cn.fu.service.impl

import cn.fu.service.ProductAttrService
import cn.fu.service.ProductMasterService
import org.springframework.stereotype.Service

/**
 *
 * @since 2024/4/2
 * @author eddie.lys
 */
@Service
class ProductAttrServiceImpl(
    /**
     * 商品主数据服务
     */
    private val productMasterService: ProductMasterService
): ProductAttrService {


}