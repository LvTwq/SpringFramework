package com.southwind.service;

import com.southwind.entity.Account;

public interface AccountService {
    Account findByUsername(String username);
}
