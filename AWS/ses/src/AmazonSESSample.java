/*
 * Copyright 2014-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.model.Message;
import org.apache.commons.io.IOUtils;

import javax.mail.*;
import javax.mail.internet.*;

public class AmazonSESSample {

    static final String FROM = "aravind@dummy.com";  // Replace with your "From" address. This address must be verified.
    static final String TO = "aravind@dummy.com"; // Replace with a "To" address. If you have not yet requested
    static List<String> CC = Arrays.asList("aravind@dummy.com");
    // production access, this address must be verified.
    static final String BODY = "This email was sent through Amazon SES by using the AWS SDK for Java.";
    static final String SUBJECT = "Amazon SES test (AWS SDK for Java)";

    /*
     * Before running the code:
     *      Fill in your AWS access credentials in the provided credentials
     *      file template, and be sure to move the file to the default location
     *      (~/.aws/credentials) where the sample code will load the
     *      credentials from.
     *      https://console.aws.amazon.com/iam/home?#security_credential
     *
     * WANRNING:
     *      To avoid accidental leakage of your credentials, DO NOT keep
     *      the credentials file in your source directory.
     */

    public static void main(String[] args) throws IOException, MessagingException {

        // Construct an object to contain the recipient address.
        //Destination destination = new Destination().withToAddresses(new String[]{TO}).withCcAddresses((String[])CC.toArray());

        // Create the subject and body of the message.
        //Content subject = new Content().withData(SUBJECT);
        //Content textBody = new Content().withData(BODY);
        //Body body = new Body().withText(textBody);


        // Create a message with the specified subject and body.
        //Message message = new Message().withSubject(subject).withBody(body);

        // Assemble the email.
        //SendEmailRequest request = new SendEmailRequest().withSource(FROM).withDestination(destination).withMessage(message);
        /*SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest()
                .withSource(FROM).withDestinations(destination.getToAddresses())
                //.withDestinations(destination.getCcAddresses())
                .withRawMessage(getRawMessage());*/

        try {
            System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

            /*
             * The ProfileCredentialsProvider will return your [default]
             * credential profile by reading from the credentials file located at
             * (~/.aws/credentials).
             *
             * TransferManager manages a pool of threads, so we create a
             * single instance and share it throughout our application.
             */
            AWSCredentials credentials;
            try {
                credentials = new ProfileCredentialsProvider().getCredentials();
            } catch (Exception e) {
                throw new AmazonClientException(
                        "Cannot load the credentials from the credential profiles file. " +
                                "Please make sure that your credentials file is at the correct " +
                                "location (~/.aws/credentials), and is in valid format.",
                        e);
            }

            // Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
            AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);

            // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your production
            // access status, sending limits, and Amazon SES identity-related settings are specific to a given
            // AWS region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are using
            // the US East (N. Virginia) region. Examples of other regions that Amazon SES supports are US_WEST_2
            // and EU_WEST_1. For a complete list, see http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html
            Region REGION = Region.getRegion(Regions.US_WEST_2);
            client.setRegion(REGION);

            // Send the email.
            //client.sendEmail(request);
            //System.out.println("Sending raw email: " + rawEmailRequest.toString());
            SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(getRawMessage());
            rawEmailRequest.setDestinations(Arrays.asList(TO));
            rawEmailRequest.setSource(FROM);
            SendRawEmailResult rawEmailResult = client.sendRawEmail(rawEmailRequest);
            System.out.println("Email Result = " + rawEmailResult.toString());
           // System.out.println("Email sent!");

        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
    }

    private static RawMessage getRawMessage() throws MessagingException, IOException {
        // JavaMail representation of the message
        Session s = Session.getInstance(new Properties(), null);
        s.setDebug(true);
        MimeMessage msg = new MimeMessage(s);

        // Sender and recipient
        msg.setFrom(new InternetAddress("aravind@dummy.com"));
        InternetAddress[] address = {new InternetAddress("aravind@dummy.com")};
        msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
        msg.setSentDate(new Date());
        // Subject
        msg.setSubject(SUBJECT);


        // Add a MIME part to the message
        //MimeMultipart mp = new MimeMultipart();
        Multipart mp = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        //mimeBodyPart.setText(BODY);

        //BodyPart part = new MimeBodyPart();
        //String myText = BODY;
        //part.setContent(URLEncoder.encode(myText, "US-ASCII"), "text/html");
        //part.setText(BODY);
        //mp.addBodyPart(part);
        //msg.setContent(mp);
        mimeBodyPart.setContent(BODY,"text/html");
        mp.addBodyPart(mimeBodyPart);
        msg.setContent(mp);

        // Print the raw email content on the console
        //PrintStream out = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);
        //String rawString = out.toString();
        //byte[] bytes = IOUtils.toByteArray(msg.getInputStream());
        //ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        //ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getEncoder().encode(rawString.getBytes()));

        //byteBuffer.put(bytes);
        //byteBuffer.put(Base64.getEncoder().encode(bytes));
        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(out.toByteArray()));
        return rawMessage;
    }
}
