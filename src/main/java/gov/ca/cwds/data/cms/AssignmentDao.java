package gov.ca.cwds.data.cms;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.Assignment;
import gov.ca.cwds.data.persistence.cms.CaseLoad;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link Assignment}.
 * 
 * @author CWDS API Team
 */
public class AssignmentDao extends CrudsDaoImpl<Assignment> {

  /**
   * Constructor
   * 
   * @param sessionFactory The sessionFactory
   */
  @Inject
  public AssignmentDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * @param staffPersonId - staffPerson Id
   * @return the caseLoads
   */
  @SuppressWarnings("unchecked")
  public CaseLoad[] findCaseLoads(String staffPersonId) {
    Query<CaseLoad> query = this.getSessionFactory().getCurrentSession()
        .getNamedQuery("gov.ca.cwds.data.persistence.cms.Assignment.findCaseLoads")
        .setParameter("fkStaffPerson", staffPersonId);
    return query.list().toArray(new CaseLoad[0]);

  }

}
