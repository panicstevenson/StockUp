package citris.stockup;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import citris.stockup.grocerylist.GroceryListApplication;
import citris.stockup.grocerylist.ViewGroceriesActivity;
import citris.stockup.grocerylist.ViewListsActivity;
import citris.stockup.grocerylist.WaitSplash;

public class LoginActivity extends Activity {
    Button btn_LoginIn = null;
    Button btn_SignUp = null;
    Button btn_ForgetPass = null;
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private GroceryListApplication app;


    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing Parse SDK
        onCreateParse();

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());

        btn_LoginIn = (Button) findViewById(R.id.btn_login);
        btn_SignUp = (Button) findViewById(R.id.btn_signup);
        btn_ForgetPass = (Button) findViewById(R.id.btn_ForgetPass);
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        btn_LoginIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // get Internet status
                isInternetPresent = cd.isConnectingToInternet();
                // check for Internet status
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    attemptLogin();
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                    showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                }

            }
        });

        btn_SignUp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in =  new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(in);
            }
        });

        btn_ForgetPass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in =  new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(in);
            }
        });



    }

    public void onCreateParse() {
        Parse.initialize(this, "0BqEQPyE7ycnXnarj1YgsGkvgzAlj8tJtkogFQL3", "388Timb3JFccZseI0M0pj92egGqd5DBaHpLr9qVV");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Clicking home takes you back to your lists
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void forgotPassword(){
		/* 
		FragmentManager fm = getSupportFragmentManager();
	     ForgotPasswordDialogFragment forgotPasswordDialog = new ForgotPasswordDialogFragment();
	     forgotPasswordDialog.show(fm, null);
		 */
    }

    public void attemptLogin() {

        clearErrors();

        // Store values at the time of the login attempt.
        String username = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (password.length() < 4) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView =mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUserNameEditText.setError(getString(R.string.error_field_required));
            focusView = mUserNameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            login(username.toLowerCase(Locale.getDefault()), password);
        }
    }

    private void login(String lowerCase, String password) {
        // TODO Auto-generated method stub
        ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // TODO Auto-generated method stub
                if(e == null)
                    loginSuccessful();
                else
                    loginUnSuccessful();
            }
        });

    }

    protected void loginSuccessful() {
        // TODO Auto-generated method stub
        Intent in =  new Intent(LoginActivity.this, WaitSplash.class);
        app = (GroceryListApplication)getApplication();
        app.updateData();
        app.inflateLists();
        startActivity(in);
    }
    protected void loginUnSuccessful() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
        showAlertDialog(LoginActivity.this,"Login", "Username or Password is invalid.", false);
    }

    private void clearErrors(){
        mUserNameEditText.setError(null);
        mPasswordEditText.setError(null);
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
