package com.fp.emailservice.controller;



import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.exception.InvalidRequestException;
import com.fp.emailservice.model.EmailNameValue;
import com.fp.emailservice.model.EmailRequest;
import com.fp.emailservice.model.EmailResponse;
import com.fp.emailservice.service.EmailService;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * Email Rest Service
 */
@RestController
@RequestMapping("/v1")
public class EmailRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailRestController.class);
    private final EmailService emailService;

    @Autowired
    public EmailRestController(final EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(value = "/emails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public EmailResponse sendEmail(@RequestBody EmailRequest emailRequest) throws FPGenericException, InvalidRequestException {
        // validate the input parameters
        if(!validateEmailRequest(emailRequest)) {
            throw new InvalidRequestException("{\"error\":\"Input Parameters are missing\"}");

        }
        LOGGER.debug("SendEmail method {}", emailRequest);
        return emailService.sendEmail(emailRequest);
    }

    /**
     * Validates the input request. Returns true if all required parameters are present
     * @param emailRequest Email Request to be validated
     * @return True if required parameters are present. False else.
     */
    private boolean validateEmailRequest(EmailRequest emailRequest) {
        // From has to be present and the emailid is non-null
        Optional<EmailNameValue> fromOptional = Optional.ofNullable(emailRequest.getFrom());
        if(!fromOptional.isPresent() || Strings.isNullOrEmpty(emailRequest.getFrom().getEmail())) {
            return false;
        }
        // To has to be present and non-null
        Optional<List<EmailNameValue>> listToOptional = Optional.ofNullable(emailRequest.getTo());
        if(!listToOptional.isPresent() || emailRequest.getTo().isEmpty()) {
            return false;
        }
        // Body text and subject has to be present as well
        return !Strings.isNullOrEmpty(emailRequest.getBody()) && !Strings.isNullOrEmpty(emailRequest.getSubject());
    }
}
