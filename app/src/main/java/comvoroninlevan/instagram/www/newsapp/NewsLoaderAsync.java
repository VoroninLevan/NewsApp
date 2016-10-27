package comvoroninlevan.instagram.www.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Леван on 04.10.2016.
 */
public class NewsLoaderAsync extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoaderAsync(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        List<News> news = Query.fetchNewsData(mUrl);
        return news;
    }
}
