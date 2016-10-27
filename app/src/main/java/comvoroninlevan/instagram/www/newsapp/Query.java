package comvoroninlevan.instagram.www.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Леван on 02.10.2016.
 */
public final class Query {

    public static final String LOG_TAG = Query.class.getSimpleName();

    public static List<News> fetchNewsData(String requestUrl){

        URL url = createUrlObject(requestUrl);
        String jsonResponse = null;
        HttpRequest httpRequest = new HttpRequest();

        try{
            jsonResponse = httpRequest.makeHttpRequest(url);
        } catch (IOException exept){
            Log.e(LOG_TAG, "Error closing input", exept);
        }
        List<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createUrlObject(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        } catch (MalformedURLException exept) {
            Log.e(LOG_TAG, "MalformedURLExeption");
        }
        return url;
    }

    private static List<News> extractFromJson(String newsJson){

        if(TextUtils.isEmpty(newsJson)){
            return null;
        }

        List<News> newsExtr = new ArrayList<News>();

        try{
            JSONObject root = new JSONObject(newsJson);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for(int i=0; i < results.length(); i++){
                JSONObject currentJsonObject = results.getJSONObject(i);

                String webTitle = currentJsonObject.getString("webTitle");
                String sectionName = currentJsonObject.getString("sectionName");
                String webPublicationDate = currentJsonObject.getString("webPublicationDate");
                String webUrl = currentJsonObject.getString("webUrl");

                String lastName = "";
                String firstName = "";
                String fullName = "";
                if(currentJsonObject.has("tags")){
                    JSONArray tags = currentJsonObject.getJSONArray("tags");
                    for (int j = 0; j < tags.length(); j++) {
                        if (j > 0) {
                            lastName = lastName + "";
                            firstName = firstName + "";
                            fullName = fullName + ", ";
                        }
                        JSONObject currentTagObject = tags.getJSONObject(j);
                        if (currentTagObject.has("lastName") | currentTagObject.has("firstName")) {
                            fullName = (firstName + currentTagObject.getString("firstName")) + " " + (lastName + currentTagObject.getString("lastName"));
                        }
                    }
                }

                News allNews = new News(webTitle, sectionName, webPublicationDate, webUrl, fullName);
                newsExtr.add(allNews);
            }
        } catch (JSONException exept){
            Log.e(LOG_TAG, "Problem parsing from JSON", exept);
        }
        return newsExtr;
    }
}
