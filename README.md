# Paddle Spring Boot Starter

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Known Vulnerabilities](https://snyk.io/test/github/poklakni/paddle-spring-boot-starter/badge.svg)](https://snyk.io/test/github/poklakni/paddle-spring-boot-starter/badge.svg)
[![codecov](https://codecov.io/gh/poklakni/paddle-spring-boot-starter/branch/main/graph/badge.svg?token=FB7XNCOFD9)](https://codecov.io/gh/poklakni/paddle-spring-boot-starter)
---

## Installation
Include this dependency in your project
```xml
<dependency>
    <groupId>io.github.poklakni.paddle</groupId>
    <artifactId>paddle-spring-boot-starter</artifactId>
    <scope>1.0.3</scope>
</dependency>
```
---
## Usage
Add Paddle properties to your project

Example usage  
Actual whitelist can be found on [Paddle official website](https://developer.paddle.com/webhook-reference/d8bbc4ae5cefa-security)
```yaml
paddle:
  enabled: true (default)
  publicKey: <your paddle public key>
  whitelist:
    - 0.0.0.0
    - 1.1.1.1
```

---
### PaddleAuthorizationManager
Example usage
```java
@Configuration
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, PaddleAuthorizationManager paddleAuthorizationManager) {
    http.authorizeHttpRequests()
      .requestMatchers("/foo/bar/**")
      .access(paddleAuthorizationManager)
      .anyRequest()
      .authenticated();
    
    return http.build();
  }
}
```

---
### PaddleSignatureVerifier
Example usage
```java
@Service
public class SubscriptionEventService {

  private final PaddleSignatureVerifier paddleSignatureVerifier;

  public SubscriptionEventService(PaddleSignatureVerifier paddleSignatureVerifier) {
    this.paddleSignatureVerifier = paddleSignatureVerifier;
  }
  
  public boolean verifySignature(Map<String, String> event) {
    return paddleSignatureVerifier.verify(event);
  }
}
```
---
## Licence
Paddle Spring BootStarter is Open Source software released under the [MIT license](https://opensource.org/license/mit/).