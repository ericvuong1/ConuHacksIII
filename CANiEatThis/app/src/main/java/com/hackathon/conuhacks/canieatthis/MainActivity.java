package com.hackathon.conuhacks.canieatthis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.*;
import java.util.Date;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.*;
import java.util.List;
import com.ibm.watson.developer_cloud.visual_recognition.v3.*;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.*;
import static android.R.attr.duration;
import static android.R.attr.name;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    Button mOpenCamera;
    WebView mWebView;
    String mCurrentPhotoPath;
    ImageView mImageView;
    Button mNewImageButton;
    TextView mTextView;
    Button mSetProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mTextView = (TextView) findViewById(R.id.mHelloWorldText);
        mOpenCamera = (Button) findViewById(R.id.mCameraButton);
        mImageView = (ImageView) findViewById(R.id.pictureTaken);
        mNewImageButton = (Button) findViewById(R.id.newImage);
        mSetProfile = (Button) findViewById(R.id.mSetProfileButton);
        SetProfile();
        OpenCamera();
        SetNewImage();
    }

    public void SetProfile(){
        mSetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });}
    public void SetNewImage(){
        mNewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile = new  File(mCurrentPhotoPath);


                try{
                classifyImage(mCurrentPhotoPath);
                }
                catch(Exception e){
                    e.printStackTrace();
                    for(StackTraceElement elem : e.getStackTrace()){
                        Log.e("gg",elem.toString());
                    }
                    Log.e("aha",Log.getStackTraceString(e));
                    (Toast.makeText(getApplicationContext(),e.getStackTrace().toString(),Toast.LENGTH_SHORT)).show();
                }}
            }
        );
    }
    public void OpenCamera(){
        mOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        Log.d("Debug Path", mCurrentPhotoPath);
        Toast toast = Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_SHORT);
        toast.show();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private String classifyImage(String path)throws IOException, JSONException{


            VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);

            service.setEndPoint("https://gateway-a.watsonplatform.net/visual-recognition/api");
            service.setApiKey("072ba1a08e2c4d39ec1d9fcdfe8b2a8f50ddcb54");

            //(hard-coded filed name) my_images.jpg

            InputStream imagesStream = new FileInputStream(path);


            ClassifyOptions classifyOptions = new ClassifyOptions.Builder().imagesFile
                    (imagesStream).imagesFilename(path).parameters("{\"classifier_ids\": [\"food\"]}")
                    .build();

            ClassifiedImages result = service.classify(classifyOptions).execute();

            Log.d("help",result.toString());

            JSONObject obj = new JSONObject(result.toString());
            //Getting first result

            JSONArray arr = obj.getJSONArray("images").getJSONObject(0).getJSONArray("classifiers").getJSONObject(0).getJSONArray("classes");
            int size = arr.length();

            JSONObject res = obj.getJSONArray("images").getJSONObject(0).getJSONArray("classifiers").getJSONObject(0).getJSONArray("classes").getJSONObject(0);
            String name = res.getString("class");
            mTextView.setText(name);

            Toast toast = Toast.makeText(this, name, Toast.LENGTH_SHORT);
            toast.show();

            return name;

    }
}
