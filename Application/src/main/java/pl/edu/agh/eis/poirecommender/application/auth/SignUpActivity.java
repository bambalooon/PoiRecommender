package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class SignUpActivity extends AppCompatActivity {
    private Config config;
    private SignUpTask activeSignUpTask;

    @Bind(R.id.input_email)
    EditText emailText;
    @Bind(R.id.input_password)
    EditText passwordText;
    @Bind(R.id.input_password_repeat)
    EditText passwordRepeatText;
    @Bind(R.id.btn_signup)
    Button signUpButton;

    @BindString(R.string.signup_creating_account)
    String creatingAccountMessage;
    @BindString(R.string.signup_failed)
    String signUpFailedMessage;
    @BindString(R.string.login_wrong_email)
    String wrongEmailMessage;
    @BindString(R.string.login_wrong_password)
    String wrongPasswordMessage;
    @BindString(R.string.signup_wrong_password_repeat)
    String wrongPasswordRepeatMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        config = new Config(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        if (activeSignUpTask != null) {
            activeSignUpTask.cancel(true);
            activeSignUpTask = null;
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_signup)
    void signUp() {
        if (!validate()) {
            onSignUpFailed();
            return;
        }
        signUpButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(creatingAccountMessage);
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordRepeat = passwordRepeatText.getText().toString();

        activeSignUpTask = new SignUpTask(this, progressDialog);
        activeSignUpTask.execute(config.getPoiRecommenderSignUpUrl(), email, password, passwordRepeat);
    }

    public void onSignUpSuccess(String basicAuthToken) {
        activeSignUpTask = null;
        signUpButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra(LoginActivity.AUTH_TOKEN_EXTRA, basicAuthToken);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignUpFailed() {
        activeSignUpTask = null;
        Toast.makeText(getBaseContext(), signUpFailedMessage, Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordRepeat = passwordRepeatText.getText().toString();

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

        if (!password.equals(passwordRepeat)) {
            passwordRepeatText.setError(wrongPasswordRepeatMessage);
            valid = false;
        } else {
            passwordRepeatText.setError(null);
        }

        return valid;
    }
}
