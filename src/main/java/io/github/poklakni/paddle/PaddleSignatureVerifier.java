package io.github.poklakni.paddle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * Paddle signature verifier
 *
 * @author Dominik Kovács
 */
public class PaddleSignatureVerifier {

  private static final Logger log = LoggerFactory.getLogger(PaddleSignatureVerifier.class);

  private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
  private static final String PUBLIC_KEY_ALGORITHM = "RSA";
  private static final String SIGNATURE_PARAMETER = "p_signature";
  private static final String BEGIN_PUBLIC_KEY_DELIMITER = "-----BEGIN PUBLIC KEY-----";
  private static final String END_PUBLIC_KEY_DELIMITER = "-----END PUBLIC KEY-----";

  private final Signature signature;

  private final PublicKey publicKey;

  /**
   * Constructs {@link PaddleSignatureVerifier} with provided public key
   *
   * @param publicKey Paddle public key
   * @throws NoSuchAlgorithmException when the SHA1withRSA algorithm is no longer supported
   * @throws InvalidKeySpecException when the provided public key is invalid
   */
  public PaddleSignatureVerifier(String publicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    Assert.hasText(publicKey, "Public key must be provided.");
    this.signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    this.publicKey = getPublicKey(publicKey);
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
    if (!event.containsKey(SIGNATURE_PARAMETER)) {
      log.warn("Missing signature in event body.");
      return false;
    }
    var signatureValue = event.remove(SIGNATURE_PARAMETER);

    try {
      byte[] serializedEvent = PHPSerializer.toSerializedString(event).getBytes();
      byte[] decodedSignatureValue = Base64.getDecoder().decode(signatureValue);

      this.signature.initVerify(publicKey);
      this.signature.update(serializedEvent);
      boolean verified = this.signature.verify(decodedSignatureValue);
      if (!verified) {
        log.warn("Signature could not be verified.");
      }
      return verified;
    } catch (Exception e) {
      log.warn("Error verifying signature.", e);
      return false;
    }
  }

  private PublicKey getPublicKey(String publicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    var key =
        publicKey.replace(BEGIN_PUBLIC_KEY_DELIMITER, "").replace(END_PUBLIC_KEY_DELIMITER, "");

    var decodedKey = Base64.getMimeDecoder().decode(key);
    var keySpec = new X509EncodedKeySpec(decodedKey);
    return KeyFactory.getInstance(PUBLIC_KEY_ALGORITHM).generatePublic(keySpec);
  }
}
