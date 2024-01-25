package com.fp.emailservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A simple holder of email alias and email.
 * xxx: Can be refactored into plain/generic key/value as well
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EmailNameValue {
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
