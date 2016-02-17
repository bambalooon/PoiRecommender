package pl.edu.agh.eis.poirecommender.application.auth;

import com.google.common.base.Charsets;
import org.springframework.util.Base64Utils;

public class AuthUtil {
    public static String createBasicAuthToken(String email, String password) {
        String credentials = email + ":" + password;
        return Base64Utils.encodeToString(credentials.getBytes(Charsets.US_ASCII));
    }
}
