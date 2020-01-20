/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.rest;

import academy.learnprogramming.service.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * @author siya
 */
@Auth
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    SecurityUtil securityUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Grab token from the headaer of the request using AUTHORIZATION constant
        // Throw an exception with a message if there's no token
        // Parse the token
        // If parsing succeeds, proceed
        // Otherwise we throw an exception with message

        String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authString == null || authString.isEmpty() || !authString.startsWith(SecurityUtil.BEARER)) {

            // Beautify this exception
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        String token = authString.substring(SecurityUtil.BEARER.length()).trim();

        try {
            Key key = securityUtil.getSecurityKey(); // some security key
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            SecurityContext originalContect = requestContext.getSecurityContext();

            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        @Override
                        public String getName() {
                            return claimsJws.getBody().getSubject();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    return originalContect.isUserInRole(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContect.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return originalContect.getAuthenticationScheme();
                }
            });
        } catch (Exception e) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}
