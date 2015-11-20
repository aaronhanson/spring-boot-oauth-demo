package springboot.pac4j.controller

import org.pac4j.core.client.BaseClient
import org.pac4j.core.client.Clients
import org.pac4j.core.context.J2EContext
import org.pac4j.core.context.WebContext
import org.pac4j.oauth.client.GitHubClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class LoginController {

    @Autowired
    Clients clients

    @RequestMapping("/login")
    String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        final WebContext context = new J2EContext(request, response)
        final GitHubClient gitHubClient = (GitHubClient) clients.findClient(GitHubClient)
        model.addAttribute("gitHubAuthUrl",  getClientLocation(gitHubClient, context))
        return "login"
    }

    public String getClientLocation(BaseClient client, WebContext context) {
        return client.getRedirectAction(context, false, false).getLocation()
    }

}
