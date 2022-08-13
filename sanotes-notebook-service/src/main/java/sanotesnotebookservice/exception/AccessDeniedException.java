package sanotesnotebookservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sanotesnotebookservice.payload.ApiResponse;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ApiResponse apiResponse;
    private final String message;

    public AccessDeniedException(ApiResponse apiResponse) {
        super();
        this.message = apiResponse.getMessage();
        this.apiResponse = apiResponse;
    }


    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
