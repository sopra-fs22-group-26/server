package ch.uzh.ifi.hase.soprafs22.util;

import javax.crypto.SecretKeyFactory;
import java.security.SecureRandom;

public class passwordHasher {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(passwordHasher.class);

    private final java.security.SecureRandom secureRandom = new SecureRandom();
    private final byte[] salt = secureRandom.generateSeed(12);
    
    
    public String hashFunction(String unhashedPassword) {

        javax.crypto.spec.PBEKeySpec pbeKeySpec = new javax.crypto.spec.PBEKeySpec(
                unhashedPassword.toCharArray(),
                salt,
                10,
                512
        );

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hash = skf.generateSecret(pbeKeySpec).getEncoded();
            return java.util.Base64.getMimeEncoder().encodeToString(hash);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return null;
    }

}
