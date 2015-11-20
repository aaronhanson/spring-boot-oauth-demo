package springboot.pac4j.conf

import org.pac4j.core.client.Clients
import org.pac4j.oauth.client.GitHubClient
import org.pac4j.springframework.security.authentication.ClientAuthenticationProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Pac4jConfig {

    @Value('${oauth.callback.url}')
    String oauthCallbackUrl

    @Value('${oauth.github.app.key}')
    String githubKey

    @Value('${oauth.github.app.secret}')
    String githubSecret

    @Bean
    ClientAuthenticationProvider clientProvider() {
        return new ClientAuthenticationProvider(clients: clients())
    }

    @Bean
    GitHubClient gitHubClient() {
        return new GitHubClient(githubKey, githubSecret)
    }

    @Bean
    Clients clients() {
        return new Clients(oauthCallbackUrl, gitHubClient())
    }

}
