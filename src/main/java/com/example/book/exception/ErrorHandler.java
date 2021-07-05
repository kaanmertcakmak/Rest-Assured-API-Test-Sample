package com.example.book.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ErrorHandler implements ErrorController {

    ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    ApiException handleError(WebRequest webRequest) {
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions
                        .of(
                                ErrorAttributeOptions.Include.MESSAGE,
                                ErrorAttributeOptions.Include.BINDING_ERRORS));
        String message = (String) attributes.get("message");
        String path = (String) attributes.get("path");
        int status = (int) attributes.get("status");
        ApiException exception = new ApiException(status, message, path);
        if(attributes.containsKey("errors")) {
            @SuppressWarnings("unchecked") List<ObjectError> fieldErrorList = (List<ObjectError>) attributes.get("errors");
            Map<String, String> validationErrors = new HashMap<>();
            fieldErrorList
                    .forEach(
                            (fieldError -> validationErrors.put("message", fieldError.getDefaultMessage()))
                    );
            exception.setValidationErrors(validationErrors);
        }
        return exception;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
