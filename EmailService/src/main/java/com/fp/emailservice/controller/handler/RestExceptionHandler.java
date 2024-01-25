package com.fp.emailservice.controller.handler;

import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.exception.InvalidRequestException;
import com.fp.emailservice.model.ErrorEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class is used by the Spring (REST) controllers to return right error response code with JSON.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = FPGenericException.class)
    protected ResponseEntity<Object> handleGenericException(RuntimeException fpgx, WebRequest webRequest) {
        LOGGER.error("Handling FPGenericException", fpgx);

        ErrorEmailResponse errorEmailResponse = new ErrorEmailResponse();
        errorEmailResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorEmailResponse.setReason(fpgx.getMessage());
        return handleExceptionInternal(fpgx, errorEmailResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    protected ResponseEntity<Object> handleInvalidException(Exception ire, WebRequest webRequest) {
        LOGGER.error("Handling Invalid Exception", ire);

        ErrorEmailResponse errorEmailResponse = new ErrorEmailResponse();
        errorEmailResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorEmailResponse.setReason(ire.getMessage());
        return handleExceptionInternal(ire, errorEmailResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleException(RuntimeException fpgx, WebRequest webRequest) {
        LOGGER.error("Handling FPGenericException", fpgx);

        ErrorEmailResponse errorEmailResponse = new ErrorEmailResponse();
        errorEmailResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorEmailResponse.setReason(fpgx.getMessage());
        return handleExceptionInternal(fpgx, errorEmailResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

  /*  @ExceptionHandler(value = org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseBody
    protected ResponseEntity<Object> handleJSONException(HttpMessageNotReadableException fpgx, WebRequest webRequest) {
        LOGGER.error("Handling JSONException", fpgx);

        ErrorEmailResponse errorEmailResponse = new ErrorEmailResponse();
        errorEmailResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorEmailResponse.setReason(fpgx.getMessage());
        return handleExceptionInternal(fpgx, errorEmailResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }*/
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest webRequest) {
      LOGGER.error("Handling HttpMessageNotReadableException", ex);
      ErrorEmailResponse errorEmailResponse = new ErrorEmailResponse();
      errorEmailResponse.setStatus(HttpStatus.BAD_REQUEST.value());
      errorEmailResponse.setReason(ex.getMessage());
      return handleExceptionInternal(ex, errorEmailResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
  }
}
