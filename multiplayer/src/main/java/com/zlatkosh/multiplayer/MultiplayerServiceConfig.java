package com.zlatkosh.multiplayer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service.multiplayer")
public class MultiplayerServiceConfig {
}
