package gov.ca.cwds.rest.services.hoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.Dao;
import gov.ca.cwds.data.cms.CaseDao;
import gov.ca.cwds.data.cms.ClientDao;
import gov.ca.cwds.data.cms.ClientRelationshipDao;
import gov.ca.cwds.data.persistence.cms.Client;
import gov.ca.cwds.data.persistence.cms.ClientRelationship;
import gov.ca.cwds.data.persistence.cms.CmsCase;
import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.data.persistence.cms.StaffPerson;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.LimitedAccessType;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeCache;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeDescriptor;
import gov.ca.cwds.rest.api.domain.hoi.HOICase;
import gov.ca.cwds.rest.api.domain.hoi.HOICaseResponse;
import gov.ca.cwds.rest.api.domain.hoi.HOIRelatedPerson;
import gov.ca.cwds.rest.api.domain.hoi.HOIRequest;
import gov.ca.cwds.rest.api.domain.hoi.HOISocialWorker;
import gov.ca.cwds.rest.api.domain.hoi.HOIVictim;
import gov.ca.cwds.rest.resources.SimpleResourceService;

/**
 * <p>
 * This service handle request from the user to get all the cases involved for the given clientId.
 * <p>
 * 
 * @author CWDS API Team
 *
 */
public class HOICaseService extends SimpleResourceService<HOIRequest, HOICase, HOICaseResponse> {

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(HOICaseService.class);

  private CaseDao caseDao;
  private ClientDao clientDao;
  private ClientRelationshipDao clientRelationshipDao;

  /**
   * @param caseDao {@link Dao} handling {@link gov.ca.cwds.data.persistence.cms.CmsCase} objects
   * @param clientDao {@link Dao} handling {@link gov.ca.cwds.data.persistence.cms.Client} objects
   * @param clientRelationshipDao {@link Dao} handling
   *        {@link gov.ca.cwds.data.persistence.cms.ClientRelationship} objects
   */
  @Inject
  public HOICaseService(CaseDao caseDao, ClientDao clientDao,
      ClientRelationshipDao clientRelationshipDao) {
    super();
    this.caseDao = caseDao;
    this.clientDao = clientDao;
    this.clientRelationshipDao = clientRelationshipDao;
  }

  @Override
  protected HOICaseResponse handleFind(HOIRequest hoiRequest) {
    if (!hoiRequest.getClientIds().isEmpty()) {
      List<HOICase> cases = findByClientId(hoiRequest);
      Collections.sort(cases);
      return new HOICaseResponse(cases);
    }
    return emptyHoiCaseResponse();
  }

  /**
   * @param hoiRequest - hoiCaseRequest
   * @return the cases linked to multiple client
   */
  public List<HOICase> findByClientId(HOIRequest hoiRequest) {
    Set<String> clientIds = hoiRequest.getClientIds();
    return findAllCasesForAllClients(clientIds);
  }

  private HOICaseResponse emptyHoiCaseResponse() {
    HOICaseResponse hoiCaseResponse = new HOICaseResponse();
    hoiCaseResponse.setHoiCases(new ArrayList<>());
    return hoiCaseResponse;
  }

  private List<HOICase> findAllCasesForAllClients(Set<String> clientIds) {
    Set<String> allClientIds = new HashSet<>();
    for (String clientId : clientIds) {
      allClientIds.addAll(findAllRelatedClientIds(clientId));
    }
    allClientIds.addAll(clientIds);
    CmsCase[] cmscases = caseDao.findByVictimClientIds(allClientIds);
    List<HOICase> hoicases = new ArrayList<>(cmscases.length);
    for (CmsCase cmscase : cmscases) {
      HOICase hoicase = constructHOICase(cmscase);
      hoicases.add(hoicase);
    }
    return hoicases;
  }

  private HOICase constructHOICase(CmsCase cmscase) {
    HOIVictim focusChild = getFocusChild(cmscase);
    SystemCodeDescriptor county = getCounty(cmscase);
    SystemCodeDescriptor serviceComponent = getServiceComponent(cmscase);
    HOISocialWorker assignedSocialWorker = getAssignedSocialWorker(cmscase);
    List<HOIRelatedPerson> parents = getParents(cmscase);
    return new HOICaseFactory().createHOICase(cmscase, county, serviceComponent, focusChild,
        assignedSocialWorker, parents);
  }

  private List<String> findAllRelatedClientIds(String clientid) {
    List<String> clientIds = new ArrayList<>();
    clientIds.add(clientid);
    ClientRelationship[] clientRelationshipsByPrimaryClient =
        clientRelationshipDao.findByPrimaryClientId(clientid);
    for (ClientRelationship relationship : clientRelationshipsByPrimaryClient) {
      clientIds.add(relationship.getSecondaryClientId());
    }
    ClientRelationship[] clientRelationshipBySecondaryClient =
        clientRelationshipDao.findBySecondaryClientId(clientid);
    for (ClientRelationship relation : clientRelationshipBySecondaryClient) {
      clientIds.add(relation.getPrimaryClientId());
    }
    return clientIds;
  }

