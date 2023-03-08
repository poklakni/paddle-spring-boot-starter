package io.github.poklakni.paddle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Paddle authorization manager based on IP whitelisting.
 *
 * @author Dominik Kovács
 */
public class PaddleAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {

  private static final Logger log = LoggerFactory.getLogger(PaddleAuthorizationManager.class);

  @Nullable private final Set<String> whitelist;

  /**
   * Constructs {@link PaddleAuthorizationManager} with IP whitelist
   *
   * @param whitelist IP whitelist
   */
  public PaddleAuthorizationManager(@Nullable Set<String> whitelist) {
    this.whitelist = whitelist;
  }

  /**
   * executes the IP verification check
   *
   * @param authentication authentication
   * @param context request context
   */
  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext context) {
    var granted =
        whitelist == null
            || whitelist.isEmpty()
            || whitelist.stream()
                .map(IpAddressMatcher::new)
                .anyMatch(ipAddressMatcher -> ipAddressMatcher.matches(context.getRequest()));

    if (!granted) {
      log.warn("Blocked request from not trusted IP {}.", context.getRequest().getRemoteAddr());
    }
    return new AuthorizationDecision(granted);
  }
}
