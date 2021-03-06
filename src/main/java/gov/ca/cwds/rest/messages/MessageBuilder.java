package gov.ca.cwds.rest.messages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import gov.ca.cwds.rest.api.domain.DomainObject;
import gov.ca.cwds.rest.api.domain.error.ErrorMessage;
import gov.ca.cwds.rest.api.domain.error.ErrorMessage.ErrorType;
import gov.ca.cwds.rest.exception.IssueDetails;
import gov.ca.cwds.rest.exception.IssueDetailsCreator;
import gov.ca.cwds.rest.exception.IssueType;

/**
 * @author CWDS API Team
 *
 */
public class MessageBuilder {

  private ArrayList<ErrorMessage> messages = new ArrayList<>();
  private HashSet<IssueDetails> issues = new HashSet<>();

  /**
   * messageBuilder - messageBuilder
   */
  public MessageBuilder() {
    messages = new ArrayList<>();
  }

  /**
   * @param message - message
   */
  public void addError(String message) {
    addError(message, ErrorMessage.ErrorType.VALIDATION);
  }

  /**
   * @param message - message
   * @param type - type
   */
  public void addError(String message, ErrorType type) {
    messages.add(new ErrorMessage(type, message, ""));

    final IssueDetails issue = new IssueDetails();
    this.issues.add(issue);
    issue.setUserMessage(message);

    if (ErrorType.DATA_ACCESS == type) {
      issue.setType(IssueType.DATA_ACCESS_EXCEPTION);
    } else if (ErrorType.BUSINESS == type) {
      issue.setType(IssueType.BUSINESS_VALIDATION);
    } else if (ErrorType.VALIDATION == type) {
      issue.setType(IssueType.CONSTRAINT_VALIDATION);
    } else if (ErrorType.CLIENT_CONTRACT == type) {
      issue.setType(IssueType.JSON_PROCESSING_EXCEPTION);
    } else {
      issue.setType(IssueType.UNEXPECTED_EXCEPTION);
    }
  }

  /**
   * @param errors - errors
   */
  public void addDomainValidationError(Set<ConstraintViolation<DomainObject>> errors) {
    if (!errors.isEmpty()) {
      errors.forEach(error -> {
        final String message = error.getPropertyPath() + " " + error.getMessage();
        messages.add(new ErrorMessage(ErrorMessage.ErrorType.VALIDATION, message, ""));
      });

      errors.forEach(error -> issues.add(IssueDetailsCreator.create(error, null)));
    }
  }

  /**
   * @return the error message
   */
  public List<ErrorMessage> getMessages() {
    return messages == null ? new ArrayList<>() : messages;
  }

  public void addMessageAndLog(String message, org.slf4j.Logger logger) {
    addError(message);
    logger.error(message);
  }

  public void addMessageAndLog(String message, Exception exception, org.slf4j.Logger logger) {
    addError(message);
    logger.error(message, exception.getMessage());
  }

  public Set<IssueDetails> getIssues() {
    return issues;
  }
}
