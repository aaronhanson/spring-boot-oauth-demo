package springboot.pac4j.security

import groovy.util.logging.Slf4j
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import springboot.pac4j.service.AccountService

@Slf4j
public class ClientUserDetailsService implements AuthenticationUserDetailsService<ClientAuthenticationToken> {

    AccountService accountService

    public UserDetails loadUserDetails(final ClientAuthenticationToken token) throws UsernameNotFoundException {
        Map account = accountService.lookupAccountByProvider(token.clientName, token.userProfile.id)

        String username = account.containsKey("displayName") ? account.displayName : ""

        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
        for (String role: token.getUserProfile().getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role))
        }

        if (!account.isEmpty() && authorities.isEmpty()) {
            // default to user role
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"))
        }

        return new ClientUserDetails(username: username, providerId: token.userProfile.id, authorities: authorities)
    }

}