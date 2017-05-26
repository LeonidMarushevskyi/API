package gov.ca.cwds.rest.business.rules;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.cms.CountyTriggerDao;
import gov.ca.cwds.data.persistence.cms.ClientAddress;
import gov.ca.cwds.data.persistence.cms.CountyTrigger;
import gov.ca.cwds.data.persistence.cms.Referral;
import gov.ca.cwds.data.persistence.cms.ReferralClient;

/**
 * Business layer object to work on LA County Trigger
 * 
 * <p>
 * If the staffPerson is from the LA County, it will trigger the countyTrigger table with the
 * associated foreign key and updates the trigger table if the record is existing
 * <p>
 * 
 * @author CWDS API Team
 */
public class LACountyTrigger {

  private static final String LA_COUNTY_SPECIFIC_CODE = "19";

  private static final String CLIENT_COUNTYOWNERSHIP = "C";

  private static final String ADDRESS_COUNTYOWNERSHIP = "A";

  private static final Logger LOGGER = LoggerFactory.getLogger(LACountyTrigger.class);

  private CountyTriggerDao countyTriggerDao;

  /**
   * @param countyTriggerDao - countyTriggerDao
   */
  @Inject
  public LACountyTrigger(CountyTriggerDao countyTriggerDao) {
    this.countyTriggerDao = countyTriggerDao;
  }

  /**
   * @param object The object
   * @return the countyTrigger
   */
  public boolean createCountyTrigger(Object object) {
    Referral referral;
    ReferralClient referralClient;
    if (object instanceof Referral) {
      referral = (Referral) object;

      boolean countyTriggerExist = false;
      if (countyTriggerDao.find(referral.getAllegesAbuseOccurredAtAddressId()) != null) {
        countyTriggerExist = true;
      }

      if (referral.getAllegesAbuseOccurredAtAddressId() != ""
          && referral.getAllegesAbuseOccurredAtAddressId() != null) {
        CountyTrigger countyTrigger =
            new CountyTrigger(referral.getAllegesAbuseOccurredAtAddressId(),
                referral.getCountySpecificCode(), ADDRESS_COUNTYOWNERSHIP, null,
                Referral.class.getDeclaredAnnotation(Table.class).name());
        if (countyTriggerExist) {
          countyTriggerDao.update(countyTrigger);
        } else {
          countyTriggerDao.create(countyTrigger);
        }
        LOGGER.info("LA county referral address triggered");
      }
    }

    if (object instanceof ReferralClient) {
      referralClient = (ReferralClient) object;

      boolean countyTriggerExist = false;
      if (countyTriggerDao.find(referralClient.getClientId()) != null) {
        countyTriggerExist = true;
      }

      if (referralClient.getClientId() != "" && referralClient.getClientId() != null) {
        CountyTrigger countyTrigger = new CountyTrigger(referralClient.getClientId(),
            referralClient.getCountySpecificCode(), CLIENT_COUNTYOWNERSHIP, null,
            ReferralClient.class.getDeclaredAnnotation(Table.class).name());
        if (countyTriggerExist) {
          countyTriggerDao.update(countyTrigger);
        } else {
          countyTriggerDao.create(countyTrigger);
        }
        LOGGER.info("LA county referralClient triggered");
      }
    }

    return true;
  }

  /**
   * @param object The object
   * @return the CountyTrigger
   */
  public boolean createClientAddressCountyTrigger(Object object) {
    ClientAddress clientAddress;

    if (object instanceof ClientAddress) {
      clientAddress = (ClientAddress) object;

      boolean countyTriggerExist = false;
      if (countyTriggerDao.find(clientAddress.getFkClient()) != null) {
        countyTriggerExist = true;
      }

      if (clientAddress.getFkClient() != "" && clientAddress.getFkClient() != null) {
        CountyTrigger countyTrigger1 = new CountyTrigger(clientAddress.getFkClient(),
            LA_COUNTY_SPECIFIC_CODE, CLIENT_COUNTYOWNERSHIP, null,
            ClientAddress.class.getDeclaredAnnotation(Table.class).name());

        CountyTrigger countyTrigger2 = new CountyTrigger(clientAddress.getFkAddress(),
            LA_COUNTY_SPECIFIC_CODE, ADDRESS_COUNTYOWNERSHIP, null,
            ClientAddress.class.getDeclaredAnnotation(Table.class).name());
        if (countyTriggerExist) {
          countyTriggerDao.update(countyTrigger1);
          countyTriggerDao.update(countyTrigger2);
        } else {
          countyTriggerDao.create(countyTrigger1);
          countyTriggerDao.create(countyTrigger2);
        }

      }
    }
    return true;
  }

}
