package gov.ca.cwds.rest.business.rules.doctool;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.cms.AddressDao;
import gov.ca.cwds.data.cms.AllegationDao;
import gov.ca.cwds.data.cms.AllegationPerpetratorHistoryDao;
import gov.ca.cwds.data.cms.AssignmentDao;
import gov.ca.cwds.data.cms.AssignmentUnitDao;
import gov.ca.cwds.data.cms.CaseLoadDao;
import gov.ca.cwds.data.cms.ChildClientDao;
import gov.ca.cwds.data.cms.ClientAddressDao;
import gov.ca.cwds.data.cms.ClientDao;
import gov.ca.cwds.data.cms.CrossReportDao;
import gov.ca.cwds.data.cms.CwsOfficeDao;
import gov.ca.cwds.data.cms.DrmsDocumentDao;
import gov.ca.cwds.data.cms.LongTextDao;
import gov.ca.cwds.data.cms.ReferralClientDao;
import gov.ca.cwds.data.cms.ReferralDao;
import gov.ca.cwds.data.cms.ReporterDao;
import gov.ca.cwds.data.cms.SsaName3Dao;
import gov.ca.cwds.data.cms.StaffPersonDao;
import gov.ca.cwds.data.cms.TestSystemCodeCache;
import gov.ca.cwds.data.persistence.cms.CaseLoad;
import gov.ca.cwds.data.persistence.cms.StaffPerson;
import gov.ca.cwds.data.rules.TriggerTablesDao;
import gov.ca.cwds.fixture.CaseLoadEntityBuilder;
import gov.ca.cwds.fixture.StaffPersonEntityBuilder;
import gov.ca.cwds.helper.CmsIdGenerator;
import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import gov.ca.cwds.rest.api.domain.cms.Address;
import gov.ca.cwds.rest.api.domain.cms.Allegation;
import gov.ca.cwds.rest.api.domain.cms.AllegationPerpetratorHistory;
import gov.ca.cwds.rest.api.domain.cms.Assignment;
import gov.ca.cwds.rest.api.domain.cms.ChildClient;
import gov.ca.cwds.rest.api.domain.cms.Client;
import gov.ca.cwds.rest.api.domain.cms.ClientAddress;
import gov.ca.cwds.rest.api.domain.cms.CrossReport;
import gov.ca.cwds.rest.api.domain.cms.DrmsDocument;
import gov.ca.cwds.rest.api.domain.cms.LongText;
import gov.ca.cwds.rest.api.domain.cms.Referral;
import gov.ca.cwds.rest.api.domain.cms.ReferralClient;
import gov.ca.cwds.rest.api.domain.cms.Reporter;
import gov.ca.cwds.rest.business.rules.ExternalInterfaceTables;
import gov.ca.cwds.rest.business.rules.LACountyTrigger;
import gov.ca.cwds.rest.business.rules.NonLACountyTriggers;
import gov.ca.cwds.rest.business.rules.Reminders;
import gov.ca.cwds.rest.business.rules.UpperCaseTables;
import gov.ca.cwds.rest.filters.TestingRequestExecutionContext;
import gov.ca.cwds.rest.messages.MessageBuilder;
import gov.ca.cwds.rest.services.ClientParticipants;
import gov.ca.cwds.rest.services.ParticipantService;
import gov.ca.cwds.rest.services.ScreeningToReferralService;
import gov.ca.cwds.rest.services.cms.AddressService;
import gov.ca.cwds.rest.services.cms.AllegationPerpetratorHistoryService;
import gov.ca.cwds.rest.services.cms.AllegationService;
import gov.ca.cwds.rest.services.cms.AssignmentService;
import gov.ca.cwds.rest.services.cms.ChildClientService;
import gov.ca.cwds.rest.services.cms.ClientAddressService;
import gov.ca.cwds.rest.services.cms.ClientService;
import gov.ca.cwds.rest.services.cms.CrossReportService;
import gov.ca.cwds.rest.services.cms.DrmsDocumentService;
import gov.ca.cwds.rest.services.cms.GovernmentOrganizationCrossReportService;
import gov.ca.cwds.rest.services.cms.LongTextService;
import gov.ca.cwds.rest.services.cms.OtherCaseReferralDrmsDocumentService;
import gov.ca.cwds.rest.services.cms.ReferralService;
import gov.ca.cwds.rest.services.cms.ReporterService;
import gov.ca.cwds.rest.services.referentialintegrity.RIAllegation;
import gov.ca.cwds.rest.services.referentialintegrity.RIAllegationPerpetratorHistory;
import gov.ca.cwds.rest.services.referentialintegrity.RIChildClient;
import gov.ca.cwds.rest.services.referentialintegrity.RIClientAddress;
import gov.ca.cwds.rest.services.referentialintegrity.RICrossReport;
import gov.ca.cwds.rest.services.referentialintegrity.RIReferral;
import gov.ca.cwds.rest.services.referentialintegrity.RIReferralClient;
import gov.ca.cwds.rest.services.referentialintegrity.RIReporter;
import io.dropwizard.jackson.Jackson;

