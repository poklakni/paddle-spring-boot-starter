package sk.poklakni.paddle;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for {@link PaddleAuthorizationManager} and {@link PaddleSignatureVerifier}.
 *
 * @author Dominik Kovács
 */
@AutoConfiguration
@EnableConfigurationProperties(sk.poklakni.paddle.PaddleProperties.class)
@ConditionalOnProperty(
    prefix = sk.poklakni.paddle.PaddleProperties.PREFIX,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class PaddleAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public sk.poklakni.paddle.PaddleAuthorizationManager paddleAuthorizationManager(sk.poklakni.paddle.PaddleProperties paddleProperties) {
    return new sk.poklakni.paddle.PaddleAuthorizationManager(paddleProperties.whitelist());
  }

  @Bean
  @ConditionalOnMissingBean
  public sk.poklakni.paddle.PaddleSignatureVerifier paddleSignatureVerifier(sk.poklakni.paddle.PaddleProperties paddleProperties) {
    return new sk.poklakni.paddle.PaddleSignatureVerifier(paddleProperties.publicKey());
  }
}
