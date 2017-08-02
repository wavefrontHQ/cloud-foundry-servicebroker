package com.wavefront.cloudfoundry.servicebroker.config;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Defines the catalog of services offered by the service broker.
 *
 * @author Vikram Raman
 */
@Configuration
public class CatalogConfig {

  @Bean
  public Catalog catalog() {
    return new Catalog(Collections.singletonList(
        new ServiceDefinition(
            "wavefront-proxy",
            "wavefront-proxy",
            "Proxy service to send points to a Wavefront instance",
            true,
            false,
            Collections.singletonList(
                new Plan("standard-plan",
                    "standard",
                    "This is a standard Wavefront proxy plan",
                    getPlanMetadata(),
                    true)),
            Arrays.asList("wavefront", "metrics"),
            getServiceDefinitionMetadata(),
            null,
            null
        )
    ));
  }

  /**
   * Gets the metadata used by the Pivotal CF Apps Manager Console.
   *
   * @return the metadata for the PCF Apps Manager Console
   */
  private Map<String, Object> getServiceDefinitionMetadata() {
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("displayName", "Wavefront Proxy");
    metadata.put("imageUrl", "https://s3-us-west-2.amazonaws.com/wavefront-img/wavefront_by_vmware_logo.png");
    metadata.put("longDescription", "Wavefront Proxy Service");
    metadata.put("providerDisplayName", "VMWare, Inc.");
    metadata.put("documentationUrl", "https://github.com/wavefrontHQ/cloudfoundry-servicebroker");
    return metadata;
  }

  private Map<String, Object> getPlanMetadata() {
    Map<String, Object> planMetadata = new HashMap<>();
    planMetadata.put("costs", getCosts());
    planMetadata.put("bullets", getBullets());
    return planMetadata;
  }

  private List<Map<String, Object>> getCosts() {
    Map<String, Object> costsMap = new HashMap<>();

    Map<String, Object> amount = new HashMap<>();
    amount.put("usd", 0.0);

    costsMap.put("amount", amount);
    costsMap.put("unit", "MONTHLY");

    return Collections.singletonList(costsMap);
  }

  private List<String> getBullets() {
    return Arrays.asList(
        "Sends data collected from hosts, containers and applications to Wavefront in a secure and reliable manner",
        "Handles hundreds to thousands of simultaneous clients",
        "Provides end-to-end flow control by queueing points when network connectivity is down"
    );
  }
}