/**
 * 
 * @author CWDS API Team
 */
public class R04537FirstResponseDeterminedByStaffPersonIdTest {

  private ScreeningToReferralService screeningToReferralService;
  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  private ReferralService referralService;
  private ClientService clientService;
  private AllegationService allegationService;
  private AllegationPerpetratorHistoryService allegationPerpetratorHistoryService;
  private CrossReportService crossReportService;
  private ReporterService reporterService;
  private AddressService addressService;
  private ClientAddressService clientAddressService;
  private ChildClientService childClientService;
  private LongTextService longTextService;
  private DrmsDocumentService drmsDocumentService;
  private OtherCaseReferralDrmsDocumentService otherCaseReferralDrmsDocumentService;
  private AssignmentService assignmentService;
  private ParticipantService participantService;
  private RIChildClient riChildClient;
  private RIAllegationPerpetratorHistory riAllegationPerpetratorHistory;
  private RIClientAddress riClientAddress;
  private RIAllegation riAllegation;
  private RICrossReport riCrossReport;
  private RIReporter riReporter;
  private RIReferral riReferral;
  private RIReferralClient riReferralClient;
  private GovernmentOrganizationCrossReportService governmentOrganizationCrossReportService;


  private ReferralDao referralDao;
  private ClientDao clientDao;
  private ReferralClientDao referralClientDao;
  private AllegationDao allegationDao;
  private AllegationPerpetratorHistoryDao allegationPerpetratorHistoryDao;
  private CrossReportDao crossReportDao;
  private ReporterDao reporterDao;
  private AddressDao addressDao;
  private ClientAddressDao clientAddressDao;
  private ChildClientDao childClientDao;
  private LongTextDao longTextDao;
  private StaffPersonDao staffpersonDao;
  private AssignmentDao assignmentDao;
  private NonLACountyTriggers nonLACountyTriggers;
  private LACountyTrigger laCountyTrigger;
  private TriggerTablesDao triggerTablesDao;
  private DrmsDocumentDao drmsDocumentDao;
  private SsaName3Dao ssaName3Dao;
  private Reminders reminders;
  private UpperCaseTables upperCaseTables;
  private ExternalInterfaceTables externalInterfaceTables;
  private CaseLoadDao caseLoadDao;
  private AssignmentUnitDao assignmentUnitDao;
  private CwsOfficeDao cwsOfficeDao;
  private MessageBuilder messageBuilder;

  private gov.ca.cwds.data.persistence.cms.Referral referral;
  private Validator validator;

  @SuppressWarnings("javadoc")
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /*
   * Load system code cache
   */
  TestSystemCodeCache testSystemCodeCache = new TestSystemCodeCache();

