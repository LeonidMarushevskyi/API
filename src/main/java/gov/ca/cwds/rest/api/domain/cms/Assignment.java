package gov.ca.cwds.rest.api.domain.cms;

import static gov.ca.cwds.data.persistence.cms.CmsPersistentObject.CMS_ID_LEN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import gov.ca.cwds.data.SystemCodeSerializer;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.DomainObject;
import gov.ca.cwds.rest.api.domain.ReportingDomain;
import gov.ca.cwds.rest.validation.Date;
import io.dropwizard.validation.OneOf;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@link DomainObject} representing an Assignment
 * 
 * @author CWDS API Team
 */
public class Assignment extends ReportingDomain implements Request, Response {
  /**
   * Serialization version
   */
  private static final long serialVersionUID = 1L;
  private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private final static DateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm:ss");

  @NotEmpty
  @Size(min = 1, max = 2)
  @ApiModelProperty(required = true, readOnly = false,
      value = "code indicating which county has primary assignment for the Referral or Case",
      example = "99")
  private String countySpecificCode;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
  @JsonProperty(value = "endDate")
  @gov.ca.cwds.rest.validation.Date(format = DATE_FORMAT, required = false)
  @ApiModelProperty(required = false, readOnly = false, value = "Assignment end date",
      example = "2017-06-18")
  private String endDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
  @JsonProperty(value = "endTime")
  @gov.ca.cwds.rest.validation.Date(format = TIME_FORMAT, required = true)
  @ApiModelProperty(required = false, readOnly = false, value = "Assignment end time",
      example = "16:41:49")
  private String endTime;

  @NotEmpty
  @Size(max = 1, message = "size must be 1 character")
  @OneOf(value = {"R", "C"}, ignoreCase = true, ignoreWhitespace = true)
  @ApiModelProperty(required = true, readOnly = false, value = "C = Case or R = Referral",
      example = "R")
  private String establishedForCode;

  @NotEmpty
  @Size(min = CMS_ID_LEN, max = CMS_ID_LEN)
  @ApiModelProperty(required = true, readOnly = false, value = "Referral Id or Case Id",
      example = "ABC1234567")
  private String establishedForId;

  @Size(max = CMS_ID_LEN)
  @ApiModelProperty(required = false, readOnly = false, value = "is part of Case Load",
      example = "ABC1234567")
  private String caseLoadId;

  @Size(max = CMS_ID_LEN)
  @ApiModelProperty(required = false, readOnly = false, value = "out of state contact party",
      example = "ABC1234567")
  private String outOfStateContactId;

  @NotNull
  @Size(max = 160)
  @ApiModelProperty(required = false, readOnly = false, value = "responsibility description",
      example = "investigate")
  private String responsibilityDescription;

  @SystemCodeSerializer(logical = true, description = true)
  @ApiModelProperty(required = false, readOnly = false, example = "1234")
  private Short secondaryAssignmentRoleType;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
  @JsonProperty(value = "startDate")
  @gov.ca.cwds.rest.validation.Date(format = DATE_FORMAT, required = false)
  @ApiModelProperty(required = true, readOnly = false, value = "Assignment start date",
      example = "2017-06-18")
  private String startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
  @JsonProperty(value = "startTime")
  @gov.ca.cwds.rest.validation.Date(format = TIME_FORMAT, required = true)
  @ApiModelProperty(required = true, readOnly = false, value = "Assignment start time",
      example = "16:41:49")
  private String startTime;

  @NotEmpty
  @Size(max = 1, message = "size must be 1 character")
  @OneOf(value = {"P", "S", "R"}, ignoreCase = true, ignoreWhitespace = true)
  @ApiModelProperty(required = true, readOnly = false,
      value = "P = Primary, S = Secondary, R = Read Only", example = "P")
  private String typeOfAssignmentCode;

  @Size(max = 5)
  @ApiModelProperty(required = true, readOnly = false)
  private String weightingNumber;

  @SuppressWarnings("javadoc")
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  /**
   * @return - county code
   */
  public String getCountySpecificCode() {
    return countySpecificCode;
  }

  /**
   * @return - end date
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * @return - end time
   */
  public String getEndTime() {
    return endTime;
  }

