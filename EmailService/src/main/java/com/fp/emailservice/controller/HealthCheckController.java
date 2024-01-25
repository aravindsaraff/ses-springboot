package com.fp.emailservice.controller;

import com.fp.emailservice.service.helper.impl.AmazonSESEmailHelperImpl;
import com.fp.emailservice.util.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.inject.Inject;

import static java.lang.String.format;

/**
 * Provides the health indicator for this service. TBD what the health indicator means.
 */
@RestController
public class HealthCheckController {
    private static final String HEALTHCHECK_SUCCESS = "<html><head/><body>" +
            "Application Name = %s<br/>Build Number = %s<br/>Amazon Connectivity = %s<br/>" +
            "Overall Status = %s</body></html>";
    private static final String HEALTHCHECK_FAILURE = "<html><head/><body>" +
            "Application Name = %s<br/>Build Number = %s<br/>Amazon Connectivity = %s<br/>" +
            "Overall Status = %s</body></html>";

    @Autowired
    private ApplicationProperties properties;

    @Resource(name="sesHelper")
    private AmazonSESEmailHelperImpl amazonSESEmailHelper;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    @ResponseBody
    public String healthCheck() {
        if(amazonSESEmailHelper == null || !amazonSESEmailHelper.isConnected()) {
            format(HEALTHCHECK_FAILURE, properties.getApplicationProperties().getProperty("application.build.name"),
                    properties.getApplicationProperties().getProperty("application.build.version"),
                    "Not Connected",
                    "Bad");
        }
        return format(HEALTHCHECK_SUCCESS, properties.getApplicationProperties().getProperty("application.build.name"),
                properties.getApplicationProperties().getProperty("application.build.version"), "Connected","Good");
    }

}
