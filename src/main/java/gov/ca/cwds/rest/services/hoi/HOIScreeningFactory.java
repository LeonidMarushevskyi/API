package gov.ca.cwds.rest.services.hoi;

import com.google.inject.Inject;
import gov.ca.cwds.data.ns.ScreeningDao;
import gov.ca.cwds.data.persistence.ns.IntakeLOVCodeEntity;
import gov.ca.cwds.data.persistence.ns.ParticipantEntity;
import gov.ca.cwds.data.persistence.ns.ScreeningEntity;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeDescriptor;
import gov.ca.cwds.rest.api.domain.hoi.HOIPerson;
import gov.ca.cwds.rest.api.domain.hoi.HOIScreening;

/**
 * @author CWDS API Team
 */
public final class HOIScreeningFactory {

  @Inject
  ScreeningDao screeningDao;

  @Inject
  HOIPersonFactory hoiPersonFactory;

  /**
   * @param screeningEntity ns ScreeningEntity
   * @return HOIScreening
   */
  public HOIScreening buildHOIScreening(ScreeningEntity screeningEntity) {
    HOIScreening result = new HOIScreening(screeningEntity);

    if (screeningEntity.getIncidentCounty() != null) {
      IntakeLOVCodeEntity code = screeningDao
          .findIntakeLOVCodeByIntakeCode(screeningEntity.getIncidentCounty());
      if (code != null) {
        result.setCounty(
            new SystemCodeDescriptor(code.getLgSysId().shortValue(), code.getIntakeDisplay()));
      }
    }

    if (screeningEntity.getParticipants() != null) {
      for (ParticipantEntity persistedParticipantEntity : screeningEntity
          .getParticipants()) {
        HOIPerson participant = hoiPersonFactory.buildHOIPerson(persistedParticipantEntity);
        result.getAllPeople().add(participant);

        if (result.getReporter() == null) {
          result.setReporter(hoiPersonFactory
              .buidHOIReporter(persistedParticipantEntity, participant.getLegacyDescriptor()));
        }
      }
    }

    if (screeningEntity.getAssigneeStaffId() != null) {
      result.setAssignedSocialWorker(
          hoiPersonFactory.buildHOISocialWorker(screeningEntity.getAssigneeStaffId()));
    }

    return result;
  }

}
