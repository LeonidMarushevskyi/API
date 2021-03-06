package gov.ca.cwds.data.persistence.cms;

import static gov.ca.cwds.rest.util.FerbDateUtils.freshDate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gov.ca.cwds.data.CmsSystemCodeDeserializer;
import gov.ca.cwds.data.SystemCodeSerializer;
import gov.ca.cwds.rest.api.domain.DomainChef;

/**
 * {@link CmsPersistentObject} Class representing an Allegation.
 * 
 * @author CWDS API Team
 */
@NamedQuery(name = "gov.ca.cwds.data.persistence.cms.Allegation.findByReferral",
    query = "FROM Allegation WHERE referralId = :referralId")
@SuppressWarnings("serial")
@Entity
@Table(name = "ALLGTN_T")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Allegation extends CmsPersistentObject {

  @Id
  @Column(name = "IDENTIFIER", length = CMS_ID_LEN)
  private String id;

  @Type(type = "date")
  @Column(name = "ABUSE_ENDT")
  private Date abuseEndDate;

  @Type(type = "short")
  @Column(name = "ABUSE_FREQ")
  private Short abuseFrequency;

  @Column(name = "ABUSE_PDCD")
  private String abuseFrequencyPeriodCode;

  @Column(name = "LOC_DSC")
  private String abuseLocationDescription;

  @Type(type = "date")
  @Column(name = "ABUSE_STDT")
  private Date abuseStartDate;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "ALG_DSPC")
  private Short allegationDispositionType;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "ALG_TPC")
  private Short allegationType;

  @Column(name = "DISPSN_DSC")
  private String dispositionDescription;

  @Type(type = "date")
  @Column(name = "DISPSN_DT")
  private Date dispositionDate;

  @Column(name = "IJHM_DET_B")
  private String injuryHarmDetailIndicator;

  @Column(name = "NON_PRT_CD")
  private String nonProtectingParentCode;

  @Column(name = "STFADD_IND")
  private String staffPersonAddedIndicator;

  @Column(name = "FKCLIENT_T", length = CMS_ID_LEN)
  private String victimClientId;

  @Column(name = "FKCLIENT_0", length = CMS_ID_LEN, nullable = true)
  private String perpetratorClientId;

  @Column(name = "FKREFERL_T", length = CMS_ID_LEN)
  private String referralId;

  @Column(name = "CNTY_SPFCD")
  private String countySpecificCode;

  @Column(name = "ZIPPY_IND")
  private String zippyCreatedIndicator;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "PLC_FCLC")
  private Short placementFacilityType;

  /**
   * #147241489: referential integrity check.
   * <p>
   * Doesn't actually load the data. Just checks the existence of the parent referral record.
   * </p>
   */
  @ManyToOne(optional = false)
  @JoinColumn(name = "FKCLIENT_T", nullable = false, insertable = false, updatable = false)
  private Client victimClients;

  @ManyToOne(optional = true)
  @JoinColumn(name = "FKCLIENT_0", nullable = true, insertable = false, updatable = false)
  private Client perpetratorClients;

  @ManyToOne(optional = false)
  @JoinColumn(name = "FKREFERL_T", nullable = false, updatable = false, insertable = false)
  private Referral referral;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public Allegation() {
    super();
  }

  /**
   * Constructor
   * 
   * @param id primary key
   * @param abuseEndDate abuse end date
   * @param abuseStartDate abuse start date
   * @param abuseFrequency abuse frequency
   * @param abuseFrequencyPeriodCode abuse frequency period code
   * @param abuseLocationDescription abuse location
   * @param allegationDispositionType abuse disposition
   * @param allegationType type of allegation
   * @param dispositionDescription description of disposition
   * @param dispositionDate disposition date
   * @param injuryHarmDetailIndicator flag injury or harm
   * @param nonProtectingParentCode non-protecting parent code
   * @param staffPersonAddedIndicator flag added by staff
   * @param victimClientId victim or client id
   * @param perpetratorClientId perpetrator's client id
   * @param referralId referral id
   * @param countySpecificCode county code
   * @param zippyCreatedIndicator created by Zippy
   * @param placementFacilityType type of placement facility
   * @param victimClients victimClient
   * @param perpetratorClients perpetratorClient
   */
  public Allegation(String id, Date abuseEndDate, Date abuseStartDate, Short abuseFrequency, // NOSONAR
      String abuseFrequencyPeriodCode, String abuseLocationDescription,
      Short allegationDispositionType, Short allegationType, String dispositionDescription,
      Date dispositionDate, String injuryHarmDetailIndicator, String nonProtectingParentCode,
      String staffPersonAddedIndicator, String victimClientId, String perpetratorClientId,
      String referralId, String countySpecificCode, String zippyCreatedIndicator,
      Short placementFacilityType, Client victimClients, Client perpetratorClients) {
    super();

    this.id = id;
    this.abuseEndDate = freshDate(abuseEndDate);
    this.abuseStartDate = freshDate(abuseStartDate);
    this.abuseFrequency = abuseFrequency;
    this.abuseFrequencyPeriodCode = abuseFrequencyPeriodCode;
    this.abuseLocationDescription = abuseLocationDescription;
    this.allegationDispositionType = allegationDispositionType;
    this.allegationType = allegationType;
    this.dispositionDescription = dispositionDescription;
    this.dispositionDate = freshDate(dispositionDate);
    this.injuryHarmDetailIndicator = injuryHarmDetailIndicator;
    this.nonProtectingParentCode = nonProtectingParentCode;
    this.staffPersonAddedIndicator = staffPersonAddedIndicator;
    this.victimClientId = victimClientId;
    this.perpetratorClientId = perpetratorClientId;
    this.referralId = referralId;
    this.countySpecificCode = countySpecificCode;
    this.zippyCreatedIndicator = zippyCreatedIndicator;
    this.placementFacilityType = placementFacilityType;
    this.victimClients = victimClients;
    this.perpetratorClients = perpetratorClients;
  }

  /**
   * Constructor
   * 
   * @param id The id
   * @param persistedAllegation persistedAllegation The domain object to construct this object from
   * @param lastUpdatedId the id of the last user to update this object
   * @param lastUpdatedTime the time of last update this object
   */
  public Allegation(String id, gov.ca.cwds.rest.api.domain.cms.Allegation persistedAllegation,
      String lastUpdatedId, Date lastUpdatedTime) {
    super(lastUpdatedId, lastUpdatedTime);
    this.id = id;
    this.abuseEndDate = DomainChef.uncookDateString(persistedAllegation.getAbuseEndDate());
    this.abuseStartDate = DomainChef.uncookDateString(persistedAllegation.getAbuseStartDate());
    this.abuseFrequency = persistedAllegation.getAbuseFrequency();
    this.abuseFrequencyPeriodCode = persistedAllegation.getAbuseFrequencyPeriodCode();
    this.abuseLocationDescription =
        StringUtils.isBlank(persistedAllegation.getAbuseLocationDescription()) ? ""
            : persistedAllegation.getAbuseLocationDescription();
    this.allegationDispositionType = persistedAllegation.getAllegationDispositionType();
    this.allegationType = persistedAllegation.getAllegationType();
    this.dispositionDescription = persistedAllegation.getDispositionDescription();
    this.dispositionDate = DomainChef.uncookDateString(persistedAllegation.getDispositionDate());
    this.injuryHarmDetailIndicator =
        DomainChef.cookBoolean(persistedAllegation.getInjuryHarmDetailIndicator());
    this.nonProtectingParentCode = persistedAllegation.getNonProtectingParentCode();
    this.staffPersonAddedIndicator =
        DomainChef.cookBoolean(persistedAllegation.getStaffPersonAddedIndicator());
    this.victimClientId = persistedAllegation.getVictimClientId();
    this.perpetratorClientId = StringUtils.isBlank(persistedAllegation.getPerpetratorClientId())
        ? null : persistedAllegation.getPerpetratorClientId();
    this.referralId = persistedAllegation.getReferralId();
    this.countySpecificCode = persistedAllegation.getCountySpecificCode();
    this.zippyCreatedIndicator =
        DomainChef.cookBoolean(persistedAllegation.getZippyCreatedIndicator());
    this.placementFacilityType = persistedAllegation.getPlacementFacilityType();
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
   * @return the abuseEndDate
   */
  public Date getAbuseEndDate() {
    return freshDate(abuseEndDate);
  }

  /**
   * @return the abuseFrequency
   */
  public Short getAbuseFrequency() {
    return abuseFrequency;
  }

  /**
   * @return the abuseFrequencyPeriodCode
   */
  public String getAbuseFrequencyPeriodCode() {
    return abuseFrequencyPeriodCode;
  }

  /**
   * @return the abuseLocationDescription
   */
  public String getAbuseLocationDescription() {
    return abuseLocationDescription;
  }

  /**
   * @return the abuseStartDate
   */
  public Date getAbuseStartDate() {
    return freshDate(abuseStartDate);
  }

  /**
   * @return the allegationDispositionType
   */
  public Short getAllegationDispositionType() {
    return allegationDispositionType;
  }

  /**
   * @return the allegationType
   */
  public Short getAllegationType() {
    return allegationType;
  }

  /**
   * @return the dispositionDescription
   */
  public String getDispositionDescription() {
    return dispositionDescription;
  }

  /**
   * @return the dispositionDate
   */
  public Date getDispositionDate() {
    return freshDate(dispositionDate);
  }

  /**
   * @return the injuryHarmDetailIndicator
   */
  public String getInjuryHarmDetailIndicator() {
    return injuryHarmDetailIndicator;
  }

  /**
   * @return the nonProtectingParentCode
   */
  public String getNonProtectingParentCode() {
    return nonProtectingParentCode;
  }

  /**
   * @return the staffPersonAddedIndicator
   */
  public String getStaffPersonAddedIndicator() {
    return staffPersonAddedIndicator;
  }

  /**
   * @return the victimClientId
   */
  public String getVictimClientId() {
    return victimClientId;
  }

  /**
   * @return the perpetratorClientId
   */
  public String getPerpetratorClientId() {
    return perpetratorClientId;
  }

  /**
   * @return the referralId
   */
  public String getReferralId() {
    return referralId;
  }

  /**
   * @return the countySpecificCode
   */
  public String getCountySpecificCode() {
    return countySpecificCode;
  }

  /**
   * @return the zippyCreatedIndicator
   */
  public String getZippyCreatedIndicator() {
    return zippyCreatedIndicator;
  }

  /**
   * @return the placementFacilityType
   */
  public Short getPlacementFacilityType() {
    return placementFacilityType;
  }

  /**
   * @return the victimClient
   */
  public Client getVictimClients() {
    return victimClients;
  }

  /**
   * @return the perpetratorClient
   */
  public Client getPerpetratorClients() {
    return perpetratorClients;
  }

  @Override
  public String toString() {
    return "Allegation [id=" + id + ", abuseEndDate=" + abuseEndDate + ", abuseFrequency="
        + abuseFrequency + ", abuseFrequencyPeriodCode=" + abuseFrequencyPeriodCode
        + ", abuseLocationDescription=" + abuseLocationDescription + ", abuseStartDate="
        + abuseStartDate + ", allegationDispositionType=" + allegationDispositionType
        + ", allegationType=" + allegationType + ", dispositionDescription="
        + dispositionDescription + ", dispositionDate=" + dispositionDate
        + ", injuryHarmDetailIndicator=" + injuryHarmDetailIndicator + ", nonProtectingParentCode="
        + nonProtectingParentCode + ", staffPersonAddedIndicator=" + staffPersonAddedIndicator
        + ", victimClientId=" + victimClientId + ", perpetratorClientId=" + perpetratorClientId
        + ", referralId=" + referralId + ", countySpecificCode=" + countySpecificCode
        + ", zippyCreatedIndicator=" + zippyCreatedIndicator + ", placementFacilityType="
        + placementFacilityType
        // + ", victimClients=" + victimClients + ", perpetratorClients=" + perpetratorClients
        + "]";
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}
