package com.delarosa.cognitoAuth.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.delarosa.cognitoAuth.data.AuthUtils;
import com.delarosa.cognitoAuth.R;
import com.delarosa.cognitoAuth.data.SharePreferences;

public class MainActivity extends AppCompatActivity {
    private static final String ISLOGGED = "isLogged";
    private Button userButton;
    private SharePreferences sharePreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharePreferences = new SharePreferences(this);
        Bundle tokens = getIntent().getExtras();
        String accessToken = "";
        String IdToken = "";
        if (tokens != null) {
            accessToken = tokens.getString(getString(R.string.app_access_token));
            IdToken = tokens.getString(getString(R.string.app_id_token));
        }
        Log.i("TOKEN", "Id Token" + IdToken);
        Log.i("TOKEN", "Access Token" + accessToken);


        userButton = (Button) findViewById(R.id.buttonSignout);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePreferences.setBoolean(ISLOGGED, false);
                AuthUtils.onButtonPress(false);

            }
        });

    }


}
