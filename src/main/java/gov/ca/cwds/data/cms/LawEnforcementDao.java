package gov.ca.cwds.data.cms;

import org.hibernate.SessionFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.LawEnforcement;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link LawEnforcement}.
 * 
 * @author CWDS API Team
 */
public class LawEnforcementDao extends CrudsDaoImpl<LawEnforcement> {

  /**
   * Constructor
   * 
   * @param sessionFactory The sessionFactory
   */
  @Inject
  public LawEnforcementDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

}
