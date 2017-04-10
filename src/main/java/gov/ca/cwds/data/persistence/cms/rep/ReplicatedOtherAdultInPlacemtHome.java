package gov.ca.cwds.data.persistence.cms.rep;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import gov.ca.cwds.data.persistence.PersistentObject;
import gov.ca.cwds.data.persistence.cms.BaseOtherAdultInPlacemtHome;

/**
 * {@link PersistentObject} representing an Other Adult In Placement Home as a
 * {@link CmsReplicatedEntity}.
 * 
 * @author CWDS API Team
 */
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "gov.ca.cwds.data.persistence.cms.rep.ReplicatedOtherAdultInPlacemtHome.findBucketRange",
        query = "select x.IDENTIFIER, x.BIRTH_DT, x.END_DT, x.GENDER_CD, x.OTH_ADLTNM, "
            + "x.START_DT, x.LST_UPD_ID, x.LST_UPD_TS, x.FKPLC_HM_T, x.COMNT_DSC, "
            + "x.OTH_ADL_CD, x.IDENTFD_DT, x.RESOST_IND, x.PASSBC_CD "
            + ", x.IBMSNAP_OPERATION, x.IBMSNAP_LOGMARKER FROM {h-schema}OTH_ADLT x "
            + "WHERE x.IDENTIFIER BETWEEN :min_id AND :max_id ORDER BY x.IDENTIFIER FOR READ ONLY",
        resultClass = ReplicatedEducationProviderContact.class, readOnly = true),
    @NamedNativeQuery(
        name = "gov.ca.cwds.data.persistence.cms.rep.ReplicatedOtherAdultInPlacemtHome.findAllUpdatedAfter",
        query = "select z.IDENTIFIER, z.BIRTH_DT, z.END_DT, z.GENDER_CD, z.OTH_ADLTNM, "
            + "z.START_DT, z.LST_UPD_ID, z.LST_UPD_TS, z.FKPLC_HM_T, z.COMNT_DSC, "
            + "z.OTH_ADL_CD, z.IDENTFD_DT, z.RESOST_IND, z.PASSBC_CD "
            + ", z.IBMSNAP_OPERATION, z.IBMSNAP_LOGMARKER "
            + "from {h-schema}OTH_ADLT z WHERE z.IBMSNAP_LOGMARKER >= :after for read only ",
        resultClass = ReplicatedOtherAdultInPlacemtHome.class),
    @NamedNativeQuery(
        name = "gov.ca.cwds.data.persistence.cms.rep.ReplicatedOtherAdultInPlacemtHome.findAllByBucket",
        query = "select z.IDENTIFIER, z.BIRTH_DT, z.END_DT, z.GENDER_CD, z.OTH_ADLTNM, "
            + "z.START_DT, z.LST_UPD_ID, z.LST_UPD_TS, z.FKPLC_HM_T, z.COMNT_DSC, "
            + "z.OTH_ADL_CD, z.IDENTFD_DT, z.RESOST_IND, z.PASSBC_CD "
            + ", 'U' as IBMSNAP_OPERATION, z.LST_UPD_TS as IBMSNAP_LOGMARKER "
            + "from ( select mod(y.rn, CAST(:total_buckets AS INTEGER)) + 1 as bucket, y.* "
            + "from ( select row_number() over (order by 1) as rn, x.* "
            + "from {h-schema}OTH_ADLT x ) y ) z where z.bucket = :bucket_num for read only",
        resultClass = ReplicatedOtherAdultInPlacemtHome.class)})
@Entity
@Table(name = "OTH_ADLT")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplicatedOtherAdultInPlacemtHome extends BaseOtherAdultInPlacemtHome
    implements CmsReplicatedEntity {

  /**
   * Default.
   */
  private static final long serialVersionUID = 1L;

  @Enumerated(EnumType.STRING)
  @Column(name = "IBMSNAP_OPERATION", updatable = false)
  private CmsReplicationOperation replicationOperation;

  @Type(type = "timestamp")
  @Column(name = "IBMSNAP_LOGMARKER", updatable = false)
  private Date replicationDate;

  @Override
  public CmsReplicationOperation getReplicationOperation() {
    return replicationOperation;
  }

  @Override
  public void setReplicationOperation(CmsReplicationOperation replicationOperation) {
    this.replicationOperation = replicationOperation;
  }

  @Override
  public Date getReplicationDate() {
    return replicationDate;
  }

  @Override
  public void setReplicationDate(Date replicationDate) {
    this.replicationDate = replicationDate;
  }

}
