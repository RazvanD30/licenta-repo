package en.ubb.networkconfiguration.boundary.exception_handling;

import en.ubb.networkconfiguration.boundary.validation.exception.BoundaryException;
import en.ubb.networkconfiguration.boundary.validation.exception.NotFoundException;
import en.ubb.networkconfiguration.boundary.validation.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({BoundaryException.class})
    public final ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        LOGGER.error("Handling " + ex.getClass().getSimpleName() + " due to " + ex.getMessage());
        ex.printStackTrace(); //TODO REMOVE AFTER I'M DONE

        if (ex instanceof ValidationException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            ValidationException exception = (ValidationException) ex;
            return handleValidationException(exception, headers, status, request);
        } else if (ex instanceof NotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            NotFoundException exception = (NotFoundException) ex;
            return handleNotFoundException(exception, headers, status, request);
        } else if (ex instanceof BoundaryException){
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            List<String> errors = Collections.singletonList(ex.getMessage());
            return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            List<String> errors = Collections.singletonList(ex.getMessage());
            return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
        }
    }

    private ResponseEntity<ApiError> handleValidationException(ValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> fieldError.getCode() != null)
                .map(fieldError -> {
                    Object[] args = pushArg(fieldError.getArguments() != null ? fieldError.getArguments() : new Object[]{},
                            fieldError.getRejectedValue());
                    return messageSource.getMessage(fieldError.getCode(), args, Locale.getDefault());
                })
                .collect(Collectors.toList());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    private ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    private ResponseEntity<ApiError> handleExceptionInternal(Exception ex, ApiError body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        /*if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }*/
        return new ResponseEntity<>(body, headers, status);
    }

    private static Object[] pushArg(Object[] array, Object push) {
        Object[] longer = new Object[array.length + 1];
        System.arraycopy(array, 0, longer, 0, array.length);
        longer[array.length] = push;
        return longer;
    }

}
