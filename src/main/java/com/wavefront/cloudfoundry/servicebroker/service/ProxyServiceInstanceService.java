package com.wavefront.cloudfoundry.servicebroker.service;

import com.wavefront.cloudfoundry.servicebroker.model.ServiceInstance;
import com.wavefront.cloudfoundry.servicebroker.repository.ProxyServiceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

/**
 * Service for handling CRUD operations on Wavefront proxy service instances.
 *
 * Instantiated proxy service instances provide a mechanism for third party apps
 * to retrieve routing information for a deployed proxy application.
 *
 * @author Vikram Raman
 */
@Service
public class ProxyServiceInstanceService implements ServiceInstanceService {

    private ProxyServiceInstanceRepository repository;

    @Autowired
    public ProxyServiceInstanceService(ProxyServiceInstanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {

        ServiceInstance instance = repository.findOne(request.getServiceInstanceId());
        if (instance != null) {
            throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
        }

        instance = new ServiceInstance(request);
        repository.save(instance);
        return new CreateServiceInstanceResponse();
    }

    @Override
    public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
        return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
    }

    @Override
    public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
        String id = request.getServiceInstanceId();
        ServiceInstance instance = repository.findOne(id);
        if (instance == null) {
            throw new ServiceInstanceDoesNotExistException(id);
        }

        repository.delete(id);
        return new DeleteServiceInstanceResponse();
    }

    @Override
    public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
        String id = request.getServiceInstanceId();
        ServiceInstance instance = repository.findOne(id);
        if (instance == null) {
            throw new ServiceInstanceDoesNotExistException(id);
        }

        repository.delete(id);
        ServiceInstance updatedInstance = new ServiceInstance(request);
        repository.save(updatedInstance);
        return new UpdateServiceInstanceResponse();
    }
}
