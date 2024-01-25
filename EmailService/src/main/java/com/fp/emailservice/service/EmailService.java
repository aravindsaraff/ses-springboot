package com.fp.emailservice.service;

import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.model.EmailRequest;
import com.fp.emailservice.model.EmailResponse;

/**
 * Backend service to send email
 */
public interface EmailService {
    public EmailResponse sendEmail(EmailRequest email) throws FPGenericException;
}
