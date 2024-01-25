package com.fp.emailservice.model;

import com.google.common.base.Objects;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A container to hold the email request fields
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(AttachmentData.class)
public class EmailRequest {
    @XmlElement
    private List<EmailNameValue> to;
    @XmlElement
    private List<EmailNameValue> cc;
    @XmlElement
    private List<EmailNameValue> bcc;
    @XmlElement
    private EmailNameValue from;
    @XmlElement
    private String subject;
    @XmlElement
    private String body;
    @XmlElement(name = "attachments")
    private List<AttachmentData> attachments;

    public List<EmailNameValue> getTo() {
        return to;
    }

    public void setTo(List<EmailNameValue> to) {
        this.to = to;
    }

    public List<EmailNameValue> getCc() {
        return cc;
    }

    public void setCc(List<EmailNameValue> cc) {
        this.cc = cc;
    }

    public EmailNameValue getFrom() {
        return from;
    }

    public void setFrom(EmailNameValue name) {
        this.from = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<EmailNameValue> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailNameValue> bcc) {
        this.bcc = bcc;
    }

    public List<AttachmentData> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentData> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("to", to)
                .add("cc", cc)
                .add("from", from)
                .add("subject", subject)
                .add("body", body)
                .toString();
    }
}
