package gov.ca.cwds.rest.api.domain;

import static gov.ca.cwds.data.persistence.cms.CmsPersistentObject.CMS_ID_LEN;
import static gov.ca.cwds.rest.util.FerbDateUtils.freshDate;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.validation.AtRiskAllegation;
import gov.ca.cwds.rest.validation.Date;
import gov.ca.cwds.rest.validation.ValidLogicalId;
import gov.ca.cwds.rest.validation.ValidParticipantRoles;
import gov.ca.cwds.rest.validation.ValidSystemCodeId;
import gov.ca.cwds.rest.validation.VictimAgeRestriction;
import io.dropwizard.jackson.JsonSnakeCase;
import io.dropwizard.validation.OneOf;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author CWDS API Team
 */
@JsonSnakeCase
@JsonPropertyOrder({"id", "legacySourceTable", "referralId", "endedAt", "incidentCounty",
    "incidentDate", "locationType", "communicationMethod", "currentLocationOfChildren", "email",
    "name", "reportNarrative", "reference", "responseTime", "startedAt", "assignee",
    "assigneeStaffId", "additionalInformation", "screeningDecision", "screeningDecisionDetail",
    "address", "participants", "crossReports", "allegations"})
@VictimAgeRestriction
public class ScreeningToReferral extends ReportingDomain implements Request {

  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  @Min(1)
  @NotNull
  @ApiModelProperty(required = true, readOnly = false, value = "Screening ID", example = "12345")
  private long id;

  @JsonProperty("legacy_source_table")
  @ApiModelProperty(required = false, readOnly = false, value = "Legacy Table",
      example = "REFERL_T")
  private String legacySourceTable;

  @JsonProperty("legacy_id")
  @ApiModelProperty(required = true, readOnly = false, value = "Legacy Referral Id",
      example = "ABC1234567")
  @Size(max = CMS_ID_LEN)
  private String referralId;

  @JsonProperty("ended_at")
  @ApiModelProperty(required = false, readOnly = false, value = "Screening end date/time",
      example = "2016-08-03T01:00:00.000Z")
  private String endedAt;

