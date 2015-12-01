package springboot.pac4j.service

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import springboot.pac4j.util.GenericRowMapper

@Service
@Slf4j
class AccountService {

    @Autowired
    JdbcTemplate jdbcTemplate

    Map lookupAccountByProvider(String providerName, String providerUserId) {
        List results = jdbcTemplate.query(
                "select * from account where provider = ? and provider_user_id = ?",
                [providerName, providerUserId] as Object[],
                new GenericRowMapper()
        )

        if (results.size() > 1) {
            throw new Exception("multiple accounts by provider [${providerName}] for id [${providerUserId}]")
        }

        return results.isEmpty() ? [:] : results[0]
    }

    Boolean createAccountForProvider(String providerName, String providerUserId, String displayName) {
        log.debug("creating new account for displayName=${displayName} using provider=${providerName} with id ${providerUserId}")

        int result = jdbcTemplate.update(
                "insert into account (display_name, provider, provider_user_id) values (?, ?, ?)",
                displayName,
                providerName,
                providerUserId
        )

        if (result != 1) {
            log.warn("creation of account for provider [${providerName}] and id [${providerUserId}] failed")
            return false
        }

        return true
    }

}
