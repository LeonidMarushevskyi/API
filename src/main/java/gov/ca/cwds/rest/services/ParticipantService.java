package gov.ca.cwds.rest.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Validator;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.RaceAndEthnicity;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import gov.ca.cwds.rest.api.domain.cms.Address;
import gov.ca.cwds.rest.api.domain.cms.ChildClient;
import gov.ca.cwds.rest.api.domain.cms.Client;
import gov.ca.cwds.rest.api.domain.cms.PostedClient;
import gov.ca.cwds.rest.api.domain.cms.ReferralClient;
import gov.ca.cwds.rest.api.domain.cms.Reporter;
import gov.ca.cwds.rest.api.domain.comparator.DateTimeComparator;
import gov.ca.cwds.rest.api.domain.comparator.DateTimeComparatorInterface;
import gov.ca.cwds.rest.business.rules.R00824SetDispositionCode;
import gov.ca.cwds.rest.business.rules.R00832SetStaffPersonAddedInd;
import gov.ca.cwds.rest.business.rules.R02265ChildClientExists;
import gov.ca.cwds.rest.messages.MessageBuilder;
import gov.ca.cwds.rest.services.cms.ChildClientService;
import gov.ca.cwds.rest.services.cms.ClientAddressService;
import gov.ca.cwds.rest.services.cms.ClientScpEthnicityService;
import gov.ca.cwds.rest.services.cms.ClientService;
import gov.ca.cwds.rest.services.cms.ReferralClientService;
import gov.ca.cwds.rest.services.cms.ReporterService;
import gov.ca.cwds.rest.validation.ParticipantValidator;

/**
 * Business layer object to work on {@link Address}
 *
 * @author CWDS API Team
 */
public class ParticipantService implements CrudsService {

  private static final String ASSESMENT = "A";

