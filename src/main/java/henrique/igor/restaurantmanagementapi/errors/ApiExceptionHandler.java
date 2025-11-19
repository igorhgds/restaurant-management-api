package henrique.igor.restaurantmanagementapi.errors;

import henrique.igor.restaurantmanagementapi.errors.exceptions.BusinessRuleException;
import henrique.igor.restaurantmanagementapi.errors.exceptions.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private ErrorResponse buildErrorResponse(HttpStatus status, ExceptionCode exceptionCode, Object details) {
        String message;
        try {
            message = messageSource.getMessage(exceptionCode.name(), null, LocaleContextHolder.getLocale());
        } catch (Exception ex) {
            message = "Unidentified error: " + exceptionCode.name();
        }

        Object finalDetails = details;

        if (details instanceof Map)
            finalDetails = translateErrorDetails((Map<String, String>) details);

        String formattedCode = String.valueOf(status.value());

        return new ErrorResponse()
                .withCode(formattedCode)
                .withReason(message)
                .withDetails(finalDetails);
    }

    private Map<String, String> translateErrorDetails(Map<String, String> originalMap) {
        Map<String, String> translatedMap = new HashMap<>();

        originalMap.forEach((field, key) -> {
            try {
                String translatedMessage = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
                translatedMap.put(field, translatedMessage);
            } catch (Exception e) {
                translatedMap.put(field, key);
            }
        });

        return translatedMap;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handlerBusinessRuleException(BusinessRuleException exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        if (exception.getCode() == ExceptionCode.DUPLICATED_RESOURCE) {
            status = HttpStatus.CONFLICT;
        } else if (exception.getCode() == ExceptionCode.ENTITY_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        }

        ErrorResponse body = buildErrorResponse(status, exception.getCode(), exception.getDetails());

        log.error("Business Error: [Code: {}] - [Details: {}]", body.getCode(), body.getDetails());
        return handleExceptionInternal(exception, body, new HttpHeaders(), status, request);
    }

}


