package ke.co.coansinternational.mydiary;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ke.co.coansinternational.mydiary.data.utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 0;
    TextInputEditText username_me, password_me;
    TextView forgotpassword, signUp;
    Button login_btn;
    ProgressDialog progressDialog;
    String username, password;
    CheckBox saveLoginCheckBox;
    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;
    GoogleSignInClient signInOptions;
    GoogleSignInOptions gso;
    GoogleSignInAccount signInAccount;
    SignInButton signInButton;
    utils utils;
    private FirebaseAuth mAuth;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ke.co.coansinternational.mydiary.data.utils.getDatabase();
        getSupportActionBar().hide();
        ke.co.coansinternational.mydiary.data.utils.getDatabase();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        signInOptions = GoogleSignIn.getClient(this, gso);
        signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        username_me = findViewById(R.id.username);
        password_me = findViewById(R.id.password);
        forgotpassword = findViewById(R.id.forgot_password);
        signUp = findViewById(R.id.registerhere);
        saveLoginCheckBox = findViewById(R.id.remember_me);
        login_btn = findViewById(R.id.login);
        signInButton = findViewById(R.id.googlesignUp);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

//        if the user has checked the remember me button

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            username_me.setText(loginPreferences.getString("username", ""));
            password_me.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        forgotpassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            progressDialog.dismiss();
                            assert user != null;
                            Toast.makeText(LoginActivity.this, "Welcome back ", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "SignIn Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = username_me.getText().toString();
        if (TextUtils.isEmpty(email)) {
            username_me.setError("Required.");
            valid = false;
        } else {
            username_me.setError(null);
        }
        String password = password_me.getText().toString();
        if (TextUtils.isEmpty(password)) {
            password_me.setError("Required.");
            valid = false;
        } else {
            password_me.setError(null);
        }

        return valid;
    }

    private void forgottenpassword() {
        if (username_me.getText().toString().equals("")) {
            Toast.makeText(this, "Kindly enter your email in the email box above", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = username_me.getText().toString();
        mAuth.sendPasswordResetEmail(email);
        Toast.makeText(this, "Email verification sent to your email kindly check to reset password", Toast.LENGTH_SHORT).show();
    }

    private void setPreferenceOnRemMe() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(username_me.getWindowToken(), 0);

        username = username_me.getText().toString();
        password = password_me.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Check if user is signed in (non-null) and go to next activity.
            Toast.makeText(this, "Welcome " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {

        }
        if (signInAccount != null) {
            // Check if user is signed in (non-null) and update UI accordingly.
            Toast.makeText(this, "Welcome ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            signInButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerhere) {
            startActivity(new Intent(LoginActivity.this, SignUp.class));
        } else if (i == R.id.login) {
            setPreferenceOnRemMe();
            signIn(username_me.getText().toString(), password_me.getText().toString());
        } else if (i == R.id.forgot_password) {
            forgottenpassword();
        } else if (i == R.id.googlesignUp) {
            signInGoogle();
        }


    }

    private void signInGoogle() {
        Intent signInIntent = signInOptions.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            progressDialog.hide();

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
