package com.olympus.repository;

import com.olympus.data.BaseUserInfoPO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author eddie.lys
 * @since 2023/5/17
 */
@Repository
public interface SystemUserRepository extends CrudRepository<BaseUserInfoPO, Integer> {

    BaseUserInfoPO findByAccount(String account);

    BaseUserInfoPO findByCid(String cid);
}