  private List<HOIRelatedPerson> getParents(CmsCase cmscase) {
    List<HOIRelatedPerson> parents = new ArrayList<>();
    ClientRelationship[] clientRelationshipByPrimaryClient =
        clientRelationshipDao.findByPrimaryClientId(cmscase.getFkchldClt());
    parents.addAll(findParentsByPrimaryRelationship(clientRelationshipByPrimaryClient));
    ClientRelationship[] clientRelationshipBySecondaryClient =
        clientRelationshipDao.findBySecondaryClientId(cmscase.getFkchldClt());
    parents.addAll(findParentsBySecondaryRelationship(clientRelationshipBySecondaryClient));
    return parents;
  }

  private List<HOIRelatedPerson> findParentsByPrimaryRelationship(
      ClientRelationship[] clientRelationship) {
    List<HOIRelatedPerson> parents = new ArrayList<>();
    for (ClientRelationship relation : clientRelationship) {
      Short type = relation.getClientRelationshipType();
      if (isRelationTypeParent(type)) {
        String clientId = relation.getSecondaryClientId();
        parents.add(findPersonByClientId(clientId, type));
      }
    }
    return parents;
  }

  private List<HOIRelatedPerson> findParentsBySecondaryRelationship(
      ClientRelationship[] clientRelationship) {
    List<HOIRelatedPerson> parents = new ArrayList<>();
    for (ClientRelationship relation : clientRelationship) {
      Short type = relation.getClientRelationshipType();
      if (isRelationTypeParent(type)) {
        String clientId = relation.getPrimaryClientId();
        parents.add(findPersonByClientId(clientId, type));
      }
    }
    return parents;
  }

  private HOIRelatedPerson findPersonByClientId(String clientId, Short type) {
    Client client = clientDao.find(clientId);
    SystemCodeDescriptor relationship = new SystemCodeDescriptor(type,
        SystemCodeCache.global().getSystemCodeShortDescription(type));
    return createHOIRelatedPerson(client, relationship);
  }

  private HOIRelatedPerson createHOIRelatedPerson(Client client,
      SystemCodeDescriptor relationship) {
    String clientId = client.getId();
    LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(clientId, CmsKeyIdGenerator.getUIIdentifierFromKey(clientId),
            new DateTime(client.getLastUpdatedTime()), LegacyTable.CLIENT.getName(),
            LegacyTable.CLIENT.getDescription());
    HOIRelatedPerson person = new HOIRelatedPerson();
    person.setId(clientId);
    person.setFirstName(client.getFirstName());
    person.setLastName(client.getLastName());
    person.setRelationship(relationship);
    person.setLimitedAccessType(LimitedAccessType.getByValue(client.getSensitivityIndicator()));
    person.setLegacyDescriptor(legacyDescriptor);
    return person;
  }

  private boolean isRelationTypeParent(Short type) {
    boolean firstCondition = type <= 214 && type >= 187;
    boolean secondCondition = type <= 254 && type >= 245;
    boolean thirdCondition = type <= 294 && type >= 282;
    boolean lastCondition =
        type == 272 || type == 273 || type == 5620 || type == 6360 || type == 6361;
    return firstCondition || secondCondition || thirdCondition || lastCondition;
  }

  private HOISocialWorker getAssignedSocialWorker(CmsCase cmscase) {
    StaffPerson staffPerson = cmscase.getStaffPerson();
    String staffId = staffPerson.getId();
    LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(staffId, staffId, new DateTime(staffPerson.getLastUpdatedTime()),
            LegacyTable.STAFF_PERSON.getName(), LegacyTable.STAFF_PERSON.getDescription());

    return new HOISocialWorker(staffId, staffPerson.getFirstName(), staffPerson.getLastName(),
        legacyDescriptor);
  }

  private SystemCodeDescriptor getServiceComponent(CmsCase cmscase) {
    return new SystemCodeDescriptor(cmscase.getActiveServiceComponentType(), SystemCodeCache
        .global().getSystemCodeShortDescription(cmscase.getActiveServiceComponentType()));
  }

  private SystemCodeDescriptor getCounty(CmsCase cmscase) {
    return new SystemCodeDescriptor(cmscase.getGovernmentEntityType(),
        SystemCodeCache.global().getSystemCodeShortDescription(cmscase.getGovernmentEntityType()));
  }

  private HOIVictim getFocusChild(CmsCase cmscase) {
    String focusChildId = cmscase.getFkchldClt();
    Client client = clientDao.find(focusChildId);
    String clientId = client.getId();
    LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(clientId, CmsKeyIdGenerator.getUIIdentifierFromKey(clientId),
            new DateTime(client.getLastUpdatedTime()), LegacyTable.CLIENT.getName(),
            LegacyTable.CLIENT.getDescription());
    return new HOIVictim(client.getId(), client.getCommonFirstName(), client.getCommonLastName(),
        legacyDescriptor);
  }

  @Override
  protected HOICaseResponse handleRequest(HOICase req) {
    LOGGER.info("HOICaseService handle request not implemented");
    throw new NotImplementedException("handle request not implemented");
  }

}
