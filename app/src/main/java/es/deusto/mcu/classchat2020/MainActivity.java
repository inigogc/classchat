package es.deusto.mcu.classchat2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private final String FA_EVENT_START_NAME = "click_start";
    private final String FA_EVENT_START_FIELD_SOURCE = "source";
    private final String FA_EVENT_START_FIELD_SOURCE_IMAGE = "image";
    private final String FA_EVENT_START_FIELD_SOURCE_BUTTON = "button";
    private final String FA_EVENT_ABOUT_NAME = "click_about";
    private final String FA_USER_PROP_INTERESTED_NAME = "interested";
    private final String FA_USER_PROP_INTERESTED_VALUE_HIGH = "high";

    Button mButtonStart;
    Button mButtonAbout;
    ImageView mImageViewMainIcon;
    private int aboutClicksCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonStart = findViewById(R.id.b_start);
        mButtonAbout = findViewById(R.id.b_about);
        mImageViewMainIcon = findViewById(R.id.iv_main_icon);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FA_EVENT_START_FIELD_SOURCE_BUTTON);
            }
        });
        mImageViewMainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(FA_EVENT_START_FIELD_SOURCE_IMAGE);
            }
        });
        mButtonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about();
            }
        });
    }

    private void about() {
        mFirebaseAnalytics.logEvent(FA_EVENT_ABOUT_NAME, null);
        aboutClicksCounter++;
        if (aboutClicksCounter >= 3) {
            registerUserAsInterested();
            aboutClicksCounter = 0;
        }
    }

    private void registerUserAsInterested() {
        Toast.makeText(getBaseContext(), R.string.thanks_interested, Toast.LENGTH_LONG).show();
        mFirebaseAnalytics.setUserProperty(
                FA_USER_PROP_INTERESTED_NAME, FA_USER_PROP_INTERESTED_VALUE_HIGH);
    }

    private void start(String source) {
        Bundle bundle = new Bundle();
        bundle.putString(FA_EVENT_START_FIELD_SOURCE, source);
        mFirebaseAnalytics.logEvent(FA_EVENT_START_NAME, bundle);
        ChatActivity.startActivity(this);
        finish();
    }
}
