package gov.ca.cwds.rest.api.domain.investigation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import gov.ca.cwds.rest.api.domain.DomainChef;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SuppressWarnings("javadoc")
public class ScreeningSummaryTest {

  private String additionalInformation = "there was excessive evidence of abuse";
  private String decision = "promoteToReferral";
  private String decisionDetail = "immediate";
  private String name = "henderson screening";
  private Set<String> safetyAlerts = validSafetyAletrs();
  private Date startedAt = DomainChef.uncookStrictTimestampString("2010-10-01T15:26:42.000-0000");
  private String safetyInformation = "the animal at residence is a lion";
  private String id = "1";

  private Set<String> validSafetyAletrs() {
    Set<String> safetyAlerts = new HashSet<String>();
    safetyAlerts.add("dangerous animal on premises");
    safetyAlerts.add("firearms in home");
    safetyAlerts.add("severe mental health status");
    return safetyAlerts;
  }

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(ScreeningSummary.class).suppress(Warning.NONFINAL_FIELDS).verify();
  }

  @Test
  public void jsonCreatorConstructorTest() throws Exception {
    ScreeningSummary domain = new ScreeningSummary(id, name, decision, decisionDetail, safetyAlerts,
        safetyInformation, additionalInformation, startedAt, null);
    assertThat(domain.getId(), is(equalTo(id)));
    assertThat(domain.getName(), is(equalTo(name)));
    assertThat(domain.getDecision(), is(equalTo(decision)));
    assertThat(domain.getDecisionDetail(), is(equalTo(decisionDetail)));
    assertThat(domain.getSafetyAlerts(), is(equalTo(safetyAlerts)));
    assertThat(domain.getSafetyInformation(), is(equalTo(safetyInformation)));
    assertThat(domain.getAdditionalInformation(), is(equalTo(additionalInformation)));
    assertThat(domain.getStartedAt(), is(equalTo(startedAt)));
    assertThat(domain.getAllegations(), is(equalTo(null)));
  }

}
