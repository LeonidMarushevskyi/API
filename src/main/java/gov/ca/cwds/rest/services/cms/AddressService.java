package gov.ca.cwds.rest.services.cms;

import java.io.Serializable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.cms.AddressDao;
import gov.ca.cwds.data.persistence.cms.Address;
import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.cms.PostedAddress;
import gov.ca.cwds.rest.services.CrudsService;
import gov.ca.cwds.rest.services.ServiceException;

/**
 * @author CWDS API Team
 */
public class AddressService implements CrudsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

  private AddressDao addressDao;

  /**
   * @param addressDao address DAO
   */
  @Inject
  public AddressService(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  @Override
  public Response create(Request request) {
    assert request instanceof gov.ca.cwds.rest.api.domain.cms.Address;

    gov.ca.cwds.rest.api.domain.cms.Address address =
        (gov.ca.cwds.rest.api.domain.cms.Address) request;

    try {
      // TODO : refactor to actually determine who is updating. 'q1p' for now - see user story
      // #136737071 - Tech Debt: Legacy Service classes must use Staff ID for last update ID value
      Address managed = new Address(CmsKeyIdGenerator.cmsIdGenertor(null), address, "q1p");

      managed = addressDao.create(managed);
      if (managed.getId() == null) {
        throw new ServiceException("Address ID cannot be null");
      }

      return new PostedAddress(managed, true);

    } catch (EntityExistsException e) {
      LOGGER.info("Address already exists : ()", address);
      throw new ServiceException(e);
    }

  }

  @Override
  public Response delete(Serializable primaryKey) {
    assert primaryKey instanceof String;
    gov.ca.cwds.data.persistence.cms.Address persistedAddress = addressDao.delete(primaryKey);
    if (persistedAddress != null) {
      return new gov.ca.cwds.rest.api.domain.cms.Address(persistedAddress, true);
    }
    return null;
  }

  @Override
  public Response find(Serializable primaryKey) {
    assert primaryKey instanceof String;

    gov.ca.cwds.data.persistence.cms.Address persistedAddress = addressDao.find(primaryKey);
    if (persistedAddress != null) {
      return new gov.ca.cwds.rest.api.domain.cms.Address(persistedAddress, true);
    }
    return null;
  }

  @Override
  public Response update(Serializable primaryKey, Request request) {
    assert primaryKey instanceof String;
    assert request instanceof gov.ca.cwds.rest.api.domain.cms.Address;
    gov.ca.cwds.rest.api.domain.cms.Address address =
        (gov.ca.cwds.rest.api.domain.cms.Address) request;

    try {
      Address managed = new Address((String) primaryKey, address, "q1p");
      managed = addressDao.update(managed);
      return new gov.ca.cwds.rest.api.domain.cms.Address(managed, true);
    } catch (EntityNotFoundException e) {
      LOGGER.info("Address not found : {}", address);
      throw new ServiceException(e);
    }
  }
}
