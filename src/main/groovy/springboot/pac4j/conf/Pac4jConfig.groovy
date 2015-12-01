package springboot.pac4j.conf

import org.pac4j.core.client.Clients
import org.pac4j.oauth.client.GitHubClient
import org.pac4j.oauth.client.Google2Client
import org.pac4j.oauth.client.TwitterClient
import org.pac4j.springframework.security.authentication.ClientAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springboot.pac4j.security.ClientUserDetailsService
import springboot.pac4j.service.AccountService

@Configuration
class Pac4jConfig {

    @Value('${oauth.callback.url}')
    String oauthCallbackUrl

    @Value('${oauth.github.app.key}')
    String githubKey

    @Value('${oauth.github.app.secret}')
    String githubSecret

    @Value('${oauth.twitter.app.key}')
    String twitterKey

    @Value('${oauth.twitter.app.secret}')
    String twitterSecret

    @Value('${oauth.google.app.key}')
    String googleKey

    @Value('${oauth.google.app.secret}')
    String googleSecret

    @Autowired
    AccountService accountService

    @Bean
    ClientUserDetailsService clientUserDetailsService() {
        return new ClientUserDetailsService(accountService: accountService)
    }

    @Bean
    ClientAuthenticationProvider clientProvider() {
        return new ClientAuthenticationProvider(
                clients: clients(),
                userDetailsService: clientUserDetailsService()
        )
    }

    @Bean
    TwitterClient twitterClient() {
        return new TwitterClient(twitterKey, twitterSecret)
    }

    @Bean
    Google2Client google2Client() {
        return new Google2Client(googleKey, googleSecret)
    }

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubClient(githubKey, githubSecret)
    }

    @Bean
    Clients clients() {
        return new Clients(oauthCallbackUrl, gitHubClient(), twitterClient(), google2Client())
    }

}
