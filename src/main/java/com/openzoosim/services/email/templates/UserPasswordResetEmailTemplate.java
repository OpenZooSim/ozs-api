package com.openzoosim.services.email.templates;

public class UserPasswordResetEmailTemplate implements IEmailTemplate {

    private String _fromAddress;
    private String _toAddress;
    private String _subject;
    private String _resetToken;

    public UserPasswordResetEmailTemplate(String toEmail, String resetToken) {
        this._fromAddress = "do-not-reply@openzoosim.com";
        this._toAddress = toEmail;
        this._subject = "OpenZooSim - Reset Your Password!";
        this._resetToken = resetToken;
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
        "<h2>OpenZooSim - Reset Your Password</h2> " +
                "<p>To reset your password, please click the link below!</p>" +
                "<p><a href=\"http://localhost:3000/auth/password-reset-verify?token=" + _resetToken + "\">RESET PASSWORD</a></p>";
    }
    
}
