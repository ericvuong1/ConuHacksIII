package com.hackathon.conuhacks.canieatthis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;

import java.util.List;

public class fatsecretTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key = "78a7438276284f46b1f4cb2aa6b85dde";
        String secret = "96dfe6b71ce543ef9a3af14a8db94d84";
        String query = "pasta";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Listener listener = new Listener();

        Request req = new Request(key, secret, listener);

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
    }

    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            System.out.println("TOTAL FOOD ITEMS: " + response.getTotalResults());

            List<CompactFood> foods = response.getResults();
            //This list contains summary information about the food items

            System.out.println("=========FOODS============");
            for (CompactFood food: foods) {
                System.out.println(food.getName());
                doToast(food.getName());

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
}