  /**
   * @return - R = referral C = case
   */
  public String getEstablishedForCode() {
    return establishedForCode;
  }

  /**
   * @return - referral id or case id
   */
  public String getEstablishedForId() {
    return establishedForId;
  }

  /**
   * @return - case load id
   */
  public String getCaseLoadId() {
    return caseLoadId;
  }

  /**
   * @return - out of state contact party id
   */
  public String getOutOfStateContactId() {
    return outOfStateContactId;
  }

  /**
   * @return - description
   */
  public String getResponsibilityDescription() {
    return responsibilityDescription;
  }

  /**
   * @return - secondary assignment role code
   */
  public Short getSecondaryAssignmentRoleType() {
    return secondaryAssignmentRoleType;
  }

  /**
   * @return - assignment start date
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * @return - assignment start time
   */
  public String getStartTime() {
    return startTime;
  }

  /**
   * @return - primary, secondary, or read only
   */
  public String getTypeOfAssignmentCode() {
    return typeOfAssignmentCode;
  }

  /**
   * @return - weight within case load
   */
  public String getWeightingNumber() {
    return weightingNumber;
  }

  /**
   * 
   */
  public Assignment() {
    super();
  }

  /**
   * @param countySpecificCode - code indicating the county with the primary assignment
   * @param endDate - end date of assignment
   * @param endTime - end time of assignment
   * @param establishedForCode - R = referral, C = case
   * @param establishedForId - Id of referral or case
   * @param caseLoadId - is part of this case load
   * @param outOfStateContactId - assigned to out of state contact id
   * @param responsibilityDescription - description
   * @param secondaryAssignmentRoleType - system code for secondary assignment description
   * @param startDate - start date of assignment
   * @param startTime - start time of assignment
   * @param typeOfAssignmentCode - P = primary
   * @param weightingNumber - weight within case load
   */
  public Assignment(String countySpecificCode,
      @Date(format = "yyyy-MM-dd", required = false) String endDate,
      @Date(format = "HH:mm:ss", required = true) String endTime,
      @OneOf(value = {"R", "C"}, ignoreCase = true,
          ignoreWhitespace = true) String establishedForCode,
      String establishedForId, String caseLoadId, String outOfStateContactId,
      String responsibilityDescription, Short secondaryAssignmentRoleType,
      @Date(format = "yyyy-MM-dd", required = false) String startDate,
      @Date(format = "HH:mm:ss", required = true) String startTime, @OneOf(value = {"P", "S", "R"},
          ignoreCase = true, ignoreWhitespace = true) String typeOfAssignmentCode,
      String weightingNumber) {
    super();
    this.countySpecificCode = countySpecificCode;
    this.endDate = endDate;
    this.endTime = endTime;
    this.establishedForCode = establishedForCode;
    this.establishedForId = establishedForId;
    this.caseLoadId = caseLoadId;
    this.outOfStateContactId = outOfStateContactId;
    this.responsibilityDescription = responsibilityDescription;
    this.secondaryAssignmentRoleType = secondaryAssignmentRoleType;
    this.startDate = startDate;
    this.startTime = startTime;
    this.typeOfAssignmentCode = typeOfAssignmentCode;
    this.weightingNumber = weightingNumber;
  }

  /**
   * @param pa - persistence cms Assignment
   */
  public Assignment(gov.ca.cwds.data.persistence.cms.Assignment pa) {
    this.countySpecificCode = pa.getCountySpecificCode();
    this.endDate = DomainChef.cookDate(pa.getEndDate());
    this.endTime = DomainChef.cookTime(pa.getEndTime());
    this.establishedForCode = pa.getEstablishedForCode();
    this.establishedForId = pa.getEstablishedForId();
    this.caseLoadId = pa.getFkCaseLoad();
    this.outOfStateContactId = pa.getFkOutOfStateContactParty();
    this.responsibilityDescription = pa.getResponsibilityDescription();
    this.secondaryAssignmentRoleType = pa.getSecondaryAssignmentRoleType();
    this.startDate = DomainChef.cookDate(pa.getStartDate());
    this.startTime = DomainChef.cookTime(pa.getStartTime());
    this.typeOfAssignmentCode = pa.getTypeOfAssignmentCode();
    this.weightingNumber = pa.getWeightingNumber();
  }

