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
@EnableConfigurationProperties(PaddleProperties.class)
@ConditionalOnProperty(
    prefix = PaddleProperties.PREFIX,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class PaddleAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public PaddleAuthorizationManager paddleAuthorizationManager(PaddleProperties paddleProperties) {
    return new PaddleAuthorizationManager(paddleProperties.whitelist());
  }

  @Bean
  @ConditionalOnMissingBean
  public PaddleSignatureVerifier paddleSignatureVerifier(PaddleProperties paddleProperties) {
    return new PaddleSignatureVerifier(paddleProperties.publicKey());
  }
}
