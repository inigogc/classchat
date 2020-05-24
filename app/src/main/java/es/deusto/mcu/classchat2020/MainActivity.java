package es.deusto.mcu.classchat2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private static final String MSG_DATA_KEY_ADD_TITLE = "addTitle";
    private static final String MSG_DATA_KEY_ADD_DESC = "addDesc";
    private static final String MSG_DATA_KEY_ADD_IMAGE_URL = "addImgUrl";
    private static final String TAG = "FCM";
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

    private TextView tvAddTitle;
    private TextView tvAddDescription;
    private ImageView ivAddImage;
    private View addLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonStart = findViewById(R.id.b_start);
        mButtonAbout = findViewById(R.id.b_about);
        mImageViewMainIcon = findViewById(R.id.iv_main_icon);

        tvAddTitle = findViewById(R.id.tv_add_title);
        tvAddDescription = findViewById(R.id.tv_add_desc);
        ivAddImage = findViewById(R.id.iv_add_img);
        addLayout = findViewById(R.id.l_add_container);

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

        checkFCMMessage(getIntent().getExtras());
    }

    private void checkFCMMessage(Bundle fcmMessageData) {
        if (null != fcmMessageData) {
            String addTitle = fcmMessageData.getString(MSG_DATA_KEY_ADD_TITLE);
            String addDesc = fcmMessageData.getString(MSG_DATA_KEY_ADD_DESC);
            String addImageUrl = fcmMessageData.getString(MSG_DATA_KEY_ADD_IMAGE_URL);
            Log.d(TAG, "checkFCMMessage: Title=" + addTitle);
            Log.d(TAG, "checkFCMMessage: Desc=" + addDesc);
            Log.d(TAG, "checkFCMMessage: ImageUrl=" + addImageUrl);
            if (addTitle != null) {
                showAdd(addTitle, addDesc, addImageUrl);
            }
        }

    }

    private void showAdd(String addTitle, String addDescription, String addImageUrl) {
        tvAddDescription.setText(addDescription);
        tvAddTitle.setText(addTitle);
        Glide.with(ivAddImage.getContext())
                .load(addImageUrl)
                .into(ivAddImage);
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLayout.setVisibility(View.GONE);
            }
        });
        addLayout.setVisibility(View.VISIBLE);
    }

    private void about() {
        ClassChatFCMService.printToken(getApplicationContext());
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
