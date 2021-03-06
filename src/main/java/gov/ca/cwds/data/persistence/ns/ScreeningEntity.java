package gov.ca.cwds.data.persistence.ns;

import static gov.ca.cwds.rest.util.FerbDateUtils.freshDate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * {@link PersistentObject} representing Screening.
 *
 * @author CWDS API Team
 */
@NamedQuery(name = "gov.ca.cwds.data.persistence.ns.ScreeningEntity.findScreeningsByReferralId",
    query = "FROM ScreeningEntity WHERE referralId = :referralId")
@NamedQuery(name = "gov.ca.cwds.data.persistence.ns.ScreeningEntity.findScreeningsByClientIds",
    query = "SELECT s FROM ScreeningEntity s JOIN s.participants p WHERE p.legacyId IN (:clientIds)")
@SuppressWarnings("serial")
@Entity
@Table(name = "screenings")
public class ScreeningEntity implements PersistentObject {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screening_id")
  @SequenceGenerator(name = "screening_id", sequenceName = "screenings_id_seq")
  private String id;

  @Column(name = "reference")
  private String reference;

  @Column(name = "ended_at")
  @Type(type = "date")
  private Date endedAt;

  @Column(name = "incident_county")
  private String incidentCounty;

  @Column(name = "incident_date")
  @Type(type = "date")
  private Date incidentDate;

  @Column(name = "location_type")
  private String locationType;

  @Column(name = "communication_method")
  private String communicationMethod;

  @Column(name = "name")
  private String name;

  @Column(name = "screening_decision")
  private String screeningDecision;

  @Column(name = "started_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startedAt;

  @Column(name = "report_narrative")
  private String narrative;

  @Column(name = "assignee")
  private String assignee;

  @Column(name = "additional_information")
  private String additionalInformation;

  @Column(name = "screening_decision_detail")
  private String screeningDecisionDetail;

  @Column(name = "safety_information")
  private String safetyInformation;

  @Column(name = "safety_alerts")
  @Type(type = "gov.ca.cwds.rest.util.StringArrayType")
  private String[] safetyAlerts;

  @Column(name = "referral_id")
  private String referralId;

  @Column(name = "assignee_staff_id")
  private String assigneeStaffId;

  @Column(name = "access_restrictions")
  private String accessRestrictions;

  @Column(name = "restrictions_rationale")
  private String restrictionsRationale;

  @Column(name = "user_county_code")
  private Integer userCountyCode;

  @Column(name = "restrictions_date")
  private Date restrictionsDate;

  @Column(name = "indexable")
  private Boolean indexable;

  @OneToMany(mappedBy = "screeningEntity", cascade = CascadeType.ALL)
  private Set<Allegation> allegations = new HashSet<>();

  @OneToMany(mappedBy = "screeningEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<ParticipantEntity> participants = new HashSet<>();

  /**
   * Default constructor
   *
   * Required for Hibernate
   */
  public ScreeningEntity() {
    super();
  }

  /**
   * Constructor
   *
   * @param reference The reference
   */
  public ScreeningEntity(String reference) {
    this.reference = reference;
  }

  /**
   * Constructor
   *
   * @param id the id
   * @param reference The reference
   * @param startedAt The started at date
   * @param endedAt The endedAt date
   * @param incidentCounty The incident county
   * @param incidentDate The incident date
   * @param locationType The location type
   * @param communicationMethod The communication method
   * @param name The name of the screening
   * @param responseTime The response time
   * @param screeningDecision The screening decision
   * @param screeningDecisionDetail The screening decision detail
   * @param narrative The narrative
   * @param contactAddress The contact address
   * @param participants The list of participants
   */
  public ScreeningEntity(String id, String reference, Date startedAt, Date endedAt, String incidentCounty, Date incidentDate,
      String locationType, String communicationMethod, String name, String responseTime,
      String screeningDecision, String screeningDecisionDetail, String narrative, Address contactAddress,
      Set<ParticipantEntity> participants) {
    super();
    this.id = id;
    this.reference = reference;
    this.startedAt = freshDate(startedAt);
    this.endedAt = freshDate(endedAt);
    this.incidentCounty = incidentCounty;
    this.incidentDate = freshDate(incidentDate);
    this.locationType = locationType;
    this.communicationMethod = communicationMethod;
    this.name = name;
    this.screeningDecision = screeningDecision;
    this.screeningDecisionDetail = screeningDecisionDetail;
    this.narrative = narrative;
    this.safetyAlerts = new String[1];
    this.participants = participants;
  }

  /**
   * {@inheritDoc}
   *
   * @see gov.ca.cwds.data.persistence.PersistentObject#getPrimaryKey()
   */
  @Override
  public String getPrimaryKey() {
    return getId();
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the reference
   */
  public String getReference() {
    return reference;
  }

  /**
   * @return the endedAt
   */
  public Date getEndedAt() {
    return freshDate(endedAt);
  }

  /**
   * @return the incidentCounty
   */
  public String getIncidentCounty() {
    return incidentCounty;
  }

  /**
   * @return the incidentDate
   */
  public Date getIncidentDate() {
    return freshDate(incidentDate);
  }

  /**
   * @return the locationType
   */
  public String getLocationType() {
    return locationType;
  }

  /**
   * @return the communicationMethod
   */
  public String getCommunicationMethod() {
    return communicationMethod;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }


  /**
   * @return the screeningDecision
   */
  public String getScreeningDecision() {
    return screeningDecision;
  }

  /**
   * @return the startedAt
   */
  public Date getStartedAt() {
    return freshDate(startedAt);
  }

  /**
   * @return the narrative
   */
  public String getNarrative() {
    return narrative;
  }

  /**
   * @return the assignee
   */
  public String getAssignee() {
    return assignee;
  }

  /**
   * @return the additionalInformation
   */
  public String getAdditionalInformation() {
    return additionalInformation;
  }

  /**
   * @return the screeningDecisionDetail
   */
  public String getScreeningDecisionDetail() {
    return screeningDecisionDetail;
  }

  /**
   * @return the safetyInformation
   */
  public String getSafetyInformation() {
    return safetyInformation;
  }

  /**
   * @return the safetyAlerts
   */
  public String[] getSafetyAlerts() {
    return safetyAlerts;
  }

  /**
   * @return the referralId
   */
  public String getReferralId() {
    return referralId;
  }

  /**
   * @return the assigneeStaffId
   */
  public String getAssigneeStaffId() {
    return assigneeStaffId;
  }

  /**
   * @return the accessRestrictions
   */
  public String getAccessRestrictions() {
    return accessRestrictions;
  }

  /**
   * @return the restrictionsRationale
   */
  public String getRestrictionsRationale() {
    return restrictionsRationale;
  }

  /**
   * @return the userCountyCode
   */
  public Integer getUserCountyCode() {
    return userCountyCode;
  }

  /**
   * @return the restrictionsDate
   */
  public Date getRestrictionsDate() {
    return freshDate(restrictionsDate);
  }

  /**
   * @return the indexable
   */
  public Boolean isIndexable() {
    return indexable;
  }

  /**
   * @return the allegations
   */
  public Set<Allegation> getAllegations() {
    return allegations;
  }

  public Set<ParticipantEntity> getParticipants() {
    return participants;
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public final int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }
}
