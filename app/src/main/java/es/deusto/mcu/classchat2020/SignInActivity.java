package es.deusto.mcu.classchat2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    private SignInButton mButtonGoogleSignIn;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQ_CODE_SIGN_IN = 0;
    private static final String TAG = SignInActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mButtonGoogleSignIn = findViewById(R.id.b_google_sign_in);
        mButtonGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonGoogleSignIn.setEnabled(false);
                signIn();
            }
        });
        configureGoogleSignInAndCreateClient();
    }

    private void configureGoogleSignInAndCreateClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this) .enableAutoManage(
                this /* FragmentActivity */,
                new GoogleApiClient.OnConnectionFailedListener() { @Override
                public void onConnectionFailed(
                        @NonNull ConnectionResult connectionResult) {
                    Log.d(TAG, "onConnectionFailed:" + connectionResult); Toast.makeText(getBaseContext(),
                            "Google Play Services error.", Toast.LENGTH_SHORT).show();
                } })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntetnt = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntetnt, REQ_CODE_SIGN_IN);
    }

    public static void startActivity(Activity caller) {
        caller.startActivity(new Intent(caller, SignInActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SIGN_IN:
                if (resultCode != RESULT_CANCELED) {
                    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    if (result.isSuccess()) {
                        // Google SignIn successful, authenticate with Firebase
                        GoogleSignInAccount account = result.getSignInAccount();
                        firebaseAuthWithGoogle(account);
                    } else {
                        // Google SignIn failed
                        Log.e(TAG, "Google Sign In failed.");
                        mButtonGoogleSignIn.setEnabled(true);
                    }
                } else {
                    // Google SignIn cancelled
                    mButtonGoogleSignIn.setEnabled(true); }
                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Auth. failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            ChatActivity.startActivity(SignInActivity.this);
                            finish();
                        }
                    }
                });
    }
}


































