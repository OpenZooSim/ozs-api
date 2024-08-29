package com.openzoosim.services.email.templates;

public class NewUserRegistrationEmailTemplate implements IEmailTemplate {

    private String _fromAddress;
    private String _toAddress;
    private String _subject;
    private String _verificationToken;

    public NewUserRegistrationEmailTemplate(String toEmail, String verificationToken) {
        this._fromAddress = "do-not-reply@openzoosim.com";
        this._toAddress = toEmail;
        this._subject = "OpenZooSim - Verify Your Account!";
        this._verificationToken = verificationToken;
    }

    @Override
    public String getFromAddress() {
        return this._fromAddress;
    }

    @Override
    public String getToAddress() {
        return this._toAddress;
    }

    @Override
    public String getSubject() {
        return this._subject;
    }

    @Override
    public String getHTMLContent() {
        return "<h2>Welcome to OpenZooSim!</h2> " +
        "<p>To confirm your new account, please click the link below to get started!</p>" +
        "<p><a href=\"http://localhost:3000/v1/auth/verify?token=" + this._verificationToken + "\">VERIFY ACCOUNT</a></p>";
    }
    
}
