package com.benoitletondor.materiallist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.benoitletondor.materiallist.stub.LoginListener;
import com.benoitletondor.materiallist.stub.Webservices;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity containing login screen
 *
 * @author Benoit LETONDOR
 */
public class LoginActivity extends BaseActivity
{
    private final static String TAG = "LoginActivity";

    private final static String PREFERENCE_LAST_LOGIN = "preference_last_login";
    private final static String PREFERENCE_LAST_PASS = "preference_last_pass";

    @Inject
    Webservices webservices;

    @Inject
    SharedPreferences preferences;

    @Bind(R.id.login_edittext)
    EditText loginEditText;

    @Bind(R.id.pass_edittext)
    EditText passEditText;

    @Bind(R.id.login_input_layout)
    View loginInputLayout;

    @Bind(R.id.login_progress_layout)
    View loginProgressLayout;

// -------------------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        setContentViewAndBind(this, R.layout.activity_login);

        String lastLogin = preferences.getString(PREFERENCE_LAST_LOGIN, null);
        if( lastLogin != null )
        {
            loginEditText.setText(lastLogin);
            loginEditText.setSelection(loginEditText.getText().length()); // Put focus at the end of the text
        }

        String lastPass = preferences.getString(PREFERENCE_LAST_PASS, null);
        if( lastPass != null )
        {
            passEditText.setText(lastPass);
        }

        setResult(RESULT_CANCELED);
    }

    @OnClick(R.id.login_button)
    public void submit()
    {
        showLoginProgress(true);

        try
        {
            webservices.login(loginEditText.getText().toString(), passEditText.getText().toString(), new LoginListener()
            {
                @Override
                public void onSucceed()
                {
                    preferences.edit()
                        .putString(PREFERENCE_LAST_LOGIN, loginEditText.getText().toString())
                        .putString(PREFERENCE_LAST_PASS, passEditText.getText().toString())
                        .apply();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(@NonNull final Exception e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            showLoginProgress(false);
                            showError(e);
                        }
                    });
                }
            });
        }
        catch (final Exception e)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    showLoginProgress(false);
                    showError(e);
                }
            });
        }
    }

    /**
     * Show an error to the user depending on the kind of exception
     *
     * @param e encountered error
     */
    private void showError(@NonNull Exception e)
    {
        Log.e(TAG, "Error while login", e);

        final String error;
        if( e instanceof IOException || (e.getCause() != null && (e.getCause() instanceof IOException )))
        {
            error = getString(R.string.login_error_connection_message);
        }
        else if( e instanceof JSONException || (e.getCause() != null && (e.getCause() instanceof JSONException )))
        {
            error = getString(R.string.login_error_login_pass_error);
        }
        else
        {
            error = e.getLocalizedMessage();
        }

        showErrorDialog(error);
    }

    /**
     * Show the loading progress or not, hiding fields and keyboard
     *
     * @param show shall loading be shown ot not
     */
    private void showLoginProgress(boolean show)
    {
        if( show )
        {
            hideKeyboard();
            loginInputLayout.setVisibility(View.GONE);
            loginProgressLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            showKeyboard(loginEditText);
            loginInputLayout.setVisibility(View.VISIBLE);
            loginProgressLayout.setVisibility(View.GONE);
        }
    }
}
