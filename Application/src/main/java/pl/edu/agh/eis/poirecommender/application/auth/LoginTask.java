package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class LoginTask extends AsyncTask<String, Void, TaskResult> {
    private final LoginActivity loginActivity;
    private final ProgressDialog progressDialog;

    @Override
    protected TaskResult doInBackground(String... params) {
        try {
            String loginUrl = params[0];
            String basicAuthToken = AuthUtil.createBasicAuthToken(params[1], params[2]);
            ResponseEntity<String> response = new RestTemplate().exchange(loginUrl, HttpMethod.GET,
                    new HttpEntity<>(createBasicAuthHeaders(basicAuthToken)), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return TaskResult.success(basicAuthToken);
            }
        } catch (Exception e) {
            log.error("Login failed: ", e);
        }
        return TaskResult.fail();
    }

    private MultiValueMap<String, String> createBasicAuthHeaders(String basicAuthToken) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + basicAuthToken);
        return headers;
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        progressDialog.dismiss();
        if (result.isSuccess()) {
            loginActivity.onLoginSuccess(result.getBasicAuthToken());
        } else {
            loginActivity.onLoginFailed();
        }
    }
}
