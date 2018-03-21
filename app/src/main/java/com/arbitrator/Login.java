package com.arbitrator;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {


    Button login, c;
    TextView em, pwd, reg;
    String email = "anushkkrastogi@gmail.com";
    String password = "password";
    SignInButton sib;
    String arr[][];
    String u;


    private FirebaseAuth mAuth;


    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.btn_login);
        em = (TextView) findViewById(R.id.input_email);
        pwd = (TextView) findViewById(R.id.input_password);
        reg = (TextView) findViewById(R.id.link_signup);
        sib = (SignInButton) findViewById(R.id.gsio);

        sib.setColorScheme(SignInButton.COLOR_LIGHT);

        u = getResources().getString(R.string.url2);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bi = new Intent(getApplicationContext(), Register.class);
                startActivity(bi);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        sib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gsin();
            }
        });
    }

    private void check() {
        try {
            JSONObject jo = null;
            arr = new String[][]{{"email", em.getText().toString()}, {"password", pwd.getText().toString()}};
            Helper pa = new Helper(u + "login", 2, arr);
            JsonHandler jh = new JsonHandler();
            try {
                jo = jh.execute(pa).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (jo.has("error")) {
                Toast.makeText(getApplicationContext(), "nay", Toast.LENGTH_SHORT).show();
            } else {
                gotomain();
            }
        } catch (Exception e) {
            Log.d("nrml check", e.getMessage());
        }
    }

    private void Gsin() {
        Intent sii = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(sii, RC_SIGN_IN);
    }

    private void gotomain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser account = mAuth.getCurrentUser();
        start(account);
    }

    public void start(FirebaseUser a) {
        if (a != null) {
            //sendmail(a);
/*
            try {
                JSONObject jo = null;
                Helper pa = new Helper(u + "emailcheck/"+a.getEmail(), 1, arr);
                JsonHandler jh = new JsonHandler();
                try {
                    jo = jh.execute(pa).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (jo.isNull("error")) {
                    Toast.makeText(getApplicationContext(), "Unregistered Email Entered", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getApplicationContext(), Register.class);
                    startActivity(i);
                } else {
*/
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            signIn();
            finish();
/*
                }
            } catch (Exception e) {
                Log.d("google", e.getMessage());
            }
*/
        }
    }

    private void signIn() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            start(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void sendmail(FirebaseUser a) {
        a.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("sjdfh", "email sent");
                        }
                    }
                });
    }

}
