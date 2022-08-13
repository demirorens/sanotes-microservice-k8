package sanotesnotebookservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import sanotesnotebookservice.payload.ApiResponse;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final ApiResponse apiResponse;


    public BadRequestException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }


    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}
