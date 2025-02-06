package com.openzoosim.services.impl;

import com.openzoosim.config.ApplicationConfig;
import com.openzoosim.services.IEmailService;
import com.openzoosim.services.ISettingsService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailServiceImpl implements IEmailService {

    @Inject
    ApplicationConfig config;

    @Inject
    Instance<ISettingsService> settingsService;

    @Override
    public boolean send(String toEmail, String subject, String html) {

        var cachedSettings = settingsService.get().getCachedSettings();
        if (!cachedSettings.is_email_enabled) {
            return true;
        }

        SendGrid client = new SendGrid(config.getEmailProviderKey());

        Mail mail = new Mail();

        Email fromEmail = new Email();
        fromEmail.setName("OpenZooSim");
        fromEmail.setEmail("do-not-reply@openzoosim.com");
        mail.setFrom(fromEmail);

        mail.setSubject(subject);

        Personalization personalization = new Personalization();
        Email to = new Email();
        to.setName(toEmail);
        to.setEmail(toEmail);
        personalization.addTo(to);
        
        personalization.setSubject(subject);
        mail.addPersonalization(personalization);

        Content content = new Content();
        content.setType("text/html");
        content.setValue(html);
        mail.addContent(content);

        try {
            final Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
        
            final Response response = client.api(request);

            if (response.getStatusCode() == 202) {
                return true;
            } else {
                System.out.println(response.getStatusCode());
                System.out.println(response.getBody());
                System.out.println(response.getHeaders());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
