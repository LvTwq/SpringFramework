package com.southwind.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.annotation.Metrics;
import com.southwind.entity.Account;
import com.southwind.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 吕茂陈
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountService {

    private final AccountMapper accountMapper;

    public int findByUsername(String username) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return Math.toIntExact(accountMapper.selectCount(wrapper));
    }


    /**
     * 启用方法监控，并且不忽略异常，可能导致异常被吞掉
     *
     * @param account
     */
    @Metrics(ignoreException = true)
    @Transactional(rollbackFor = Exception.class)
    public void createAccount(Account account) {
        accountMapper.insert(account);
        // 这里抛异常，如果被 Transactional 的切面捕获（invokeWithinTransaction），会回滚，但如果被 MetricsAspect 捕获，
        if (account.getUsername().contains("lmc")) {
            throw new RuntimeException("无效 username ！！！！");
        }
    }
}
