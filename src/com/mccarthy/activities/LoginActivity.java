package com.mccarthy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Response;
import com.cloudmine.api.ACMUser;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.Strings;
import com.cloudmine.api.rest.response.LoginResponse;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.utility.ErrorHandling;
import com.mccarthy.utility.PreferenceSaver;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * <br>
 * Copyright CloudMine LLC. All rights reserved<br>
 * See LICENSE file included with SDK for details.
 */
@ContentView(R.layout.login)
public class LoginActivity extends RoboActionBarActivity {

    public static Intent newInstance(Context callingContext, PreferenceSaver preferenceSaver) {
        Intent intent = new Intent(callingContext, LoginActivity.class);
        preferenceSaver.clearSessionTokenAndAccessList(callingContext);
        return intent;
    }

    @Inject
    private ErrorHandling errorHandling;
    @Inject
    private PreferenceSaver preferenceSaver;
    @InjectView(R.id.txt_password)
    private EditText passwordTxt;
    @InjectView(R.id.txt_username)
    private EditText usernameTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CMSessionToken sessionToken = preferenceSaver.getSessionToken(this);
        if(sessionToken != null) {
            startActivity(FindArtActivity.newIntent(this, sessionToken));
        }
    }

    public void onLogin(View view) {

        String password = passwordTxt.getText().toString();
        String username = usernameTxt.getText().toString();
        boolean hadErrors = false;
        if(Strings.isEmpty(password)) {
            hadErrors = true;
            passwordTxt.setError(getText(R.string.account_missing_password));
        } else {
            passwordTxt.setError(null);
        }
        if(Strings.isEmpty(username)) {
            hadErrors = true;
            usernameTxt.setError(getText(R.string.account_missing_username));
        } else {
            usernameTxt.setError(null);
        }
        ACMUser user = new ACMUser(null, username, password);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(R.string.logging_in);;
        user.login(this, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                preferenceSaver.storeSessionToken(LoginActivity.this, loginResponse.getSessionToken());
                dialog.dismiss();
                startActivity(FindArtActivity.newIntent(LoginActivity.this, loginResponse.getSessionToken()));
            }
        }, errorHandling.defaultErrorListener(this, R.string.failed_login, dialog));
    }

    public void onCreateAccount(View view) {
        startActivity(CreateAccountActivity.newIntent(this));
    }
}
