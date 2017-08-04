package com.wavefront.cloudfoundry.servicebroker.repository;

import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstance;

import org.springframework.data.repository.CrudRepository;

/**
 * Respository interface for proxy service instances.
 *
 * @author Vikram Raman
 */
public interface ProxyServiceInstanceRepository extends CrudRepository<ServiceInstance, String> {
}
