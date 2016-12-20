package gov.ca.cwds.rest.api.persistence.cms;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class SubstituteCareProviderTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(SubstituteCareProvider.class).suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(SubstituteCareProvider.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void persistentConstructorTest() throws Exception {
    SubstituteCareProvider vsucp = validSubstituteCareProvider();

    SubstituteCareProvider persistent =
        new SubstituteCareProvider(vsucp.getId(), vsucp.getAdditionalPhoneNumber(),
            vsucp.getAdditionlPhoneExtensionNumber(), vsucp.getAnnualIncomeAmount(),
            vsucp.getBirthDate(), vsucp.getCaDriverLicenseNumber(), vsucp.getCityName(),
            vsucp.getEducationType(), vsucp.getEmailAddress(), vsucp.getEmployerName(),
            vsucp.getEmploymentStatusType(), vsucp.getEthUnableToDetReasonCode(),
            vsucp.getFirstName(), vsucp.getForeignAddressIndicatorVar(), vsucp.getGenderIndicator(),
            vsucp.getHispUnableToDetReasonCode(), vsucp.getHispanicOriginCode(),
            vsucp.getIndianTribeType(), vsucp.getLastName(), vsucp.getLisOwnershipIndicator(),
            vsucp.getLisPersonId(), vsucp.getMaritalStatusType(), vsucp.getMiddleInitialName(),
            vsucp.getNamePrefixDescription(), vsucp.getPassedBackgroundCheckCode(),
            vsucp.getPrimaryIncomeType(), vsucp.getResidedOutOfStateIndicator(),
            vsucp.getSecondaryIncomeType(), vsucp.getSocialSecurityNumber(),
            vsucp.getStateCodeType(), vsucp.getStreetName(), vsucp.getStreetNumber(),
            vsucp.getSuffixTitleDescription(), vsucp.getZipNumber(), vsucp.getZipSuffixNumber());

    assertThat(persistent.getId(), is(equalTo(vsucp.getId())));
    assertThat(persistent.getAdditionalPhoneNumber(),
        is(equalTo(vsucp.getAdditionalPhoneNumber())));
    assertThat(persistent.getAdditionlPhoneExtensionNumber(),
        is(equalTo(vsucp.getAdditionlPhoneExtensionNumber())));
    assertThat(persistent.getAnnualIncomeAmount(), is(equalTo(vsucp.getAnnualIncomeAmount())));
    assertThat(persistent.getBirthDate(), is(equalTo(vsucp.getBirthDate())));
    assertThat(persistent.getCaDriverLicenseNumber(),
        is(equalTo(vsucp.getCaDriverLicenseNumber())));
    assertThat(persistent.getCityName(), is(equalTo(vsucp.getCityName())));
    assertThat(persistent.getEducationType(), is(equalTo(vsucp.getEducationType())));
    assertThat(persistent.getEmailAddress(), is(equalTo(vsucp.getEmailAddress())));
    assertThat(persistent.getEmployerName(), is(equalTo(vsucp.getEmployerName())));
    assertThat(persistent.getEmploymentStatusType(), is(equalTo(vsucp.getEmploymentStatusType())));
    assertThat(persistent.getEthUnableToDetReasonCode(),
        is(equalTo(vsucp.getEthUnableToDetReasonCode())));
    assertThat(persistent.getFirstName(), is(equalTo(vsucp.getFirstName())));
    assertThat(persistent.getForeignAddressIndicatorVar(),
        is(equalTo(vsucp.getForeignAddressIndicatorVar())));
    assertThat(persistent.getGenderIndicator(), is(equalTo(vsucp.getGenderIndicator())));
    assertThat(persistent.getHispUnableToDetReasonCode(),
        is(equalTo(vsucp.getHispUnableToDetReasonCode())));
    assertThat(persistent.getHispanicOriginCode(), is(equalTo(vsucp.getHispanicOriginCode())));
    assertThat(persistent.getIndianTribeType(), is(equalTo(vsucp.getIndianTribeType())));
    assertThat(persistent.getLastName(), is(equalTo(vsucp.getLastName())));
    assertThat(persistent.getLisOwnershipIndicator(),
        is(equalTo(vsucp.getLisOwnershipIndicator())));
    assertThat(persistent.getLisPersonId(), is(equalTo(vsucp.getLisPersonId())));
    assertThat(persistent.getMaritalStatusType(), is(equalTo(vsucp.getMaritalStatusType())));
    assertThat(persistent.getMiddleInitialName(), is(equalTo(vsucp.getMiddleInitialName())));
    assertThat(persistent.getNamePrefixDescription(),
        is(equalTo(vsucp.getNamePrefixDescription())));
    assertThat(persistent.getPassedBackgroundCheckCode(),
        is(equalTo(vsucp.getPassedBackgroundCheckCode())));
    assertThat(persistent.getPrimaryIncomeType(), is(equalTo(vsucp.getPrimaryIncomeType())));
    assertThat(persistent.getResidedOutOfStateIndicator(),
        is(equalTo(vsucp.getResidedOutOfStateIndicator())));
    assertThat(persistent.getSecondaryIncomeType(), is(equalTo(vsucp.getSecondaryIncomeType())));
    assertThat(persistent.getSocialSecurityNumber(), is(equalTo(vsucp.getSocialSecurityNumber())));
    assertThat(persistent.getStateCodeType(), is(equalTo(vsucp.getStateCodeType())));
    assertThat(persistent.getStreetName(), is(equalTo(vsucp.getStreetName())));
    assertThat(persistent.getStreetNumber(), is(equalTo(vsucp.getStreetNumber())));
    assertThat(persistent.getSuffixTitleDescription(),
        is(equalTo(vsucp.getSuffixTitleDescription())));
    assertThat(persistent.getZipNumber(), is(equalTo(vsucp.getZipNumber())));
    assertThat(persistent.getZipSuffixNumber(), is(equalTo(vsucp.getZipSuffixNumber())));

  }

  private SubstituteCareProvider validSubstituteCareProvider()
      throws JsonParseException, JsonMappingException, IOException {

    SubstituteCareProvider validSubstituteCareProvider =
        MAPPER.readValue(fixture("fixtures/domain/legacy/SubstituteCareProvider/valid/valid.json"),
            SubstituteCareProvider.class);

    return validSubstituteCareProvider;

  }
}
