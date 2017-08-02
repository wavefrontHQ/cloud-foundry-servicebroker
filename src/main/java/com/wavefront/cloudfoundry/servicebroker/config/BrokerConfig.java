package com.wavefront.cloudfoundry.servicebroker.config;

import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Broker configuration.
 *
 * @author Vikram Raman
 */
@Configuration
public class BrokerConfig {

  @Bean
  public BrokerApiVersion brokerApiVersion() {
    return new BrokerApiVersion();
  }
}
