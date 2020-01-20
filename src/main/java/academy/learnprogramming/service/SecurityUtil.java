/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academy.learnprogramming.service;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;

/**
 *
 * @author siya
 */

@ApplicationScoped
public class SecurityUtil {
    
    public static final String HASHED_PASSWORD_KEY = "hashedPassword";
    public static final String SALT_KEY = "salt";
    public static final String BEARER = "Bearer";
    
    private SecretKey securityKey;
    
    @PostConstruct
    private void init(){
        this.securityKey = this.generateKey();
    }

    @Inject
    private QueryService queryService;

    public Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public boolean passwordMatch(String dbStoredHashedPassword, String saltText, String clearTextPassword){
        ByteSource salt = ByteSource.Util.bytes(Hex.decode(saltText));
        String hashedPassword = hashAndSaltPassword(clearTextPassword, salt);
        
        return hashedPassword.equals(dbStoredHashedPassword);
    }

    public Map<String, String> hashPassword(String clearTextPassword) {
        ByteSource salt = getSalt();

        Map<String, String> credMap = new HashMap<>();
        credMap.put(HASHED_PASSWORD_KEY, hashAndSaltPassword(clearTextPassword, salt));
        credMap.put(SALT_KEY, salt.toHex());
        return credMap;
    }

    public boolean authenticateUser(String email, String password) {
        return queryService.authenticateUser(email, password);
    }

    private ByteSource getSalt() {
        return new SecureRandomNumberGenerator().nextBytes();
    }

    private String hashAndSaltPassword(String clearTextPassword, ByteSource salt) {
        return new Sha512Hash(clearTextPassword, salt, 2000000).toHex();
    }
    
    private SecretKey generateKey() {
        return MacProvider.generateKey(SignatureAlgorithm.HS512);
    }

    /**
     * @return the securityKey
     */
    public SecretKey getSecurityKey() {
        return securityKey;
    }
    
    
}
