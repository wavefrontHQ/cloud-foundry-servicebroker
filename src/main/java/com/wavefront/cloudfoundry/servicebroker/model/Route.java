package com.wavefront.cloudfoundry.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Vikram Raman
 */
@Entity
@Table(name = "routes")
@JsonIgnoreProperties({"id"})
public class Route {

  @Id
  private String id;

  @Column(nullable = false)
  private String hostname;

  @Column(nullable = false)
  private int port;

  @Column(nullable = false)
  private int distributionPort;

  @Column(nullable = false)
  private int tracingPort;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public int getDistributionPort() {
    return distributionPort;
  }

  public void setDistributionPort(int port) {
    this.distributionPort = port;
  }

  public int getTracingPort() {
    return tracingPort;
  }

  public void setTracingPort(int port) {
    this.tracingPort = port;
  }

}
