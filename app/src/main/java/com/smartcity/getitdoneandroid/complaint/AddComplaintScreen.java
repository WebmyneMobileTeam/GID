package com.smartcity.getitdoneandroid.complaint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ragnarok.rxcamera.RxCamera;
import com.ragnarok.rxcamera.RxCameraData;
import com.ragnarok.rxcamera.config.RxCameraConfig;
import com.ragnarok.rxcamera.config.RxCameraConfigChooser;
import com.ragnarok.rxcamera.request.Func;
import com.smartcity.getitdoneandroid.AppUser;
import com.smartcity.getitdoneandroid.R;
import com.smartcity.getitdoneandroid.helpers.ComplexPreferences;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import rx.functions.Action1;

public class AddComplaintScreen extends AppCompatActivity {

    private LinearLayout linearCatList;
    private String cats[] = {"Road","Water","Greenery","Waste"};
    private int cat_images[] = {R.drawable.road_transportation,R.drawable.water_waste,R.drawable.greenary_solution,R.drawable.waste};
    private int selectioncat = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button submitbtn;
    Bitmap imageBitmap;
    private EditText edTitle;
    private EditText edDescription;
    private ProgressDialog dialogUpload;
    private String title;
    private String description;
    private String fbid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_complaint_screen);
        getSupportActionBar().setTitle("Add Complaint");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearCatList = (LinearLayout)findViewById(R.id.linearCatList);
        edDescription = (EditText)findViewById(R.id.edDescription);
        edTitle = (EditText)findViewById(R.id.edTitle);

        setupCats();
        setselectionforcat(selectioncat);

    }

    public void onSubmitComplaint(View view) {

        if(isFailed()){
            Toast.makeText(AddComplaintScreen.this, "Please fill all details", Toast.LENGTH_SHORT).show();
        }else{

            new ImageUploadTask().execute();
        }

    }

    public boolean isFailed(){
        return edTitle.getText().toString().isEmpty() && edDescription.getText().toString().isEmpty();
    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        String sResponse;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // dialogUpload = ProgressDialog.show(AddComplaintScreen.this,"Please wait","Submitting Complaint");
            Toast.makeText(AddComplaintScreen.this, "Uploading.....", Toast.LENGTH_SHORT).show();

            title = edTitle.getText().toString();
            description = edDescription.getText().toString();

            ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(AddComplaintScreen.this,"userpref",MODE_PRIVATE);
            AppUser appUser = preferences.getObject("appuser",AppUser.class);
            fbid = appUser.getFbid();
        }

        @Override
        protected String doInBackground(Void... unsued) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost("http://getitdonee.azurewebsites.net/post");


                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                entity.addPart("title", new StringBody(title));
                entity.addPart("description", new StringBody(description));
                entity.addPart("facebook_id", new StringBody(fbid));
                entity.addPart("latitude", new StringBody("22.3288499"));
                entity.addPart("longitude", new StringBody("73.2077872"));
                entity.addPart("file", new ByteArrayBody(data, "myImage.jpg"));
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage(), e);
                return null;
            }

            // (null);
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
//                if (dialogUpload.isShowing())
//                    dialogUpload.dismiss();

                Log.e("Response Upload",sResponse);
                Toast.makeText(AddComplaintScreen.this, "Complaint successfully posted", Toast.LENGTH_SHORT).show();
                finish();

            } catch (Exception e) {

                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == android.R.id.home){
           this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupCats() {

        for(int i=0;i<linearCatList.getChildCount();i++){
            View view = linearCatList.getChildAt(i);
            ImageView imageView = (ImageView)view.findViewById(R.id.imgcategoryadd);
            TextView textView = (TextView)view.findViewById(R.id.txtcataddscreen);
            imageView.setImageResource(cat_images[i]);
            textView.setText(cats[i]);
            view.setOnClickListener(viewClick);

         }

    }

    private View.OnClickListener viewClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int pos = linearCatList.indexOfChild(view);
            setselectionforcat(pos);

        }
    };

    private void setselectionforcat(int pos) {
        selectioncat = pos;

        for(int i=0;i<linearCatList.getChildCount();i++){
            View view = linearCatList.getChildAt(i);
            ImageView imageView = (ImageView)view.findViewById(R.id.imgcategoryadd);
            TextView textView = (TextView)view.findViewById(R.id.txtcataddscreen);
            imageView.setImageResource(cat_images[i]);

            if(i==pos){
                textView.setTextColor(ContextCompat.getColor(AddComplaintScreen.this,R.color.colorPrimary));
                imageView.setColorFilter(Color.parseColor("#2D936C"), PorterDuff.Mode.SRC_ATOP);
            }else{
                textView.setTextColor(Color.parseColor("#494949"));
                imageView.setColorFilter(Color.parseColor("#494949"), PorterDuff.Mode.SRC_ATOP);
            }



        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");
            ((ImageView)findViewById(R.id.cameraIV)).setImageBitmap(imageBitmap);
        }
    }

    public void openCamera(View view) {
        dispatchTakePictureIntent();
    }

}
