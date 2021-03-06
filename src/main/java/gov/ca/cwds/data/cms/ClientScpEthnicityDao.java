package gov.ca.cwds.data.cms;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.ClientScpEthnicity;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link ClientScpEthnicity}.
 * 
 * @author CWDS API Team
 */
public class ClientScpEthnicityDao extends CrudsDaoImpl<ClientScpEthnicity> {
  /**
   * Constructor
   * 
   * @param sessionFactory The sessionFactory
   */
  @Inject
  public ClientScpEthnicityDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * @param establishedId - establishedId
   * @return the list of race and hispanic codes
   */
  public List<ClientScpEthnicity> getClientScp(String establishedId) {
    Query query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery("gov.ca.cwds.data.persistence.cms.ClientScpEthnicity.createOrUpdate")
        .setParameter("establishedId", establishedId);
    return query.list();
  }

}
