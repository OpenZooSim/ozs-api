package com.openzoosim.modules.email;

import com.openzoosim.common.EnvService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;

@ApplicationScoped
public class EmailService {

    @Inject
    EnvService _envService;

    public boolean SendEmail(String to, String from, String subject, String text) {
        try {
            SendGrid sg = new SendGrid(_envService.GetEmailProviderAPIKey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            text = text.replace("\"", "\\\"");
            request.setBody("{\"personalizations\":[{\"to\":[{\"email\":\"" + to + "\"}],\"subject\":\"" + subject + "\"}],\"from\":{\"email\":\"" + from + "\"},\"content\":[{\"type\":\"text/html\",\"value\": \"" + text + "\"}]}");
            Response res = sg.api(request);
            return true;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

}
