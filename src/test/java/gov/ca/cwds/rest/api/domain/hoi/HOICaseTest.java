package gov.ca.cwds.rest.api.domain.hoi;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import gov.ca.cwds.rest.api.domain.AccessLimitation;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeDescriptor;

public class HOICaseTest {

  @Test
  public void type() throws Exception {
    assertThat(HOICase.class, notNullValue());
  }

  @Test
  public void instantiation() throws Exception {
    HOICase target = new HOICase();
    assertThat(target, notNullValue());
  }

  @Test
  public void getId_Args__() throws Exception {
    HOICase target = new HOICase();
    String actual = target.getId();
    String expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setId_Args__String() throws Exception {
    HOICase target = new HOICase();
    String id = null;
    target.setId(id);
  }

  @Test
  public void getStartDate_Args__() throws Exception {
    HOICase target = new HOICase();
    Date actual = target.getStartDate();
    Date expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setStartDate_Args__Date() throws Exception {
    HOICase target = new HOICase();
    Date startDate = mock(Date.class);
    target.setStartDate(startDate);
  }

  @Test
  public void getEndDate_Args__() throws Exception {
    HOICase target = new HOICase();
    Date actual = target.getEndDate();
    Date expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setEndDate_Args__Date() throws Exception {
    HOICase target = new HOICase();
    Date endDate = mock(Date.class);
    target.setEndDate(endDate);
  }

  @Test
  public void getCounty_Args__() throws Exception {
    HOICase target = new HOICase();
    SystemCodeDescriptor actual = target.getCounty();
    SystemCodeDescriptor expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setCounty_Args__SystemCodeDescriptor() throws Exception {
    HOICase target = new HOICase();
    SystemCodeDescriptor county = mock(SystemCodeDescriptor.class);
    target.setCounty(county);
  }

  @Test
  public void getServiceComponent_Args__() throws Exception {
    HOICase target = new HOICase();
    SystemCodeDescriptor actual = target.getServiceComponent();
    SystemCodeDescriptor expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setServiceComponent_Args__SystemCodeDescriptor() throws Exception {
    HOICase target = new HOICase();
    SystemCodeDescriptor serviceComponent = mock(SystemCodeDescriptor.class);
    target.setServiceComponent(serviceComponent);
  }

  @Test
  public void getFocusChild_Args__() throws Exception {
    HOICase target = new HOICase();
    HOIVictim actual = target.getFocusChild();
    HOIVictim expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setFocusChild_Args__Victim() throws Exception {
    HOICase target = new HOICase();
    HOIVictim focusChild = mock(HOIVictim.class);
    target.setFocusChild(focusChild);
  }

  @Test
  public void getAssignedSocialWorker_Args__() throws Exception {
    HOICase target = new HOICase();
    HOISocialWorker actual = target.getAssignedSocialWorker();
    HOISocialWorker expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setAssignedSocialWorker_Args__SocialWorker() throws Exception {
    HOICase target = new HOICase();
    HOISocialWorker assignedSocialWorker = mock(HOISocialWorker.class);
    target.setAssignedSocialWorker(assignedSocialWorker);
  }

  @Test
  public void getParents_Args__() throws Exception {
    HOICase target = new HOICase();
    List<HOIRelatedPerson> actual = target.getParents();
    List<HOIRelatedPerson> expected = new ArrayList<>();
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setParents_Args__List() throws Exception {
    HOICase target = new HOICase();
    List<HOIRelatedPerson> parents = new ArrayList<HOIRelatedPerson>();
    target.setParents(parents);
  }

  @Test
  public void getAccessLimitation_Args__() throws Exception {
    HOICase target = new HOICase();
    AccessLimitation actual = target.getAccessLimitation();
    AccessLimitation expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setAccessLimitation_Args__AccessLimitation() throws Exception {
    HOICase target = new HOICase();
    AccessLimitation accessLimitation = mock(AccessLimitation.class);
    target.setAccessLimitation(accessLimitation);
  }

  @Test
  public void getLegacyDescriptor_Args__() throws Exception {
    HOICase target = new HOICase();
    LegacyDescriptor actual = target.getLegacyDescriptor();
    LegacyDescriptor expected = null;
    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void setLegacyDescriptor_Args__LegacyDescriptor() throws Exception {
    HOICase target = new HOICase();
    LegacyDescriptor legacyDescriptor = mock(LegacyDescriptor.class);
    target.setLegacyDescriptor(legacyDescriptor);
  }

}
