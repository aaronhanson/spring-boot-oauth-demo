package springboot.pac4j.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class ClientUserDetails implements UserDetails {

    private static final long serialVersionUID = 6523314653561682296L

    String username
    String providerId
    Collection<GrantedAuthority> authorities
    String password

    boolean accountNonExpired = true
    boolean accountNonLocked = true
    boolean credentialsNonExpired = true
    boolean enabled = true

}