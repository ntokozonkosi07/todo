package academy.learnprogramming.rest;

import academy.learnprogramming.entity.User;
import academy.learnprogramming.service.SecurityUtil;
import academy.learnprogramming.service.TodoService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDateTime;
import java.util.Date;

@Path("user")
public class UserRest {

    @Inject
    private SecurityUtil securityUtil;

    @Inject
    private TodoService todoService;

    @Context
    private UriInfo uriInfo;

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @NotNull @FormParam("email") String email,
            @NotNull @FormParam("password") String password
    ) {
        // Authenticate the user
        // Generate token
        // Return token in response header to client

        boolean authenticated = securityUtil.authenticateUser(email, password);
        if (!authenticated) {
            throw new SecurityException("Email or password is not valid");
        }

        String token = generateToken(email);

        return Response.ok().header(HttpHeaders.AUTHORIZATION, SecurityUtil.BEARER + " " + token).build();
    }

    private String generateToken(String email) {
        SecretKey securityKey = securityUtil.getSecurityKey();
        return Jwts.builder().setSubject(email)
                .setIssuedAt(new Date())
                .setIssuer(uriInfo.getBaseUri().toString())
                .setAudience(uriInfo.getAbsolutePath().toString())
                .setExpiration(securityUtil.toDate(LocalDateTime.now().plusMinutes(15)))
                .signWith(SignatureAlgorithm.HS512, securityKey)
                .compact();
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(@NotNull User user) {
        todoService.saveUser(user);
        return Response.ok(user).build();
    }
}
