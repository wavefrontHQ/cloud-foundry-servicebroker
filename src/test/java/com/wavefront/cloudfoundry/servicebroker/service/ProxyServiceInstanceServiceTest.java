package com.wavefront.cloudfoundry.servicebroker.service;

import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstance;
import com.wavefront.cloudfoundry.servicebroker.repository.ProxyServiceInstanceRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.model.fixture.ServiceInstanceFixture;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

/**
 * Tests for the {@code ProxyServiceInstanceService}.
 *
 * @author Vikram Raman
 */
@RunWith(SpringRunner.class)
public class ProxyServiceInstanceServiceTest {

  @Mock
  private ProxyServiceInstanceRepository repository;

  private ProxyServiceInstanceService service;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    service = new ProxyServiceInstanceService(repository);
  }

  @Test
  public void newServiceInstanceCreatedSuccessfully() throws Exception {
    when(repository.findById(any(String.class))).thenReturn(null);

    CreateServiceInstanceResponse response = service.createServiceInstance(buildCreateRequest());

    assertNotNull(response);
    assertNull(response.getDashboardUrl());
    assertFalse(response.isAsync());

    verify(repository).save(isA(ServiceInstance.class));
  }

  @Test(expected = ServiceInstanceExistsException.class)
  public void serviceInstanceCreationFailsWithExistingInstance() throws Exception {
      Optional<ServiceInstance> instance = buildServiceInstance();
    when(repository.findById(any(String.class))).thenReturn(instance);
    service.createServiceInstance(buildCreateRequest());
  }

  @Test
  public void serviceInstanceDeletedSuccessfully() throws Exception {
    Optional<ServiceInstance> instance = buildServiceInstance();
    when(repository.findById(any(String.class))).thenReturn(instance);
    String id = instance.get().getServiceInstanceId();

    DeleteServiceInstanceResponse response = service.deleteServiceInstance(buildDeleteRequest());

    assertNotNull(response);
    assertFalse(response.isAsync());

    verify(repository).deleteById(id);
  }


  @Test(expected = ServiceInstanceDoesNotExistException.class)
  public void unknownServiceInstanceDeleteCallSuccessful() throws Exception {
    when(repository.findById(any(String.class))).thenReturn(null);

    DeleteServiceInstanceRequest request = buildDeleteRequest();

    DeleteServiceInstanceResponse response = service.deleteServiceInstance(request);

    assertNotNull(response);
    assertFalse(response.isAsync());

    verify(repository).deleteById(request.getServiceInstanceId());
  }

  private Optional<ServiceInstance> buildServiceInstance() {
    CreateServiceInstanceRequest request = buildCreateRequest();
    ServiceInstance instance = new ServiceInstance(request);
    return Optional.of(instance);
  }

  private CreateServiceInstanceRequest buildCreateRequest() {
    return ServiceInstanceFixture.buildCreateServiceInstanceRequest(false);
  }

  private DeleteServiceInstanceRequest buildDeleteRequest() {
    return ServiceInstanceFixture.buildDeleteServiceInstanceRequest(false);
  }
}
