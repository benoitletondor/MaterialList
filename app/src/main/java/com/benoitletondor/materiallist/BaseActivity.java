package com.benoitletondor.materiallist;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.benoitletondor.materiallist.injection.AppComponent;

import butterknife.ButterKnife;

/**
 * Base activity containing common methods
 *
 * @author Benoit LETONDOR
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private final static String TAG = "BaseActivity";

    /**
     * Call {@link #setContentView(int)} and {@link ButterKnife#bind(Activity)}
     *
     * @param activity the activity
     * @param layoutResID the layout id
     */
    protected void setContentViewAndBind(@NonNull BaseActivity activity, @LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        ButterKnife.bind(activity);
    }

    /**
     * Get the dagger {@link AppComponent}
     *
     * @return the dagger app component
     */
    protected AppComponent getAppComponent()
    {
        return ((App) getApplication()).getComponent();
    }

    /**
     * Hide the keyboard if shown
     */
    protected void hideKeyboard()
    {
        try
        {
            if( getCurrentFocus() != null )
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while hiding keyboard", e);
        }
    }

    /**
     * Show the keyboard and put focus on the given edittext
     *
     * @param editText the edit text to focus
     */
    protected void showKeyboard(@NonNull EditText editText)
    {
        try
        {
            editText.requestFocus();

            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            editText.setSelection(editText.getText().length()); // Put focus at the end of the text
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while showing keyboard", e);
        }
    }

    /**
     * Show a generic error dialog
     *
     * @param error the error to show
     */
    protected void showErrorDialog(@NonNull String error)
    {
        new AlertDialog.Builder(this)
            .setTitle(R.string.error_dialog_title)
            .setMessage(getString(R.string.error_dialog_message, error))
            .setPositiveButton(android.R.string.ok, null)
            .show();
    }
}
