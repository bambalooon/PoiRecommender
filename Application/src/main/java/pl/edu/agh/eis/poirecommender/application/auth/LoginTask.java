package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.google.common.base.Charsets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class LoginTask extends AsyncTask<String, Void, LoginTask.Result> {
    private final LoginActivity loginActivity;
    private final ProgressDialog progressDialog;

    @Override
    protected Result doInBackground(String... params) {
        try {
            String loginUrl = params[0];
            String basicAuthToken = createBasicAuthToken(params[1], params[2]);
            ResponseEntity<String> response = new RestTemplate().exchange(loginUrl, HttpMethod.GET,
                    new HttpEntity<>(createBasicAuthHeaders(basicAuthToken)), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Result.success(basicAuthToken);
            }
        } catch (Exception e) {
            log.error("Login failed: ", e);
        }
        return Result.fail();
    }

    private String createBasicAuthToken(String email, String password) {
        String credentials = email + ":" + password;
        return Base64Utils.encodeToString(credentials.getBytes(Charsets.US_ASCII));
    }

    private MultiValueMap<String, String> createBasicAuthHeaders(String basicAuthToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + basicAuthToken);
        return headers;
    }

    @Override
    protected void onPostExecute(LoginTask.Result result) {
        progressDialog.dismiss();
        if (result.isSuccess()) {
            loginActivity.onLoginSuccess(result.getBasicAuthToken());
        } else {
            loginActivity.onLoginFailed();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    static class Result {
        private final boolean success;
        private final String basicAuthToken;

        private static Result success(String basicAuthToken) {
            return new Result(true, basicAuthToken);
        }
        private static Result fail() {
            return new Result(false, null);
        }
    }
}
