package es.deusto.mcu.classchat2020;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    private TextView mTextViewUsername;
    private TextView mTextViewUserEmail;
    private ImageView mImageViewUserPhoto;
    private Button mButtonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mTextViewUsername = findViewById(R.id.tv_username);
        mTextViewUserEmail = findViewById(R.id.tv_useremail);
        mImageViewUserPhoto = findViewById(R.id.iv_userphoto);
        mButtonSignOut = findViewById(R.id.b_signout);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        initFirebaseAuth();
        initGoogleApiClient();
    }

    private void signOut() {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null)
                .addApi(Auth.GOOGLE_SIGN_IN_API) .build();
    }

    private void initFirebaseAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            SignInActivity.startActivity(this);
            finish();
        } else {
            mTextViewUserEmail.setText(mFirebaseUser.getEmail());
            mTextViewUsername.setText(mFirebaseUser.getDisplayName());
            if (mFirebaseUser.getPhotoUrl() != null) {
                Glide.with(mImageViewUserPhoto.getContext())
                        .load(mFirebaseUser.getPhotoUrl().toString())
                        .into(mImageViewUserPhoto);
            }
        }
    }

    public static void startActivity(Activity caller) {
        caller.startActivity(new Intent(caller, ChatActivity.class));
    }

}
