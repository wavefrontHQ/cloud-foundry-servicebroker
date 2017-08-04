package com.wavefront.cloudfoundry.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;

import javax.persistence.*;

/**
 * Represents a service instance binding.
 *
 * @author Vikram Raman
 */
@Entity
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ServiceInstanceBinding {

  @JsonSerialize
  @JsonProperty("service_instance_binding_id")
  @Id
  private String id;

  @JsonSerialize
  @JsonProperty("service_instance_id")
  private String serviceInstanceId;

  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
  @JoinColumn(name = "service_instance_binding_id")
  private Route route;

  @JsonSerialize
  @JsonProperty("app_guid")
  private String appGuid;

  @SuppressWarnings("unused")
  private ServiceInstanceBinding() {
  }

  public ServiceInstanceBinding(String id, String serviceInstanceId, Route route, String appGuid) {
    this.id = id;
    this.serviceInstanceId = serviceInstanceId;
    this.route = route;
    this.appGuid = appGuid;
  }

  public String getId() {
    return this.id;
  }

  public String getServiceInstanceId() {
    return this.serviceInstanceId;
  }

  public Route getRoute() {
    return this.route;
  }

  public String getAppGuid() {
    return this.appGuid;
  }
}

