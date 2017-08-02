package com.wavefront.cloudfoundry.servicebroker.repository;

import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstanceBinding;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for proxy service instance bindings.
 *
 * @author Vikram Raman
 */
public interface ProxyServiceInstanceBindingRepository extends CrudRepository<ServiceInstanceBinding, String> {
}
