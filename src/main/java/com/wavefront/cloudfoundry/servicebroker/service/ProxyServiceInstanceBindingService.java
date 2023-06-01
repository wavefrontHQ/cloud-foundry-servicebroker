package com.wavefront.cloudfoundry.servicebroker.service;

import com.wavefront.cloudfoundry.servicebroker.config.ProxyConfig;
import com.wavefront.cloudfoundry.servicebroker.model.Route;
import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstanceBinding;
import com.wavefront.cloudfoundry.servicebroker.repository.ProxyServiceInstanceBindingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for binding/unbinding apps to a proxy service instance.
 *
 * @author Vikram Raman
 */
@Service
public class ProxyServiceInstanceBindingService implements ServiceInstanceBindingService {

  private final ProxyServiceInstanceBindingRepository repository;

  private final ProxyConfig proxyConfig;

  @Autowired
  public ProxyServiceInstanceBindingService(ProxyConfig config, ProxyServiceInstanceBindingRepository repository) {
    this.proxyConfig = config;
    this.repository = repository;
  }

  @Override
  public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
      CreateServiceInstanceBindingRequest request) {

    String bindingId = request.getBindingId();
    String serviceInstanceId = request.getServiceInstanceId();

    Optional<ServiceInstanceBinding> binding = repository.findById(bindingId);
    if (binding.isPresent()) {
      throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
    }

    Route route = new Route();
    route.setId(UUID.randomUUID().toString());
    route.setHostname(proxyConfig.getHostname());
    route.setPort(proxyConfig.getPort());
    route.setDistributionPort(proxyConfig.getPort());
    route.setTracingPort(30000);

    repository.save(new ServiceInstanceBinding(bindingId, serviceInstanceId, route, request.getBoundAppGuid()));

    return new CreateServiceInstanceAppBindingResponse().withCredentials(wrapCredentials(route));
  }

  @Override
  public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
    String bindingId = request.getBindingId();
    Optional<ServiceInstanceBinding> binding = repository.findById(bindingId);

    if (!binding.isPresent()) {
      throw new ServiceInstanceBindingDoesNotExistException(bindingId);
    }
    repository.deleteById(bindingId);
  }

  private Map<String, Object> wrapCredentials(Route creds) {
    Map<String, Object> credsMap = new HashMap<>();
    credsMap.put("hostname", creds.getHostname());
    credsMap.put("port", creds.getPort());
    credsMap.put("distributionPort", creds.getDistributionPort());
    credsMap.put("tracingPort", creds.getTracingPort());
    return credsMap;
  }
}
