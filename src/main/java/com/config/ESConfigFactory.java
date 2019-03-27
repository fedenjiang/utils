package com.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ES配置信息
 */
@SpringBootApplication
@ConfigurationProperties(prefix = "es-config")
public class ESConfigFactory {

    @JsonProperty("enable")
    private boolean enable = false;

    @JsonProperty("servers")
    private String servers;

    @JsonProperty("user")
    private String user;

    @JsonProperty("pass")
    private String pass;


    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
