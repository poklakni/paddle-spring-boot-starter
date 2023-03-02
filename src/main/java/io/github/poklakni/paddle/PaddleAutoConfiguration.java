package io.github.poklakni.paddle;

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

  /**
   * Auto-configured {@link PaddleAuthorizationManager}
   * @param paddleProperties Paddle properties
   * @return auto-configured {@link PaddleAuthorizationManager}
   */
  @Bean
  @ConditionalOnMissingBean
  public PaddleAuthorizationManager paddleAuthorizationManager(PaddleProperties paddleProperties) {
    return new PaddleAuthorizationManager(paddleProperties.whitelist());
  }

  /**
   * Auto-configured {@link PaddleSignatureVerifier}
   * @param paddleProperties Paddle properties
   * @return auto-configured {@link PaddleSignatureVerifier}
   */
  @Bean
  @ConditionalOnMissingBean
  public PaddleSignatureVerifier paddleSignatureVerifier(PaddleProperties paddleProperties) {
    return new PaddleSignatureVerifier(paddleProperties.publicKey());
  }
}
