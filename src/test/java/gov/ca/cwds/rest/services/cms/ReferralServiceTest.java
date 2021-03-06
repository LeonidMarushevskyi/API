package gov.ca.cwds.rest.services.cms;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.cms.ReferralDao;
import gov.ca.cwds.data.cms.StaffPersonDao;
import gov.ca.cwds.data.cms.TestSystemCodeCache;
import gov.ca.cwds.data.persistence.cms.LongText;
import gov.ca.cwds.data.persistence.cms.StaffPerson;
import gov.ca.cwds.data.rules.TriggerTablesDao;
import gov.ca.cwds.fixture.AddressResourceBuilder;
import gov.ca.cwds.fixture.LongTextEntityBuilder;
import gov.ca.cwds.fixture.ReferralEntityBuilder;
import gov.ca.cwds.fixture.ReferralResourceBuilder;
import gov.ca.cwds.fixture.ScreeningToReferralResourceBuilder;
import gov.ca.cwds.fixture.StaffPersonEntityBuilder;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.Address;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import gov.ca.cwds.rest.api.domain.cms.PostedDrmsDocument;
import gov.ca.cwds.rest.api.domain.cms.PostedLongText;
import gov.ca.cwds.rest.api.domain.cms.PostedReferral;
import gov.ca.cwds.rest.api.domain.cms.Referral;
import gov.ca.cwds.rest.business.rules.LACountyTrigger;
import gov.ca.cwds.rest.business.rules.NonLACountyTriggers;
import gov.ca.cwds.rest.filters.TestingRequestExecutionContext;
import gov.ca.cwds.rest.messages.MessageBuilder;
import gov.ca.cwds.rest.services.ServiceException;
import gov.ca.cwds.rest.services.referentialintegrity.RIReferral;
import io.dropwizard.jackson.Jackson;

