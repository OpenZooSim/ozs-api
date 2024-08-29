package com.openzoosim.services.email.templates;

public interface IEmailTemplate {
    public String getFromAddress();
    public String getToAddress();
    public String getSubject();
    public String getHTMLContent();
}
