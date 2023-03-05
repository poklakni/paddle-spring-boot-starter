package io.github.poklakni.paddle;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
   *
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
   *
   * @param paddleProperties Paddle properties
   * @return auto-configured {@link PaddleSignatureVerifier}
   * @throws NoSuchAlgorithmException when the SHA1withRSA algorithm is no longer supported
   * @throws InvalidKeySpecException when the provided public key is invalid
   */
  @Bean
  @ConditionalOnMissingBean
  public PaddleSignatureVerifier paddleSignatureVerifier(PaddleProperties paddleProperties)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    return new PaddleSignatureVerifier(paddleProperties.publicKey());
  }
}
