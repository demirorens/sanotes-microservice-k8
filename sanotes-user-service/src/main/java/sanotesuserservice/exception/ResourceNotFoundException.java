package sanotesuserservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sanotesuserservice.payload.ApiResponse;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ApiResponse apiResponse;

    private final String resourceName;
    private final String fieldName;
    private final transient Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        String message = String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);
        apiResponse = new ApiResponse(Boolean.FALSE, message);
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
