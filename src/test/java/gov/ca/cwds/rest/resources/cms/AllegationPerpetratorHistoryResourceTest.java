package gov.ca.cwds.rest.resources.cms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.hamcrest.junit.ExpectedException;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.squarespace.jersey2.guice.JerseyGuiceUtils;

import gov.ca.cwds.fixture.AllegationPerpetratorHistoryResourceBuilder;
import gov.ca.cwds.rest.api.domain.cms.AllegationPerpetratorHistory;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
import io.dropwizard.testing.junit.ResourceTestRule;

/**
 * NOTE : The CWDS API Team has taken the pattern of delegating Resource functions to
 * {@link ServiceBackedResourceDelegate}. As such the tests in here reflect that assumption.
 * 
 * @author CWDS API Team
 */
@SuppressWarnings("javadoc")
public class AllegationPerpetratorHistoryResourceTest {

  private static final String ROOT_RESOURCE = "/_allegationPerpetratorHistory/";
  private static final String FOUND_RESOURCE = "/_allegationPerpetratorHistory/abc";

  @After
  public void ensureServiceLocatorPopulated() {
    JerseyGuiceUtils.reset();
  }

  @ClassRule
  public static JerseyGuiceRule rule = new JerseyGuiceRule();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @SuppressWarnings("unchecked")
  private final static TypedResourceDelegate<String, AllegationPerpetratorHistory> typedResourceDelegate =
      mock(TypedResourceDelegate.class);

  @ClassRule
  public final static ResourceTestRule inMemoryResource = ResourceTestRule.builder()
      .addResource(new AllegationPerpetratorHistoryResource(typedResourceDelegate)).build();

  @Before
  public void initMocks() {
    MockitoAnnotations.initMocks(this);
  }

  /*
   * Get Tests
   */
  @Test
  public void getDelegatesToResourceDelegate() throws Exception {
    inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
        .get();
    verify(typedResourceDelegate).get("abc");
  }


  /*
   * Create Tests
   */
  @Test
  public void createDelegatesToResourceDelegate() throws Exception {
    AllegationPerpetratorHistory serialized =
        new AllegationPerpetratorHistoryResourceBuilder().createAllegationPerpetratorHistory();

    inMemoryResource.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
        .post(Entity.entity(serialized, MediaType.APPLICATION_JSON));
    verify(typedResourceDelegate).create(eq(serialized));
  }

  @Test
  public void createValidatesEntity() throws Exception {
    AllegationPerpetratorHistory serialized = new AllegationPerpetratorHistoryResourceBuilder()
        .setCountySpecificCode("1234567").createAllegationPerpetratorHistory();

    int status =
        inMemoryResource.client().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(serialized, MediaType.APPLICATION_JSON)).getStatus();
    assertThat(status, is(422));
  }

  /*
   * Delete Tests
   */
  @Test
  public void deleteDelegatesToResourceDelegate() throws Exception {
    inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
        .delete();
    verify(typedResourceDelegate).delete("abc");
  }

  /*
   * Update Tests
   */
  @Test
  public void updateDelegatesToResourceDelegate() throws Exception {
    AllegationPerpetratorHistory serialized =
        new AllegationPerpetratorHistoryResourceBuilder().createAllegationPerpetratorHistory();

    inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
        .put(Entity.entity(serialized, MediaType.APPLICATION_JSON));
    verify(typedResourceDelegate).update(eq("abc"), eq(serialized));
  }

  @Test
  public void updateValidatesEntity() throws Exception {
    AllegationPerpetratorHistory serialized = new AllegationPerpetratorHistoryResourceBuilder()
        .setCountySpecificCode("1234567").createAllegationPerpetratorHistory();

    int status = inMemoryResource.client().target(FOUND_RESOURCE).request()
        .accept(MediaType.APPLICATION_JSON)
        .put(Entity.entity(serialized, MediaType.APPLICATION_JSON)).getStatus();
    assertThat(status, is(422));
  }

}
