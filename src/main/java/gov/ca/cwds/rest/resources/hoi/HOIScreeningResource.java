package gov.ca.cwds.rest.resources.hoi;

import static gov.ca.cwds.rest.core.Api.RESOURCE_HOI_SCREENINGS;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import gov.ca.cwds.inject.HOIScreeningServiceBackedResource;
import gov.ca.cwds.rest.api.domain.hoi.HOIRequest;
import gov.ca.cwds.rest.api.domain.hoi.HOIScreening;
import gov.ca.cwds.rest.api.domain.hoi.HOIScreeningResponse;
import gov.ca.cwds.rest.resources.SimpleResourceDelegate;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
import gov.ca.cwds.rest.services.hoi.HOIScreeningService;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A resource providing a RESTful interface for {@link HOIScreening}. It delegates functions to
 * {@link TypedResourceDelegate}. It decorates the {@link TypedResourceDelegate} not in
 * functionality but with @see
 * <a href= "https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X">Swagger
 * Annotations</a> and
 * <a href="https://jersey.java.net/documentation/latest/user-guide.html#jaxrs-resources">Jersey
 * Annotations</a>
 *
 * @author CWDS API Team
 */
@Api(value = RESOURCE_HOI_SCREENINGS)
@Path(value = RESOURCE_HOI_SCREENINGS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HOIScreeningResource {

  private SimpleResourceDelegate<HOIRequest, HOIScreening, HOIScreeningResponse, HOIScreeningService> simpleResourceDelegate;

  /**
   * Constructor
   *
   * @param simpleResourceDelegate The resourceDelegate to delegate to.
   */
  @Inject
  public HOIScreeningResource(
      @HOIScreeningServiceBackedResource SimpleResourceDelegate<HOIRequest, HOIScreening, HOIScreeningResponse, HOIScreeningService> simpleResourceDelegate) {
    this.simpleResourceDelegate = simpleResourceDelegate;
  }

  /**
   * Finds history of involvement by screening id.
   *
   * @param hoiScreeningRequest HOI Screening Request containing a list of Client Id-s
   * @return the response
   */
  @UnitOfWork(value = "ns", readOnly = true, transactional = false)
  @POST
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find history of involvement by screening id",
      response = HOIScreeningResponse.class)
  public Response post(@ApiParam(required = true, name = "clientIds",
      value = "List of Client Id-s") HOIRequest hoiScreeningRequest) {
    return simpleResourceDelegate.find(hoiScreeningRequest);
  }

}
