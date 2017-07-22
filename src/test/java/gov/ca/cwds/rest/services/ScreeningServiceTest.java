package gov.ca.cwds.rest.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gov.ca.cwds.data.es.ElasticsearchDao;
import gov.ca.cwds.rest.ElasticsearchConfiguration;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.domain.Screening;

public class ScreeningServiceTest {

  private ScreeningService screeningService;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private ElasticsearchDao esDao;

  @Mock
  private Client esClient;

  @Mock
  private IndexRequestBuilder indexRequestBuilder;

  @Mock
  private UpdateRequestBuilder updateRequestBuilder;

  @Mock
  private ListenableActionFuture indexListenableActionFuture;

  @Mock
  private ListenableActionFuture updateListenableActionFuture;

  @Mock
  private ElasticsearchConfiguration esConfig;

  @Before
  public void setup() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(esDao.getConfig()).thenReturn(esConfig);
    when(esDao.getClient()).thenReturn(esClient);
    when(esConfig.getElasticsearchAlias()).thenReturn("screenings");
    when(esConfig.getElasticsearchDocType()).thenReturn("screening");

    when(esClient.prepareIndex(any(), any(), any())).thenReturn(indexRequestBuilder);
    when(esClient.prepareUpdate(any(), any(), any())).thenReturn(updateRequestBuilder);

    when(indexRequestBuilder.execute()).thenReturn(indexListenableActionFuture);
    when(updateRequestBuilder.execute()).thenReturn(updateListenableActionFuture);

    screeningService = new ScreeningService(esDao);
  }

  @Test
  public void testFind() {
    try {
      screeningService.find("abc");
      fail("Expected exception");
    } catch (Exception e) {

    }
  }

  @Test
  public void testDelete() {
    try {
      screeningService.delete("abc");
      fail("Expected exception");
    } catch (Exception e) {

    }
  }

  @Test
  public void testCreate() {
    Screening screening = new Screening("abc", null, null, null, null, null, null);
    Screening actual = screeningService.create(screening);
    assertThat(actual, is(screening));
  }

  @Test
  public void testUpdate() {
    Screening screening = new Screening("abc", null, null, null, null, null, null);
    Screening actual = screeningService.update("abc", screening);
    assertThat(actual, is(screening));
  }

  @Test
  public void testUpdatePrimaryKeyValueMismatch() {
    Screening screening = new Screening("abc", null, null, null, null, null, null);
    try {
      screeningService.update("abcd", screening);
      fail("Expected exception");
    } catch (Exception e) {
      assertThat(e.getMessage(), is("Primary key mismatch, [abcd != abc]"));
    }
  }

  @Test
  public void testUpdatePrimaryKeyObjectTypMismatchn() {
    try {
      screeningService.update(new Integer(1), null);
      fail("Expected exception");
    } catch (java.lang.AssertionError e) {
    }
  }

  @Test
  public void testUpdateRequestObjectTypMismatchn() {
    Request request = mock(Request.class);
    try {
      screeningService.update("abc", request);
      fail("Expected exception");
    } catch (java.lang.AssertionError e) {
    }
  }

  @Test
  public void testCreateRequestObjectTypMismatchn() {
    Request request = mock(Request.class);
    try {
      screeningService.create(request);
      fail("Expected exception");
    } catch (java.lang.AssertionError e) {
    }
  }
}
