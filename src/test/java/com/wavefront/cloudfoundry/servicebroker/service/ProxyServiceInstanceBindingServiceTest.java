package com.wavefront.cloudfoundry.servicebroker.service;

import java.util.Collections;
import java.util.Map;

import com.wavefront.cloudfoundry.servicebroker.config.ProxyConfig;
import com.wavefront.cloudfoundry.servicebroker.model.Route;
import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstance;
import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstanceBinding;
import com.wavefront.cloudfoundry.servicebroker.repository.ProxyServiceInstanceBindingRepository;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.model.fixture.ServiceInstanceBindingFixture;
import org.springframework.cloud.servicebroker.model.fixture.ServiceInstanceFixture;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@code ProxyServiceInstanceBindingService}.
 *
 * @author Vikram Raman
 */
@RunWith(SpringRunner.class)
public class ProxyServiceInstanceBindingServiceTest {

  private static final String HOSTNAME = "test-hostname";

  private static final int PORT = 2878;

  @Mock
  private ProxyServiceInstanceBindingRepository repository;

  private ProxyServiceInstanceBindingService service;

  private ServiceInstance instance;
  private Optional<ServiceInstanceBinding> instanceBinding;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    ProxyConfig config = new ProxyConfig();
    config.setHostname(HOSTNAME);
    config.setPort(PORT);

    service = new ProxyServiceInstanceBindingService(config, repository);
    instance = buildServiceInstance();
    instanceBinding = buildServiceBindingInstance();
  }

  @Test
  public void newServiceInstanceBindingCreatedSuccessfully() throws Exception {

    when(repository.findById(any(String.class))).thenReturn(null);

    CreateServiceInstanceAppBindingResponse response =
        (CreateServiceInstanceAppBindingResponse) service.createServiceInstanceBinding(buildCreateRequest());

    assertNotNull(response);
    assertNotNull(response.getCredentials());
    assertNull(response.getSyslogDrainUrl());

    verify(repository).save(isA(ServiceInstanceBinding.class));
  }


  @Test(expected = ServiceInstanceBindingExistsException.class)
  public void serviceInstanceCreationFailsWithExistingInstance() throws Exception {

    when(repository.findById(any(String.class)))
        .thenReturn(buildServiceBindingInstance());

    service.createServiceInstanceBinding(buildCreateRequest());
  }

  @Test
  public void serviceInstanceBindingDeletedSuccessfully() throws Exception {
      Optional<ServiceInstanceBinding> binding = buildServiceBindingInstance();
    when(repository.findById(any(String.class))).thenReturn(binding);

    service.deleteServiceInstanceBinding(buildDeleteRequest());
    verify(repository).deleteById(binding.get().getId());
  }


  @Test(expected = ServiceInstanceBindingDoesNotExistException.class)
  public void unknownServiceInstanceDeleteCallSuccessful() throws Exception {
      Optional<ServiceInstanceBinding> binding = buildServiceBindingInstance();

    when(repository.findById(any(String.class))).thenReturn(null);

    service.deleteServiceInstanceBinding(buildDeleteRequest());
    verify(repository, never()).deleteById(binding.get().getId());
  }

  private CreateServiceInstanceBindingRequest buildCreateRequest() {
    Map<String, Object> bindResource =
        Collections.singletonMap(ServiceBindingResource.BIND_RESOURCE_KEY_APP.toString(), (Object) "app_guid");
    return new CreateServiceInstanceBindingRequest(instance.getServiceDefinitionId(), instance.getPlanId(),
        "app_guid", bindResource)
        .withServiceInstanceId(instance.getServiceInstanceId())
        .withBindingId(instanceBinding.get().getId());
  }

  private DeleteServiceInstanceBindingRequest buildDeleteRequest() {
    return new DeleteServiceInstanceBindingRequest(instance.getServiceInstanceId(), instanceBinding.get().getId(),
        instance.getServiceDefinitionId(), instance.getPlanId(), null);
  }

  private ServiceInstance buildServiceInstance() {
    CreateServiceInstanceRequest request = ServiceInstanceFixture.buildCreateServiceInstanceRequest(false);
    ServiceInstance instance = new ServiceInstance(request);
    return instance;
  }

  private Optional<ServiceInstanceBinding> buildServiceBindingInstance() {
    CreateServiceInstanceBindingRequest request = ServiceInstanceBindingFixture.buildCreateAppBindingRequest();
    Route route = new Route();
    route.setHostname(HOSTNAME);
    route.setPort(PORT);
    ServiceInstanceBinding instance = new ServiceInstanceBinding(request.getBindingId(),
        request.getServiceInstanceId(), route, request.getAppGuid());
    return Optional.of(instance);
  }

}
