package gov.ca.cwds.data.persistence.cms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import gov.ca.cwds.data.CmsSystemCodeDeserializer;
import gov.ca.cwds.data.SystemCodeSerializer;
import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * {@link PersistentObject} representing a Client Collateral.
 * 
 * @author CWDS API Team
 */
@Entity
@Table(name = "CLN_COLT")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientCollateral extends CmsPersistentObject {

  private static final long serialVersionUID = 1L;

  @Column(name = "ACTIVE_IND")
  private String activeIndicator;

  @SystemCodeSerializer(logical = true, description = true)
  @JsonDeserialize(using = CmsSystemCodeDeserializer.class)
  @Type(type = "short")
  @Column(name = "COL_RELC")
  private Short collateralClientReporterRelationshipType;

  @Column(name = "COMNT_DSC")
  private String commentDescription;

  @Column(name = "FKCLIENT_T")
  private String clientId;

  @Column(name = "FKCOLTRL_T")
  private String collateralIndividualId;

  @Id
  @Column(name = "THIRD_ID", length = CMS_ID_LEN)
  private String thirdId;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public ClientCollateral() {
    super();
  }

  /**
   * Constructor
   * 
   * @param activeIndicator = Indicates if relationship to CollateralIndividual is active (Y)
   * @param collateralClientReporterRelationshipType = Collateral Client Rptr Relationship Type in
   *        System Codes
   * @param commentDescription - description
   * @param clientId - foreign key Client table
   * @param collateralIndividualId - foreign key to CollateralIndividual table
   * @param thirdId - Unique key
   */
  public ClientCollateral(String activeIndicator, Short collateralClientReporterRelationshipType,
      String commentDescription, String clientId, String collateralIndividualId, String thirdId) {
    super();
    this.activeIndicator = activeIndicator;
    this.collateralClientReporterRelationshipType = collateralClientReporterRelationshipType;
    this.commentDescription = commentDescription;
    this.clientId = clientId;
    this.collateralIndividualId = collateralIndividualId;
    this.thirdId = thirdId;
  }

  /**
   * @return serialVersionUID
   */
  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @SuppressWarnings("javadoc")
  public String getActiveIndicator() {
    return activeIndicator;
  }

  @SuppressWarnings("javadoc")
  public Short getCollateralClientReporterRelationshipType() {
    return collateralClientReporterRelationshipType;
  }

  /**
   * @return commentDescription
   */
  public String getCommentDescription() {
    return commentDescription;
  }

  @SuppressWarnings("javadoc")
  public String getClientId() {
    return clientId;
  }

  @SuppressWarnings("javadoc")
  public String getCollateralIndividualId() {
    return collateralIndividualId;
  }

  @SuppressWarnings("javadoc")
  public String getThirdId() {
    return thirdId;
  }

  @Override
  public String getPrimaryKey() {
    return getThirdId();
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
