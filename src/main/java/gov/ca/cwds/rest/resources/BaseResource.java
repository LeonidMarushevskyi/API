package gov.ca.cwds.rest.resources;

import gov.ca.cwds.rest.core.ApiPoc;
import gov.ca.cwds.rest.services.Service;
import gov.ca.cwds.rest.setup.ServiceEnvironment;

/**
 * 
 * @author CWDS API Team
 *
 * @param <T>	The {@link Service} interface the resource delegates to.  NOTE : this should be an interface because we store versions of the implementations based on {@link ApiPoc.Version}
 */
public abstract class BaseResource<T extends Service> {
	private ServiceEnvironment serviceEnvironment;
	private Class<T> clazz;
	
	/**
	 * Constructor 
	 * 
	 * @param serviceEnvironment The {@link ServiceEnvironment} for this resource
	 * @param clazz	The Service interface the versioned resource delegates to
	 */
	public BaseResource(ServiceEnvironment serviceEnvironment, Class<T> clazz) {
		this.serviceEnvironment = serviceEnvironment;
		this.clazz = clazz;
	}
	
	/**
	 * Get the implementation of the service for the given {@link ApiPoc.Version}
	 * 
	 * @param mediaType	The mediaType of the version being requested
	 * @return	The implementation of the {@link Service} based on the given mediaType
	 */
	@SuppressWarnings("unchecked")
	protected T versionedService(String mediaType) {
		//TODO : loop through media types, use first one found
		//TODO : Test this
		//check out - text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8 
		return (T)serviceEnvironment.getService(clazz, mediaType);
	}

}
