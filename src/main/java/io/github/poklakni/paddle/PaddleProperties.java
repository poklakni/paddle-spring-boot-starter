package io.github.poklakni.paddle;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * Paddle configuration properties.
 *
 * @param whitelist List of allowed IP addresses that Paddle sends requests from. Read more on the
 *     Paddle official page <a
 *     href="https://developer.paddle.com/webhook-reference/d8bbc4ae5cefa-security#ip-allowlisting">IP
 *     whitelisting</a>
 * @param publicKey public key used to verify the signature of incoming Paddle webhook.
 * @author Dominik Kovács
 */
@ConfigurationProperties(prefix = PaddleProperties.PREFIX)
public record PaddleProperties(@Nullable Set<String> whitelist, String publicKey) {

  /** Paddle configuration properties prefix */
  public static final String PREFIX = "paddle";
}
