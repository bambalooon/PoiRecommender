package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.edu.agh.eis.poirecommender.Config;
import pl.edu.agh.eis.poirecommender.R;

public class LoginActivity extends AppCompatActivity {
    public static final String ACTION_LOGIN = "pl.edu.agh.eis.poirecommender.action.LOGIN";
    public static final String AUTH_TOKEN_EXTRA = "auth_token";

    private Config config;
    private LoginTask activeLoginTask;

    @Bind(R.id.input_email)
    EditText emailText;
    @Bind(R.id.input_password)
    EditText passwordText;
    @Bind(R.id.btn_login)
    Button loginButton;

    @BindString(R.string.login_authenticating)
    String authenticatingMessage;
    @BindString(R.string.login_failed)
    String loginFailedMessage;
    @BindString(R.string.login_wrong_email)
    String wrongEmailMessage;
    @BindString(R.string.login_wrong_password)
    String wrongPasswordMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        config = new Config(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (activeLoginTask != null) {
            activeLoginTask.cancel(true);
        }
        moveTaskToBack(true);
    }

    @OnClick(R.id.btn_login)
    void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        loginButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(authenticatingMessage);
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        activeLoginTask = new LoginTask(this, progressDialog);
        activeLoginTask.execute(config.getPoiRecommenderLoginUrl(), email, password);
    }

    public void onLoginSuccess(String basicAuthToken) {
        activeLoginTask = null;
        loginButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra(AUTH_TOKEN_EXTRA, basicAuthToken);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onLoginFailed() {
        activeLoginTask = null;
        Toast.makeText(getBaseContext(), loginFailedMessage, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(wrongEmailMessage);
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError(wrongPasswordMessage);
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
