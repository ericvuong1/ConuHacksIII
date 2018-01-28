package vrproject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.*;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.model.CompactFood;
import java.util.List;
import com.ibm.watson.developer_cloud.visual_recognition.v3.*;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.*;


public class ClassifyImage {
	

	public static void main(String[] args) throws IOException, JSONException {

		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
 
		service.setEndPoint("https://gateway-a.watsonplatform.net/visual-recognition/api");
		service.setApiKey("072ba1a08e2c4d39ec1d9fcdfe8b2a8f50ddcb54");

		String path = args[0]; //path to image
		//(hard-coded filed name) my_images.jpg
		
		InputStream imagesStream = new FileInputStream(path);

		ClassifyOptions classifyOptions = new ClassifyOptions.Builder().imagesFile
				(imagesStream).imagesFilename(path).parameters("{\"classifier_ids\": [\"food\"]}")
				.build();
	
		ClassifiedImages result = service.classify(classifyOptions).execute();
		
		System.out.println("Classification Results:");
		System.out.println(result);
		
		JSONObject obj = new JSONObject(result);
		//Getting first result
		
		JSONArray arr = obj.getJSONArray("images").getJSONObject(0).getJSONArray("classifiers").getJSONObject(0).getJSONArray("classes");
		int size = arr.length();
		
		JSONObject res = obj.getJSONArray("images").getJSONObject(0).getJSONArray("classifiers").getJSONObject(0).getJSONArray("classes").getJSONObject(0);
		String name = res.getString("className");
		
		
		// System.out.println(res.toString());
		// To print out the .json after classes
		
		int totalItems=0;
		for(int i=0; i<size; i++){
			
			JSONObject item = arr.getJSONObject(i);
			
			if(item.getDouble("score")>0.6){
				System.out.println(item.getString("className") + ", Score: " + 
				item.getDouble("score"));
				totalItems++;
			}
			
		}
		
		String[] list = new String[totalItems];
		
		//For each item in the results, check if the user should
		//avoid it
		
		for(int i=0; i<totalItems; i++){
			list[i] = arr.getJSONObject(i).getString("className");
			
			search(list[i], new String[]{"Calcium", "Sodium", "Sugar"});
			//second argument is a String array of things to avoid
		}
		
	}
	
	public static void search(String foodString, String[] avoidFoods){
		
		String key = "78a7438276284f46b1f4cb2aa6b85dde";
		String secret = "96dfe6b71ce543ef9a3af14a8db94d84";
		FatsecretService service = new FatsecretService(key, secret);
		
		Response<CompactFood> response = service.searchFoods(foodString);
		//Taking the first result
		
		List<CompactFood> list = response.getResults();
		CompactFood food = list.get(0);
		
		String desc = food.getDescription();
		
		System.out.println("Avoid the following:");
		
		for(int i=0; i<avoidFoods.length; i++){
			if(desc.toLowerCase().contains(avoidFoods[i].toLowerCase())){
				System.out.println(avoidFoods[i]);
			}
		}
		
	
		
		
	}
}