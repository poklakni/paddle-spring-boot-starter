package io.github.poklakni.paddle;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Paddle authorization manager based on IP whitelisting.
 *
 * @author Dominik Kovács
 */
public class PaddleAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {

  private final Set<String> whitelist;

  public PaddleAuthorizationManager(Set<String> whitelist) {
    Assert.notNull(whitelist, "Whitelist must not be set to null.");
    this.whitelist = whitelist;
  }

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext context) {

    var granted =
        whitelist.isEmpty()
            || whitelist.stream()
                .map(IpAddressMatcher::new)
                .anyMatch(ipAddressMatcher -> ipAddressMatcher.matches(context.getRequest()));

    return new AuthorizationDecision(granted);
  }
}
