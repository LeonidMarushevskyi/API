package gov.ca.cwds.data.persistence.cms;

import static gov.ca.cwds.rest.util.FerbDateUtils.freshDate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
import gov.ca.cwds.data.persistence.PersistentObject;
import gov.ca.cwds.rest.api.domain.DomainChef;

/**
 * {@link PersistentObject} representing a Client Relationship
 * 
 * @author CWDS API Team
 */
@NamedQuery(
    name = "gov.ca.cwds.data.persistence.cms.ClientRelationship.findClientRelationshipByPrimaryClientId",
    query = "FROM ClientRelationship WHERE primaryClientId = :primaryClientId")
@NamedQuery(
    name = "gov.ca.cwds.data.persistence.cms.ClientRelationship.findClientRelationshipBySecondaryClientId",
    query = "FROM ClientRelationship WHERE secondaryClientId = :secondaryClientId")
@Entity
@Table(name = "CLN_RELT")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientRelationship extends CmsPersistentObject {

  private static final long serialVersionUID = 1L;

  @Column(name = "ABSENT_CD")
  private String absentParentCode;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "CLNTRELC")
  private Short clientRelationshipType;

  @Type(type = "date")
  @Column(name = "END_DT")
  private Date endDate;

  @Column(name = "FKCLIENT_0")
  private String secondaryClientId;

  @Column(name = "FKCLIENT_T")
  private String primaryClientId;

  @Id
  @Column(name = "IDENTIFIER", length = CMS_ID_LEN)
  private String id;

  @Column(name = "SAME_HM_CD")
  private String sameHomeCode;

  @Type(type = "date")
  @Column(name = "START_DT")
  private Date startDate;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public ClientRelationship() {
    super();
  }

  /**
   * Constructor
   * 
   * @param absentParentCode indicates if the parent CLIENT is absent for the child with whom the
   *        relationship is being defined (N)
   * @param clientRelationshipType Client Relationship Type from System Code table
   * @param endDate date the relationship ended
   * @param secondaryClientId Mandatory Foreign key that includes a secondary individual as a CLIENT
   * @param primaryClientId Mandatory Foreign key that includes a primary individual as a CLIENT
   * @param id unique key
   * @param sameHomeCode indicates whether the two CLIENTs live in the same home (Y)
   * @param startDate date the relationship began
   */
  public ClientRelationship(String absentParentCode, Short clientRelationshipType, Date endDate,
      String secondaryClientId, String primaryClientId, String id, String sameHomeCode,
      Date startDate) {
    super();
    this.absentParentCode = absentParentCode;
    this.clientRelationshipType = clientRelationshipType;
    this.endDate = freshDate(endDate);
    this.secondaryClientId = secondaryClientId;
    this.primaryClientId = primaryClientId;
    this.id = id;
    this.sameHomeCode = sameHomeCode;
    this.startDate = freshDate(startDate);
  }

  /**
   * 
   * @param id unique key
   * @param clientRelationship the domain object to construct this object from
   * @param lastUpdatedId the id of the last person to update this object
   * @param lastUpdatedTime the time when this object is last updated
   */
  public ClientRelationship(String id,
      gov.ca.cwds.rest.api.domain.cms.ClientRelationship clientRelationship, String lastUpdatedId,
      Date lastUpdatedTime) {
    super(lastUpdatedId, lastUpdatedTime);
    this.absentParentCode = clientRelationship.getAbsentParentCode();
    this.clientRelationshipType = clientRelationship.getClientRelationshipType();
    this.endDate = DomainChef.uncookDateString(clientRelationship.getEndDate());
    this.secondaryClientId = clientRelationship.getSecondaryClientId();
    this.primaryClientId = clientRelationship.getPrimaryClientId();
    this.id = id;
    this.sameHomeCode = clientRelationship.getSameHomeCode();
    this.startDate = DomainChef.uncookDateString(clientRelationship.getStartDate());
  }

  @Override
  public String getPrimaryKey() {
    return getId();
  }

  /**
   * @return the absentParentCode
   */
  public String getAbsentParentCode() {
    return absentParentCode;
  }

  /**
   * @return the clientRelationshipType
   */
  public Short getClientRelationshipType() {
    return clientRelationshipType;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate() {
    return freshDate(endDate);
  }

  /**
   * @return the secondaryClientId
   */
  public String getSecondaryClientId() {
    return secondaryClientId;
  }

  /**
   * @return the primaryClientId
   */
  public String getPrimaryClientId() {
    return primaryClientId;
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the sameHomeCode
   */
  public String getSameHomeCode() {
    return StringUtils.trimToEmpty(sameHomeCode);
  }

  /**
   * @return the startDate
   */
  public Date getStartDate() {
    return freshDate(startDate);
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
