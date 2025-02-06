package com.openzoosim.services;

public interface IEmailService {
    boolean send(String to, String subject, String html);
}
