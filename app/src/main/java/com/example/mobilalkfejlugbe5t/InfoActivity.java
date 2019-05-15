package com.example.mobilalkfejlugbe5t;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class InfoActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView nevTF, emailTF;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        nevTF = findViewById(R.id.profil_nev);
        emailTF = findViewById(R.id.profil_email);
        circleImageView = findViewById(R.id.profil_kep);
        callbackManager = CallbackManager.Factory.create();
        checkLoginStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String keresztnev = object.getString("first_name");
                    String vezeteknev = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                    emailTF.setText("Email: " + email);
                    nevTF.setText("Név: " + vezeteknev + " " + keresztnev);
                    Toast.makeText(InfoActivity.this, "Üdvözöllek " + keresztnev + "!", Toast.LENGTH_LONG).show();
                    Glide.with(InfoActivity.this).load(image_url).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void back(android.view.View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}


