package io.github.poklakni.paddle;

import org.springframework.util.Assert;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * Paddle signature verifier
 *
 * @author Dominik Kovács
 */
public class PaddleSignatureVerifier {

  private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
  private static final String PUBLIC_KEY_ALGORITHM = "RSA";
  private static final String SIGNATURE_PARAMETER = "p_signature";

  private final String publicKey;

  /**
   * Constructs {@link PaddleSignatureVerifier} with provided public key
   *
   * @param publicKey Paddle public key
   */
  public PaddleSignatureVerifier(String publicKey) {
    Assert.hasText(publicKey, "Public key must be provided.");
    this.publicKey = publicKey;
  }

  /**
   * Checks that webhooks are genuinely sent by Paddle. Signature field "p_signature" present in
   * every webhook body is used for verification. More info on <a
   * href="https://developer.paddle.com/webhook-reference/ZG9jOjI1MzUzOTg2-verifying-webhooks#verifying-webhooks">Paddle
   * official website</a>
   *
   * @param event Paddle event received via webhook.
   * @return verification result whether the signature was successfully verified.
   * @author Dominik Kovács
   */
  public boolean verify(Map<String, String> event) {
    var signature = event.remove(SIGNATURE_PARAMETER);
    if (signature == null) {
      return false;
    }

    try {
      var signer = Signature.getInstance(SIGNATURE_ALGORITHM);
      signer.initVerify(getPublicKey());
      signer.update(buildSignerString(event).getBytes());

      return signer.verify(Base64.getDecoder().decode(signature));
    } catch (Exception e) {
      return false;
    }
  }

  private PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    var key =
        publicKey
            .replace("-----BEGIN PUBLIC KEY-----\n", "")
            .replace("-----END PUBLIC KEY-----", "");

    var decodedKey = Base64.getMimeDecoder().decode(key);
    var keySpec = new X509EncodedKeySpec(decodedKey);
    return KeyFactory.getInstance(PUBLIC_KEY_ALGORITHM).generatePublic(keySpec);
  }

  private String buildSignerString(Map<String, String> map) {
    var builder = new StringBuilder().append("a:").append(map.size()).append(":{");

    for (var entry : new TreeMap<>(map).entrySet()) {
      var valueDecoded = URLDecoder.decode(entry.getValue(), StandardCharsets.UTF_8);
      builder.append(
          String.format(
              "s:%d:\"%s\";s:%d:\"%s\";",
              entry.getKey().getBytes(StandardCharsets.UTF_8).length,
              entry.getKey(),
              valueDecoded.getBytes(StandardCharsets.UTF_8).length,
              valueDecoded));
    }

    builder.append("}");
    return builder.toString();
  }
}
