package gov.ca.cwds.data.persistence.cms;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

import gov.ca.cwds.data.persistence.PersistentObject;


/**
 * {@link PersistentObject} representing an OtherClientName
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("serial")
@NamedQueries({
    @NamedQuery(name = "gov.ca.cwds.data.persistence.cms.OtherClientName.findAll",
        query = "FROM OtherClientName"),
    @NamedQuery(name = "gov.ca.cwds.data.persistence.cms.OtherClientName.findAllUpdatedAfter",
        query = "FROM OtherClientName WHERE lastUpdatedTime > :after")})
@Entity
@Table(name = "OCL_NM_T")
public class OtherClientName extends CmsPersistentObject {

  /**
   * Hibernate annotation {@link IdClass} requires that members match the id columns of the parent
   * class. From the Javadoc of said annotation,
   * 
   * <blockquote> "The names of the fields or properties in the primary key class and the primary
   * key fields or properties of the entity must correspond and their types must be the same."
   * </blockquote>
   * 
   * <p>
   * Instead of {@link IdClass}, use the nifty approach below using {@link Embeddable} and
   * {@link EmbeddedId}. Try it on your friends!
   * </p>
   * 
   * @see VarargPrimaryKey
   */
  @Embeddable
  public static final class EmbeddablePrimaryKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id1 = "";
    private String id2 = "";

    /**
     * Default ctor.
     */
    public EmbeddablePrimaryKey() {
      // Default values.
    }

    /**
     * Construct from arguments.
     * 
     * @param id1 generic id 1
     * @param id2 generic id 2
     */
    public EmbeddablePrimaryKey(String id1, String id2) {
      this.id1 = id1;
      this.id2 = id2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return "referralId=" + id2.trim() + ",clientId=" + id1.trim();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id1 == null) ? 0 : id1.hashCode());
      result = prime * result + ((id2 == null) ? 0 : id2.hashCode());
      return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      EmbeddablePrimaryKey other = (EmbeddablePrimaryKey) obj;
      if (id1 == null) {
        if (other.id1 != null)
          return false;
      } else if (!id1.equals(other.id1))
        return false;
      if (id2 == null) {
        if (other.id2 != null)
          return false;
      } else if (!id2.equals(other.id2))
        return false;
      return true;
    }

    /**
     * @return arbitrary id column, {@link #id1}.
     */
    public String getId1() {
      return id1;
    }

    /**
     * @param id1 arbitrary id column, {@link #id1}.
     */
    public void setId1(String id1) {
      this.id1 = id1;
    }

    /**
     * @return arbitrary id column, {@link #id2}.
     */
    public String getId2() {
      return id2;
    }

    /**
     * @param id2 arbitrary id column, {@link #id2}.
     */
    public void setId2(String id2) {
      this.id2 = id2;
    }
  }

  @AttributeOverrides({
      @AttributeOverride(name = "id1", column = @Column(name = "FKCLIENT_T", length = CMS_ID_LEN)),
      @AttributeOverride(name = "id2", column = @Column(name = "THIRD_ID", length = CMS_ID_LEN))})
  @EmbeddedId
  private OtherClientName.EmbeddablePrimaryKey id;

  @Column(name = "FIRST_NM")
  private String firstName;

  @Column(name = "LAST_NM")
  private String lastName;

  @Column(name = "MIDDLE_NM")
  private String middleName;

  @Column(name = "NMPRFX_DSC")
  private String namePrefixDescription;

  @Type(type = "short")
  @Column(name = "NAME_TPC")
  private Short nameType;

  @Column(name = "SUFX_TLDSC")
  private String suffixTitleDescription;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public OtherClientName() {
    super();
    this.id = new OtherClientName.EmbeddablePrimaryKey();
  }

  /**
   * Construct from String inputs.
   * 
   * @param clientId the client id
   * @param firstName first name
   * @param lastName last name
   * @param middleName middle name
   * @param namePrefixDescription name prefix description, if any
   * @param nameType name type
   * @param suffixTitleDescription suffix title description, if any
   * @param thirdId third id, used to uniquely identify records
   */
  public OtherClientName(String clientId, String firstName, String lastName, String middleName,
      String namePrefixDescription, Short nameType, String suffixTitleDescription, String thirdId) {
    super();
    this.id = new OtherClientName.EmbeddablePrimaryKey(clientId, thirdId);

    this.firstName = firstName;
    this.lastName = lastName;
    this.middleName = middleName;
    this.namePrefixDescription = namePrefixDescription;
    this.nameType = nameType;
    this.suffixTitleDescription = suffixTitleDescription;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.data.persistence.PersistentObject#getPrimaryKey()
   */
  @Override
  public OtherClientName.EmbeddablePrimaryKey getPrimaryKey() {
    return this.id;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return StringUtils.trimToEmpty(firstName);
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return StringUtils.trimToEmpty(lastName);
  }

  /**
   * @return the middleName
   */
  public String getMiddleName() {
    return StringUtils.trimToEmpty(middleName);
  }

  /**
   * @return the namePrefixDescription
   */
  public String getNamePrefixDescription() {
    return StringUtils.trimToEmpty(namePrefixDescription);
  }

  /**
   * @return the nameType
   */
  public Short getNameType() {
    return nameType;
  }

  /**
   * @return the suffixTitleDescription
   */
  public String getSuffixTitleDescription() {
    return StringUtils.trimToEmpty(suffixTitleDescription);
  }

  /**
   * Delegate accessor: get the client id through the composite primary key.
   * 
   * @return the clientId
   */
  @JsonProperty(value = "clientId")
  public String getClientId() {
    return StringUtils.trimToEmpty(id.getId1());
  }

  /**
   * Delegate accessor: get the third id through the composite primary key.
   * 
   * @return the "thirdId"
   */
  @JsonProperty(value = "thirdId")
  public String getThirdId() {
    return StringUtils.trimToEmpty(id.getId2());
  }

  /**
   * Delegate accessor: set the client id through the composite primary key.
   * 
   * @param clientId the clientId
   */
  @JsonProperty(value = "clientId")
  public void setClientId(String clientId) {
    id.setId1(clientId);
  }

  /**
   * Delegate accessor: set the third id through the composite primary key.
   * 
   * @param thirdId the "thirdId"
   */
  @JsonProperty(value = "thirdId")
  public void setThirdId(String thirdId) {
    id.setId2(thirdId);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int ret = 1;
    ret = prime * ret + ((id == null || id.getId1() == null) ? 0 : id.getId1().hashCode());
    ret = prime * ret + ((id == null || id.getId2() == null) ? 0 : id.getId2().hashCode());
    ret = prime * ret + ((firstName == null) ? 0 : firstName.hashCode());
    ret = prime * ret + ((lastName == null) ? 0 : lastName.hashCode());
    ret = prime * ret + ((middleName == null) ? 0 : middleName.hashCode());
    ret = prime * ret + ((namePrefixDescription == null) ? 0 : namePrefixDescription.hashCode());
    ret = prime * ret + ((nameType == null) ? 0 : nameType.hashCode());
    ret = prime * ret + ((suffixTitleDescription == null) ? 0 : suffixTitleDescription.hashCode());
    ret = prime * ret
        + ((super.getLastUpdatedId() == null) ? 0 : super.getLastUpdatedId().hashCode());
    ret = prime * ret
        + ((super.getLastUpdatedTime() == null) ? 0 : super.getLastUpdatedTime().hashCode());

    return ret;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof OtherClientName)) {
      return false;
    }
    OtherClientName o = (OtherClientName) obj;

    // Reduce cognitive complexity -- at the expense of slightly slower performance.
    if (!EqualsBuilder.reflectionEquals(this, o, false))
      return false;

    if (super.getLastUpdatedId() == null) {
      if (o.getLastUpdatedId() != null) {
        return false;
      }
    } else if (!super.getLastUpdatedId().equals(o.getLastUpdatedId())) {
      return false;
    }
    if (super.getLastUpdatedTime() == null) {
      if (o.getLastUpdatedTime() != null) {
        return false;
      }
    } else if (!super.getLastUpdatedTime().equals(o.getLastUpdatedTime())) {
      return false;
    }
    return true;
  }

}
