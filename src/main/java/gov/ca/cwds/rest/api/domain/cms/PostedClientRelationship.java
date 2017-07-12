package gov.ca.cwds.rest.api.domain.cms;

import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.services.ServiceException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@link Response} adding an id to the {@link ClientRelationship}
 * 
 * @author CWDS API Team
 */
public class PostedClientRelationship extends ClientRelationship {
  /**
   * Serialization version
   */
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id;

  /**
   * Constructor
   * 
   * @param clientRelationship The persisted Client Relationship
   * 
   */
  public PostedClientRelationship(
      gov.ca.cwds.data.persistence.cms.ClientRelationship clientRelationship) {
    super(clientRelationship);
    if (StringUtils.isBlank(clientRelationship.getId())) {
      throw new ServiceException("ClientRelationship ID cannot be empty");
    }

    this.id = clientRelationship.getId();
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }


}
