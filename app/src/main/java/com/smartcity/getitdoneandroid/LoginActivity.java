package com.smartcity.getitdoneandroid;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.smartcity.getitdoneandroid.helpers.ComplexPreferences;
import com.smartcity.getitdoneandroid.helpers.PrefUtils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static CallbackManager callbackManager;
    private String TAG = "Advise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PrefUtils.isLogin(this)){
            startActivity(new Intent(this,DrawerActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId("568563739982339");
        LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        // Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        //handleFacebookAccessToken(loginResult.getAccessToken());
                        //LoginManager.getInstance().logOut();

                        Log.e(TAG,"Here in the success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject user, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                            try {

                                                JSONObject fbProfile = response.getJSONObject();
                                                Log.e("FNAME",fbProfile.getString("first_name"));
                                                Log.e("LNAME",fbProfile.getString("last_name"));
                                                AppUser appuser = new AppUser();
                                                appuser.setFbid(fbProfile.getString("id"));
                                                appuser.setFname(fbProfile.getString("first_name"));
                                                appuser.setLname(fbProfile.getString("last_name"));
                                                appuser.setEmail(fbProfile.getString("email"));

                                                ComplexPreferences pref = ComplexPreferences.getComplexPreferences(LoginActivity.this,"userpref",MODE_PRIVATE);
                                                pref.putObject("appuser",appuser);
                                                pref.commit();

                                                PrefUtils.setLogin(LoginActivity.this,true);

                                                startActivity(new Intent(LoginActivity.this,DrawerActivity.class));
                                                finish();




                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name,last_name, email, gender, birthday, link");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void doFacebookLogin(View view) {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
