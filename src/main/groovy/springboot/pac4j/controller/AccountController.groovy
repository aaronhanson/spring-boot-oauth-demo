package springboot.pac4j.controller

import groovy.util.logging.Slf4j
import org.pac4j.core.credentials.Credentials
import org.pac4j.core.profile.UserProfile
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import springboot.pac4j.security.ClientUserDetailsService
import springboot.pac4j.service.AccountService

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@Slf4j
class AccountController {

    final String savedAuthAttribute = "savedAuthToken"

    @Value('${registration.successful.redirect.url:/}')
    String defaultRedirectUrl

    @Autowired
    ClientUserDetailsService clientUserDetailsService

    @Autowired
    AccountService accountService

    @RequestMapping("/account")
    @PreAuthorize('isAuthenticated()')
    String accounts() {
        return "account/index"
    }

    @RequestMapping(value="/registration")
    @PreAuthorize("permitAll")
    String registration(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = (Authentication) SecurityContextHolder.context.authentication
        Authentication registrationAuth = (Authentication) request.session.getAttribute(savedAuthAttribute)

        if (isPostRequest(request)) {
            if (registrationAuth && registrationAuth instanceof ClientAuthenticationToken) {
                if (registrationAuth.authorities.isEmpty()) {
                    registerNewAccount(registrationAuth, request)
                    resetAuthorizationToken(registrationAuth)
                    request.session.removeAttribute(savedAuthAttribute)
                    SavedRequest savedRequest = (SavedRequest) new HttpSessionRequestCache().getRequest(request, response)
                    if (savedRequest) {
                        return "redirect:${savedRequest.getRedirectUrl()}"
                    }
                }
            }
        } else if (!isAuthenticated() && registrationAuth) {
            return "account/registration"
        }

        return "redirect:${defaultRedirectUrl}"
    }

    protected void registerNewAccount(ClientAuthenticationToken registrationAuth, HttpServletRequest request) {
        ClientAuthenticationToken registrationToken = (ClientAuthenticationToken) registrationAuth
        UserProfile profile = registrationToken.userProfile

        String displayName = request.getParameter("displayName") ?: ''
        accountService.createAccountForProvider(registrationToken.clientName, profile.id, displayName)
    }

    protected boolean isPostRequest(final HttpServletRequest request) {
        return request.method.equalsIgnoreCase(RequestMethod.POST.toString())
    }

    protected boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.context.authentication
        return !(auth instanceof AnonymousAuthenticationToken)
    }

    protected void resetAuthorizationToken(ClientAuthenticationToken savedAuthToken) {
        UserDetails userDetails = clientUserDetailsService.loadUserDetails(savedAuthToken)
        SecurityContextHolder.context.authentication = new ClientAuthenticationToken(
                savedAuthToken.credentials as Credentials,
                savedAuthToken.clientName,
                savedAuthToken.userProfile,
                userDetails.authorities,
                userDetails
        )
    }

}