  @JsonProperty("incident_county")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false,
      value = "County with primary assignment responsibility for Referral", example = "34")
  @ValidLogicalId(required = true, category = SystemCodeCategoryId.COUNTY_CODE)
  private String incidentCounty;

  @Date
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
  @JsonProperty("incident_date")
  @ApiModelProperty(required = false, readOnly = false, value = "Incident date",
      example = "2015-01-13")
  private String incidentDate;

  @JsonProperty("location_type")
  @ApiModelProperty(required = true, readOnly = false, value = "Location Type",
      example = "foster home")
  @Size(max = 75)
  private String locationType;

  @JsonProperty("communication_method")
  @NotNull
  @ApiModelProperty(required = true, readOnly = false, value = "Communication Method",
      allowableValues = "$ID:CMM_MTHC", example = "409")
  @ValidSystemCodeId(required = true, category = SystemCodeCategoryId.COMMUNICATION_METHOD)
  private Short communicationMethod;

  @JsonProperty("current_location_of_children")
  @ApiModelProperty(required = false, readOnly = false, value = "location of the child",
      example = "At the school")
  @Size(max = 254)
  private String currentLocationOfChildren;

  @JsonProperty("name")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false, value = "Title/Name of referral",
      example = "a referral name")
  @Size(max = 35)
  private String name;

  @JsonProperty("report_narrative")
  @ApiModelProperty(required = false, readOnly = false, value = "Report Narrative",
      example = "On the evening of...")
  @Size(max = 254)
  private String reportNarrative;

  @JsonProperty("reference")
  @ApiModelProperty(required = false, readOnly = false, value = "Referrence",
      example = "reference WXTSKD")
  @Size(max = 50)
  private String reference;

  @JsonProperty("response_time")
  @NotNull
  @ApiModelProperty(required = true, readOnly = false, value = "Referral Response Type",
      example = "1520")
  @ValidSystemCodeId(required = true, category = SystemCodeCategoryId.REFERRAL_RESPONSE)
  private Short responseTime;

  @JsonProperty("started_at")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false, value = "Date/time incident started",
      example = "2016-08-03T01:00:00.000Z")
  private String startedAt;

  @JsonProperty("assignee")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false, value = "Assigned social worker",
      example = "Mich Bastow")
  @Size(max = 50)
  private String assignee;

  @JsonProperty("assignee_staff_id")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false, value = "q1p", example = "q1p")
  @Size(min = 3, max = 3)
  private String assigneeStaffId;

  @JsonProperty("additional_information")
  @ApiModelProperty(required = false, readOnly = false, value = "Additional Information",
      example = "more information about this referral")
  @Size(max = 50)
  private String additionalInformation;

  @JsonProperty("screening_decision")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false, value = "Screening Decision",
      example = "Response time")
  @Size(max = 50)
  private String screeningDecision;

  @JsonProperty("screening_decision_detail")
  @ApiModelProperty(required = false, readOnly = false, value = "Screening decision detail",
      example = "decision detail")
  @Size(max = 1500)
  private String screeningDecisionDetail;

  @JsonProperty("approval_status")
  @ValidSystemCodeId(required = false, category = SystemCodeCategoryId.APPROVAL_STATUS_TYPE)
  @ApiModelProperty(required = false, readOnly = false, value = "Status of the approval",
      example = "118")
  private int approvalStatus;

  @JsonProperty("family_awareness")
  @ApiModelProperty(required = false, readOnly = false, value = "Family is aware of referral",
      example = "true")
  private boolean familyAwareness;

  @JsonProperty("filed_with_law_enforcement")
  @ApiModelProperty(required = false, readOnly = false,
      value = "Filed Cross Report with Law Enforcement", example = "N")
  private boolean filedWithLawEnforcement;

  @JsonProperty("responsible_agency")
  @ApiModelProperty(required = false, readOnly = false, value = "Responsible Agency Code",
      example = "C", allowableValues = "C, P, O, A, S, I, K, M")
  @OneOf(value = {"C", "P", "O", "A", "S", "I", "K", "M"})
  @Size(max = 1)
  private String responsibleAgency;

  @JsonProperty("limited_access_code")
  @Size(max = 1)
  @OneOf(value = {"R", "S", "N"})
  @ApiModelProperty(required = true, readOnly = false, value = "limited access code", example = "N")
  private String limitedAccessCode;

  @JsonProperty("limited_access_description")
  @Size(max = 254)
  @ApiModelProperty(required = false, readOnly = false, value = "limited access description",
      example = "Some text describing the limited access")
  private String limitedAccessDescription;

  @JsonProperty("limited_access_agency")
  @ApiModelProperty(required = false, readOnly = false, value = "40", example = "The user county")
  @ValidLogicalId(required = false, category = SystemCodeCategoryId.COUNTY_CODE, ignoreable = true,
      ignoredValue = "")
  private String limitedAccessAgency;

  @JsonProperty("limited_access_date")
  @ApiModelProperty(required = false, readOnly = false, value = "yyyy-MM-dd",
      example = "2000-01-01")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private java.util.Date limitedAccessDate;

  @NotNull
  @ApiModelProperty(required = true, readOnly = false)
  @Valid
  private Address address;

  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false)
  @Valid
  @ValidParticipantRoles
  private Set<Participant> participants;

  @JsonProperty("cross_reports")
  @ApiModelProperty(required = false, readOnly = false)
  @Valid
  private Set<CrossReport> crossReports;

  @JsonProperty("allegations")
  @NotEmpty
  @ApiModelProperty(required = true, readOnly = false)
  @Valid
  @AtRiskAllegation
  private Set<Allegation> allegations;

  @SuppressWarnings("javadoc")
  public ScreeningToReferral() {
    super();
  }

  /**
   * @param id - the screening Id
   * @param legacySourceTable - legacy source table
   * @param referralId - the referral Id
   * @param endedAt - screening ended at date
   * @param incidentCounty - county of incident
   * @param incidentDate - date of incident
   * @param locationType - location type
   * @param communicationMethod - communication method
   * @param currentLocationOfChildren - currentLocationOfChildren
   * @param name - screening name
   * @param reportNarrative - narrative
   * @param reference - reference
   * @param responseTime - response time
   * @param startedAt - response started at time
   * @param assignee - staff person assigned
   * @param assigneeStaffId - assignee Id
   * @param additionalInformation - additional information
   * @param screeningDecision - screening decision
   * @param screeningDecisionDetail - screening decision detail
   * @param approvalStatus - approvalStatus
   * @param familyAwareness - familyAwareness
   * @param filedWithLawEnforcement - filedWithLawEnforcement
   * @param responsibleAgency - responsibleAgency
   * @param limitedAccessCode - limitedAccessCode
   * @param limitedAccessDescription - limitedAccessDescription
   * @param limitedAccessAgency - limitedAccessAgency
   * @param limitedAccessDate - limitedAccessDate
   * @param address - address associated with participants
   * @param participants - participants associated with this screening
   * @param crossReports - Cross Report
   * @param allegations - Allegations
   */
  public ScreeningToReferral(long id, String legacySourceTable, String referralId,
      @Date String endedAt, String incidentCounty, @Date String incidentDate, String locationType,
      Short communicationMethod, String currentLocationOfChildren, String name,
      String reportNarrative, String reference, Short responseTime, @Date String startedAt,
      String assignee, String assigneeStaffId, String additionalInformation,
      String screeningDecision, String screeningDecisionDetail, int approvalStatus,
      boolean familyAwareness, boolean filedWithLawEnforcement, String responsibleAgency,
      String limitedAccessCode, String limitedAccessDescription, String limitedAccessAgency,
      java.util.Date limitedAccessDate, Address address, Set<Participant> participants,
      Set<CrossReport> crossReports, Set<Allegation> allegations) {
    super();
    this.id = id;
    this.referralId = referralId;
    this.legacySourceTable = legacySourceTable;
    this.endedAt = endedAt;
    this.incidentCounty = incidentCounty;
    this.incidentDate = incidentDate;
    this.locationType = locationType;
    this.communicationMethod = communicationMethod;
    this.currentLocationOfChildren = currentLocationOfChildren;
    this.name = name;
    this.reportNarrative = reportNarrative;
    this.reference = reference;
    this.responseTime = responseTime;
    this.startedAt = startedAt;
    this.assignee = assignee;
    this.assigneeStaffId = assigneeStaffId;
    this.additionalInformation = additionalInformation;
    this.screeningDecision = screeningDecision;
    this.screeningDecisionDetail = screeningDecisionDetail;
    this.approvalStatus = approvalStatus;
    this.familyAwareness = familyAwareness;
    this.filedWithLawEnforcement = filedWithLawEnforcement;
    this.responsibleAgency = responsibleAgency;
    this.limitedAccessCode = limitedAccessCode;
    this.limitedAccessDescription = limitedAccessDescription;
    this.limitedAccessAgency = limitedAccessAgency;
    this.limitedAccessDate = freshDate(limitedAccessDate);
    this.address = address;
    this.participants = participants;
    this.crossReports = crossReports;
    this.allegations = allegations;
  }

  /**
   * @return id
   */
  public long getId() {
    return id;
  }

  /**
   * @return legacySourceTable
   */
  public String getLegacySourceTable() {
    return legacySourceTable;
  }

  /**
   * @return referralId
   */
  public String getReferralId() {
    return referralId;
  }

  /**
   * @return ended date
   */
  public String getEndedAt() {
    return endedAt;
  }

  /**
   * @return incident county
   */
  public String getIncidentCounty() {
    return incidentCounty;
  }

  /**
   * @return incident Date
   */
  public String getIncidentDate() {
    return incidentDate;
  }

  /**
   * @return location type
   */
  public String getLocationType() {
    return locationType;
  }

  /**
   * @return communication method
   */
  public Short getCommunicationMethod() {
    return communicationMethod;
  }

  /**
   * @return the currentLocationOfChildren
   */
  public String getCurrentLocationOfChildren() {
    return currentLocationOfChildren;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @return report narrative
   */
  public String getReportNarrative() {
    return reportNarrative;
  }

  /**
   * @return reference
   */
  public String getReference() {
    return reference;
  }

  /**
   * @return response time
   */
  public Short getResponseTime() {
    return responseTime;
  }

  /**
   * @return = started at
   */
  public String getStartedAt() {
    return startedAt;
  }

  /**
   * @return assignee
   */
  public String getAssignee() {
    return assignee;
  }

  /**
   * @return assigneeStaffId
   */
  public String getAssigneeStaffId() {
    return assigneeStaffId;
  }

  /**
   * @return addtional infoma
   */
  public String getAdditionalInformation() {
    return additionalInformation;
  }

  /**
   * @return - screening decio
   */
  public String getScreeningDecision() {
    return screeningDecision;
  }

  /**
   * @return screening decision detail
   */
  public String getScreeningDecisionDetail() {
    return screeningDecisionDetail;
  }

  /**
   * @return approval status
   */
  public int getApprovalStatus() {
    return approvalStatus;
  }

  /**
   * @return family awareness
   */
  public boolean isFamilyAwareness() {
    return familyAwareness;
  }

  /**
   * @return crossreport filed with Law Enforecement
   */
  public boolean isFiledWithLawEnforcement() {
    return filedWithLawEnforcement;
  }

  /**
   * @return responsible agency
   */
  public String getResponsibleAgency() {
    return responsibleAgency;
  }

  /**
   * @return limitedAccessCode the limited access code
   */
  public String getLimitedAccessCode() {
    return limitedAccessCode;
  }

  @JsonIgnore
  public boolean isAccessLimited() {
    return limitedAccessCode == "S" || limitedAccessCode == "R";
  }

  /**
   * @return limitedAccessDescription limited Access Description
   */
  public String getLimitedAccessDescription() {
    return limitedAccessDescription;
  }

  /**
   * @return limitedAccessAgency limited Access Agency
   */
  public String getLimitedAccessAgency() {
    return limitedAccessAgency;
  }

  /**
   * @return limitedAccessDate limited Access Date
   */
  public java.util.Date getLimitedAccessDate() {
    return freshDate(limitedAccessDate);
  }

  @SuppressWarnings("javadoc")
  public Address getAddress() {
    return address;
  }

  /**
   * @param address - domain address
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  @SuppressWarnings("javadoc")
  public Set<Participant> getParticipants() {
    return participants;
  }

  @SuppressWarnings("javadoc")
  public Set<Allegation> getAllegations() {
    return allegations;
  }

  @SuppressWarnings("javadoc")
  public Set<CrossReport> getCrossReports() {
    return crossReports;
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}
