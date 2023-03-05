package io.github.poklakni.paddle;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PaddleAuthorizationManagerTests {

  private static Stream<Arguments> provideWhitelistAndExpectedDecision() {
    return Stream.of(
        Arguments.of(Set.of("192.168.0.1"), true),
        Arguments.of(Set.of("10.0.0.1"), false),
        Arguments.of(Collections.emptySet(), true),
        Arguments.of(null, true));
  }

  @ParameterizedTest
  @MethodSource("provideWhitelistAndExpectedDecision")
  void shouldGrantAuthorizationDecisionBasedOnIpWhitelist(
      Set<String> whitelist, boolean expectedDecision) {

    // Given
    var authorizationManager = new PaddleAuthorizationManager(whitelist);
    var request = new MockHttpServletRequest();
    request.setRemoteAddr("192.168.0.1");
    var context = new RequestAuthorizationContext(request);

    // When
    var decision = authorizationManager.check(() -> null, context);

    // Then
    assertThat(decision)
        .isNotNull()
        .extracting(AuthorizationDecision::isGranted)
        .isEqualTo(expectedDecision);
  }
}
