package gov.ca.cwds.rest.api.domain;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import gov.ca.cwds.data.cms.TestSystemCodeCache;
import gov.ca.cwds.fixture.ScreeningToReferralResourceBuilder;
import gov.ca.cwds.rest.core.Api;
import gov.ca.cwds.rest.resources.ScreeningToReferralResource;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

@SuppressWarnings("javadoc")
public class ScreeningToReferralTest {

  private static final String ROOT_RESOURCE = "/" + Api.RESOURCE_REFERRALS + "/";;
  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
  public static final String SACRAMENTO_COUNTY_CODE = "34";
  private Validator validator;


  private static final ScreeningToReferralResource mockedScreeningToReferralResource =
      mock(ScreeningToReferralResource.class);

  private String agencyType = "Law enforcement";
  private String agencyName = "Sacramento County Sheriff Deparment";
  private Integer method = 2095; // "electronic report";
  private String informDate = "2017-03-15";
  private Set<Participant> participants = new HashSet<Participant>();
  private Set<CrossReport> crossReports = new HashSet<CrossReport>();
  private Set<Allegation> allegations = new HashSet<Allegation>();
  private long id = 2;
  private boolean filedOutOfState = false;
  private String countyId = "1101";

  int approvalStatus = 118;
  boolean familyAwarness = false;
  boolean filedWithLawEnforcement = false;
  String responsibleAgency = "C";

  Short communicationMethod = 409;
  String currentLocationOfChildren = "current location of children";

  private TestSystemCodeCache testSystemCodeCache = new TestSystemCodeCache();

  private Short responseTime = (short) 1520;

  @ClassRule
  public static final ResourceTestRule resources =
      ResourceTestRule.builder().addResource(mockedScreeningToReferralResource).build();

  @Before
  public void setup() {
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /*
   * incident Date Tests
   */
  @Test
  public void successWhenIncidentDateEmpty() throws Exception {
    ScreeningToReferral toCreate =
        new ScreeningToReferralResourceBuilder().setIncidentDate("").createScreeningToReferral();
    Response response =
        resources.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(toCreate, MediaType.APPLICATION_JSON));
    assertThat(response.getStatus(), is(equalTo(204)));
  }

