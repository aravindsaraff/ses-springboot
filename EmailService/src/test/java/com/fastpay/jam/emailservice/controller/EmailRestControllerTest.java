package com.fp.emailservice.controller;

import com.fp.emailservice.model.EmailNameValue;
import com.fp.emailservice.model.EmailRequest;
import com.fp.emailservice.model.EmailResponse;
import com.fp.emailservice.service.EmailServiceImpl;
import com.google.common.collect.Lists;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailRestControllerTest {
    private static final String MESSAGE_ID = "12345xyz";

    @Mock(name = "emailService")
    private EmailServiceImpl emailServiceMock;

    @InjectMocks
    private EmailRestController emailController;

    private EmailRequest emailRequest;
    private EmailResponse emailResponse;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        emailRequest = setupEmailRequest();
    }

    @Test
    public void testSendEmail() throws Exception {
        emailResponse = new EmailResponse();
        emailResponse.setMessageId(MESSAGE_ID);

        when(emailServiceMock.sendEmail(emailRequest)).thenReturn(emailResponse);
        EmailResponse emailResponse = emailController.sendEmail(emailRequest);
        Assert.assertNotNull(emailResponse);
        Assert.assertEquals(emailResponse.getMessageId(),MESSAGE_ID);
        verify(emailServiceMock).sendEmail(emailRequest);
    }

    private EmailRequest setupEmailRequest() {
        emailRequest = new EmailRequest();
        EmailNameValue nameValue = new EmailNameValue();
        nameValue.setEmail("you@fp.com");
        nameValue.setName("you");
        emailRequest.setSubject("TEST Subject");
        emailRequest.setBody("Blah Blah Blah");
        emailRequest.setFrom(nameValue);
        emailRequest.setTo(Lists.newArrayList(nameValue));
        return emailRequest;
    }
}