/**
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public class ReferralServiceTest {

  private AssignmentService assignmentService;
  private DrmsDocumentService drmsDocumentService;
  private OtherCaseReferralDrmsDocumentService otherCaseReferralDrmsDocumentService;
  private AddressService addressService;
  private LongTextService longTextService;
  private ReferralService referralService;
  private ReferralDao referralDao;
  private NonLACountyTriggers nonLACountyTriggers;
  private LACountyTrigger laCountyTrigger;
  private TriggerTablesDao triggerTablesDao;
  private StaffPersonDao staffpersonDao;
  private Validator validator;
  private RIReferral riReferral;

  private MessageBuilder mockMessageBuilder;
  private String dateStarted;
  private String timeStarted;
  private StaffPerson staffPerson;
  private Referral referralDomain;

  private static Boolean isLaCountyTrigger = false;

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * Initialize system code cache
   */
  TestSystemCodeCache testSystemCodeCache = new TestSystemCodeCache();

  @Before
  public void setup() throws Exception {
    new TestingRequestExecutionContext("0X5");
    validator = Validation.buildDefaultValidatorFactory().getValidator();
    referralDao = mock(ReferralDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    laCountyTrigger = mock(LACountyTrigger.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    assignmentService = mock(AssignmentService.class);
    drmsDocumentService = mock(DrmsDocumentService.class);
    otherCaseReferralDrmsDocumentService = mock(OtherCaseReferralDrmsDocumentService.class);
    addressService = mock(AddressService.class);
    longTextService = mock(LongTextService.class);
    staffPerson = new StaffPersonEntityBuilder().setId("q1p").setCountyCode("51").build();
    when(staffpersonDao.find(any(String.class))).thenReturn(staffPerson);
    when(triggerTablesDao.getLaCountySpecificCode()).thenReturn("21");

    riReferral = mock(RIReferral.class);
    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    referralDomain = new ReferralResourceBuilder().setPrimaryContactStaffPersonId("q1p")
        .setCountySpecificCode("51").build();

    mockMessageBuilder = mock(MessageBuilder.class);
    dateStarted = "2009-12-20";
    timeStarted = "07:00:00";
  }

  // find test
  @Test
  public void testFindReturnsCorrectEntity() throws Exception {
    Referral expected = MAPPER
        .readValue(fixture("fixtures/domain/legacy/Referral/valid/valid.json"), Referral.class);

    // #145948067: the Referral domain bean doesn't store the primary key???
    final String key = "1234567ABC";

    gov.ca.cwds.data.persistence.cms.Referral referral =
        new gov.ca.cwds.data.persistence.cms.Referral(key, expected, "0XA");

    when(referralDao.find(key)).thenReturn(referral);
    Referral found = referralService.find(key);
    assertThat(found, is(expected));
  }

  @Test
  public void testFindReturnsNullWhenNotFound() throws Exception {
    Response found = referralService.find("ABC1234567");
    assertThat(found, is(nullValue()));
  }

  // delete test
  @Test
  public void testDeleteDelegatesToCrudsService() {
    referralService.delete("ABC2345678");
    verify(referralDao, times(1)).delete("ABC2345678");
  }


  @Test
  public void testDeleteReturnsNullWhenNotFound() throws Exception {
    Response found = referralService.delete("ABC1234567");
    assertThat(found, is(nullValue()));
  }


  @Test
  public void testDeleteReturnsClass() throws Exception {
    // delete success
    Referral referralDomain = new ReferralResourceBuilder().build();

    gov.ca.cwds.data.persistence.cms.Referral referral =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "ABC");

    when(referralDao.delete("ABC1234567")).thenReturn(referral);

    Referral returned = referralService.delete("ABC1234567");

    assertThat(returned.getClass(), is(Referral.class));

  }

  // update test
  @Test
  public void testUpdateReturnsDomain() throws Exception {
    Referral expected = new ReferralResourceBuilder().build();

    gov.ca.cwds.data.persistence.cms.Referral referral =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", expected, "ABC");

    when(referralDao.update(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(referral);

    Object retval = referralService.update("1234567ABC", expected);
    assertThat(retval.getClass(), is(Referral.class));
  }

  @Test
  public void testUpdateThrowsServiceException() throws Exception {

    try {
      Referral referralRequest = new ReferralResourceBuilder().build();

      when(referralDao.update(any())).thenThrow(EntityNotFoundException.class);

      referralService.update("ZZZZZZZ0X5", referralRequest);

      Assert.fail("Expected EntityNotFoundException was not thrown");

    } catch (Exception ex) {
      assertEquals(ex.getClass(), ServiceException.class);
    }
  }

  // create test
  @Test
  public void referralServiceCreateThrowsEntityExistsException() throws Exception {
    try {
      Referral referralRequest = new ReferralResourceBuilder().build();

      when(referralDao.create(any())).thenThrow(EntityExistsException.class);

      referralService.create(referralRequest);
    } catch (Exception e) {
      assertEquals(e.getClass(), ServiceException.class);
    }
  }

  @Test
  public void testCreateReturnsPostedClass() throws Exception {
    Referral referralDomain = new ReferralResourceBuilder().build();
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "0XA");

    Referral request = MAPPER.readValue(fixture("fixtures/domain/legacy/Referral/valid/valid.json"),
        Referral.class);

    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(toCreate);

    Response response = referralService.create(request);
    assertThat(response.getClass(), is(PostedReferral.class));
  }

  @Test
  public void testCreateReturnsCorrectEntity() throws Exception {
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "0XA");

    Referral request = new Referral(toCreate);
    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(toCreate);

    PostedReferral expected = new PostedReferral(toCreate);
    PostedReferral returned = referralService.create(request);
    assertThat(returned, is(expected));
  }

  @Test
  public void testCreateEmptyIDError() throws Exception {
    try {
      gov.ca.cwds.data.persistence.cms.Referral toCreate =
          new gov.ca.cwds.data.persistence.cms.Referral("", referralDomain, "0XA");

      Referral request = new Referral(toCreate);
      when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
          .thenReturn(toCreate);

      @SuppressWarnings("unused")
      PostedReferral returned = referralService.create(request);

    } catch (ServiceException e) {
      assertEquals("Referral ID cannot be empty", e.getMessage());
    }
  }

  @Test
  public void testCreateNullIDError() throws Exception {
    try {
      Referral referralDomain = new ReferralResourceBuilder().build();
      gov.ca.cwds.data.persistence.cms.Referral toCreate =
          new gov.ca.cwds.data.persistence.cms.Referral(null, referralDomain, "0XA");

      when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
          .thenReturn(toCreate);

      @SuppressWarnings("unused")
      PostedReferral expected = new PostedReferral(toCreate);

    } catch (ServiceException e) {
      assertEquals("Referral ID cannot be empty", e.getMessage());
    }
  }

  @Test
  public void testCreateBlankIDError() throws Exception {
    try {
      Referral referralDomain = new ReferralResourceBuilder().build();
      gov.ca.cwds.data.persistence.cms.Referral toCreate =
          new gov.ca.cwds.data.persistence.cms.Referral("   ", referralDomain, "0XA");

      when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
          .thenReturn(toCreate);

      @SuppressWarnings("unused")
      PostedReferral expected = new PostedReferral(toCreate);

    } catch (ServiceException e) {
      assertEquals("Referral ID cannot be empty", e.getMessage());
    }
  }

  @Test
  public void testCreateReturnsCorrectReferralId() throws Exception {
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "0XA");

    Referral request = new Referral(toCreate);

    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(toCreate);

    PostedReferral returned = referralService.create(request);

    assertThat(returned.getId(), is("1234567ABC"));
  }

  /*
   * Test for checking the new Referral Id generated and lenght is 10
   */
  @Test
  public void createReturnsGeneratedId() throws Exception {
    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenAnswer(new Answer<gov.ca.cwds.data.persistence.cms.Referral>() {

          @Override
          public gov.ca.cwds.data.persistence.cms.Referral answer(InvocationOnMock invocation)
              throws Throwable {
            gov.ca.cwds.data.persistence.cms.Referral report =
                (gov.ca.cwds.data.persistence.cms.Referral) invocation.getArguments()[0];
            return report;
          }
        });

    PostedReferral returned = referralService.create(referralDomain);
    assertEquals(returned.getId().length(), 10);
    PostedReferral newReturned = referralService.create(referralDomain);
    Assert.assertNotEquals(returned.getId(), newReturned.getId());
  }

  @Test
  public void testCreateLACountyTriggerForReferralCreate() throws Exception {
    Referral referralDomain = new ReferralResourceBuilder().setPrimaryContactStaffPersonId("BTr")
        .setCountySpecificCode("19").build();
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "BTr");

    Referral request = new Referral(toCreate);

    when(triggerTablesDao.getLaCountySpecificCode()).thenReturn("19");

    StaffPerson staffPerson = new StaffPerson("BTr", null, "External Interface",
        "external interface", "SCXCIN7", " ", "", BigDecimal.valueOf(9165672100L), 0, null, "    ",
        "N", "MIZN02k00E", "  ", "    ", "19", "N", "3XPCP92q38", null);

    when(staffpersonDao.find(any(String.class))).thenReturn(staffPerson);
    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(toCreate);

    when(laCountyTrigger.createCountyTrigger(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenAnswer(new Answer<Boolean>() {


          @Override
          public Boolean answer(InvocationOnMock invocation) throws Throwable {
            isLaCountyTrigger = true;
            return true;
          }
        });

    referralService.create(request);
    assertThat(isLaCountyTrigger, is(true));

  }

  @Test
  public void testCreateLACountyTriggerForReferralUpdate() throws Exception {
    Referral referralDomain = new ReferralResourceBuilder().build();

    gov.ca.cwds.data.persistence.cms.Referral toUpdate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "BTr");

    Referral request = new Referral(toUpdate);

    when(triggerTablesDao.getLaCountySpecificCode()).thenReturn("19");

    StaffPerson staffPerson = new StaffPerson("BTr", null, "External Interface",
        "external interface", "SCXCIN7", " ", "", BigDecimal.valueOf(9165672100L), 0, null, "    ",
        "N", "MIZN02k00E", "  ", "    ", "19", "N", "3XPCP92q38", null);

    when(staffpersonDao.find(any(String.class))).thenReturn(staffPerson);
    when(referralDao.update(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(toUpdate);

    when(laCountyTrigger.createCountyTrigger(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenAnswer(new Answer<Boolean>() {


          @Override
          public Boolean answer(InvocationOnMock invocation) throws Throwable {
            isLaCountyTrigger = true;
            return true;
          }
        });

    referralService.update("1234567ABC", request);
    assertThat(isLaCountyTrigger, is(true));

  }

  @Test
  public void shouldSetApprovalCodeToNotSubmittedDefaultValue() throws Exception {
    Address address = new AddressResourceBuilder().createAddress();
    ScreeningToReferral screeningToReferral =
        new ScreeningToReferralResourceBuilder().setAddress(address).createScreeningToReferral();

    PostedDrmsDocument drmsDocument = mock(PostedDrmsDocument.class);
    DrmsDocumentService drmsDocumentService = mock(DrmsDocumentService.class);
    when(drmsDocumentService.create(any())).thenReturn(drmsDocument);

    addressService = mock(AddressService.class);
    when(addressService.createAddressFromScreening(eq(screeningToReferral), any()))
        .thenReturn(address);

    longTextService = mock(LongTextService.class);
    LongText longTextEntity = new LongTextEntityBuilder().setId("1234567890").build();
    PostedLongText postedLongText = new PostedLongText(longTextEntity);
    when(longTextService.create(any())).thenReturn(postedLongText);

    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    Referral referralCreated = referralService.createReferralWithDefaults(screeningToReferral,
        dateStarted, timeStarted, mockMessageBuilder);

    assertEquals("Expected the approval status type to be 118", (short) 118,
        (short) referralCreated.getApprovalStatusType());
  }

  @Test
  public void shouldReturnReferralIdWhenReferralHasExistingReferralID() {
    String referralId = "1234567890";
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setReferralId(referralId).createScreeningToReferral();

    gov.ca.cwds.data.persistence.cms.Referral savedReferral = new ReferralEntityBuilder().build();
    when(referralDao.find(referralId)).thenReturn(savedReferral);
    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    MessageBuilder messageBuilder = new MessageBuilder();
    String response = referralService.createCmsReferralFromScreening(screeningToReferral,
        dateStarted, timeStarted, messageBuilder);

    assertEquals("Expected the reponse to be the referral ID", referralId, response);
    assertEquals("Expected there not to be any error messages", 0,
        messageBuilder.getMessages().size());
  }

  @Test
  public void shouldFailWhenSpecifyingALegacyReferralIdThatDoesNotExist() throws Exception {
    String nonExistingReferralId = "9999999999";
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setReferralId(nonExistingReferralId).createScreeningToReferral();

    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    MessageBuilder messageBuilder = mock(MessageBuilder.class);
    referralService.createCmsReferralFromScreening(screeningToReferral, dateStarted, timeStarted,
        mockMessageBuilder);

    verify(mockMessageBuilder).addMessageAndLog(
        eq("Legacy Id does not correspond to an existing CMS/CWS Referral"), any(), any());
  }

  @Test
  public void shouldFailSavingWhenReferralHasInvalidDateTimeFormat() throws Exception {
    ScreeningToReferral screeningToReferral = new ScreeningToReferralResourceBuilder()
        .setassigneeStaffId("q1p").setIncidentCounty("51").createScreeningToReferral();

    PostedDrmsDocument drmsDocument = mock(PostedDrmsDocument.class);
    DrmsDocumentService drmsDocumentService = mock(DrmsDocumentService.class);
    when(drmsDocumentService.create(any())).thenReturn(drmsDocument);

    Address address = new AddressResourceBuilder().createAddress();
    addressService = mock(AddressService.class);
    when(addressService.createAddressFromScreening(eq(screeningToReferral), any()))
        .thenReturn(address);

    longTextService = mock(LongTextService.class);
    LongText longTextEntity = new LongTextEntityBuilder().setId("1234567890").build();
    PostedLongText postedLongText = new PostedLongText(longTextEntity);
    when(longTextService.create(any())).thenReturn(postedLongText);

    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    gov.ca.cwds.data.persistence.cms.Referral savedReferral =
        new ReferralEntityBuilder().setId("1234567890").setPrimaryContactStaffPersonId("q1p")
            .setCountySpecificCode("51").build();
    when(referralDao.create(any())).thenReturn(savedReferral);

    MessageBuilder messageBuilder = new MessageBuilder();

    referralService.createCmsReferralFromScreening(screeningToReferral, "01-30-2019", timeStarted,
        messageBuilder);

    assertEquals("Expected a single error message", 1, messageBuilder.getMessages().size());
    assertEquals("ExpectedDate Error message", "receivedDate must be in the format of yyyy-MM-dd",
        messageBuilder.getMessages().get(0).getMessage());

  }

  @Test
  public void testAllegesAbuseOccurredAtAddressId() throws Exception {
    String legacyId = "1234567890";
    Address address = new AddressResourceBuilder().setLegacyId(legacyId).createAddress();
    ScreeningToReferral screeningToReferral =
        new ScreeningToReferralResourceBuilder().setassigneeStaffId("q1p").setIncidentCounty("51")
            .setAddress(address).createScreeningToReferral();

    PostedDrmsDocument drmsDocument = mock(PostedDrmsDocument.class);
    DrmsDocumentService drmsDocumentService = mock(DrmsDocumentService.class);
    when(drmsDocumentService.create(any())).thenReturn(drmsDocument);

    addressService = mock(AddressService.class);
    when(addressService.createAddressFromScreening(eq(screeningToReferral), any()))
        .thenReturn(address);

    longTextService = mock(LongTextService.class);
    LongText longTextEntity = new LongTextEntityBuilder().setId("1234567890").build();
    PostedLongText postedLongText = new PostedLongText(longTextEntity);
    when(longTextService.create(any())).thenReturn(postedLongText);

    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    gov.ca.cwds.data.persistence.cms.Referral savedReferral =
        new ReferralEntityBuilder().setId("1234567890").setPrimaryContactStaffPersonId("q1p")
            .setCountySpecificCode("51").build();
    when(referralDao.create(any())).thenReturn(savedReferral);

    referralService.createCmsReferralFromScreening(screeningToReferral, dateStarted, timeStarted,
        mockMessageBuilder);

    ArgumentCaptor<gov.ca.cwds.data.persistence.cms.Referral> referralCaptor =
        ArgumentCaptor.forClass(gov.ca.cwds.data.persistence.cms.Referral.class);
    verify(referralDao).create(referralCaptor.capture());
    gov.ca.cwds.data.persistence.cms.Referral capturedReferral = referralCaptor.getValue();
    assertEquals("Expected referal to have been save allegeges Abuse Occured At to be the Address "
        + "legacy id", capturedReferral.getAllegesAbuseOccurredAtAddressId(), legacyId);
  }


  @Test(expected = ServiceException.class)
  public void shouldThrowErrorIfStaffPersonIdIsNotFound() throws Exception {
    Referral referralDomain = new ReferralResourceBuilder().build();
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "0XA");

    Referral request = new Referral(toCreate);

    referralService.create(request);

  }

  @Test(expected = ServiceException.class)
  public void shouldThrowErrorIfReferralIsNotSavedSuccessfully() throws Exception {
    when(referralDao.create(any())).thenReturn(null);
    Referral referralDomain = new ReferralResourceBuilder().build();
    gov.ca.cwds.data.persistence.cms.Referral toCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("1234567ABC", referralDomain, "0XA");

    Referral request = new Referral(toCreate);

    referralService.create(request);

  }

}
