package gov.ca.cwds.rest.services.cms;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.ca.cwds.auth.realms.PerryUserIdentity;

/**
 * @author CWDS API Team
 *
 */
public class StaffPersonIdRetriever {

  private static final Logger LOGGER = LoggerFactory.getLogger(StaffPersonIdRetriever.class);

  private static final String DEFAULT_STAFF_ID = "0X5";
  private static final String DEFAULT_USER_ID = "CWDST";

  StaffPersonIdRetriever() {}

  /**
   * Retrieves the Staff Person Id of the current user. Defaults to a hard-coded value if
   * authorization information is not passed in or if StaffId is not included as part of the
   * security token
   * 
   * @return the last updated id for persistence, this is the Staff Person Id of the current user
   */
  public String getStaffPersonId() {

    return getPerryUserIdentity().getStaffId();
  }

  /**
   * @return the perry user
   */
  public static PerryUserIdentity getPerryUserIdentity() {
    PerryUserIdentity userIdentity = null;

    Subject currentUser = SecurityUtils.getSubject();
    PrincipalCollection principalCollection = currentUser.getPrincipals();

    LOGGER.warn("====================== PrincipalCollection=" + principalCollection);

    if (principalCollection != null) {
      @SuppressWarnings("rawtypes")
      List principals = currentUser.getPrincipals().asList();
      int principalCount = principals.size();
      Object currentPrincipal = principalCount > 1 ? principals.get(1) : null;


      LOGGER.warn("====================== principals=" + principals);
      LOGGER.warn("====================== principalCount=" + principalCount);
      LOGGER.warn("====================== currentPrincipal=" + currentPrincipal);

      if (currentPrincipal != null && currentPrincipal instanceof PerryUserIdentity) {
        PerryUserIdentity currentUserInfo = (PerryUserIdentity) currentPrincipal;
        String staffPersonId = currentUserInfo.getStaffId();
        LOGGER.warn("====================== staffPersonId=" + staffPersonId);

        if (!StringUtils.isBlank(staffPersonId)) {
          userIdentity = currentUserInfo;
        }
      }
    }

    if (userIdentity == null) {
      LOGGER.warn(
          "====================== PerryUserIdentity not found, using default ====================== ");
      String localDEvprop = "true"; // System.getenv("LOCAL_DEV");
      if (StringUtils.isNotBlank(localDEvprop) && "true".equals(localDEvprop)) {
        userIdentity = new PerryUserIdentity();
        userIdentity.setStaffId(DEFAULT_STAFF_ID);
        userIdentity.setUser(DEFAULT_USER_ID);
      }
    }
    LOGGER.info("Staff Person Id = " + userIdentity.getStaffId());
    return userIdentity;
  }

}
