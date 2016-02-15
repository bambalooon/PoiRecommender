package pl.edu.agh.eis.poirecommender.application.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthActivity extends Activity {
    public static final String ACTION_AUTH = "pl.edu.agh.eis.poirecommender.action.AUTHENTICATE";
    public static final String AUTH_TOKEN_EXTRA = "auth_token";
    private static final String AUTH_PAGE = "http://pr.biz.tm/login";
    private static final String AUTH_TOKEN = "remember-me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String cookies = CookieManager.getInstance().getCookie(url);
                String token = extractToken(cookies);
                if (token != null) {
                    Intent intent = new Intent();
                    intent.putExtra(AUTH_TOKEN_EXTRA, token);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(AUTH_PAGE);
    }

    private String extractToken(String cookies) {
        for (String cookie : cookies.split(";")) {
            String[] parts = cookie.split("=", 2);
            if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(AUTH_TOKEN)) {
                return parts[1];
            }
        }
        return null;
    }
}
