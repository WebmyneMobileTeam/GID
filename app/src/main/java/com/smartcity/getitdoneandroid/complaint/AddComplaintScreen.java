package com.smartcity.getitdoneandroid.complaint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ragnarok.rxcamera.RxCamera;
import com.ragnarok.rxcamera.RxCameraData;
import com.ragnarok.rxcamera.config.RxCameraConfig;
import com.ragnarok.rxcamera.config.RxCameraConfigChooser;
import com.ragnarok.rxcamera.request.Func;
import com.smartcity.getitdoneandroid.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.functions.Action1;

public class AddComplaintScreen extends AppCompatActivity {

    private LinearLayout linearCatList;
    private String cats[] = {"Road","Water","Greenery","Waste"};
    private int cat_images[] = {R.drawable.road_transportation,R.drawable.water_waste,R.drawable.greenary_solution,R.drawable.waste};
    private int selectioncat = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_complaint_screen);
        getSupportActionBar().setTitle("Add Complaint");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearCatList = (LinearLayout)findViewById(R.id.linearCatList);
        setupCats();
        setselectionforcat(selectioncat);

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
                imageView.setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
            }else{
                textView.setTextColor(Color.parseColor("#494949"));
                imageView.setColorFilter(Color.parseColor("#494949"), PorterDuff.Mode.SRC_ATOP);
            }



        }

    }

    public void openCamera(View view) {

    }

}
