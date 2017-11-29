package gov.ca.cwds.rest.business.rules;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.cms.Assignment;
import gov.ca.cwds.rest.business.RuleValidatator;

public class R04530AssignmentEndDateValidator implements RuleValidatator {

  Assignment assignment = null;

  /**
   * Rule - R04530 - validating assignment end date
   */
  @Override
  public boolean isValid() {

    boolean validEndDate = false;
    // end date is optional value.. it can be null.
    if (isNullEndDate()) {
      return true;
    }

    Date assignemntStartDate =
        this.concatenateDateAndTime(assignment.getStartDate(), assignment.getStartTime());
    Date assignemntEndDate =
        this.concatenateDateAndTime(assignment.getEndDate(), assignment.getEndTime());


    if (this.isAssignmentStartDateBeforeOrEqualsEndDate(assignemntStartDate, assignemntEndDate)
        && (this.isCurrentDateBeforeOrEqualAssignmentEndDate(assignemntEndDate))) {
      validEndDate = true;
    }
    return validEndDate;

  }

  private boolean isCurrentDateBeforeOrEqualAssignmentEndDate(Date assignemntEndDate) {
    Date currentDate = new Date();
    return (currentDate.before(assignemntEndDate) || currentDate.equals(assignemntEndDate));

  }

  /**
   * validating start date should be before or equals start date
   * 
   * @param startDate - assignment start date
   * @param endDate - assignment end date
   * @return
   */
  private boolean isAssignmentStartDateBeforeOrEqualsEndDate(Date startDate, Date endDate) {
    return (startDate.before(endDate) || (startDate.equals(endDate)));

  }

  /**
   * method to combine date and time
   * 
   * @param date - date object
   * @param time - time object
   * @return - combines date and time
   */
  private Date concatenateDateAndTime(String date, String time) {
    return DomainChef.concatenateDateAndTime(unCookDateString(date), unCookTimeString(time));

  }

  /**
   * checking end date is null or not;
   * 
   * @return true if endDate is null
   */
  private boolean isNullEndDate() {
    return (StringUtils.isBlank(assignment.getEndDate())
        && StringUtils.isBlank(assignment.getEndTime()));

  }

  /**
   * constructor to set assignment object
   * 
   * @param assignment - assignment object
   */
  public R04530AssignmentEndDateValidator(Assignment assignment) {
    this.assignment = assignment;

  }

  /**
   * service to convert strong to date object.
   * 
   * @param date - date string object
   * @return converted date object.
   */
  private Date unCookDateString(String date) {
    return DomainChef.uncookDateString(date);
  }

  /**
   * service to convert strong to date object.
   * 
   * @param date - date string object
   * @return converted date object.
   */
  private Date unCookTimeString(String time) {
    return DomainChef.uncookTimeString(time);
  }



}