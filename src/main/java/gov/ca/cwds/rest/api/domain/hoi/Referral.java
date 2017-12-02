package gov.ca.cwds.rest.api.domain.hoi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import gov.ca.cwds.ObjectMapperUtils;
import gov.ca.cwds.data.ApiTypedIdentifier;
import gov.ca.cwds.data.std.ApiObjectIdentity;
import gov.ca.cwds.rest.api.domain.AccessLimitation;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.LimitedAccessType;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeDescriptor;
import gov.ca.cwds.rest.util.FerbDateUtils;

/**
 * Referral for HOI.
 * 
 * @author CWDS API Team
 */
public class Referral extends ApiObjectIdentity implements ApiTypedIdentifier<String> {

  /**
   * Default serialization version.
   */
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private String id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonProperty("start_date")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonProperty("end_date")
  private Date endDate;

  @JsonProperty("county")
  private SystemCodeDescriptor county;

  @JsonProperty("response_time")
  private SystemCodeDescriptor responseTime;

  @JsonProperty("reporter")
  private Reporter reporter;

  @JsonProperty("assigned_social_worker")
  private SocialWorker assignedSocialWorker;

  @JsonProperty("access_limitation")
  private AccessLimitation accessLimitation;

  @JsonProperty("allegations")
  private List<Allegation> allegations = new ArrayList<>();

  @JsonProperty("legacy_descriptor")
  private LegacyDescriptor legacyDescriptor;

  /**
   * No-argument constructor
   */
  public Referral() {
    // No-argument constructor
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public Date getStartDate() {
    return FerbDateUtils.freshDate(startDate);
  }

  public void setStartDate(Date startDate) {
    this.startDate = FerbDateUtils.freshDate(startDate);
  }

  public Date getEndDate() {
    return FerbDateUtils.freshDate(endDate);
  }

  public void setEndDate(Date endDate) {
    this.endDate = FerbDateUtils.freshDate(endDate);
  }

  public SystemCodeDescriptor getCounty() {
    return county;
  }

  public void setCounty(SystemCodeDescriptor county) {
    this.county = county;
  }

  public SystemCodeDescriptor getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(SystemCodeDescriptor responseTime) {
    this.responseTime = responseTime;
  }

  public Reporter getReporter() {
    return reporter;
  }

  public void setReporter(Reporter reporter) {
    this.reporter = reporter;
  }

  public SocialWorker getAssignedSocialWorker() {
    return assignedSocialWorker;
  }

  public void setAssignedSocialWorker(SocialWorker assignedSocialWorker) {
    this.assignedSocialWorker = assignedSocialWorker;
  }

  public List<Allegation> getAllegations() {
    return allegations;
  }

  public void setAllegations(List<Allegation> allegations) {
    this.allegations = allegations;
  }

  public AccessLimitation getAccessLimitation() {
    return accessLimitation;
  }

  public void setAccessLimitation(AccessLimitation accessLimitation) {
    this.accessLimitation = accessLimitation;
  }

  public LegacyDescriptor getLegacyDescriptor() {
    return legacyDescriptor;
  }

  public void setLegacyDescriptor(LegacyDescriptor legacyDescriptor) {
    this.legacyDescriptor = legacyDescriptor;
  }

  public static void main(String[] args) throws Exception {
    Referral referral = new Referral();

    AccessLimitation accessLimitation = new AccessLimitation();
    accessLimitation.setLimitedAccessCode(LimitedAccessType.SEALED);
    accessLimitation.setLimitedAccessDate(new Date());
    accessLimitation.setLimitedAccessDescription("bla bla blah");
    SystemCodeDescriptor govtEntity = new SystemCodeDescriptor();
    govtEntity.setId((short) 1101);
    govtEntity.setDescription("Sacramento");
    accessLimitation.setLimitedAccessGovernmentEntity(govtEntity);
    referral.setAccessLimitation(accessLimitation);


    Allegation allegation = new Allegation();
    allegation.setDescription("Allegation description");

    SystemCodeDescriptor aleggationDisposition = new SystemCodeDescriptor();
    aleggationDisposition.setId((short) 45);
    aleggationDisposition.setDescription("Substantiated");
    allegation.setDisposition(aleggationDisposition);

    allegation.setId("jhdgfkhaj");
    allegation.setLegacyDescriptor(new LegacyDescriptor("jhdgfkhaj", "jhdgfkhaj-hohj-jkj",
        new DateTime(), LegacyTable.ALLEGATION.getName(), LegacyTable.ALLEGATION.getDescription()));

    Victim victim = new Victim();
    victim.setFirstName("Victim First Name");
    victim.setLastName("Victim Last Name");
    victim.setId("iiiiiii");
    victim.setLegacyDescriptor(new LegacyDescriptor("iiiiiii", "iiiiiii-hohj-jkj", new DateTime(),
        LegacyTable.CLIENT.getName(), LegacyTable.CLIENT.getDescription()));
    victim.setLimitedAccessType(LimitedAccessType.SEALED);
    allegation.setVictim(victim);

    Perpetrator perpetrator = new Perpetrator();
    perpetrator.setFirstName("Perpetrator First Name");
    perpetrator.setLastName("Perpetrator Last Name");
    perpetrator.setId("pppppppp");
    perpetrator.setLegacyDescriptor(new LegacyDescriptor("pppppppp", "pppppppp-hohj-jkj",
        new DateTime(), LegacyTable.CLIENT.getName(), LegacyTable.CLIENT.getDescription()));
    perpetrator.setLimitedAccessType(LimitedAccessType.NONE);
    allegation.setPerpetrator(perpetrator);

    List<Allegation> allegations = new ArrayList<>();
    allegations.add(allegation);
    referral.setAllegations(allegations);

    SocialWorker socialWorker = new SocialWorker();
    socialWorker.setFirstName("Worker First Name");
    socialWorker.setLastName("Worker Last Name");
    socialWorker.setId("jhgguhgjh");
    socialWorker
        .setLegacyDescriptor(new LegacyDescriptor("jhgguhgjh", "jhgguhgjh-hohj-jkj", new DateTime(),
            LegacyTable.STAFF_PERSON.getName(), LegacyTable.STAFF_PERSON.getDescription()));
    referral.setAssignedSocialWorker(socialWorker);


    SystemCodeDescriptor county = new SystemCodeDescriptor();
    county.setId((short) 1101);
    county.setDescription("Sacramento");
    referral.setCounty(county);

    referral.setEndDate(new Date());
    referral.setId("jhvuify0X5");

    referral.setLegacyDescriptor(new LegacyDescriptor("jhvuify0X5", "jhgguhgjh-hohj-jkj",
        new DateTime(), LegacyTable.REFERRAL.getName(), LegacyTable.REFERRAL.getDescription()));


    Reporter reporter = new Reporter();
    reporter.setFirstName("Reporter First Name");
    reporter.setLastName("Reporter Last Name");
    reporter.setId("jhgjhgjh");
    reporter.setLegacyDescriptor(new LegacyDescriptor("jhgjhgjh", "jhgjhgjh-hohj-jkj",
        new DateTime(), LegacyTable.REPORTER.getName(), LegacyTable.REPORTER.getDescription()));
    referral.setReporter(reporter);

    SystemCodeDescriptor responseTime = new SystemCodeDescriptor();
    responseTime.setId((short) 1518);
    responseTime.setDescription("5 Day");
    referral.setResponseTime(responseTime);

    referral.setStartDate(new Date());

    String json = ObjectMapperUtils.createObjectMapper().writeValueAsString(referral);
    System.out.println(json);
  }
}