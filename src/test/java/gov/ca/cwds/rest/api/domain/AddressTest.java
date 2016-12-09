package gov.ca.cwds.rest.api.domain;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.rest.resources.AddressResource;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class AddressTest {

  private String street_name = "123 Main";
  private String city = "Sacramento";
  private String state = "CA";
  private Integer zip = 95757;

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  private static final AddressResource mockedAddressResource = mock(AddressResource.class);

  @ClassRule
  public static final ResourceTestRule resources =
      ResourceTestRule.builder().addResource(mockedAddressResource).build();


  /*
   * Serialization and de-serialization
   */
  @Test
  public void serializesToJSON() throws Exception {
    String expected = MAPPER.writeValueAsString(new Address("123 Main", "Sacramento", "CA", 95757));

    String serialized = MAPPER.writeValueAsString(
        MAPPER.readValue(fixture("fixtures/domain/address/valid/valid.json"), Address.class));

    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    Address expected = new Address("123 Main", "Sacramento", "CA", 95757);
    Address serialized =
        MAPPER.readValue(fixture("fixtures/domain/address/valid/valid.json"), Address.class);
    assertThat(serialized, is(expected));
  }

  @Test
  public void equalsHashCodeWork() throws Exception {
    EqualsVerifier.forClass(Address.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  public void persistentObjectConstructorTest() throws Exception {
    Address domain = this.validAddress();

    gov.ca.cwds.rest.api.persistence.ns.Address persistent =
        new gov.ca.cwds.rest.api.persistence.ns.Address(domain, (long) 1234565);

    Address totest = new Address(persistent);
    assertThat(totest.getCity(), is(equalTo(persistent.getCity())));
    assertThat(totest.getState(), is(equalTo(persistent.getState())));
    assertThat(totest.getStreet_address(), is(equalTo(persistent.getStreetAddress())));
    assertThat(totest.getZip(), is(equalTo(persistent.getZip())));
  }

  @Test
  public void jsonCreatorConstructorTest() throws Exception {
    Address domain = new Address(street_name, city, state, zip);

    assertThat(domain.getCity(), is(equalTo(city)));
    assertThat(domain.getState(), is(equalTo(state)));
    assertThat(domain.getStreet_address(), is(equalTo(street_name)));
    assertThat(domain.getZip(), is(equalTo(zip)));
  }

  /*
   * zip test - invalid format
   */
  // NOTE : we aren't validating zip right now
  // @Test
  // public void failsWhenInvalidZip() throws Exception {
  // Person serialized =
  // MAPPER.readValue(fixture("fixtures/domain/address/invalid/zip/invalid.json"), Person.class);
  // Response response =
  // resources.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
  // .post(Entity.entity(serialized, MediaType.APPLICATION_JSON));
  // assertThat(response.getStatus(), is(equalTo(422)));
  // assertThat(response.readEntity(String.class).indexOf("must be in the format of"),
  // is(greaterThanOrEqualTo(0)));
  // }


  private Address validAddress() {

    try {
      Address validAddress =
          MAPPER.readValue(fixture("fixtures/domain/address/valid/valid.json"), Address.class);

      return validAddress;

    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }
}

