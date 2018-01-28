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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;

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

    String query;
    Request req;
    RequestQueue requestQueue;

    VisualRecognition service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);

        service.setEndPoint("https://gateway-a.watsonplatform.net/visual-recognition/api");
        service.setApiKey("072ba1a08e2c4d39ec1d9fcdfe8b2a8f50ddcb54");

        String key = "78a7438276284f46b1f4cb2aa6b85dde";
        String secret = "96dfe6b71ce543ef9a3af14a8db94d84";
        //String query = "pasta";
        requestQueue = Volley.newRequestQueue(this);
        Listener listener = new Listener();

        req = new Request(key, secret, listener);

        Intent myIntent = getIntent(); // gets the previously created intent
        String ProteinValue = myIntent.getStringExtra("ProteinValue"); // will return "FirstKeyValue"
        String FatValue= myIntent.getStringExtra("FatValue"); // will return "SecondKeyValue"

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

        /*
        //This response contains the list of food items at zeroth page for your query
        req.getFoods(requestQueue, query,0);

        //This response contains the list of food items at page number 3 for your query
        //If total results are less, then this response will have empty list of the food items
        req.getFoods(requestQueue, query, 3);

        //This food object contains detailed information about the food item
        req.getFood(requestQueue, 29304L);

        //This response contains the list of recipe items at zeroth page for your query
        req.getRecipes(requestQueue, query,0);

        //This response contains the list of recipe items at page number 2 for your query
        //If total results are less, then this response will have empty list of the recipe items
        req.getRecipes(requestQueue, query, 2);

        //This recipe object contains detailed information about the recipe item
        req.getRecipe(requestQueue, 315L);
        */
    }

    public void SetProfile(){
        mSetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openProfile();
            }
        });}
    public void openProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void SetNewImage(){
        mNewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgFile = new  File(mCurrentPhotoPath);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    mImageView.setImageBitmap(myBitmap);
                }

                try{
                    query = classifyImage(mCurrentPhotoPath,service);
                    req.getFoods(requestQueue, query,0);
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
    private String classifyImage(String path, VisualRecognition service)throws IOException, JSONException{

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

    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            //System.out.println("TOTAL FOOD ITEMS: " + response.getTotalResults());

            List<CompactFood> foods = response.getResults();
            //This list contains summary information about the food items

            //System.out.println("=========FOODS============");
            for (CompactFood food: foods) {
                //System.out.println(food.getName());
                //doToast(food.getName());
                if(isForbidden(food.getName(),new String[]{"Lettuce", "cookie", "tide"})) doToast("Can't eat "+food.getName());
                else doToast("Enjoy the "+food.getName()+" :D");
            }
        }

        @Override
        public void onRecipeListRespone(Response<CompactRecipe> response) {
            System.out.println("TOTAL RECIPES: " + response.getTotalResults());

            List<CompactRecipe> recipes = response.getResults();
            System.out.println("=========RECIPES==========");
            for (CompactRecipe recipe: recipes) {
                System.out.println(recipe.getName());
                doToast(recipe.getName());
            }
        }

        @Override
        public void onFoodResponse(Food food) {
            System.out.println("FOOD NAME: " + food.getName());
            doToast(food.getName());

        }

        @Override
        public void onRecipeResponse(Recipe recipe) {
            System.out.println("RECIPE NAME: " + recipe.getName());
            doToast(recipe.getName());
        }
    }
    public void doToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public boolean isForbidden(String food, String[] avoidFoods){
        for(String avoid : avoidFoods){
            if(food.equals(avoid)) return true;
        }
        return false;
    }

    public String checkNutrients(CompactFood food, String[] proteinAndFat){
        float protein = Float.parseFloat(proteinAndFat[0]);
        float fats = Float.parseFloat(proteinAndFat[1]);

        String desc = food.getDescription();
        String[] desc1 = desc.split("-");
        String[] nutr = desc1[0].split("|");

        float cal = Float.parseFloat(nutr[0].replaceAll("[^\\.0123456789]",""));
        float fat = Float.parseFloat(nutr[1].replaceAll("[^\\.0123456789]",""));
        float carbs = Float.parseFloat(nutr[0].replaceAll("[^\\.0123456789]",""));
        float prot = Float.parseFloat(nutr[0].replaceAll("[^\\.0123456789]",""));

        float dailyFat = 70; //grams
        float dailyProtein = 50; //grams

        if(dailyFat/fat > 2100/cal) return "no bueno, too much fat";
        if(dailyProtein/prot > 2100/cal) return "no bueno, too much prot";

        return "bueno";
    }
}