  /**
   * @param countyCode - county code for the assignment
   * @param referralId - referral Id
   * @param caseLoadId - CaseLoad Id
   * @return Assignment
   */
  public static Assignment createDefaultReferralAssignment(String countyCode, String referralId,
      String caseLoadId) {

    final String endDate = "";
    final String endTime = "";
    final String establishedForCode = "R";
    final String outOfStateContactId = null;
    final String responsibilityDescription = "";
    final Short secondaryAssignmentRoleType = 0;
    final String typeOfAssignmentCode = "P";
    final String weightingNumber = "0";

    final java.util.Date date = new java.util.Date();
    final String startDate = df.format(date);
    final String startTime = timeOnlyFormat.format(date);

    return new Assignment(countyCode, endDate, endTime, establishedForCode, referralId, caseLoadId,
        outOfStateContactId, responsibilityDescription, secondaryAssignmentRoleType, startDate,
        startTime, typeOfAssignmentCode, weightingNumber);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((caseLoadId == null) ? 0 : caseLoadId.hashCode());
    result = prime * result + ((countySpecificCode == null) ? 0 : countySpecificCode.hashCode());
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
    result = prime * result + ((establishedForCode == null) ? 0 : establishedForCode.hashCode());
    result = prime * result + ((establishedForId == null) ? 0 : establishedForId.hashCode());
    result = prime * result + ((outOfStateContactId == null) ? 0 : outOfStateContactId.hashCode());
    result = prime * result
        + ((responsibilityDescription == null) ? 0 : responsibilityDescription.hashCode());
    result = prime * result
        + ((secondaryAssignmentRoleType == null) ? 0 : secondaryAssignmentRoleType.hashCode());
    result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
    result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
    result =
        prime * result + ((typeOfAssignmentCode == null) ? 0 : typeOfAssignmentCode.hashCode());
    result = prime * result + ((weightingNumber == null) ? 0 : weightingNumber.hashCode());
    return result;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Assignment)) {
      return false;
    }
    Assignment other = (Assignment) obj;
    if (caseLoadId == null) {
      if (other.caseLoadId != null)
        return false;
    } else if (!caseLoadId.equals(other.caseLoadId))
      return false;
    if (countySpecificCode == null) {
      if (other.countySpecificCode != null)
        return false;
    } else if (!countySpecificCode.equals(other.countySpecificCode))
      return false;
    if (endDate == null) {
      if (other.endDate != null)
        return false;
    } else if (!endDate.equals(other.endDate))
      return false;
    if (endTime == null) {
      if (other.endTime != null)
        return false;
    } else if (!endTime.equals(other.endTime))
      return false;
    if (establishedForCode == null) {
      if (other.establishedForCode != null)
        return false;
    } else if (!establishedForCode.equals(other.establishedForCode))
      return false;
    if (establishedForId == null) {
      if (other.establishedForId != null)
        return false;
    } else if (!establishedForId.equals(other.establishedForId))
      return false;
    if (outOfStateContactId == null) {
      if (other.outOfStateContactId != null)
        return false;
    } else if (!outOfStateContactId.equals(other.outOfStateContactId))
      return false;
    if (responsibilityDescription == null) {
      if (other.responsibilityDescription != null)
        return false;
    } else if (!responsibilityDescription.equals(other.responsibilityDescription))
      return false;
    if (secondaryAssignmentRoleType == null) {
      if (other.secondaryAssignmentRoleType != null)
        return false;
    } else if (!secondaryAssignmentRoleType.equals(other.secondaryAssignmentRoleType))
      return false;
    if (startDate == null) {
      if (other.startDate != null)
        return false;
    } else if (!startDate.equals(other.startDate))
      return false;
    if (startTime == null) {
      if (other.startTime != null)
        return false;
    } else if (!startTime.equals(other.startTime))
      return false;
    if (typeOfAssignmentCode == null) {
      if (other.typeOfAssignmentCode != null)
        return false;
    } else if (!typeOfAssignmentCode.equals(other.typeOfAssignmentCode))
      return false;
    if (weightingNumber == null) {
      if (other.weightingNumber != null)
        return false;
    } else if (!weightingNumber.equals(other.weightingNumber))
      return false;
    return true;
  }

}

