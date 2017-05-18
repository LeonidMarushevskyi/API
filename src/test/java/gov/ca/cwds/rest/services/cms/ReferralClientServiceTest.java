package gov.ca.cwds.rest.services.cms;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.cms.CountyOwnershipDao;
import gov.ca.cwds.data.cms.ReferralClientDao;
import gov.ca.cwds.data.cms.StaffPersonDao;
import gov.ca.cwds.data.persistence.cms.CountyOwnership;
import gov.ca.cwds.data.persistence.cms.StaffPerson;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.cms.ReferralClient;
import gov.ca.cwds.rest.services.ServiceException;
import io.dropwizard.jackson.Jackson;

/**
 * See story #136586059, Tech debt: exception handling in service layer.
 * 
 * @author CWDS API Team
 */
public class ReferralClientServiceTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  // Unit under test:
  private ReferralClientService referralClientService;
  private ReferralClientDao referralClientDao;
  private StaffPersonDao staffpersonDao;
  private CountyOwnershipDao countyOwnershipDao;

  private boolean created = false;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @SuppressWarnings("javadoc")
  @Before
  public void setup() throws Exception {
    referralClientDao = mock(ReferralClientDao.class);
    countyOwnershipDao = mock(CountyOwnershipDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    referralClientService =
        new ReferralClientService(referralClientDao, countyOwnershipDao, staffpersonDao);
  }

  // find test
  // TODO: Story #136701343: Tech debt: exception handling in service layer.

  @SuppressWarnings("javadoc")
  @Test
  public void findReturnsCorrectReferralClientWhenFound() throws Exception {
    ReferralClient expected = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(expected, "ABC");

    when(referralClientDao.find(referralClient.getPrimaryKey())).thenReturn(referralClient);
    ReferralClient found = referralClientService.find(referralClient.getPrimaryKey().toString());
    assertThat(found, is(expected));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void findReturnsNullWhenNotFound() throws Exception {
    ReferralClient expected = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(expected, "ABC");

    Response found = referralClientService.find(referralClient.getPrimaryKey().toString());
    assertThat(found, is(nullValue()));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void deleteDelegatesToCrudsService() throws Exception {
    ReferralClient expected = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(expected, "ABC");
    referralClientService.delete(referralClient.getPrimaryKey().toString());
    verify(referralClientDao, times(1)).delete(referralClient.getPrimaryKey());
  }

  // delete test
  @SuppressWarnings("javadoc")
  @Test
  public void deleteReturnsNullWhenNotFound() throws Exception {
    Response found = referralClientService.delete("referralId=1234567ABC,clientId=ABC1234567");
    assertThat(found, is(nullValue()));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void deleteReturnsReferralClientResponseOnSuccess() throws Exception {
    ReferralClient expected = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(expected, "ABC");

    when(referralClientDao.delete(any())).thenReturn(referralClient);
    Object retval = referralClientService.delete(referralClient.getPrimaryKey().toString());
    assertThat(retval.getClass(), is(ReferralClient.class));
  }

  // update test
  @SuppressWarnings("javadoc")
  @Test
  public void updateThrowsAssertionError() throws Exception {
    thrown.expect(AssertionError.class);
    try {
      referralClientService.update("referralId=1234567ABC,clientId=ABC1234567", null);
    } catch (AssertionError e) {
      assertEquals("Expected AssertionError", e.getMessage());
    }
  }

  @SuppressWarnings("javadoc")
  @Test
  public void updateReturnsReferralClientResponseOnSuccess() throws Exception {
    ReferralClient expected = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(expected, "ABC");

    when(referralClientDao.find(referralClient.getPrimaryKey().toString()))
        .thenReturn(referralClient);
    when(referralClientDao.update(any())).thenReturn(referralClient);
    Object retval =
        referralClientService.update(referralClient.getPrimaryKey().toString(), expected);
    assertThat(retval.getClass(), is(ReferralClient.class));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void updateReturnsCorrectReferralClientOnSuccess() throws Exception {
    ReferralClient referralClientRequest = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

    gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientRequest, "ABC");

    when(referralClientDao.find(referralClient.getPrimaryKey().toString()))
        .thenReturn(referralClient);
    when(referralClientDao.update(any())).thenReturn(referralClient);

    ReferralClient expected = new ReferralClient(referralClient);
    ReferralClient updated =
        referralClientService.update(referralClient.getPrimaryKey().toString(), expected);

    assertThat(updated, is(expected));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void updateThrowsExceptionWhenReferralClientNotFound() throws Exception {

    try {
      ReferralClient referralClientRequest = MAPPER.readValue(
          fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);

      gov.ca.cwds.data.persistence.cms.ReferralClient referralClient =
          new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientRequest, "ABC");

      when(referralClientDao.find(referralClient.getPrimaryKey().toString()))
          .thenReturn(referralClient);
      when(referralClientDao.update(any())).thenReturn(referralClient);
      referralClientService.update("referralId=ZZZZZZZABC,clientId=ABCZZZZZZZ",
          referralClientRequest);
    } catch (ServiceException e) {
      assertEquals(e.getClass(), ServiceException.class);
    }
  }

  // create test
  @SuppressWarnings("javadoc")
  @Test
  public void createThrowsAssertionError() throws Exception {
    thrown.expect(AssertionError.class);
    try {
      referralClientService.create(null);
    } catch (AssertionError e) {
      assertEquals("Expected AssertionError", e.getMessage());
    }
  }

  @SuppressWarnings("javadoc")
  @Test
  public void createReturnsPostedReferralClient() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);
    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);

    Response response = referralClientService.create(request);
    assertThat(response.getClass(), is(ReferralClient.class));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void createReturnsNonNull() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);
    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);

    ReferralClient postedReferralClient = referralClientService.create(request);
    assertThat(postedReferralClient, is(notNullValue()));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void createReturnsPostedReferralClientClass() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);
    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);

    Response response = referralClientService.create(request);
    assertThat(response.getClass(), is(ReferralClient.class));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void createReturnsCorrectPostedPerson() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);
    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);

    ReferralClient expected = new ReferralClient(toCreate);
    ReferralClient returned = referralClientService.create(request);
    assertThat(returned, is(expected));
  }

  @SuppressWarnings("javadoc")
  @Test
  public void createReturnsUpdatedCountyOwnership() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);

    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);
    StaffPerson staffPerson = new StaffPerson("q1p", null, "External Interface",
        "external interface", "SCXCIN7", " ", "", BigDecimal.valueOf(9165672100L), 0, null, "    ",
        "N", "MIZN02k00E", "  ", "    ", "99", "N", "3XPCP92q38", null);
    when(staffpersonDao.find("q1p")).thenReturn(staffPerson);
    CountyOwnership countyOwnership = new CountyOwnership();
    when(countyOwnershipDao.find(any(String.class))).thenReturn(countyOwnership);
    when(countyOwnershipDao.update(any(CountyOwnership.class)))
        .thenAnswer(new Answer<CountyOwnership>() {

          @Override
          public CountyOwnership answer(InvocationOnMock invocation) throws Throwable {
            CountyOwnership report = (CountyOwnership) invocation.getArguments()[0];
            return report;
          }
        });
    referralClientService.create(request);
    assertThat(countyOwnership.getCounty62Flag(), is("Y"));

  }

  @SuppressWarnings("javadoc")
  @Test
  public void resultsCheckCreatedCountyOwnership() throws Exception {
    ReferralClient referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/ReferralClient/valid/valid.json"), ReferralClient.class);
    gov.ca.cwds.data.persistence.cms.ReferralClient toCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(referralClientDomain, "ABC");

    ReferralClient request = new ReferralClient(toCreate);

    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(toCreate);
    StaffPerson staffPerson = new StaffPerson("q1p", null, "External Interface",
        "external interface", "SCXCIN7", " ", "", BigDecimal.valueOf(9165672100L), 0, null, "    ",
        "N", "MIZN02k00E", "  ", "    ", "99", "N", "3XPCP92q38", null);

    when(staffpersonDao.find("q1p")).thenReturn(staffPerson);

    when(countyOwnershipDao.find(any(String.class))).thenReturn(null);
    when(countyOwnershipDao.create(any(CountyOwnership.class)))
        .thenAnswer(new Answer<CountyOwnership>() {

          @Override
          public CountyOwnership answer(InvocationOnMock invocation) throws Throwable {
            created = true;
            CountyOwnership report = (CountyOwnership) invocation.getArguments()[0];
            return report;
          }
        });
    referralClientService.create(request);
    assertThat(created, is(true));

  }

}
