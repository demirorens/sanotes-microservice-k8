package sanotesnoteservice.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import sanotesnoteservice.exeption.BadRequestException;
import sanotesnoteservice.exeption.ResourceNotFoundException;
import sanotesnoteservice.exeption.SANotesException;
import sanotesnoteservice.payload.ApiResponse;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 400:
                return new BadRequestException(new ApiResponse(response.reason()));
            case 404:
                return new ResourceNotFoundException(new ApiResponse());
            default:
                return new SANotesException(response.reason());
        }
    }
}
