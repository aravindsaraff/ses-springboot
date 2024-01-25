package com.fp.emailservice.service.helper.impl;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.fp.emailservice.config.fpAWSBean;
import com.fp.emailservice.exception.FPGenericException;
import com.fp.emailservice.model.AttachmentData;
import com.fp.emailservice.model.EmailNameValue;
import com.fp.emailservice.util.Base64Decoder;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

import static javax.mail.Message.*;

/**
 * Encapsulates Amazon SES specific connection information and other SES related helper methods.
 */
@Component("sesHelper")
public class AmazonSESEmailHelperImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonSESEmailHelperImpl.class);
    @Value("${aws_fp_region}")
    private String region;
    @Autowired
    private fpAWSBean awsBean;
    // Amazon SDK Simple Email Client to interact with SES
    private AmazonSimpleEmailServiceClient amazonSimpleEmailServiceClient;

    /**
     * A simple method wrapping the SES specific API invocation. Given a request to send email, this method invokes
     * the SES endpoint using the Amazon SDK.
     * @param toAddressList A {@link java.util.List} of email addresses to be sent to
     * @param bccList List of emails that are binary copied
     * @param ccList  List of emails that are to be in cc field
     * @param from EmailNameValue of from email address (required) and name (optional)
     * @param emailSubject  Subject of the email
     * @param emailBody     Email Content   @return A messageId String
     */
    public String send(List<EmailNameValue> toAddressList, List<EmailNameValue> bccList, List<EmailNameValue> ccList,
                       EmailNameValue from, String emailSubject, String emailBody, List<AttachmentData> attachmentData)
            throws FPGenericException {
        Optional<List<EmailNameValue>> toAddressListOptional = Optional.of(toAddressList);
        if(!toAddressListOptional.isPresent() && toAddressList.isEmpty()) {
            throw new FPGenericException("To Address is required for the service to work");
        }
        SendRawEmailRequest rawEmailRequest;
        try {
            if(bccList == null) {
                bccList = Lists.newArrayList();
            }
            if(ccList == null) {
                ccList = Lists.newArrayList();
            }
            if(attachmentData == null) {
                attachmentData = Lists.newArrayList();
            }
            rawEmailRequest = new SendRawEmailRequest(buildRawMessage(from, toAddressList, bccList, ccList,
                    emailSubject, emailBody, attachmentData));
        } catch (MessagingException | IOException e) {
            throw new FPGenericException("Unable to Send Email Message", e);
        }
        // add to list of To field after converting the custom email-name-value pairs to email address list
        // Java8 collectors! Awesome
        List<String> emailList = toAddressList.stream().map(EmailNameValue::getEmail).collect(Collectors.toList());

        // add any bcc
        if(!bccList.isEmpty()) {
            emailList.addAll(bccList.stream().map(EmailNameValue::getEmail).collect(Collectors.toList()));
        }
        // add any cc
        if(!ccList.isEmpty()) {
            emailList.addAll(ccList.stream().map(EmailNameValue::getEmail).collect(Collectors.toList()));
        }
        // this seems like redundant. But for SES AWS, the destination is to be set else it throws error out
        rawEmailRequest.setDestinations(emailList);
        rawEmailRequest.setSource(from.getEmail());
        SendRawEmailResult rawEmailResult = amazonSimpleEmailServiceClient.sendRawEmail(rawEmailRequest);
        return rawEmailResult.getMessageId();
    }

    private RawMessage buildRawMessage(EmailNameValue fromEmailAddress, List<EmailNameValue> toAddressList,
                                       List<EmailNameValue> bccList, List<EmailNameValue> ccList, String emailSubject,
                                       String emailBody, List<AttachmentData> attachmentData)
            throws MessagingException, IOException {
        Session s = Session.getInstance(new Properties(), null);
        s.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(s);

        // Add Sender
        if(Strings.isNullOrEmpty(fromEmailAddress.getName())) {
            mimeMessage.setFrom(new InternetAddress(fromEmailAddress.getEmail()));
        } else {
            mimeMessage.setFrom(new InternetAddress(fromEmailAddress.getEmail(), fromEmailAddress.getName()));
        }
        // Add recipient
        for(EmailNameValue emailNameValue : toAddressList) {
            if(!Strings.isNullOrEmpty(emailNameValue.getName())) {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(emailNameValue.getEmail(),
                        emailNameValue.getName()));
            } else {
                mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(emailNameValue.getEmail()));
            }
        }
        // Add Bcc
        for(EmailNameValue bccAddress : bccList) {
            if(!Strings.isNullOrEmpty(bccAddress.getName())) {
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bccAddress.getEmail(),
                        bccAddress.getName()));
            } else {
                mimeMessage.addRecipient(RecipientType.BCC, new InternetAddress(bccAddress.getEmail()));
            }
        }
        // Add cc
        for(EmailNameValue ccAddress : ccList) {
            if(!Strings.isNullOrEmpty(ccAddress.getName())) {
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(ccAddress.getEmail(),
                        ccAddress.getName()));
            } else {
                mimeMessage.addRecipient(RecipientType.CC, new InternetAddress(ccAddress.getEmail()));
            }
        }
        mimeMessage.setSentDate(new Date());
        // Add message
        mimeMessage.setSubject(emailSubject);
        Multipart mp = new MimeMultipart();
        BodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(emailBody,"text/html");
        mp.addBodyPart(mimeBodyPart);
        // Add any attachments
        for(AttachmentData attach : attachmentData) {
            mimeBodyPart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(Base64Decoder.decode(attach.getAttachment()), attach.getType());
            mimeBodyPart.setDataHandler(new DataHandler(dataSource));
            mimeBodyPart.setFileName(attach.getName());
            mp.addBodyPart(mimeBodyPart);
        }
        mimeMessage.setContent(mp);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mimeMessage.writeTo(out);
        return new RawMessage(ByteBuffer.wrap(out.toByteArray()));
    }

    @PostConstruct
    public void initialize() {
        amazonSimpleEmailServiceClient = new AmazonSimpleEmailServiceClient(awsBean);
        amazonSimpleEmailServiceClient.setRegion(Region.getRegion(Regions.fromName(region)));
    }

    /**
     * Finds if we have a successful connectivity to Amazon instance
     * @return True if the connectivity is a success
     */
    public boolean isConnected() {
        GetSendQuotaResult quotaResult = amazonSimpleEmailServiceClient.getSendQuota();
        return Optional.ofNullable(quotaResult).isPresent();
    }
}
