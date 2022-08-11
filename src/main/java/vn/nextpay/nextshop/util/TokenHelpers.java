package vn.nextpay.nextshop.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import vn.nextpay.nextshop.constant.Constant;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class TokenHelpers {

    public static Claims verifyToken(String token) {
        try {
            PublicKey publicKey = getPublicKey();
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("Error when verifying token from request: ", e);
            log.info("Using JWT_SECRET");
            String jwtSecret = System.getenv("JWT_SECRET");
            Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
            if(claims!=null){
                return claims;
            } else {
                return null;
            }
        }
    }

    private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, SignatureException {
        String publicKey =  Constant.PUBLIC_KEY.replaceAll("\r\n", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyFactory kf = KeyFactory.getInstance("RSA");
        EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        return kf.generatePublic(keySpec);
    }

}
