package io.github.poklakni.paddle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PaddleAutoConfigurationTests {

  private static final String RANDOM_PUBLIC_KEY =
      """
            -----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAlxMy2QvQa1HAP2iScit3
            J6U7v6YBwxWajPmLkRSxcxU1zAE8hejWl54RbTcKfT7TfTaaTvwQsLVs3C/SsV7v
            hGHsl27V7nHL/w/VgOdV7QUbH1YThG7jBd+VBia05JhZZy33fL5/jgJi7Gn4X48I
            aOkb5g5fe5nNJbSDF5yHKAd2Q3c3qvk3Qd8ztZ6Utzv6LpkNNx57RJ6iv8yNMKjz
            0GBPOlC7ymd9ZT9Twh1/3E1aA55Pl2FTyLhN7vPhluOjgV7EjrYH1uV7i2gj/nO/
            7toqup82fAahOwajXMtKb7VhhEJYhZ7VQfcPyG71Ny7HznBmt/jrJ7VGV/KnA+eR
            fA9TccT7cwJykE+3q7VQPlCKkjhnG7DwbfqU3qMAU2LL+8WDBDXe6v/0ghRXR57D
            N8I+7z6vCfJ/sLmD1j5+YpzBzv3FyC2zz4WRtKjCE2+omvDZaSWOaz8ZZKBlG18t
            YXBtIn7/GfMIMtZiXjNC8xIb/vmkbB1tdfzg1tUTKjHnzDdYpGumZSjXW5x8vcse
            Ld51I5+tj4QWwq8y/7VcI/4icrC2Wq1tkjKplh90ss0mbRvP6hvG+56UOgm6ePTl
            gOaXsJxLsFByVpEjK+D7OuUtP8QH7TJj1AdorZ+9gF8sUBGn7EzKjD+V7mL8aT/G
            7jK1Whr/2C+e3qJjB7NTbFMCAwEAAQ==
            -----END PUBLIC KEY-----""";

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(PaddleAutoConfiguration.class));

  @Test
  void paddleAutoConfigurationEnabled() {
    this.contextRunner
        .withPropertyValues("paddle.publicKey:" + RANDOM_PUBLIC_KEY, "paddle.whitelist:1,2,3")
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
