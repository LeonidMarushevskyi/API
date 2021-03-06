package gov.ca.cwds.rest.api.domain.investigation;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gov.ca.cwds.data.persistence.cms.Client;
import gov.ca.cwds.data.persistence.cms.ClientRelationship;
import gov.ca.cwds.rest.api.domain.DomainObject;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.util.CmsRecordUtils;
import io.dropwizard.jackson.JsonSnakeCase;
import io.swagger.annotations.ApiModelProperty;

/**
 * {@link DomainObject} representing a ClientRelationship
 * 
 * @author CWDS API Team
 */
@JsonSnakeCase
@JsonPropertyOrder({"related_person_first_name", "related_person_last_name",
    "index_person_relationship", "relationship_context", "related_person_relationship",
    "legacy_description"})
public final class RelationshipTo implements Serializable {

  /**
   * Serialization version
   */
  private static final long serialVersionUID = 1L;

  @JsonProperty("related_person_first_name")
  @ApiModelProperty(required = false, readOnly = false, value = "first name", example = "jane")
  @Size(min = 1, max = 20)
  private String relatedFirstName;

  @JsonProperty("related_person_last_name")
  @NotBlank
  @Size(min = 1, max = 25)
  @ApiModelProperty(required = true, readOnly = false, value = "last name", example = "sufer")
  private String relatedLastName;

  @JsonProperty("indexed_person_relationship")
  @ApiModelProperty(required = true, readOnly = false, value = "relationship to the person",
      example = "Sister")
  private String relationshipToPerson;

  @JsonProperty("relationship_context")
  @ApiModelProperty(required = true, readOnly = false, value = "context of the relationship",
      example = "Paternal")
  private String relationshipContext;

  @JsonProperty("related_person_relationship")
  @ApiModelProperty(required = true, readOnly = false, value = "persons relationship to",
      example = "Brother")
  private String relatedPersonRelationship;

  @JsonProperty("legacy_descriptor")
  private CmsRecordDescriptor cmsRecordDescriptor;

  /**
   * empty constructor
   */
  public RelationshipTo() {
    super();
  }

  /**
   * @param relatedFirstName - related persons first name
   * @param relatedLastName - related persons last name
   * @param relationshipToPerson - relation of owning person
   * @param relationshipContext - context information
   * @param relatedPersonRelationship - relation to owning person
   * @param cmsRecordDescriptor - CMS record description
   */
  public RelationshipTo(String relatedFirstName, String relatedLastName,
      String relationshipToPerson, String relationshipContext, String relatedPersonRelationship,
      CmsRecordDescriptor cmsRecordDescriptor) {
    super();
    this.relatedFirstName = relatedFirstName;
    this.relatedLastName = relatedLastName;
    this.relationshipToPerson = relationshipToPerson;
    this.relationshipContext = relationshipContext;
    this.relatedPersonRelationship = relatedPersonRelationship;
    this.cmsRecordDescriptor = cmsRecordDescriptor;
  }

  /**
   * constructing RelationshipTo object
   * 
   * @param clientRelationship - client relationship object
   * @param client - client object
   */
  public RelationshipTo(ClientRelationship clientRelationship, Client client) {
    this.relatedFirstName = client.getFirstName();
    this.relatedLastName = client.getLastName();
    this.relationshipToPerson = String.valueOf(clientRelationship.getClientRelationshipType());
    relationshipContext = " ";
    relatedPersonRelationship = " ";
    this.cmsRecordDescriptor = CmsRecordUtils.createLegacyDescriptor(clientRelationship.getId(),
        LegacyTable.CLIENT_RELATIONSHIP);
  }

  /**
   * @return - related first name
   */
  public String getRelatedFirstName() {
    return relatedFirstName;
  }


  /**
   * @return - related last name
   */
  public String getRelatedLastName() {
    return relatedLastName;
  }


  /**
   * @return - relationship to person
   */
  public String getRelationshipToPerson() {
    return relationshipToPerson;
  }


  /**
   * @return - relationship context
   */
  public String getRelationshipContext() {
    return relationshipContext;
  }


  /**
   * @return - related person relationship
   */
  public String getRelatedPersonRelationship() {
    return relatedPersonRelationship;
  }


  /**
   * @return - CMS record description
   */
  public CmsRecordDescriptor getCmsRecordDescriptor() {
    return cmsRecordDescriptor;
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
