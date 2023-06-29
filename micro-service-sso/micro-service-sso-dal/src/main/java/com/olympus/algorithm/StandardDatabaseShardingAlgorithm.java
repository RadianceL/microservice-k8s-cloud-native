package com.olympus.algorithm;

import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * @author eddie.lys
 * @since 2023/5/17
 */
@Slf4j
@Component(value = "standardDatabaseShardingAlgorithm")
public class StandardDatabaseShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    /**
     * 精确分片
     *
     * @param availableTargetNames 有效的数据源或表的名字。这里就对应配置文件中配置的数据源信息
     * @param shardingValue        包含 逻辑表名、分片列和分片列的值。
     * @return 返回目标结果
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        long companyId = shardingValue.getValue();
        // 假设数据库名格式为db1、db2、db3等，根据公司ID进行分片计算
        int databaseIndex = (int) (companyId % availableTargetNames.size());
        for (String databaseName : availableTargetNames) {
            if (databaseName.endsWith(String.valueOf(databaseIndex))) {
                return databaseName;
            }
        }
        throw new IllegalArgumentException("No matching database found for companyId: " + companyId);
    }

    /**
     * 范围分片
     *
     * @param availableTargetNames 可用
     * @param rangeShardingValue   包含逻辑表名、分片列和分片列的条件范围。
     * @return 返回目标结果。可以是多个。
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> rangeShardingValue) {
        Collection<String> collect = new ArrayList<>();
        collect.add("db1");
        collect.add("db2");
        collect.add("db3");
        return collect;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public String getType() {
        return "STANDARD_ALGORITHM";
    }

    @Override
    public void init(Properties props) {
        System.out.println(props);
    }
}
