package gov.ca.cwds.rest.api.domain.cms;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.resources.cms.JerseyGuiceRule;
import gov.ca.cwds.rest.resources.cms.ReporterResource;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public class AddressTest {


  private static final ReporterResource mockedReporterResource = mock(ReporterResource.class);

  @After
  public void ensureServiceLocatorPopulated() {
    JerseyGuiceUtils.reset();
  }

  @ClassRule
  public static JerseyGuiceRule rule = new JerseyGuiceRule();

  @ClassRule
  public static final ResourceTestRule resources =
      ResourceTestRule.builder().addResource(mockedReporterResource).build();

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
  private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

  private String id = "0123456ABC";
  private String cityName = "Sacramento";
  private String description = "test CWS address";
  private int emergencyPhoneExtension = 1234;
  private BigDecimal emergencyPhoneNumber = new BigDecimal(9876543);
  private Boolean foreignAddressIndicator = false;
  private Short governmentEntityType = 99;
  private int messagePhoneExtension = 1234;
  private BigDecimal messagePhoneNumber = new BigDecimal(9876543);
  private String otherHeaderAddress = "";
  private String postDirectionTextCode = "";
  private String preDirectionTextCode = "";
  private int primaryPhoneNumberExtension = 4321;
  private BigDecimal primaryPhoneNumber = new BigDecimal(8765432);
  private Short stateCodeType = 99;
  private String streetName = "First Street";
  private String streetNumber = "1234";
  private Short streetSuffixType = 0;
  private Short unitDesignatorType = 0;
  private String unitNumber = "";
  private int zip = 95666;
  private Short zipSuffix = 1234;

  @Before
  public void setup() throws Exception {}

  /*
   * Constructor Tests
   */
  @Test
  public void testDomainConstructorSuccess() throws Exception {
    Address address = new Address(id, cityName, emergencyPhoneNumber, emergencyPhoneExtension,
        foreignAddressIndicator, governmentEntityType, messagePhoneNumber, messagePhoneExtension,
        otherHeaderAddress, primaryPhoneNumber, primaryPhoneNumberExtension, stateCodeType,
        streetName, streetNumber, zip, description, zipSuffix, postDirectionTextCode,
        preDirectionTextCode, streetSuffixType, unitDesignatorType, unitNumber);

    assertThat(address.getCity(), is(equalTo(cityName)));
    assertThat(address.getPrimaryNumber(), is(equalTo(primaryPhoneNumber)));
    assertThat(address.getEmergencyExtension(), is(equalTo(emergencyPhoneExtension)));
    assertThat(address.getEmergencyNumber(), is(equalTo(emergencyPhoneNumber)));
    assertThat(address.getFrgAdrtB(), is(equalTo(foreignAddressIndicator)));
    assertThat(address.getGovernmentEntityCd(), is(equalTo(governmentEntityType)));
    assertThat(address.getMessageNumber(), is(equalTo(messagePhoneNumber)));
    assertThat(address.getMessageExtension(), is(equalTo(messagePhoneExtension)));
    assertThat(address.getHeaderAddress(), is(equalTo(otherHeaderAddress)));
    assertThat(address.getPrimaryNumber(), is(equalTo(primaryPhoneNumber)));
    assertThat(address.getPrimaryExtension(), is(equalTo(primaryPhoneNumberExtension)));
    assertThat(address.getState(), is(equalTo(stateCodeType)));
    assertThat(address.getStreetName(), is(equalTo(streetName)));
    assertThat(address.getStreetNumber(), is(equalTo(streetNumber)));
    assertThat(address.getZip(), is(equalTo(zip)));
    assertThat(address.getAddressDescription(), is(equalTo(description)));
    assertThat(address.getZip4(), is(equalTo(zipSuffix)));
    assertThat(address.getPostDirCd(), is(equalTo(postDirectionTextCode)));
    assertThat(address.getPreDirCd(), is(equalTo(preDirectionTextCode)));
    assertThat(address.getStreetSuffixCd(), is(equalTo(streetSuffixType)));
    assertThat(address.getUnitDesignationCd(), is(equalTo(unitDesignatorType)));
    assertThat(address.getUnitNumber(), is(equalTo(unitNumber)));
  }

  @Test
  public void testPersistentConstructorSuccess() throws Exception {
    Address domain = validAddress();

    gov.ca.cwds.data.persistence.cms.Address pa =
        new gov.ca.cwds.data.persistence.cms.Address(id, domain, "0X5");

    Address da = new Address(pa, false);

    assertThat(pa.getAddressDescription(), is(equalTo(da.getAddressDescription())));
    assertThat(pa.getCity(), is(equalTo(da.getCity())));
    assertThat(pa.getPrimaryNumber(), is(equalTo(da.getPrimaryNumber())));
    assertThat(pa.getEmergencyExtension(), is(equalTo(da.getEmergencyExtension())));
    assertThat(pa.getEmergencyNumber(), is(equalTo(da.getEmergencyNumber())));
    assertThat(pa.getFrgAdrtB(), is(equalTo(DomainChef.cookBoolean(da.getFrgAdrtB()))));
    assertThat(pa.getGovernmentEntityCd(), is(equalTo(da.getGovernmentEntityCd())));
    assertThat(pa.getMessageNumber(), is(equalTo(da.getMessageNumber())));
    assertThat(pa.getHeaderAddress(), is(equalTo(da.getHeaderAddress())));
    assertThat(pa.getMessageExtension(), is(equalTo(da.getMessageExtension())));
    assertThat(pa.getPrimaryNumber(), is(equalTo(da.getPrimaryNumber())));
    assertThat(pa.getPrimaryExtension(), is(equalTo(da.getPrimaryExtension())));
    assertThat(pa.getStateCd(), is(equalTo(da.getState())));
    assertThat(pa.getStreetName(), is(equalTo(da.getStreetName())));
    assertThat(pa.getStreetNumber(), is(equalTo(da.getStreetNumber())));
    assertThat(pa.getZip(), is(equalTo(da.getZip().toString())));
    assertThat(pa.getAddressDescription(), is(equalTo(da.getAddressDescription())));
    assertThat(pa.getZip4(), is(equalTo(da.getZip4())));
    assertThat(pa.getPostDirCd(), is(equalTo(da.getPostDirCd())));
    assertThat(pa.getPreDirCd(), is(equalTo(da.getPreDirCd())));
    assertThat(pa.getStreetSuffixCd(), is(equalTo(da.getStreetSuffixCd())));
    assertThat(pa.getUnitDesignationCd(), is(equalTo(da.getUnitDesignationCd())));
    assertThat(pa.getUnitNumber(), is(equalTo(da.getUnitNumber())));
  }

  @Test
  public void serializesToJSON() throws Exception {
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    final String expected = MAPPER.writeValueAsString(MAPPER.readValue(
        fixture("fixtures/domain/legacy/Address/valid/validAddress.json"), Address.class));
    System.out.println(expected);
    assertThat(MAPPER.writeValueAsString(validAddress()), is(equalTo(expected)));
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    assertThat(MAPPER.readValue(fixture("fixtures/domain/legacy/Address/valid/validAddress.json"),
        Address.class), is(equalTo(validAddress())));
  }

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(Address.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  /*
   * Utilities
   */
  private Address validAddress() throws JsonParseException, JsonMappingException, IOException {

    Address validAddress = MAPPER.readValue(
        fixture("fixtures/domain/legacy/Address/valid/validAddress.json"), Address.class);

    return validAddress;

  }
}