package com.fp.emailservice.service;

import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.model.EmailRequest;
import com.fp.emailservice.model.EmailResponse;
import com.fp.emailservice.service.helper.FileHelper;
import com.fp.emailservice.service.helper.impl.AmazonSESEmailHelperImpl;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Sends Email using a helper class that encapsulates any attachment/template access logic. The sending itself is
 * done using the JavaMail and/or AWS SES SDK.
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Resource(name = "s3Helper")
    private FileHelper fileHelper;

    @Resource(name="sesHelper")
    private AmazonSESEmailHelperImpl sesEmailHelper;

    @Override
    public EmailResponse sendEmail(EmailRequest email) throws FPGenericException {
        // TODO use Dozer bean conversion or a builder
        String messageId = sesEmailHelper.send(email.getTo(), email.getBcc(), email.getCc(), email.getFrom(),
                email.getSubject(), email.getBody(), email.getAttachments());
        if(Strings.isNullOrEmpty(messageId)) {
            throw new FPGenericException("Email Send Failure for unknown reason");
        }
        EmailResponse response = new EmailResponse();
        response.setMessageId(messageId);
        response.setStatus("Sent");
        return response;
    }
}
