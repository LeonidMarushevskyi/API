package gov.ca.cwds.rest.services.contact;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gov.ca.cwds.data.dao.contact.DeliveredServiceDao;
import gov.ca.cwds.fixture.contacts.DeliveredServiceResourceBuilder;
import gov.ca.cwds.rest.api.contact.DeliveredServiceDomain;
import gov.ca.cwds.rest.filters.TestRequestExecutionContext;
import gov.ca.cwds.rest.services.ServiceException;
import gov.ca.cwds.rest.services.cms.AbstractShiroTest;

/***
 * 
 * @author CWDS API Team
 *
 */
@SuppressWarnings("javadoc")
public class DeliveredServiceTest extends AbstractShiroTest {

  private DeliveredService deliveredService;
  private DeliveredServiceDao deliveredServiceDao;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() throws Exception {
    Subject mockSubject = mock(Subject.class);
    PrincipalCollection principalCollection = mock(PrincipalCollection.class);

    List<Object> list = new ArrayList<>();
    list.add("msg");

    when(principalCollection.asList()).thenReturn(list);
    when(mockSubject.getPrincipals()).thenReturn(principalCollection);
    setSubject(mockSubject);

    @SuppressWarnings("unused")
    TestRequestExecutionContext testApiRequestCommonInfo = new TestRequestExecutionContext();

    deliveredServiceDao = mock(DeliveredServiceDao.class);
    deliveredService = new DeliveredService(deliveredServiceDao);

  }

  // find test
  @Test
  public void findThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    deliveredService.find("string");
  }

  // delete test
  @Test
  public void deleteThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    deliveredService.delete("string");
  }

  // update test
  @Test
  public void updateThrowsNotImplementedException() throws Exception {
    thrown.expect(NotImplementedException.class);
    deliveredService.update("string", null);
  }

  // create test
  @Test
  public void deliveredServiceCreateReturnsPostedClass() throws Exception {
    String id = "5cFeXJN0Dv";
    DeliveredServiceDomain deliveredServiceDomain =
        new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

    gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity toCreate =
        new gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity(id, deliveredServiceDomain,
            "ABC", new Date());

    DeliveredServiceDomain request = new DeliveredServiceDomain(toCreate);

    when(deliveredServiceDao
        .create(any(gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity.class)))
            .thenReturn(toCreate);

    DeliveredServiceDomain response = deliveredService.create(request);
    assertThat(response.getClass(), is(DeliveredServiceDomain.class));
  }

  @Test
  public void deliveredServiceCreateReturnsNonNull() throws Exception {
    String id = "5cFeXJN0Dv";
    DeliveredServiceDomain deliveredServiceDomain =
        new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

    gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity toCreate =
        new gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity(id, deliveredServiceDomain,
            "ABC", new Date());

    DeliveredServiceDomain request = new DeliveredServiceDomain(toCreate);
    when(deliveredServiceDao
        .create(any(gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity.class)))
            .thenReturn(toCreate);

    DeliveredServiceDomain postedDeliveredServiceDomain = deliveredService.create(request);
    assertThat(postedDeliveredServiceDomain, is(notNullValue()));
  }

  @Test
  public void deliveredServiceCreateReturnsCorrectEntity() throws Exception {
    String id = "5cFeXJN0Dv";
    DeliveredServiceDomain deliveredServiceDomain =
        new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

    gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity toCreate =
        new gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity(id, deliveredServiceDomain,
            "ABC", new Date());

    DeliveredServiceDomain request = new DeliveredServiceDomain(toCreate);
    when(deliveredServiceDao
        .create(any(gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity.class)))
            .thenReturn(toCreate);

    DeliveredServiceDomain expected = new DeliveredServiceDomain(toCreate);
    DeliveredServiceDomain returned = deliveredService.create(request);
    assertThat(returned, is(expected));
  }

  @Test
  public void deliveredServiceCreateThrowsEntityExistsException() throws Exception {
    try {
      DeliveredServiceDomain deliveredServiceDomain =
          new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

      when(deliveredServiceDao.create(any())).thenThrow(EntityExistsException.class);

      deliveredService.create(deliveredServiceDomain);
    } catch (Exception e) {
      assertEquals(e.getClass(), ServiceException.class);
    }
  }

  @Test
  public void deliveredServiceCreateNullIDError() throws Exception {
    try {
      DeliveredServiceDomain deliveredServiceDomain =
          new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

      gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity toCreate =
          new gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity(null,
              deliveredServiceDomain, "ABC", new Date());

      when(deliveredServiceDao
          .create(any(gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity.class)))
              .thenReturn(toCreate);

      DeliveredServiceDomain expected = new DeliveredServiceDomain(toCreate);
    } catch (ServiceException e) {
      assertEquals("DeliveredService ID cannot be blank", e.getMessage());
    }

  }

  @Test
  public void deliveredServiceCreateBlankIDError() throws Exception {
    try {
      DeliveredServiceDomain deliveredServiceDomain =
          new DeliveredServiceResourceBuilder().buildDeliveredServiceResource();

      gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity toCreate =
          new gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity(" ",
              deliveredServiceDomain, "ABC", new Date());

      when(deliveredServiceDao
          .create(any(gov.ca.cwds.data.persistence.contact.DeliveredServiceEntity.class)))
              .thenReturn(toCreate);

      DeliveredServiceDomain expected = new DeliveredServiceDomain(toCreate);
    } catch (ServiceException e) {
      assertEquals("DeliveredService ID cannot be blank", e.getMessage());
    }

  }

}