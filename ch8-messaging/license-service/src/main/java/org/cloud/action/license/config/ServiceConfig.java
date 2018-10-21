package org.cloud.action.license.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig{
    @Value("${example.property}")
    private String exampleProperty;

    @Value("${redis.server}")
    private String redisServer = "";

    @Value("${redis.port}")
    private Integer redisPort;

    public String getExampleProperty(){
        return exampleProperty;
    }

    public String getRedisServer() {
        return redisServer;
    }

    public int getRedisPort() {
        return redisPort;
    }
}
