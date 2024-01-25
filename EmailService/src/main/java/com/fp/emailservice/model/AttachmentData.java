package com.fp.emailservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Attachment related data+metadata
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AttachmentData {
    @XmlElement
    private String attachment;
    @XmlElement
    private String type;
    @XmlElement
    private String name;

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
