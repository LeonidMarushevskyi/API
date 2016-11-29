package gov.ca.cwds.rest.jdbi.cms;

import org.hibernate.SessionFactory;

import gov.ca.cwds.rest.api.persistence.cms.CrossReport;
import gov.ca.cwds.rest.jdbi.CrudsDaoImpl;

public class CrossReportDao extends CrudsDaoImpl<CrossReport> {

  /**
   * Constructor
   * 
   * @param sessionFactory The sessionFactory
   */
  public CrossReportDao(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}

