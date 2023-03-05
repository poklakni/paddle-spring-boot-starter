package io.github.poklakni.paddle;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class TestSigner {

  private final PublicKey publicKey;

  private final PrivateKey privateKey;

  public TestSigner() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
    keyPairGen.initialize(512);
    KeyPair pair = keyPairGen.generateKeyPair();
    this.privateKey = pair.getPrivate();
    this.publicKey = pair.getPublic();
  }

  public Map<String, String> sign(Map<String, String> event)
      throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

    String serialized = PHPSerializer.toSerializedString(event);
    byte[] data = serialized.getBytes(StandardCharsets.UTF_8);
    Signature signature = Signature.getInstance("SHA1WithRSA");
    signature.initSign(privateKey);
    signature.update(data);

    String signatureValue = Base64.getEncoder().encodeToString(signature.sign());
    Map<String, String> updatedEvent = new HashMap<>(event);
    updatedEvent.put("p_signature", signatureValue);
    return updatedEvent;
  }

  public String getPublicKey() {
    return "-----BEGIN PUBLIC KEY-----"
        .concat(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
        .concat("-----END PUBLIC KEY-----");
  }
}
