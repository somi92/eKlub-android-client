package rs.fon.eklubmobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.util.EKlubApplication;

public class StartActivity extends Activity {

    private String mAuthUrl = "http://192.168.1.181:8081/oauth/authorize";
    private String mAuthClientId = "sampleClientId";
    private String mAuthResponseType = "token";
    private String mAuthRedirectUri = "https://mpowafin.co.za/assets/icon-tick.svg";
    private String mAuthScope = "read";
    private String mAuthUriTemplate = "%s?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s";

    private TextView mAuthMessage;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuthMessage = (TextView) findViewById(R.id.txtAuthMessage);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.startsWith(mAuthRedirectUri) && url.contains("#access_token=")) {
                    String accessToken = retrieveAccessToken(url);
                    if(accessToken != null) {
                        EKlubApplication eKlubContext = (EKlubApplication) getApplicationContext();
                        eKlubContext.setmAccessToken(accessToken);
                        Intent intent = new Intent(StartActivity.this, TrainingActivity.class);
                        startActivity(intent);
                    } else {
                        mAuthMessage.setText("Greška prilikom autentikacije. Pokušajte ponovo.");
                    }
                }
            }
        });

        String authUri = String.format(mAuthUriTemplate, mAuthUrl, mAuthResponseType, mAuthClientId, mAuthRedirectUri, mAuthScope);
        mWebView.loadUrl(authUri);
    }

    private String retrieveAccessToken(String url) {
        int startIndex = url.indexOf("#access_token");
        int endIndex = url.indexOf("&", startIndex);
        if(startIndex < 1)
            return null;
        String accessToken = url.substring(startIndex, endIndex).split("=")[1];
        return accessToken;
    }
}
