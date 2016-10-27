package comvoroninlevan.instagram.www.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    LoaderManager loaderManager;
    View indicator;
    ListView newsList;
    EditText query;
    String API = "API key here";
    private TextView mEmptyTextView;
    private static final int NEWS_LOADER = 1;
    private NewsAdapter mAdapter;
    private static final String NEWS = "http://content.guardianapis.com/search?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mEmptyTextView = (TextView)findViewById(R.id.empty_view);
        newsList = (ListView)findViewById(R.id.list);
        newsList.setEmptyView(mEmptyTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsList.setAdapter(mAdapter);

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER, null, this);
        } else {
            indicator = findViewById(R.id.loading_indicator);
            indicator.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.noConnection);
        }

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent webUrl = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webUrl);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        query = (EditText)findViewById(R.id.edit_query);
        String userQuery = query.getText().toString();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(
                getString(R.string.filter_order_by_key),
                getString(R.string.filter_order_by_default));
        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        String newsPerPage = sharedPreferences1.getString(
                getString(R.string.news_per_page_key),
                getString(R.string.news_per_page_default));
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(this);
        String section = sharedPreferences2.getString(
                getString(R.string.section_key),
                getString(R.string.section_default));

        Uri baseUri = Uri.parse(NEWS);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("q", userQuery);
        builder.appendQueryParameter("format", "json");
        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("page-size", newsPerPage);
        if(section.equals("all")) {

        }else {
            builder.appendQueryParameter("section", section);
        }
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("api-key", API);

        return new NewsLoaderAsync(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        indicator = findViewById(R.id.loading_indicator);
        indicator.setVisibility(View.GONE);

        mEmptyTextView.setText(R.string.no_data);

        mAdapter.clear();

        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent filterIntent = new Intent(this, FilterActivity.class);
            startActivity(filterIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateLoader(View view){

        indicator.setVisibility(View.VISIBLE);
        mAdapter.clear();

        loaderManager = getLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this);
    }
}
