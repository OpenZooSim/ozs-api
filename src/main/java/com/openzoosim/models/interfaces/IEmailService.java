package com.openzoosim.models.interfaces;

public interface IEmailService {
    public boolean sendEmail(String to, String from, String subject, String text);
}
