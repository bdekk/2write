package nl.bdekk.authapi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.bdekk.authapi.domain.Token;
import nl.bdekk.authapi.domain.User;

import javax.faces.bean.ApplicationScoped;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@ApplicationScoped
public class TokenService {

    private final String TOKEN_SECRET = "secret";
    private final String TOKEN_TYPE = "JWT";
    private final String TOKEN_CONTEXT = "Bearer";

    public Token createAndSignToken(User user) {

        Date expiresAt = new Date();

        String tokenValue = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            tokenValue = JWT.create()
                    .withIssuer(user.getUsername())
                    .sign(algorithm);
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }

        Token token = null;
        if(tokenValue != null) {
            token = new Token();
            token.setValue(TOKEN_CONTEXT.join(" ", tokenValue));
            token.setType(TOKEN_TYPE);
        }
        return token;
    }

    public boolean verifyToken(Token token) {
        String tokenValue = token.getValue();
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(tokenValue);
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return false;
        }
        return true;
    }

}
