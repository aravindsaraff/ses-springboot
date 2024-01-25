
Very old service developed in Mid 2014
===============================================================
Email Service
-------------
Email Service is a provides ability to send emails using Amazon SES. The application uses Apache-Shade
to generate a executable jar file and a embedded Jetty container.

Build instructions
------------------
Install Maven 3.2.x and Java SDK 1.8.x. 

Build and package the application. This will produce a self-executable jar file in the target directory
```
mvn clean install
```

The command below generates executable jar
```
mvn clean package
```

Starting the server during development
--------------------------------------
During development you can use the IDE to start the application. For example in Intellij you mention:
1. Main class: com.fp.emailservice.JettyApplicationLauncher
2. VM arguments: -Dlogback.configurationFile=/usr/local/fp/logback.xml
3. Working Directory: /Technology/Projects/Pitstop
4. Use classpath of module: <modulename>


Installation and deployment to QA/Production
--------------------------------------------
To install EmailService on another server, copy the emailservice-1.0.0-SNAPSHOT.jar to the server location. You have to copy
a number of configuration files to the location '/usr/local/fp' on that server as well:
- logback.xml 
- emailservice.properties
- AWSCredentials.properties (Please update any environment specific property keys)

Then you can launch the service as: java  -Dlogback.configurationFile=/usr/local/fp/logback.xml -jar emailservice-1.0.0-SNAPSHOT.jar

Sample Usage
--------------------------------------
Once the server is started you can use the following JSON in a cURL command or a SOAPUI or a REST plugin in your favorite browser:
(Don't forget to set the Content-Type (-H using cURL) to application/json header. Else, it won't work)
For example:
curl -i -H "Content-Type: application/json" -X POST -d @emailattachments.json http://localhost:8080/v1/emails

where emailattachments.json would have
{
"to":[{"name":"aravind","email":"aravind@yopmail.com"}],
"cc":[{"email":"xxx@yopmail.com","name":"xxxx yyy"}],
"from":{"email":"aravind@yopmail.com"},
"subject":"Test Attachments from cURL",
"body":"<html><b>Hello</b><br/><b>World</b></html>",
"attachments":[{"attachment":"YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXo=","type":"text/plain","name":"a1.txt"},{"attachment":"cXVpY2sgZm94IGp1bXBlZCBvdmVyIGJyb3duIGRvZw==","type":"text/plain","name":"b.txt"},
{"attachment":"TG9yZW4gSXBzdW0gTG9yZW4gSXBzdW0K","type":"application/pdf","name":"something.pdf"}]
}

The parameters in the JSON are:
- An array of email-to addresses with names (names is optional)
- An array of email-cc addresses with names (optional)
- An array of email-bcc addresses with names (optional)
- A from email id with name
- A subject text
- A body text (HTML or Plain/Text)
- An array of attachments where the attachment is base64 encoded along with type of the attachment and name of the attachment
