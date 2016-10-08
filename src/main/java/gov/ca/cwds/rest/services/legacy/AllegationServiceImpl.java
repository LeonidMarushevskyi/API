package gov.ca.cwds.rest.services.legacy;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.ca.cwds.rest.api.persistence.legacy.Allegation;
import gov.ca.cwds.rest.services.CrudsService;

/**
 * Implementation of {@link AllegationService} backed by a DAO layer.
 * 
 * @author CDWS API Team
 */
@Deprecated
public class AllegationServiceImpl implements AllegationService {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AllegationServiceImpl.class);

	private CrudsService<gov.ca.cwds.rest.api.domain.legacy.Allegation, Allegation> crudsService;

	/**
	 * Constructor
	 * 
	 * @param crudsService
	 *            The {@link CrudsService} used by this service
	 */
	public AllegationServiceImpl(CrudsService<gov.ca.cwds.rest.api.domain.legacy.Allegation, Allegation> crudsService) {
		this.crudsService = crudsService;
	}

	/* (non-Javadoc)
	 * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
	 */
	@Override
	public gov.ca.cwds.rest.api.domain.legacy.Allegation find(Serializable primaryKey) {
		return (gov.ca.cwds.rest.api.domain.legacy.Allegation) crudsService.find(primaryKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
	 */
	@Override
	public gov.ca.cwds.rest.api.domain.legacy.Allegation delete(Serializable id) {
		return (gov.ca.cwds.rest.api.domain.legacy.Allegation) crudsService.delete(id);
	}

	/* (non-Javadoc)
	 * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.domain.DomainObject)
	 */
	@Override
	public Serializable create(gov.ca.cwds.rest.api.domain.legacy.Allegation object) {
		return crudsService.create(object);
	}

	/* (non-Javadoc)
	 * @see gov.ca.cwds.rest.services.CrudsService#update(gov.ca.cwds.rest.api.domain.DomainObject)
	 */
	@Override
	public String update(gov.ca.cwds.rest.api.domain.legacy.Allegation object) {
		return crudsService.update(object);
	}


}