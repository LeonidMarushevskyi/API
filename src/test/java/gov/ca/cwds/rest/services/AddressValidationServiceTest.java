package gov.ca.cwds.rest.services;

import static org.mockito.Mockito.mock;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gov.ca.cwds.data.validation.SmartyStreetsDao;
import gov.ca.cwds.rest.api.domain.Address;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;

/**
 * @author CWDS API Team
 *
 */
@SuppressWarnings("javadoc")
public class AddressValidationServiceTest {
  private AddressValidationService addressValidationService;
  private SmartyStreetsDao smartyStreetDao;
  private LegacyDescriptor legacyDescriptor = new LegacyDescriptor();


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() throws Exception {
    smartyStreetDao = mock(SmartyStreetsDao.class);
    addressValidationService = new AddressValidationService(smartyStreetDao);
  }

  /*
   * find tests
   */
  @Test
  public void findThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    addressValidationService.find(new Long(1));
  }

  /*
   * create tests
   */
  @Test
  public void createThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    addressValidationService.create(new Address("", "", "742 Evergreen Terrace", "Springfield",
        1828, "98700", 32, legacyDescriptor));
  }

  /*
   * delete tests
   */
  @Test
  public void deleteThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    addressValidationService.delete(new Long(1));
  }

  /*
   * update tests
   */
  @Test
  public void updateThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);

    addressValidationService.update(1L,
        new Address("", "", "street", "city", 1828, "95555", 32, legacyDescriptor));
  }

  @Test
  public void testfetchValidatedAddressesThrowsExceptionWhenSmartyStreetsConfigurationNotProvided()
      throws Exception {
    thrown.expect(Exception.class);
    addressValidationService.fetchValidatedAddresses(
        new Address("", "", "street", "city", 1828, "95555", 32, legacyDescriptor));
  }

}