  private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantService.class);

  private static final String REPORTER_TABLE_NAME = "REPTR_T";
  private static final String CLIENT_TABLE_NAME = "CLIENT_T";

  private Validator validator;

  private ClientService clientService;
  private ReferralClientService referralClientService;
  private ReporterService reporterService;
  private ChildClientService childClientService;
  private ClientAddressService clientAddressService;
  private ClientScpEthnicityService clientScpEthnicityService;

  /**
   * Constructor
   * 
   * @param clientService clientService
   * @param referralClientService referralClientService
   * @param reporterService reporterService
   * @param childClientService childClientService
   * @param clientAddressService clientAddressService
   * @param validator validator
   * @param clientScpEthnicityService clientScpEthnicityService
   */
  @Inject
  public ParticipantService(ClientService clientService,
      ReferralClientService referralClientService, ReporterService reporterService,
      ChildClientService childClientService, ClientAddressService clientAddressService,
      Validator validator, ClientScpEthnicityService clientScpEthnicityService) {
    this.validator = validator;
    this.clientService = clientService;
    this.referralClientService = referralClientService;
    this.reporterService = reporterService;
    this.childClientService = childClientService;
    this.clientAddressService = clientAddressService;
    this.clientScpEthnicityService = clientScpEthnicityService;
  }

  /**
   * {@inheritDoc}
   *
   * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.Request)
   */
  @Override
  public Response create(Request request) {
    throw new NotImplementedException("");
  }

  /**
   * @param screeningToReferral - screeningToReferral
   * @param dateStarted - dateStarted
   * @param referralId - referralId
   * @param messageBuilder - messageBuilder
   * @return the savedParticiants
   */
  public ClientParticipants saveParticipants(ScreeningToReferral screeningToReferral,
      String dateStarted, String referralId, MessageBuilder messageBuilder) {
    ClientParticipants clientParticipants = new ClientParticipants();

    Set<Participant> participants = screeningToReferral.getParticipants();
    for (Participant incomingParticipant : participants) {
      if (!ParticipantValidator.hasValidRoles(incomingParticipant)) {
        String message = " Participant contains incompatible roles ";
        messageBuilder.addMessageAndLog(message, LOGGER);
        // next participant
        continue;
      }
      String genderCode = "";
      if (!incomingParticipant.getGender().isEmpty()) {
        genderCode = incomingParticipant.getGender().toUpperCase().substring(0, 1);
      }
      Set<String> roles = new HashSet<>(incomingParticipant.getRoles());
      processReporterRole(screeningToReferral, dateStarted, referralId, messageBuilder,
          clientParticipants, incomingParticipant, genderCode, roles);
    } // next participant

    return clientParticipants;
  }

  private void processReporterRole(ScreeningToReferral screeningToReferral, String dateStarted,
      String referralId, MessageBuilder messageBuilder, ClientParticipants clientParticipants,
      Participant incomingParticipant, String genderCode, Set<String> roles) {
    /**
     * process the roles of this participant
     */
    for (String role : roles) {
      boolean isRegularReporter = ParticipantValidator.roleIsReporterType(role)
          && (!ParticipantValidator.roleIsAnonymousReporter(role)
              && !ParticipantValidator.selfReported(incomingParticipant));
      if (isRegularReporter) {
        saveRegularReporter(screeningToReferral, referralId, messageBuilder,
            incomingParticipant, role);

      } else if (!ParticipantValidator.roleIsAnyReporter(role)) {
        saveClient(screeningToReferral, dateStarted, referralId, messageBuilder,
            clientParticipants, incomingParticipant, genderCode, role);
      }
      clientParticipants.addParticipant(incomingParticipant);
    } // next role
  }

  private void saveRegularReporter(ScreeningToReferral screeningToReferral, String referralId,
      MessageBuilder messageBuilder, Participant incomingParticipant, String role) {
    saveReporter(screeningToReferral, referralId, messageBuilder, incomingParticipant, role);
  }

  private void saveClient(ScreeningToReferral screeningToReferral, String dateStarted,
      String referralId, MessageBuilder messageBuilder, ClientParticipants clientParticipants,
      Participant incomingParticipant, String genderCode, String role) {
    String clientId;

    boolean newClient = StringUtils.isBlank(incomingParticipant.getLegacyId());
    if (newClient) {
      clientId = createNewClient(screeningToReferral, dateStarted, messageBuilder,
          incomingParticipant, genderCode);
    } else {
      // legacy Id passed - check for existence in CWS/CMS - no update yet
      clientId = incomingParticipant.getLegacyId();
      updateClient(screeningToReferral, messageBuilder, incomingParticipant, clientId);
    }

    processReferralClient(screeningToReferral, referralId, messageBuilder, incomingParticipant,
        clientId);

    /*
     * determine other participant/roles attributes relating to CWS/CMS allegation
     */
    if (ParticipantValidator.roleIsVictim(role)) {
      clientParticipants.addVictimIds(incomingParticipant.getId(), clientId);
      // since this is the victim - process the ChildClient
      try {
        processChildClient(clientId, messageBuilder);
      } catch (ServiceException e) {
        String message = e.getMessage();
        messageBuilder.addMessageAndLog(message, e, LOGGER);
        // next role
      }
    }

    if (ParticipantValidator.roleIsPerpetrator(role)) {
      clientParticipants.addPerpetratorIds(incomingParticipant.getId(), clientId);
    }

    try {
      // addresses associated with a client
      processClientAddress(incomingParticipant, referralId, clientId, messageBuilder);
    } catch (ServiceException e) {
      String message = e.getMessage();
      messageBuilder.addMessageAndLog(message, e, LOGGER);
      // next role
    }
  }

  private boolean saveReporter(ScreeningToReferral screeningToReferral, String referralId,
      MessageBuilder messageBuilder, Participant incomingParticipant, String role) {
    /*
     * CMS Reporter - if role is 'mandated reporter' or 'non-mandated reporter' and not anonymous
     * reporter or self-reported
     */
    try {
      Reporter savedReporter = saveReporter(incomingParticipant, role, referralId,
          screeningToReferral.getIncidentCounty(), messageBuilder);
      incomingParticipant.setLegacyId(savedReporter.getReferralId());
      incomingParticipant.setLegacySourceTable(REPORTER_TABLE_NAME);
      incomingParticipant.getLegacyDescriptor().setLastUpdated(savedReporter.getLastUpdatedTime());
    } catch (ServiceException e) {
      String message = e.getMessage();
      messageBuilder.addMessageAndLog(message, e, LOGGER);
      // next role
      return true;
    }
    return false;
  }

  private ReferralClient processReferralClient(ScreeningToReferral screeningToReferral,
      String referralId, MessageBuilder messageBuilder, Participant incomingParticipant,
      String clientId) {
    boolean dispositionCode =
        new R00824SetDispositionCode(screeningToReferral, incomingParticipant).isValid();
    boolean staffPersonAddedIndicator =
        new R00832SetStaffPersonAddedInd(screeningToReferral).isValid();

    ReferralClient referralClient = ReferralClient.createWithDefault(
        ParticipantValidator.selfReported(incomingParticipant), staffPersonAddedIndicator,
        dispositionCode ? ASSESMENT : "", referralId, clientId,
        screeningToReferral.getIncidentCounty(), LegacyDefaultValues.DEFAULT_APPROVAL_STATUS_CODE);

    messageBuilder.addDomainValidationError(validator.validate(referralClient));

    try {
      referralClientService.create(referralClient);
    } catch (ServiceException se) {
      messageBuilder.addMessageAndLog(se.getMessage(), se, LOGGER);
    }
    return referralClient;
  }

  private boolean updateClient(ScreeningToReferral screeningToReferral,
      MessageBuilder messageBuilder, Participant incomingParticipant, String clientId) {
    Client foundClient = this.clientService.find(clientId);
    if (foundClient != null) {
      updateClient(screeningToReferral, messageBuilder, incomingParticipant, foundClient);
    } else {
      String message =
          " Legacy Id of Participant does not correspond to an existing CWS/CMS Client ";
      messageBuilder.addMessageAndLog(message, LOGGER);
      // next role
      return true;
    }
    return false;
  }

  private void updateClient(ScreeningToReferral screeningToReferral, MessageBuilder messageBuilder,
      Participant incomingParticipant, Client foundClient) {
    DateTimeComparatorInterface comparator = new DateTimeComparator();
    if (okToUpdateClient(incomingParticipant, foundClient, comparator)) {
      List<Short> allRaceCodes = getAllRaceCodes(incomingParticipant.getRaceAndEthnicity());
      Short primaryRaceCode = getPrimaryRaceCode(allRaceCodes);
      List<Short> otherRaceCodes = getOtherRaceCodes(allRaceCodes, primaryRaceCode);

      String unableToDetermineCode = incomingParticipant.getRaceAndEthnicity() != null
          ? incomingParticipant.getRaceAndEthnicity().getUnableToDetermineCode()
          : "";
      String hispanicUnableToDetermineCode = incomingParticipant.getRaceAndEthnicity() != null
          ? incomingParticipant.getRaceAndEthnicity().getHispanicUnableToDetermineCode()
          : "";
      String hispanicOriginCode = incomingParticipant.getRaceAndEthnicity() != null
          ? incomingParticipant.getRaceAndEthnicity().getHispanicOriginCode()
          : "";

      foundClient.update(incomingParticipant.getFirstName(), incomingParticipant.getMiddleName(),
          incomingParticipant.getLastName(), incomingParticipant.getNameSuffix(), primaryRaceCode,
          unableToDetermineCode, hispanicUnableToDetermineCode, hispanicOriginCode);

      update(messageBuilder, incomingParticipant, foundClient, otherRaceCodes);
    } else {
      String message =
          String.format("Unable to Update %s %s Client. Client was previously modified",
              incomingParticipant.getFirstName(), incomingParticipant.getLastName());
      messageBuilder.addMessageAndLog(message, LOGGER);
    }
  }

  private boolean okToUpdateClient(Participant incomingParticipant, Client foundClient,
      DateTimeComparatorInterface comparator) {
    return comparator.compare(incomingParticipant.getLegacyDescriptor().getLastUpdated(),
        foundClient.getLastUpdatedTime());
  }

  private void update(MessageBuilder messageBuilder, Participant incomingParticipant,
      Client foundClient, List<Short> otherRaceCodes) {
    Client savedClient = this.clientService.update(incomingParticipant.getLegacyId(), foundClient);
    clientScpEthnicityService.createOtherEthnicity(foundClient.getExistingClientId(),
        otherRaceCodes);
    if (savedClient != null) {
      incomingParticipant.getLegacyDescriptor().setLastUpdated(savedClient.getLastUpdatedTime());
    } else {
      messageBuilder.addMessageAndLog("Unable to save Client", LOGGER);
    }
  }

  private String createNewClient(ScreeningToReferral screeningToReferral, String dateStarted,
      MessageBuilder messageBuilder, Participant incomingParticipant, String genderCode) {
    String clientId;

    List<Short> allRaceCodes = getAllRaceCodes(incomingParticipant.getRaceAndEthnicity());
    Short primaryRaceCode = getPrimaryRaceCode(allRaceCodes);
    List<Short> otherRaceCodes = getOtherRaceCodes(allRaceCodes, primaryRaceCode);
    boolean childClientIndicatorVar =
        new R02265ChildClientExists(incomingParticipant, dateStarted).isValid();

    Client client = Client.createWithDefaults(incomingParticipant, dateStarted, genderCode,
        primaryRaceCode, childClientIndicatorVar);

    messageBuilder.addDomainValidationError(validator.validate(client));
    PostedClient postedClient = this.clientService.create(client);
    clientId = postedClient.getId();
    incomingParticipant.setLegacyId(clientId);
    incomingParticipant.setLegacySourceTable(CLIENT_TABLE_NAME);
    incomingParticipant.getLegacyDescriptor().setLastUpdated(postedClient.getLastUpdatedTime());
    clientScpEthnicityService.createOtherEthnicity(postedClient.getId(), otherRaceCodes);
    return clientId;
  }

  private Reporter saveReporter(Participant ip, String role, String referralId,
      String countySpecificCode, MessageBuilder messageBuilder) {
    gov.ca.cwds.rest.api.domain.Address reporterAddress = null;

    if (ip.getAddresses() != null) {
      Set<gov.ca.cwds.rest.api.domain.Address> addresses = new HashSet<>(ip.getAddresses());

      // use the first address node only
      for (gov.ca.cwds.rest.api.domain.Address address : addresses) {
        if (address == null) {
          // next address
          continue;
        }
        reporterAddress = address;
        Short zipSuffix = null; // BUG: assign this ... what?
        if (address.getZip().length() > 5) {
          zipSuffix = Short.valueOf(address.getZip().substring(5));
        }
        break;
      }
    }

    Boolean mandatedReporterIndicator = ParticipantValidator.roleIsMandatedReporter(role);
    Reporter theReporter = reporterService.find(referralId);
    if (theReporter == null) {
      Reporter reporter = Reporter.createWithDefaults(referralId, mandatedReporterIndicator,
          reporterAddress, ip, countySpecificCode);

      messageBuilder.addDomainValidationError(validator.validate(reporter));
      theReporter = reporterService.create(reporter);
    }
    return theReporter;
  }

  private ChildClient processChildClient(String clientId, MessageBuilder messageBuilder) {
    ChildClient exsistingChild = this.childClientService.find(clientId);
    if (exsistingChild == null) {
      ChildClient childClient = ChildClient.createWithDefaults(clientId);
      messageBuilder.addDomainValidationError(validator.validate(childClient));
      exsistingChild = this.childClientService.create(childClient);
    }
    return exsistingChild;
  }

  /*
   * CMS Address - create ADDRESS and CLIENT_ADDRESS for each address of the participant
   */
  private Participant processClientAddress(Participant clientParticipant, String referralId,
      String clientId, MessageBuilder messageBuilder) {

    return clientAddressService.saveClientAddress(clientParticipant, referralId, clientId,
        messageBuilder);
  }

  @Override
  public Response delete(Serializable arg0) {
    return null;
  }

  @Override
  public Response find(Serializable arg0) {
    return null;
  }

  @Override
  public Response update(Serializable arg0, Request arg1) {
    return null;
  }

  /**
   * First address in the list is primary.
   *
   * @param allRaceCodes list of incoming races
   * @return race code
   */
  private Short getPrimaryRaceCode(List<Short> allRaceCodes) {
    Short primaryRaceCode = 0;
    if (!allRaceCodes.isEmpty()) {
      primaryRaceCode = allRaceCodes.get(0);
    }
    return primaryRaceCode;
  }

  private List<Short> getOtherRaceCodes(List<Short> allRaceCodes, Short primaryRaceCode) {
    List<Short> otherRaceCodes = new ArrayList<>(allRaceCodes);
    if (!otherRaceCodes.isEmpty()) {
      otherRaceCodes.remove(primaryRaceCode);
    }
    return otherRaceCodes;
  }

  private List<Short> getAllRaceCodes(RaceAndEthnicity raceAndEthnicity) {
    List<Short> allRaceCodes = new ArrayList<>();

    if (raceAndEthnicity != null) {
      List<Short> raceCodes = raceAndEthnicity.getRaceCode();
      if (raceCodes != null) {
        allRaceCodes.addAll(raceCodes);
      }

      List<Short> hispanicCodes = raceAndEthnicity.getHispanicCode();
      if (hispanicCodes != null) {
        allRaceCodes.addAll(hispanicCodes);
      }
    }
    return allRaceCodes;
  }

}
