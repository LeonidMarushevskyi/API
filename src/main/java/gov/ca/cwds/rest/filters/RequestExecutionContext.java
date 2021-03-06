package gov.ca.cwds.rest.filters;

import java.util.Date;

/**
 * Request execution context.
 * 
 * @author CWDS API Team
 */
public interface RequestExecutionContext {

  /**
   * Known request execution parameters
   */
  public enum Parameter {
    USER_IDENTITY, REQUEST_START_TIME, SEQUENCE_EXTERNAL_TABLE
  }

  /**
   * Store request execution parameter
   * 
   * @param parameter Parameter
   * @param value Parameter value
   */
  void put(Parameter parameter, Object value);

  /**
   * Retrieve request execution parameter
   * 
   * @param parameter Parameter
   * @return The parameter value
   */
  Object get(Parameter parameter);

  /**
   * Get user id if stored.
   * 
   * @return The user id
   */
  String getUserId();

  /**
   * Get staff id if stored.
   * 
   * @return The staff id
   */
  String getStaffId();

  /**
   * Get request start time if stored
   * 
   * @return The request start time
   */
  Date getRequestStartTime();

  /**
   * Get instance of RequestExecutionContext from registry
   * 
   * @return RequestExecutionContext instance
   */
  static RequestExecutionContext instance() {
    return RequestExecutionContextRegistry.get();
  }
}
