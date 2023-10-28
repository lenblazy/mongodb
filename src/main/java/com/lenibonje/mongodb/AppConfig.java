package com.lenibonje.mongodb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mongodb")
public record AppConfig(String connection) {
}