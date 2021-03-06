package gov.ca.cwds.data.persistence.ns;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

import gov.ca.cwds.data.ns.NsPersistentObject;

/**
 * {@link NsPersistentObject} representing an Address
 * 
 * @author CWDS API Team
 */
@NamedQuery(name = "gov.ca.cwds.rest.api.persistence.ns.Address.findAll", query = "FROM Address")
@NamedQuery(name = "gov.ca.cwds.rest.api.persistence.ns.Address.findAllUpdatedAfter",
    query = "FROM Address WHERE lastUpdatedTime > :after")
@SuppressWarnings("serial")
@Entity
@Table(name = "address")
public class Address extends NsPersistentObject {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_address_id")
  @SequenceGenerator(name = "seq_address_id", sequenceName = "seq_address_id", allocationSize = 50)
  @Column(name = "address_id")
  private Long id;

  @Column(name = "street_address")
  private String streetAddress;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "zip")
  private String zip;

  @Column(name = "address_type_id")
  private String type;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "personAddressId.address")
  private Set<PersonAddress> personAddress = new HashSet<>();

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public Address() {
    super();
  }

  /**
   * Constructor
   * 
   * @param id - unique id
   * @param streetAddress - street address
   * @param city - city
   * @param state - state
   * @param zip - zip code
   * @param type - address type
   */
  public Address(Long id, String streetAddress, String city, String state, String zip,
      String type) {
    super();
    this.id = id;
    this.streetAddress = streetAddress;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.type = type;
  }

  /**
   * Constructor
   * 
   * @param address The domain object to construct this object from
   * @param lastUpdatedId the id of the last person to update this object
   * @param createUserId the id of the person created the record
   */
  public Address(gov.ca.cwds.rest.api.domain.Address address, String lastUpdatedId,
      String createUserId) {
    super(lastUpdatedId, createUserId);
    this.streetAddress = address.getStreetAddress();
    this.city = address.getCity();
    this.state = address.getState() != null ? address.getState().toString() : null;
    this.zip = address.getZip();
    this.type = address.getType() != null ? address.getType().toString() : null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.data.persistence.PersistentObject#getPrimaryKey()
   */
  @Override
  public Long getPrimaryKey() {
    return getId();
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the streetAddress
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * @return the address type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the personAddress
   */
  public Set<PersonAddress> getPersonAddress() {
    return personAddress;
  }

}
