package com.openzoosim.modules.health;

import com.openzoosim.modules.user.UserDTO;
import com.openzoosim.modules.user.UserService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/health")
public class HealthController {

    @Inject
    UserService _userService;

    @GET
    // @Produces(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO GetHealthCheck() throws Exception {

        return new UserDTO(_userService.GetUserByEmail("dylanlegendre09@gmail.com"));
    }
}
