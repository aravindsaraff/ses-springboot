package com.fp.emailservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Error Response Holder
 */
@XmlRootElement (name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorEmailResponse {
    @XmlElement
    private int status;
    @XmlElement
    private String reason;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
