package com.example.policy_based_access_control.model;

import java.util.Properties;

/**
 * Created by igor on 2017-07-21.
 */
public class Request {
    // Resource is the resource that access is requested to.
    protected String resource;

    // Action is the action that is requested on the resource.
    protected String action ;

    // Subejct is the subject that is requesting access.
    protected String subject;

    protected String currentTime;

    // Context is the request's environmental context.
    protected Properties context = new Properties();

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Properties getContext() {
        return context;
    }

    public void setContext(Properties context) {
        this.context = context;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
