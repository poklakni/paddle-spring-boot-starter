package io.github.poklakni.paddle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

class PHPSerializer {

  private static final Logger log = LoggerFactory.getLogger(PHPSerializer.class);

  private PHPSerializer() {}

  static String toSerializedString(Map<String, String> event) {
    var builder = new StringBuilder().append("a:").append(event.size()).append(":{");

    for (var entry : new TreeMap<>(event).entrySet()) {
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
    log.debug("Event map converted to PHP serialized string: {}", builder);
    return builder.toString();
  }
}
