// package com.openzoosim.controllers;

// import com.openzoosim.annotations.Authorize;
// import com.openzoosim.config.ApplicationConfig;
// import com.openzoosim.models.dtos.ListCreateRequest;
// import com.openzoosim.redis.UserSessionPOJO;
// import com.openzoosim.services.IAppLogService;
// import com.openzoosim.services.IListService;

// import io.netty.util.internal.StringUtil;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.enterprise.inject.Instance;
// import jakarta.inject.Inject;
// import jakarta.ws.rs.Consumes;
// import jakarta.ws.rs.POST;
// import jakarta.ws.rs.Path;
// import jakarta.ws.rs.Produces;
// import jakarta.ws.rs.container.ContainerRequestContext;
// import jakarta.ws.rs.core.Context;
// import jakarta.ws.rs.core.MediaType;
// import jakarta.ws.rs.core.Response;

// @Path("/lists")
// @Consumes(MediaType.APPLICATION_JSON)
// @ApplicationScoped
// public class ListController {

//     @Inject
//     ApplicationConfig config;

//     @Inject
//     Instance<IListService> listService;

//     @Inject
//     Instance<IAppLogService> logService;

//     @POST
//     @Path("/")
//     @Authorize
//     @Produces(MediaType.APPLICATION_JSON)
//     public Response createList(ListCreateRequest request, @Context ContainerRequestContext requestContext) {

//         try {
//             if (StringUtil.isNullOrEmpty(request.name())) {
//                 return Response.status(Response.Status.BAD_REQUEST)
//                         .entity("List name is required")
//                         .build();
//             }

//             UserSessionPOJO userSession = (UserSessionPOJO) requestContext.getProperty("sessionUser");

//             var newList = this.listService.get().createList(request, userSession);

//             return Response.status(Response.Status.CREATED)
//                     .entity(newList)
//                     .build();
//         } catch (Exception e) {
//             this.logService.get().error(e.getMessage(), e);
//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                     .entity(e.getMessage())
//                     .build();
//         }
//     }

// }

