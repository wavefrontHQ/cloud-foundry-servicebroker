package com.wavefront.cloudfoundry.servicebroker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Proxy route properties.
 *
 * @author Vikram Raman
 */
@Configuration
@ConfigurationProperties(prefix = "wavefront.proxy")
public class ProxyConfig {

  /**
   * The cloud foundry TCP route for the proxy.
   */
  private String hostname;

  /**
   * The cloud foundry TCP port for the proxy.
   */
  private int port;

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }
}
