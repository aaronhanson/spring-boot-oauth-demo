package springboot.pac4j

import groovy.util.logging.Slf4j
import org.pac4j.core.profile.UserProfile
import org.pac4j.springframework.security.authentication.ClientAuthenticationProvider
import org.pac4j.springframework.security.authentication.ClientAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.security.web.savedrequest.SavedRequest
import org.springframework.util.StringUtils
import springboot.pac4j.service.AccountService

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class ClientSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache()

    String registrationUrl = "/registration"

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response)

        if (authentication instanceof ClientAuthenticationToken) {
            if (authentication.authorities.isEmpty()) {
                request.session.setAttribute("savedAuthToken", authentication)
                // "log them out" until they finish the registration
                SecurityContextHolder.clearContext()
                getRedirectStrategy().sendRedirect(request, response, registrationUrl)
                return
            }
        }

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication)
            return
        }

        String targetUrlParameter = getTargetUrlParameter()
        boolean hasTargetUrl = targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter))
        if (isAlwaysUseDefaultTargetUrl() || hasTargetUrl) {
            requestCache.removeRequest(request, response)
            super.onAuthenticationSuccess(request, response, authentication)
            return
        }

        clearAuthenticationAttributes(request)

        // redirect back to where the user was going
        getRedirectStrategy().sendRedirect(request, response, savedRequest.getRedirectUrl())
    }

}
