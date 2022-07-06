package com.southwind.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.southwind.entity.Account;
import com.southwind.mapper.AccountMapper;
import com.southwind.service.AccountService;

import lombok.RequiredArgsConstructor;

/**
 * @author 吕茂陈
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;

    @Override
    public Account findByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        return accountMapper.selectOne(wrapper);
    }
}
