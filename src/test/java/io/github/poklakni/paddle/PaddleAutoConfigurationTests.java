package io.github.poklakni.paddle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

class PaddleAutoConfigurationTests {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(PaddleAutoConfiguration.class));

  @Test
  void paddleAutoConfigurationEnabled() throws NoSuchAlgorithmException {
    this.contextRunner
        .withPropertyValues(
            "paddle.publicKey:" + new TestSigner().getPublicKey(), "paddle.whitelist:1,2,3")
        .run(
            context -> {
              assertThat(context).hasSingleBean(PaddleProperties.class);
              assertThat(context).hasSingleBean(PaddleSignatureVerifier.class);
              assertThat(context).hasSingleBean(PaddleAuthorizationManager.class);
            });
  }

  @Test
  void paddleAutoConfigurationDisabled() {
    this.contextRunner
        .withPropertyValues("paddle.enabled:false")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean(PaddleProperties.class);
              assertThat(context).doesNotHaveBean(PaddleSignatureVerifier.class);
              assertThat(context).doesNotHaveBean(PaddleAuthorizationManager.class);
            });
  }
}
