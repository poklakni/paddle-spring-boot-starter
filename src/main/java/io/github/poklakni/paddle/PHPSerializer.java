package io.github.poklakni.paddle;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

class PHPSerializer {

  private PHPSerializer() {}

  static String toSerializedString(Map<String, String> map) {
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
