package gov.ca.cwds.fixture;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;

/**
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public class ScreeningToReferralResourceBuilder {

  private static final DateFormat dateTimeFormat =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.US);

  private long id = 1L;
  private String referralId = "";
  private String legacySourceTable = "";
  private String endedAt = DomainChef.cookDate(new Date());
  private String incidentCounty = "34";
  private String incidentDate = DomainChef.cookDate(new Date());
  private String locationType = "Foster Home";
  private Short communicationMethod = 409;
  private String currentLocationOfChildren = "Location of the child";
  private String name = "The Rocky Horror Show";
  private String reportNarrative = "Narrative 123 test";
  private String reference = "123ABC";
  private Short responseTime = 1516;
  private String startedAt = dateTimeFormat.format(new Date());
  private String assignee = "Michael Bastow";
  private String assigneeStaffId = "0X5";
  private String additionalInformation = "additional information about the referral";
  private String screeningDecision = "Response time";
  private String screeningDecisionDetail = "Detail";
  private int approvalStatus = 118;
  private boolean familyAwareness = false;
  private boolean filedWithLawEnforcement = false;
  private String responsibleAgency = "C";
  private Short injuryHarmCategory = 2178;
  private String limitedAccessCode = "N";
  private String limitedAccessDescription = "";
  private String limitedAccessAgency = "23";
  private Date limitedAccessDate = null;
  private gov.ca.cwds.rest.api.domain.Address address;
  private Set<Participant> participants;
  private Set<gov.ca.cwds.rest.api.domain.CrossReport> crossReports;
  private Set<gov.ca.cwds.rest.api.domain.Allegation> allegations;

  public ScreeningToReferralResourceBuilder() {
    address = new AddressResourceBuilder().createAddress();
    Participant victim = new ParticipantResourceBuilder().setGender("M").createVictimParticipant();
    Participant perp = new ParticipantResourceBuilder().setGender("F").createPerpParticipant();
    Participant reporter =
        new ParticipantResourceBuilder().setGender("M").createReporterParticipant();
    this.participants = new HashSet<>(Arrays.asList(victim, perp, reporter));
    gov.ca.cwds.rest.api.domain.CrossReport crossReport =
        new CrossReportResourceBuilder().createCrossReport();
    this.crossReports = new HashSet<>(Arrays.asList(crossReport));
    gov.ca.cwds.rest.api.domain.Allegation allegation =
        new AllegationResourceBuilder().setInjuryHarmType(injuryHarmCategory).createAllegation();
    this.allegations = new HashSet<>(Arrays.asList(allegation));

  }

  public static DateFormat getDatetimeformat() {
    return dateTimeFormat;
  }

  public long getId() {
    return id;
  }

  public String getReferralId() {
    return referralId;
  }

  public String getLegacySourceTable() {
    return legacySourceTable;
  }

  public String getEndedAt() {
    return endedAt;
  }

  public String getIncidentCounty() {
    return incidentCounty;
  }

  public String getIncidentDate() {
    return incidentDate;
  }

  public String getLocationType() {
    return locationType;
  }

  public Short getCommunicationMethod() {
    return communicationMethod;
  }

  public String getCurrentLocationOfChildren() {
    return currentLocationOfChildren;
  }

  public String getAssigneeStaffId() {
    return assigneeStaffId;
  }

  public String getName() {
    return name;
  }

  public String getReportNarrative() {
    return reportNarrative;
  }

  public String getReference() {
    return reference;
  }

  public Short getResponseTime() {
    return responseTime;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public String getAssignee() {
    return assignee;
  }

  public String getassigneeStaffId() {
    return assigneeStaffId;
  }

  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public String getScreeningDecision() {
    return screeningDecision;
  }

  public String getScreeningDecisionDetail() {
    return screeningDecisionDetail;
  }

  public int getApprovalStatus() {
    return approvalStatus;
  }

  public boolean isFamilyAwareness() {
    return familyAwareness;
  }

  public boolean isFiledWithLawEnforcement() {
    return filedWithLawEnforcement;
  }

  public String getResponsibleAgency() {
    return responsibleAgency;
  }

  public Short getInjuryHarmCategory() {
    return injuryHarmCategory;
  }

  public gov.ca.cwds.rest.api.domain.Address getAddress() {
    return address;
  }

  public Set<Participant> getParticipants() {
    return participants;
  }

  public Set<gov.ca.cwds.rest.api.domain.CrossReport> getCrossReports() {
    return crossReports;
  }

  public Set<gov.ca.cwds.rest.api.domain.Allegation> getAllegations() {
    return allegations;
  }

  public ScreeningToReferralResourceBuilder setId(long id) {
    this.id = id;
    return this;
  }

  public ScreeningToReferralResourceBuilder setReferralId(String referralId) {
    this.referralId = referralId;
    return this;
  }

  public ScreeningToReferralResourceBuilder setLegacySourceTable(String legacySourceTable) {
    this.legacySourceTable = legacySourceTable;
    return this;
  }

  public ScreeningToReferralResourceBuilder setEndedAt(String endedAt) {
    this.endedAt = endedAt;
    return this;
  }

  public ScreeningToReferralResourceBuilder setIncidentCounty(String incidentCounty) {
    this.incidentCounty = incidentCounty;
    return this;
  }

  public ScreeningToReferralResourceBuilder setIncidentDate(String incidentDate) {
    this.incidentDate = incidentDate;
    return this;
  }

  public ScreeningToReferralResourceBuilder setLocationType(String locationType) {
    this.locationType = locationType;
    return this;
  }

  public ScreeningToReferralResourceBuilder setCommunicationMethod(Short communicationMethod) {
    this.communicationMethod = communicationMethod;
    return this;
  }

  public void setCurrentLocationOfChildren(String currentLocationOfChildren) {
    this.currentLocationOfChildren = currentLocationOfChildren;
  }

  public void setAssigneeStaffId(String assigneeStaffId) {
    this.assigneeStaffId = assigneeStaffId;
  }

  public ScreeningToReferralResourceBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public ScreeningToReferralResourceBuilder setReportNarrative(String reportNarrative) {
    this.reportNarrative = reportNarrative;
    return this;
  }

  public ScreeningToReferralResourceBuilder setReference(String reference) {
    this.reference = reference;
    return this;
  }

  public ScreeningToReferralResourceBuilder setResponseTime(Short responseTime) {
    this.responseTime = responseTime;
    return this;
  }

  public ScreeningToReferralResourceBuilder setStartedAt(String startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  public ScreeningToReferralResourceBuilder setAssignee(String assignee) {
    this.assignee = assignee;
    return this;
  }

  public ScreeningToReferralResourceBuilder setassigneeStaffId(String assigneeStaffId) {
    this.assigneeStaffId = assigneeStaffId;
    return this;
  }

  public ScreeningToReferralResourceBuilder setInjuryHarmCategory(Short injuryHarmCategory) {
    this.injuryHarmCategory = injuryHarmCategory;
    return this;
  }

  public ScreeningToReferralResourceBuilder setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
    return this;
  }

  public ScreeningToReferralResourceBuilder setScreeningDecision(String screeningDecision) {
    this.screeningDecision = screeningDecision;
    return this;
  }

  public ScreeningToReferralResourceBuilder setScreeningDecisionDetail(
      String screeningDecisionDetail) {
    this.screeningDecisionDetail = screeningDecisionDetail;
    return this;
  }

  public ScreeningToReferralResourceBuilder setApprovalStatus(int approvalStatus) {
    this.approvalStatus = approvalStatus;
    return this;
  }

  public ScreeningToReferralResourceBuilder setFamilyAwareness(boolean familyAwareness) {
    this.familyAwareness = familyAwareness;
    return this;
  }

  public ScreeningToReferralResourceBuilder setFiledWithLawEnforcement(
      boolean filedWithLawEnforcement) {
    this.filedWithLawEnforcement = filedWithLawEnforcement;
    return this;
  }

  public ScreeningToReferralResourceBuilder setResponsibleAgency(String responsibleAgency) {
    this.responsibleAgency = responsibleAgency;
    return this;
  }

  public String getLimitedAccessCode() {
    return limitedAccessCode;
  }

  public ScreeningToReferralResourceBuilder setLimitedAccessCode(String limitedAccessCode) {
    this.limitedAccessCode = limitedAccessCode;
    return this;
  }

  public String getLimitedAccessDescription() {
    return limitedAccessDescription;
  }

  public ScreeningToReferralResourceBuilder setLimitedAccessDescription(
      String limitedAccessDescription) {
    this.limitedAccessDescription = limitedAccessDescription;
    return this;
  }

  public String getLimitedAccessAgency() {
    return limitedAccessAgency;
  }

  public ScreeningToReferralResourceBuilder setLimitedAccessAgency(String limitedAccessAgency) {
    this.limitedAccessAgency = limitedAccessAgency;
    return this;
  }

  public Date getLimitedAccessDate() {
    return limitedAccessDate;
  }

  public ScreeningToReferralResourceBuilder setLimitedAccessDate(Date limitedAccessDate) {
    this.limitedAccessDate = limitedAccessDate;
    return this;
  }

  public ScreeningToReferralResourceBuilder setAddress(
      gov.ca.cwds.rest.api.domain.Address address) {
    this.address = address;
    return this;
  }

  public ScreeningToReferralResourceBuilder setParticipants(Set<Participant> participants) {
    this.participants = participants;
    return this;
  }

  public ScreeningToReferralResourceBuilder setCrossReports(
      Set<gov.ca.cwds.rest.api.domain.CrossReport> crossReports) {
    this.crossReports = crossReports;
    return this;
  }

  public ScreeningToReferralResourceBuilder setAllegations(
      Set<gov.ca.cwds.rest.api.domain.Allegation> allegations) {
    this.allegations = allegations;
    return this;
  }

  public ScreeningToReferral createScreeningToReferral() {
    return new ScreeningToReferral(id, legacySourceTable, referralId, endedAt, incidentCounty,
        incidentDate, locationType, communicationMethod, currentLocationOfChildren, name,
        reportNarrative, reference, responseTime, startedAt, assignee, assigneeStaffId,
        additionalInformation, screeningDecision, screeningDecisionDetail, approvalStatus,
        familyAwareness, filedWithLawEnforcement, responsibleAgency, limitedAccessCode,
        limitedAccessDescription, limitedAccessAgency, limitedAccessDate, address, participants,
        crossReports, allegations);
  }
}
