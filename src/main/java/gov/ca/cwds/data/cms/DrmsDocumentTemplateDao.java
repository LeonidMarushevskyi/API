package gov.ca.cwds.data.cms;

import com.google.inject.Inject;
import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.DrmsDocumentTemplate;
import gov.ca.cwds.inject.CmsSessionFactory;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * DAO for {@link DrmsDocumentTemplateDao}.
 *
 * @author Intake Team 4
 */
public class DrmsDocumentTemplateDao  extends CrudsDaoImpl<DrmsDocumentTemplate> {

  public static final Short APPLICATION_CONTEXT_REFERRAL_DOCUMENTS = 82;
  public static final Short GOVERMENT_ENTITY_SYSTEM = 0;
  public static final Short LANGUAGE_ENGLISH = 1253;
  public static final String SCREENERNARRATIVE = "Screener Narrative";

  /**
   * Constructor
   *
   * @param sessionFactory The session factory
   */
  @Inject
  public DrmsDocumentTemplateDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * finding templates based on application contex id and goverment entity type
   *
   * @param applicationContextType - application context type id
   * @param govermentEntityType - govermant entity type id.
   * @return - list of DrmsDocumentTemplates
   */
  @SuppressWarnings("unchecked")
  public DrmsDocumentTemplate[] findByApplicationContextAndGovermentEntity(
      Short applicationContextType, Short govermentEntityType) {

    final Query<DrmsDocumentTemplate> query = this.getSessionFactory().getCurrentSession()
            .getNamedQuery(DrmsDocumentTemplate.NQ_TEMPLATES_BY_APPLICATION_CONTEXT_AND_GOVERMANT_ENTITY);

    query.setParameter("applicationContextType", applicationContextType)
         .setParameter("govermentEntityType", govermentEntityType);
    return query.list().toArray(new DrmsDocumentTemplate[0]);
  }

}
