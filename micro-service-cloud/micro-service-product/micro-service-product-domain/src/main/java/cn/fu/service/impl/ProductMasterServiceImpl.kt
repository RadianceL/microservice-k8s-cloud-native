package cn.fu.service.impl

import cn.fu.service.ProductAttrService
import cn.fu.service.ProductMasterService
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author eddie.lys
 * @since 2024/4/2
 */
@Service
class ProductMasterServiceImpl(
    private val productAttrService: ProductAttrService
) : ProductMasterService {

}
