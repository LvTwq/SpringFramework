package com.lvmc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvmc.entity.Account;
import com.lvmc.mapper.AccountMapper;
import com.lvmc.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * (Account)表服务实现类
 *
 * @author 吕茂陈
 * @since 2023-10-19 20:13:09
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
