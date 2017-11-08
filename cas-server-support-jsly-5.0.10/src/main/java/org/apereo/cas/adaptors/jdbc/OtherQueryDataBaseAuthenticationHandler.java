package org.apereo.cas.adaptors.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.authentication.*;
import org.apereo.cas.exception.InvalidReTryTimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * description: 重写jdbc验证,添加密码错误次数校验
 * </p>
 *
 * @author guanfeng_yang
 * @date 2017/11/6 17:54
 */
public class OtherQueryDataBaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(OtherQueryDataBaseAuthenticationHandler.class);
    private String sql;

    private int retryTimes;
    private String lockedSql;
    private String fieldPassword;
    private String fieldLocked;
    private String fieldDisabled;
    private Map<String, Integer> countMap = new HashMap<String, Integer>();

    @Transactional
    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential, final String originalPassword)
            throws GeneralSecurityException, PreventedException {
        LOGGER.info("=====---------cas 开始认证---------=====");
        if (StringUtils.isBlank(this.sql) || getJdbcTemplate() == null) {
            throw new GeneralSecurityException("Authentication handler is not configured correctly. "
                    + "No SQL statement or JDBC template is found.");
        }
        int failureTimes = 0;
        final String username = credential.getUsername();
        final String password = credential.getPassword();

        try {
            final Map<String, Object> map = getJdbcTemplate().queryForMap(this.sql, username);
            if (map == null || map.size() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            }
            String dbPassword = (String) map.get(this.fieldPassword);
            if (dbPassword == null) {
                throw new GeneralSecurityException("Authentication handler is not configured correctly. fieldPassword not fond.");
            }
            String isEnable = (String) map.get(this.fieldDisabled);
            if (isEnable != null) {
                if (!"Y".equalsIgnoreCase(isEnable) && !"1".equalsIgnoreCase(isEnable)) {
                    //用户被禁用
                    throw new AccountDisabledException("用户" + username + "被禁用,请联系管理员!");
                }
            }

            String isLocked = (String) map.get(this.fieldLocked);
            boolean checkLocked = isLocked != null;
            if (checkLocked) {
                if ("Y".equalsIgnoreCase(isLocked) || "1".equalsIgnoreCase(isLocked)) {
                    //用户已经被锁定
                    throw new AccountLockedException("用户" + username + "被系统锁定,请联系管理员!");
                }
            }

            if ((StringUtils.isNotBlank(originalPassword) && !this.matches(originalPassword, dbPassword))
                    || (StringUtils.isBlank(originalPassword) && !StringUtils.equals(password, dbPassword))) {
                //密码不匹配
                //用户密码错误，错误次数加1
                if (checkLocked) {
                    Integer ct = countMap.get(username);
                    failureTimes = ct == null ? 0 : ct;
                    failureTimes = failureTimes + 1;
                    if (failureTimes > this.retryTimes) {
                        //TODO 事物怎么配置
                        //  getJdbcTemplate().update(this.lockedSql, username);
                        PreparedStatement pst = null;
                        try {
                            Connection connection = getJdbcTemplate().getDataSource().getConnection();
                            connection.setAutoCommit(false);
                            pst = connection.prepareStatement(this.lockedSql);
                            pst.setString(1, username);
                            int rows = pst.executeUpdate();
                            connection.commit();
                            LOGGER.debug("SQL update affected " + rows + " rows");
                            countMap.remove(username);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (pst != null) {
                                try {
                                    pst.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        throw new AccountLockedException("用户" + username + "被系统锁定,请联系管理员!");
                    } else {
                        countMap.put(username, failureTimes);
                        throw new InvalidReTryTimeException("密码已错误" + failureTimes + "次，密码错误次数超过" + retryTimes + "次将锁定用户!");
                    }
                }
                throw new FailedLoginException("账号或密码不正确!");
            } else {
                if (checkLocked)
                    countMap.remove(username);
            }
        } catch (final IncorrectResultSizeDataAccessException e) {
            if (e.getActualSize() == 0) {
                throw new AccountNotFoundException(username + " not found with SQL query");
            }
            throw new FailedLoginException("Multiple records found for " + username);
        } catch (final DataAccessException e) {
            throw new PreventedException("SQL exception while executing query for " + username, e);
        }
        return createHandlerResult(credential, this.principalFactory.createPrincipal(username), null);
    }


    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(final UsernamePasswordCredential credential)
            throws GeneralSecurityException, PreventedException {
        return authenticateUsernamePasswordInternal(credential, null);
    }

    public void setSql(final String sql) {
        this.sql = sql;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public void setLockedSql(String lockedSql) {
        this.lockedSql = lockedSql;
    }

    public void setFieldPassword(String fieldPassword) {
        this.fieldPassword = fieldPassword;
    }

    public void setFieldLocked(String fieldLocked) {
        this.fieldLocked = fieldLocked;
    }

    public void setFieldDisabled(String fieldDisabled) {
        this.fieldDisabled = fieldDisabled;
    }
}
