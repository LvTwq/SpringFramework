package com.southwind.realm;

import com.southwind.entity.Account;
import com.southwind.service.impl.AccountService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 吕茂陈
 */
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;


    /**
     * 授权：从数据库里拿到一个对象，把数据库里存的角色塞给SimpleAuthorizationInfo
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 从subject获取当前登录的用户信息
        Subject subject = SecurityUtils.getSubject();
        Account account = (Account) subject.getPrincipal();

        //设置角色
        Set<String> roles = new HashSet<>();
        roles.add(account.getRole());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);

        //设置权限
        info.addStringPermission(account.getPerms());
        return info;
    }


    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException 用户名和密码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 把拿到的账号和密码封装成token
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        Account account = accountService.findByUsername(token.getUsername());
//        if (account != null) {
//            return new SimpleAuthenticationInfo(account, account.getPassword(), getName());
//        }
        return null;
    }
}
