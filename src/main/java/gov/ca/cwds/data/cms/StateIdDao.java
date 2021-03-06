package gov.ca.cwds.data.cms;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.StateId;
import gov.ca.cwds.inject.CmsSessionFactory;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * CWDS API Team
 */
public class StateIdDao extends CrudsDaoImpl<StateId> {

  /**
   * Constructor
   *
   * @param sessionFactory The session factory
   */
  @Inject
  public StateIdDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @SuppressWarnings("unchecked")
  public List<StateId> findAllByClientId(String clientId) {
    Query query = namedQuery(getEntityClass().getName() + ".findByClientId");
    query.setParameter("clientId", clientId);
    return list(query);
  }

}