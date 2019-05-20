package com.yanzhenjie.album.sample.Authorization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.yanzhenjie.album.sample.Home.MainActivity;
import com.yanzhenjie.album.sample.R;


public class LogIn_Activity extends AppCompatActivity {

    EditText username,
            password,
            reg_username,
            reg_password,
            reg_firstName,
            reg_lastName,
            reg_email,
            reg_confirmemail;
    Button button,
            reg_register,
            sign_up,
            mForgetPassword;
    ProgressBar progressBar;
    final int RC_SIGN_IN = 100;
//    //TODO:this has to be changed as to login to gmail
    SignInButton googleSignIn;
    //TODO:this has to be changed as to login
    FirebaseAuth mAuth;
    //CallbackManager callbackManager;
    ProgressDialog mProgressDialog;
    RelativeLayout  rellay2;
    LinearLayout rellay1;
    //TODO:this has to be changed as to login to gmail
    private GoogleApiClient googleApiClient;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        //TODO:this has to be changed as to login to gmail
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("861905530205-4lvbor6uqeavngcdjqvbsd984mu6ca0v.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        initialization();
        ClickLogin();
        //TODO:this has to be changed as to login to gmail
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LogIn_Activity.this, MainActivity.class));
            finish();
        }
        googleSignIn = findViewById(R.id.sign_in_button);
//TODO:this has to be changed as to login to gmail
                googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        sign_up=findViewById(R.id.signup);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickSignUp();
            }
        });
        mForgetPassword=findViewById(R.id.forget_password);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clickforgot();
            }
        });
    }

    /**
     * this method will be used to initialize the component of screen
     */
    private void initialization() {
//TODO:this has to be changed as to login to gmail

        mAuth = FirebaseAuth.getInstance();


        username=findViewById(R.id.email);
        password=findViewById(R.id.password);
        button=findViewById(R.id.upload);

        rellay1 =  findViewById(R.id.rellay1);
        rellay2 =  findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        mProgressDialog=new ProgressDialog(this);
        //TODO:this has to be changed as to login to gmail
       // callbackManager = CallbackManager.Factory.create();
        googleApiClient.connect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //TODO:this has to be changed as to login to gmail
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("tag", "Google sign in failed", e);
                // ...
            }
        }
    }


    //TODO:this has to be changed as to login to gmail
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Tag", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(LogIn_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }


    private void userLogin() {
        String email = username.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        //TODO:this has to be changed as to login to gmail
        mAuth.signInWithEmailAndPassword(email, user_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login Sucessfull", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(LogIn_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ClickLogin() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status=true;
                final String mUserEmail=username.getText().toString();
                final String mPassword=password.getText().toString();

                if (mUserEmail.isEmpty() ) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    username.setError("Username should not be empty");
                    status=false;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(mUserEmail).matches() && status) {
                    Snackbar snackbar = Snackbar.make(view, "Please Enter A Valid Email",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    password.setError("Please Enter A Valid Email");
                    status=false;
                }

                if (mPassword.isEmpty() || mPassword.length()<6 && status) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    password.setError("Password should not be empty");
                    status=false;
                }

                if (mPassword.length()<6 && status) {
                    Snackbar snackbar = Snackbar.make(view, "Minimum length six character",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    password.setError("Minimum length six character");
                    status=false;
                }
                if(status)
                {
                    userLogin();
                }
            }
        });

    }

    private void ClickSignUp() {

        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.register, null);
        dialog.setView(dialogView);

        reg_username = dialogView.findViewById(R.id.reg_username);
        reg_password = dialogView.findViewById(R.id.reg_password);
        reg_firstName = dialogView.findViewById(R.id.reg_firstName);
        reg_lastName = dialogView.findViewById(R.id.reg_lastName);
        reg_email = dialogView.findViewById(R.id.reg_email);
        reg_confirmemail = dialogView.findViewById(R.id.reg_confirmemail);
        reg_register = dialogView.findViewById(R.id.reg_register);
        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean status = true;
                final String user_name=reg_username.getText().toString();
                final String password=reg_password.getText().toString();
                final String register_email=reg_email.getText().toString();
                if (user_name.trim().isEmpty()) {
                    reg_username.setError("Please fill out this field");
                    status = false;
                }
                if (password.trim().isEmpty()) {
                    reg_password.setError("Please fill out this field");
                    status = false;
                }
                if ( password.trim().length()<6) {
                    reg_password.setError("Minimum length six character");
                    status = false;
                }
                if (reg_firstName.getText().toString().trim().isEmpty()) {

                    reg_firstName.setError("Please fill out this field");
                    status = false;
                }
                if (reg_lastName.getText().toString().trim().isEmpty()) {
                    reg_lastName.setError("Please fill out this field");
                    status = false;
                }
                if ( register_email.trim().isEmpty() ) {
                    reg_email.setError("Please fill out this field");
                    status = false;
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(register_email).matches() ) {
                        reg_email.setError("Please enter a valid email");
                        status = false;
                    }
                    if (reg_confirmemail.getText().toString().trim().isEmpty() ) {
                        reg_confirmemail.setError("Please fill out this field");
                        status = false;
                    }
                    if (!reg_confirmemail.getText().toString().trim().equals(register_email) ) {
                        reg_confirmemail.setError("Email id Does not match");
                        status = false;
                    }
                }
                if(status) {
                    mProgressDialog.setTitle("Signing Up");
                    mProgressDialog.show();
                    //TODO:this has to be changed as to login to gmail
                    mAuth.createUserWithEmailAndPassword(register_email, password)
                            .addOnCompleteListener(LogIn_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        dialog.dismiss();
                                        Toast.makeText(LogIn_Activity.this, "Sucessfully Registered ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LogIn_Activity.this, "Registration failded ,Try again ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        dialog.show();
    }

    private void Clickforgot() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.forgetpassword,null);
        dialog.setView(dialogView);
        reg_username = dialogView.findViewById(R.id.reg_username);
        reg_register = dialogView.findViewById(R.id.reg_register);
        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean status=true;
                String email=reg_username.getText().toString().trim();
                if (email.isEmpty() ) {
                    reg_username.setError("Please fill out this field");
                    status = false;
                } else {

                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
                        reg_username.setError("Please enter a valid email");
                        status = false;
                    }
                }
                if(status){
                    mProgressDialog.setTitle("Signing Up");
                    mProgressDialog.show();
//TODO:this has to be changed as to login to gmail

                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProgressDialog.dismiss();
                            Toast.makeText(LogIn_Activity.this, "Password reset email send", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialog.show();
    }
}