  @Test
  public void successWhenIncidentDateNull() throws Exception {
    ScreeningToReferral toCreate =
        new ScreeningToReferralResourceBuilder().setIncidentDate(null).createScreeningToReferral();
    Response response =
        resources.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(toCreate, MediaType.APPLICATION_JSON));
    assertThat(response.getStatus(), is(equalTo(204)));
  }

  /*
   * Serialization and deserialization
   */
  @Test
  public void serializesToJSON() throws Exception {

    Address address = validAddress();
    Participant participant = validParticipant();
    participants.add(participant);
    CrossReport crossReport = new CrossReport("", "", "", filedOutOfState, method, informDate,
        countyId, Sets.newHashSet());
    crossReports.add(crossReport);
    Allegation allegation = validAllegation();
    allegations.add(allegation);

    String expected = MAPPER.writeValueAsString(new ScreeningToReferral(id, "", "",
        "2016-08-03T01:00:00.000Z", SACRAMENTO_COUNTY_CODE, "2016-08-02", "Foster Home",
        communicationMethod, currentLocationOfChildren, "The Rocky Horror Show",
        "Narrative 123 test", "123ABC", responseTime, "2016-08-03T01:00:00.000Z", "Michael Bastow",
        "0X5", "addtional information", "Screening Descision", "Detail", approvalStatus,
        familyAwarness, filedWithLawEnforcement, responsibleAgency, "S", "", "23", null, address,
        participants, crossReports, allegations));

    String serialized = MAPPER.writeValueAsString(
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validstr.json"),
            ScreeningToReferral.class));
    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    Address address = validAddress();
    Participant participant = validParticipant();
    participants.add(participant);
    CrossReport crossReport = new CrossReport("", "", "", filedOutOfState, method, informDate,
        countyId, Sets.newHashSet());
    crossReports.add(crossReport);
    Allegation allegation = validAllegation();
    allegations.add(allegation);

    ScreeningToReferral expected = new ScreeningToReferral(id, "", "", "2016-08-03T01:00:00.000Z",
        SACRAMENTO_COUNTY_CODE, "2016-08-02", "Foster Home", communicationMethod,
        currentLocationOfChildren, "The Rocky Horror Show", "Narrative 123 test", "123ABC",
        responseTime, "2016-08-03T01:00:00.000Z", "Michael Bastow", "0X5", "addtional information",
        "Screening Descision", "Detail", approvalStatus, familyAwarness, filedWithLawEnforcement,
        responsibleAgency, "S", "", "23", null, address, participants, crossReports, allegations);

    ScreeningToReferral serialized =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validstr.json"),
            ScreeningToReferral.class);

    assertThat(serialized, is(expected));

  }

  @Test
  public void testWithValidSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validDomainScreeningToReferral.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithNullParticipantsFail() throws Exception {

    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/nullParticipants.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(2, constraintViolations.size());
    // assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    // String[] expectedMessages =
    // {"may not be empty", "must contain a Victim, Perpetrator, and Reporter"};
    // Iterator itr = constraintViolations.iterator();
    // String[] actualMessages = {((ConstraintViolation) itr.next()).getMessage(),
    // ((ConstraintViolation) itr.next()).getMessage()};
    // Arrays.sort(expectedMessages);
    // Arrays.sort(actualMessages);
    // assertArrayEquals(expectedMessages, actualMessages);
  }

  @Test
  public void testWithEmptyParticipantsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/emptyParticipants.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(2, constraintViolations.size());
    // assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
    // String[]actualMessages = null;
    // String[] expectedMessages = {"may not be empty"};
    // Iterator itr = constraintViolations.iterator();
    // if (itr.hasNext()){
    // actualMessages = {
    // ((ConstraintViolation) itr.next()).getMessage(),
    // ((ConstraintViolation) itr.next()).getMessage()
    // };
    // }
    // Arrays.sort(expectedMessages);
    // Arrays.sort(actualMessages);
    // assertArrayEquals(expectedMessages, actualMessages);
  }

  @Test
  public void testWithNullAllegationsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/nullAllegations.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testWithEmptyAllegationsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/emptyAllegations.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void equalsHashCodeWork() throws Exception {
    // EqualsVerifier.forClass(ScreeningToReferral.class).suppress(Warning.NONFINAL_FIELDS).verify();
    ScreeningToReferral expected = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/emptyAllegations.json"),
        ScreeningToReferral.class);
    assertThat(expected.hashCode(), is(not(0)));
  }

  @Test
  public void testWithEmptyCrossReportFail() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/emptyCrossReport.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithNullCrossReportFail() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/nullCrossReport.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithMultipleCrossReportsSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validMultipleCrossReports.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());

  }

  @Test
  public void testWithInvalidIncidentDateFormatFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/invalidIncidentDateFormat.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("must be in the format of yyyy-MM-dd",
        constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testBlankLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/blankLegacySourceTable.json"),
        ScreeningToReferral.class);

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testNullLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/nullLegacySourceTable.json"),
        ScreeningToReferral.class);

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testMissingLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/missingLegacySourceTable.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithEmptyLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/emptyReferralId.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithNullLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/nullReferralId.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithMissingLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/missingReferralId.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testLegacyIdTooLongFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/legacyIdTooLong.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("size must be between 0 and 10",
        constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testWithoutPerpetratorParticipantSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/withoutPerpetratorParticipant.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithInvalidCountySpecificCodeFails() throws Exception {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setIncidentCounty("XX").createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);

    assertEquals(1, constraintViolations.size());
    assertEquals("must be a valid logical id code for category GVR_ENTC",
        constraintViolations.iterator().next().getMessage());

  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessCodeIsNull() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessCode(null).createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access code value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessCodeIsValid() {
    List<String> accessCodes = Arrays.asList("N", "R", "S");

    for (String code : accessCodes) {
      ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
          .setLimitedAccessCode(code).createScreeningToReferral();

      Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
          validator.validate(screeningToReferral);
      String message = "Expected limited access code value of " + code + " to not be an error";
      assertEquals(message, 0, constraintViolations.size());
    }
  }

  @Test
  public void shouldHaveErrorsWhenLimitedAccessCodeIsInValid() {
    List<String> accessCodes = Arrays.asList("$", "X", "1", "Y", "n", "s", "r");

    for (String code : accessCodes) {
      ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
          .setLimitedAccessCode(code).createScreeningToReferral();

      Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
          validator.validate(screeningToReferral);
      String message = "Expected limited access code value of " + code + " to not be an error";
      assertEquals(message, 1, constraintViolations.size());
    }
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessDescriptionIsNull() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessDescription(null).createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access description value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessDescriptionIsValid() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessDescription("Limited access because ...").createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access description value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessAgencyIsNull() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessAgency(null).createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access agency value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessAgencyIsAnEmptyString() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessAgency("").createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access agency value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldNotHaveErrorsWhenLimitedAccessAgencyIsAValidCode() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessAgency("23").createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access agency to not have an error when code is correct", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveErrorsWhenLimitedAccessAgencyIsAnInValidCode() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessAgency("999").createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access agency to have an error when code is incorrect", 1,
        constraintViolations.size());
  }

  @Test
  public void shouldHaveNoErrorsWhenLimitedAccessDateIsNull() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessDate(null).createScreeningToReferral();

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(screeningToReferral);
    assertEquals("Expected limited access date value of null to not be an error", 0,
        constraintViolations.size());
  }

  @Test
  public void shouldReportAccessIsLimitedWhenCodeIsS() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessCode("S").createScreeningToReferral();
    assertTrue("Expected access to be limited", screeningToReferral.isAccessLimited());
  }

  @Test
  public void shouldReportAccessIsLimitedWhenCodeIsR() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessCode("R").createScreeningToReferral();
    assertTrue("Expected access to be limited", screeningToReferral.isAccessLimited());

  }

  @Test
  public void shouldReportAccessIsNotLimitedWhenCodeIsN() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessCode("N").createScreeningToReferral();
    assertFalse("Expected access to not be limited", screeningToReferral.isAccessLimited());

  }

  @Test
  public void shouldReportAccessIsNotLimitedWhenCodeIsNull() {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setLimitedAccessCode(null).createScreeningToReferral();
    assertFalse("Expected access to not be limited", screeningToReferral.isAccessLimited());

  }

  private ScreeningToReferral validScreeningToReferral() {
    ScreeningToReferral str = null;
    try {
      str = MAPPER.readValue(
          fixture("fixtures/domain/ScreeningToReferral/valid/validDomainScreeningToReferral.json"),
          ScreeningToReferral.class);
    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return str;
  }

  private Address validAddress() {

    try {
      Address validAddress =
          MAPPER.readValue(fixture("fixtures/domain/address/valid/valid.json"), Address.class);

      return validAddress;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Participant validParticipant() {

    try {
      Participant validParticipant = MAPPER
          .readValue(fixture("fixtures/domain/participant/valid/valid.json"), Participant.class);
      return validParticipant;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Allegation validAllegation() {

    try {
      Allegation validAllegation = MAPPER
          .readValue(fixture("fixtures/domain/Allegation/valid/valid.json"), Allegation.class);
      return validAllegation;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
