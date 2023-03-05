package io.github.poklakni.paddle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PaddleSignatureVerifierTests {

  private TestSigner testSigner;

  private PaddleSignatureVerifier signatureVerifier;

  @BeforeEach
  void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException {
    testSigner = new TestSigner();
    signatureVerifier = new PaddleSignatureVerifier(testSigner.getPublicKey());
  }

  @Test
  void shouldAcceptEventWithValidSignature()
      throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

    // Given
    Map<String, String> event = new HashMap<>();
    event.put("test", "value");
    event.put("foo", "bar");
    Map<String, String> signedEvent = testSigner.sign(event);

    // When
    var verified = signatureVerifier.verify(signedEvent);

    // Then
    assertThat(verified).isTrue();
  }

  @Test
  void shouldNotAcceptEventWithoutSignature() {

    // Given
    Map<String, String> event = new HashMap<>();
    event.put("test", "value");
    event.put("foo", "bar");

    // When
    var verified = signatureVerifier.verify(event);

    // Then
    assertThat(verified).isFalse();
  }

  @Test
  void shouldNotAcceptEventWithInvalidSignature() {

    // Given
    Map<String, String> event = new HashMap<>();
    event.put("test", "value");
    event.put("foo", "bar");
    event.put("p_signature", Base64.getEncoder().encodeToString("invalidSignature".getBytes()));

    // When
    var verified = signatureVerifier.verify(event);

    // Then
    assertThat(verified).isFalse();
  }
}