  @SuppressWarnings("javadoc")
  @Before
  public void setup() throws Exception {
    new TestingRequestExecutionContext("0X5");
    validator = Validation.buildDefaultValidatorFactory().getValidator();

    referralDao = mock(ReferralDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    laCountyTrigger = mock(LACountyTrigger.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    staffpersonDao = mock(StaffPersonDao.class);

    clientDao = mock(ClientDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    ssaName3Dao = mock(SsaName3Dao.class);
    upperCaseTables = mock(UpperCaseTables.class);
    externalInterfaceTables = mock(ExternalInterfaceTables.class);
    clientService = new ClientService(clientDao, staffpersonDao, triggerTablesDao,
        nonLACountyTriggers, ssaName3Dao, upperCaseTables, externalInterfaceTables);

    referralClientDao = mock(ReferralClientDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    laCountyTrigger = mock(LACountyTrigger.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    riReferralClient = mock(RIReferralClient.class);

    allegationDao = mock(AllegationDao.class);
    riAllegation = mock(RIAllegation.class);
    allegationService = new AllegationService(allegationDao, riAllegation);

    allegationPerpetratorHistoryDao = mock(AllegationPerpetratorHistoryDao.class);
    riAllegationPerpetratorHistory = mock(RIAllegationPerpetratorHistory.class);
    allegationPerpetratorHistoryService = new AllegationPerpetratorHistoryService(
        allegationPerpetratorHistoryDao, riAllegationPerpetratorHistory);

    crossReportDao = mock(CrossReportDao.class);
    riCrossReport = mock(RICrossReport.class);
    crossReportService = new CrossReportService(crossReportDao, riCrossReport);

    reporterDao = mock(ReporterDao.class);
    riReporter = mock(RIReporter.class);
    reporterService = new ReporterService(reporterDao, riReporter);

    addressDao = mock(AddressDao.class);
    addressService = new AddressService(addressDao, ssaName3Dao, upperCaseTables, validator);

    clientAddressDao = mock(ClientAddressDao.class);
    laCountyTrigger = mock(LACountyTrigger.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    riClientAddress = mock(RIClientAddress.class);
    clientAddressService =
        new ClientAddressService(clientAddressDao, staffpersonDao, triggerTablesDao,
            laCountyTrigger, nonLACountyTriggers, riClientAddress, validator, addressService);

    longTextDao = mock(LongTextDao.class);
    longTextService = new LongTextService(longTextDao);

    drmsDocumentDao = mock(DrmsDocumentDao.class);
    drmsDocumentService = new DrmsDocumentService(drmsDocumentDao);
    otherCaseReferralDrmsDocumentService = mock(OtherCaseReferralDrmsDocumentService.class);

    childClientDao = mock(ChildClientDao.class);
    riChildClient = mock(RIChildClient.class);
    childClientService = new ChildClientService(childClientDao, riChildClient);

    assignmentDao = mock(AssignmentDao.class);
    staffpersonDao = mock(StaffPersonDao.class);
    nonLACountyTriggers = mock(NonLACountyTriggers.class);
    triggerTablesDao = mock(TriggerTablesDao.class);
    caseLoadDao = mock(CaseLoadDao.class);
    assignmentUnitDao = mock(AssignmentUnitDao.class);
    cwsOfficeDao = mock(CwsOfficeDao.class);
    messageBuilder = mock(MessageBuilder.class);

    assignmentService = new AssignmentService(assignmentDao, nonLACountyTriggers, staffpersonDao,
        triggerTablesDao, validator, externalInterfaceTables, caseLoadDao, referralDao,
        assignmentUnitDao, cwsOfficeDao, messageBuilder);

    reminders = mock(Reminders.class);
    riReferral = mock(RIReferral.class);

    participantService = mock(ParticipantService.class);
    ClientParticipants referralParticipants = new ClientParticipants();
    when(participantService.saveParticipants(any(), any(), any(), any()))
        .thenReturn(referralParticipants);

    governmentOrganizationCrossReportService = mock(GovernmentOrganizationCrossReportService.class);

    referralService = new ReferralService(referralDao, nonLACountyTriggers, laCountyTrigger,
        triggerTablesDao, staffpersonDao, assignmentService, validator, drmsDocumentService,
        otherCaseReferralDrmsDocumentService, addressService, longTextService, riReferral);

    screeningToReferralService = new ScreeningToReferralService(referralService, allegationService,
        crossReportService, participantService, validator, referralDao, new MessageBuilder(),
        allegationPerpetratorHistoryService, reminders, governmentOrganizationCrossReportService);
  }

  /**
   * <blockquote>
   * 
   * <pre>
   * BUSINESS RULE: "R - 04537" - FKSTFPERS0 set when first referral determined
   * 
   * IF    referralResponseTypeCode is set to default
   * THEN  firstResponseDeterminedByStaffPersonId is set to the staffpersonId
   *
   * </pre>
   * 
   * </blockquote>
   * 
   * @throws Exception on general error
   */
  @Test
  public void testForfirstResponseDeterminedByStaffPersonId() throws Exception {
    Referral referralDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validReferral.json"), Referral.class);
    gov.ca.cwds.data.persistence.cms.Referral referralToCreate =
        new gov.ca.cwds.data.persistence.cms.Referral("0123456ABC", referralDomain, "2016-10-31");
    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenReturn(referralToCreate);

    DrmsDocument drmsDocumentDomain = MAPPER.readValue(
        fixture("fixtures/domain/legacy/DrmsDocument/valid/valid.json"), DrmsDocument.class);
    gov.ca.cwds.data.persistence.cms.DrmsDocument drmsDocumentToCreate =
        new gov.ca.cwds.data.persistence.cms.DrmsDocument("ABD1234568", drmsDocumentDomain, "ABC",
            new Date());
    when(drmsDocumentDao.create(any(gov.ca.cwds.data.persistence.cms.DrmsDocument.class)))
        .thenReturn(drmsDocumentToCreate);

    ChildClient childClient = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/childClient.json"), ChildClient.class);
    gov.ca.cwds.data.persistence.cms.ChildClient childClientToCreate =
        new gov.ca.cwds.data.persistence.cms.ChildClient("1234567ABC", childClient, "0XA");
    when(childClientDao.create(any(gov.ca.cwds.data.persistence.cms.ChildClient.class)))
        .thenReturn(childClientToCreate);

    Set<Client> clientDomain =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validClient.json"),
            new TypeReference<Set<Client>>() {});
    gov.ca.cwds.data.persistence.cms.Client clientToCreate =
        new gov.ca.cwds.data.persistence.cms.Client("1234567ABC",
            (Client) clientDomain.toArray()[0], "2016-10-31", new Date());
    when(clientDao.create(any(gov.ca.cwds.data.persistence.cms.Client.class)))
        .thenReturn(clientToCreate);

    Set<ReferralClient> referralClientDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validReferralClient.json"),
        new TypeReference<Set<ReferralClient>>() {});
    gov.ca.cwds.data.persistence.cms.ReferralClient referralClientToCreate =
        new gov.ca.cwds.data.persistence.cms.ReferralClient(
            (ReferralClient) referralClientDomain.toArray()[0], "ABC", new Date());
    when(referralClientDao.create(any(gov.ca.cwds.data.persistence.cms.ReferralClient.class)))
        .thenReturn(referralClientToCreate);

    Set<Allegation> allegationDomain =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validAllegation.json"),
            new TypeReference<Set<Allegation>>() {});
    gov.ca.cwds.data.persistence.cms.Allegation allegationToCreate =
        new gov.ca.cwds.data.persistence.cms.Allegation("2345678ABC",
            (Allegation) allegationDomain.toArray()[0], "ABC", new Date());
    when(allegationDao.create(any(gov.ca.cwds.data.persistence.cms.Allegation.class)))
        .thenReturn(allegationToCreate);

    AllegationPerpetratorHistory allegationPerpHistoryDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validAllegationPerpetratorHistory.json"),
        AllegationPerpetratorHistory.class);
    gov.ca.cwds.data.persistence.cms.AllegationPerpetratorHistory allegationPerpHistoryToCreate =
        new gov.ca.cwds.data.persistence.cms.AllegationPerpetratorHistory("567890ABC",
            allegationPerpHistoryDomain, "2017-07-03", new Date());
    when(allegationPerpetratorHistoryDao
        .create(any(gov.ca.cwds.data.persistence.cms.AllegationPerpetratorHistory.class)))
            .thenReturn(allegationPerpHistoryToCreate);

    Set<CrossReport> crossReportDomain =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validCrossReport.json"),
            new TypeReference<Set<CrossReport>>() {});
    gov.ca.cwds.data.persistence.cms.CrossReport crossReportToCreate =
        new gov.ca.cwds.data.persistence.cms.CrossReport("3456789ABC",
            // ((CrossReport) crossReportDomain).getThirdId(),
            (CrossReport) crossReportDomain.toArray()[0], "OXA", new Date());
    when(crossReportDao.create(any(gov.ca.cwds.data.persistence.cms.CrossReport.class)))
        .thenReturn(crossReportToCreate);

    Reporter reporterDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validReporter.json"), Reporter.class);
    gov.ca.cwds.data.persistence.cms.Reporter reporterToCreate =
        new gov.ca.cwds.data.persistence.cms.Reporter(reporterDomain, "ABC", new Date());
    when(reporterDao.create(any(gov.ca.cwds.data.persistence.cms.Reporter.class)))
        .thenReturn(reporterToCreate);

    Address addressDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validAddress.json"), Address.class);
    gov.ca.cwds.data.persistence.cms.Address addressToCreate =
        new gov.ca.cwds.data.persistence.cms.Address("345678ABC", addressDomain, "ABC", new Date());
    when(addressDao.create(any(gov.ca.cwds.data.persistence.cms.Address.class)))
        .thenReturn(addressToCreate);

    ClientAddress clientAddressDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validClientAddress.json"),
        ClientAddress.class);
    gov.ca.cwds.data.persistence.cms.ClientAddress clientAddressToCreate =
        new gov.ca.cwds.data.persistence.cms.ClientAddress("456789ABC", clientAddressDomain, "ABC",
            new Date());
    when(clientAddressDao.create(any(gov.ca.cwds.data.persistence.cms.ClientAddress.class)))
        .thenReturn(clientAddressToCreate);

    LongText longTextDomain = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validLongText.json"), LongText.class);
    gov.ca.cwds.data.persistence.cms.LongText longTextToCreate =
        new gov.ca.cwds.data.persistence.cms.LongText("567890ABC", longTextDomain, "ABC");
    when(longTextDao.create(any(gov.ca.cwds.data.persistence.cms.LongText.class)))
        .thenReturn(longTextToCreate);

    Assignment assignment =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validAssignment.json"),
            Assignment.class);
    gov.ca.cwds.data.persistence.cms.Assignment assignmentToCreate =
        new gov.ca.cwds.data.persistence.cms.Assignment("6789012ABC", assignment, "ABC",
            new Date());
    when(assignmentDao.create(any(gov.ca.cwds.data.persistence.cms.Assignment.class)))
        .thenReturn(assignmentToCreate);
    CaseLoad caseLoad = new CaseLoadEntityBuilder().setId("ABC1234567").build();
    CaseLoad[] caseLoadList = new CaseLoad[1];
    caseLoadList[0] = caseLoad;
    StaffPerson staffPerson =
        new StaffPersonEntityBuilder().setId("0X5").setCountyCode("34").build();
    when(staffpersonDao.find(any(String.class))).thenReturn(staffPerson);
    when(triggerTablesDao.getLaCountySpecificCode()).thenReturn("BTr");
    when(assignmentDao.findCaseLoads(any(String.class))).thenReturn(caseLoadList);
    CaseLoad caseload = new CaseLoadEntityBuilder().build();
    when(caseLoadDao.find(any())).thenReturn(caseload);

    ScreeningToReferral screeningToReferral = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/valid.json"), ScreeningToReferral.class);

    when(referralDao.create(any(gov.ca.cwds.data.persistence.cms.Referral.class)))
        .thenAnswer(new Answer<gov.ca.cwds.data.persistence.cms.Referral>() {

          @Override
          public gov.ca.cwds.data.persistence.cms.Referral answer(InvocationOnMock invocation)
              throws Throwable {
            referral = (gov.ca.cwds.data.persistence.cms.Referral) invocation.getArguments()[0];
            return referral;
          }
        });

    ClientParticipants clientParticipants = new ClientParticipants();
    Set<Participant> participants = screeningToReferral.getParticipants();

    CmsIdGenerator generator = new CmsIdGenerator();
    for (Participant participant : participants) {
      participant.setLegacyId(generator.generate());
    }
    clientParticipants.addParticipants(participants);
    when(participantService.saveParticipants(any(), any(), any(), any()))
        .thenReturn(clientParticipants);

    screeningToReferralService.create(screeningToReferral);
    assertThat(referral.getFirstResponseDeterminedByStaffPersonId(), is(equalTo("0X5")));
  }
}
