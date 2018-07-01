package ke.co.coansinternational.mydiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.coansinternational.mydiary.data.DataAnalysis;

public class SignUp extends AppCompatActivity {
    public static final String TAG = SignUp.class.getSimpleName();
    ProgressDialog progressDialog;
    TextInputEditText names, idno, email, pas, phoneno, occupation;
    Button reg;
    AlertDialog.Builder builder;
    String a, b, c, d, e, f, g, h, i, j, k;
    String number;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        names = findViewById(R.id.firstlast);
        idno = findViewById(R.id.clientidno);
        occupation = findViewById(R.id.occupation);
        email = findViewById(R.id.email);
        pas = findViewById(R.id.password);
        phoneno = findViewById(R.id.phoneno);
        reg = findViewById(R.id.register);
        mdata = FirebaseDatabase.getInstance().getReference();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                createAccount(email.getText().toString(), pas.getText().toString());
            }
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    // ...
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            UserProfileChangeRequest profileupdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(c).build();
                            assert user != null;
                            user.updateProfile(profileupdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        writeNewUser(a, b, c, d);
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Account Registered for " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(SignUp.this, "Sign Up failed..Retry",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailad = email.getText().toString();
        if (TextUtils.isEmpty(emailad)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String password = pas.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pas.setError("Required.");
            valid = false;
        } else {
            pas.setError(null);
        }
        String allnames = names.getText().toString();
        if (TextUtils.isEmpty(allnames)) {
            names.setError("Required.");
            valid = false;
        } else {
            names.setError(null);
        }
        String id = idno.getText().toString();
        if (TextUtils.isEmpty(id)) {
            idno.setError("Required.");
            valid = false;
        } else {
            idno.setError(null);
        }
        String phonenos = phoneno.getText().toString();
        if (TextUtils.isEmpty(phonenos)) {
            phoneno.setError("Required.");
            valid = false;
        } else {
            phoneno.setError(null);
        }
        String occupations = occupation.getText().toString();
        if (TextUtils.isEmpty(occupations)) {
            occupation.setError("Required.");
            valid = false;
        } else {
            occupation.setError(null);
        }

        return valid;
    }

    private void writeNewUser(String name, String clientidno, String phonenumber, String occupation) {

        DataAnalysis user = new DataAnalysis(name, clientidno, phonenumber, occupation);

        mdata.child("users").setValue(user);
    }

}
