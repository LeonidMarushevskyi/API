package gov.ca.cwds.fixture.hoi;

import org.joda.time.DateTime;

import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.hoi.HOIReporter;
import gov.ca.cwds.rest.api.domain.hoi.HOIReporter.Role;

/**
 * 
 * @author CWDS API Team
 */
public class HOIReporterResourceBuilder {

  private String id = "reporterabc";
  private String firstName = "Reporter1";
  private String lastName = "Dino";
  private LegacyDescriptor legacyDescriptor;
  private Role role;

  public HOIReporterResourceBuilder setId(String id) {
    this.id = id;
    return this;
  }


  public HOIReporterResourceBuilder setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }


  public HOIReporterResourceBuilder setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public HOIReporterResourceBuilder setLegacyDescriptor(LegacyDescriptor legacyDescriptor) {
    this.legacyDescriptor = legacyDescriptor;
    return this;
  }

  public HOIReporterResourceBuilder setRole(Role role) {
    this.role = role;
    return this;
  }


  public HOIReporterResourceBuilder() {

    legacyDescriptor = new LegacyDescriptor("reporterabc", "reporterabc-hohj-jkj", new DateTime(),
        LegacyTable.REPORTER.getName(), LegacyTable.REPORTER.getDescription());
    role = Role.ANONYMOUS_REPORTER;
  }


  /**
   * @return the HOIReporter
   */
  public gov.ca.cwds.rest.api.domain.hoi.HOIReporter createHOIReporter() {
    return new HOIReporter(role, id, firstName, lastName, legacyDescriptor);
  }
}
