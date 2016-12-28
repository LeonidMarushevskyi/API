package gov.ca.cwds.rest.services.cms;

import java.io.Serializable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.domain.cms.PostedReporter;
import gov.ca.cwds.rest.api.persistence.cms.Reporter;
import gov.ca.cwds.rest.jdbi.Dao;
import gov.ca.cwds.rest.jdbi.cms.ReporterDao;
import gov.ca.cwds.rest.services.CrudsService;
import gov.ca.cwds.rest.services.ServiceException;

/**
 * Business layer object to work on {@link Reporter}
 * 
 * @author CWDS API Team
 */
public class ReporterService implements CrudsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReporterService.class);

  private ReporterDao reporterDao;

  /**
   * Constructor
   * 
   * @param reporterDao The {@link Dao} handling
   *        {@link gov.ca.cwds.rest.api.persistence.cms.Reporter} objects.
   */
  @Inject
  public ReporterService(ReporterDao reporterDao) {
    this.reporterDao = reporterDao;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.Reporter find(Serializable primaryKey) {
    assert primaryKey instanceof String;

    gov.ca.cwds.rest.api.persistence.cms.Reporter persistedReporter = reporterDao.find(primaryKey);
    if (persistedReporter != null) {
      return new gov.ca.cwds.rest.api.domain.cms.Reporter(persistedReporter);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.Reporter delete(Serializable primaryKey) {
    assert primaryKey instanceof String;
    gov.ca.cwds.rest.api.persistence.cms.Reporter persistedReporter =
        reporterDao.delete(primaryKey);
    if (persistedReporter != null) {
      return new gov.ca.cwds.rest.api.domain.cms.Reporter(persistedReporter);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.Request)
   */
  @Override
  public PostedReporter create(Request request) {
    assert request instanceof gov.ca.cwds.rest.api.domain.cms.Reporter;

    gov.ca.cwds.rest.api.domain.cms.Reporter reporter =
        (gov.ca.cwds.rest.api.domain.cms.Reporter) request;

    try {
      // TODO : refactor to actually determine who is updating. 'q1p' for now - #136737071 - Tech
      // Debt: Legacy Service classes must use Staff ID for last update ID value

      Reporter managed = new Reporter(reporter, "q1p");
      managed = reporterDao.create(managed);
      return new PostedReporter(managed);
    } catch (EntityExistsException e) {
      LOGGER.info("Reporter already exists : {}", reporter);
      throw new ServiceException(e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#update(java.io.Serializable,
   *      gov.ca.cwds.rest.api.Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.Reporter update(Serializable primaryKey, Request request) {
    assert primaryKey instanceof String;
    assert request instanceof gov.ca.cwds.rest.api.domain.cms.Reporter;
    gov.ca.cwds.rest.api.domain.cms.Reporter reporter =
        (gov.ca.cwds.rest.api.domain.cms.Reporter) request;

    try {
      Reporter managed = new Reporter(reporter, "q1p");
      managed = reporterDao.update(managed);
      return new gov.ca.cwds.rest.api.domain.cms.Reporter(managed);
    } catch (EntityNotFoundException e) {
      LOGGER.info("Reporter not found : {}", reporter);
      throw new ServiceException(e);
    }
  }

}
