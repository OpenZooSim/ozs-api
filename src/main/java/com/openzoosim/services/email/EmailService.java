package com.openzoosim.services.email;

import com.openzoosim.models.interfaces.IAppLogService;
import com.openzoosim.models.interfaces.IEmailService;
import com.openzoosim.models.interfaces.IEnvService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.io.IOException;

@ApplicationScoped
public class EmailService implements IEmailService {

    @Inject
    Instance<IAppLogService> _log;

    @Inject
    Instance<IEnvService> _envService;

    public boolean sendEmail(String to, String from, String subject, String text) {
        try {
            SendGrid sg = new SendGrid(_envService.get().GetEmailProviderAPIKey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            text = text.replace("\"", "\\\"");
            request.setBody("{\"personalizations\":[{\"to\":[{\"email\":\"" + to + "\"}],\"subject\":\"" + subject + "\"}],\"from\":{\"email\":\"" + from + "\"},\"content\":[{\"type\":\"text/html\",\"value\": \"" + text + "\"}]}");
            Response res = sg.api(request);
            if (res.getStatusCode() != 202) {
                _log.get().warning("EMAIL FAILURE", "API Key may be invalid?", null);
                return false;
            }
            return true;
        } catch (IOException ex) {
            _log.get().warning("EMAIL FAILURE", ex.getMessage(), ex);
            return false;
        }
    }

}
