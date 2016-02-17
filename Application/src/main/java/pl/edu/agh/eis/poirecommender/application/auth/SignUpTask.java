package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.edu.agh.eis.poirecommender.model.UserCreateForm;

@Slf4j
@RequiredArgsConstructor
public class SignUpTask extends AsyncTask<String, Void, TaskResult> {
    private final SignUpActivity signUpActivity;
    private final ProgressDialog progressDialog;

    @Override
    protected TaskResult doInBackground(String... params) {
        try {
            String signUpUrl = params[0];
            String email = params[1];
            String password = params[2];

            UserCreateForm userCreateForm = new UserCreateForm();
            userCreateForm.setEmail(email);
            userCreateForm.setPassword(password);
            userCreateForm.setPasswordRepeated(params[3]);

            ResponseEntity<Object> response = new RestTemplate().postForEntity(signUpUrl, userCreateForm, Object.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return TaskResult.success(AuthUtil.createBasicAuthToken(email, password));
            }
        } catch (Exception e) {
            log.error("Sign up failed: ", e);
        }
        return TaskResult.fail();
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        progressDialog.dismiss();
        if (result.isSuccess()) {
            signUpActivity.onSignUpSuccess(result.getBasicAuthToken());
        } else {
            signUpActivity.onSignUpFailed();
        }
    }
}

