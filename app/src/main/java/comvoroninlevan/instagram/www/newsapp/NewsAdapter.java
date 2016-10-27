package comvoroninlevan.instagram.www.newsapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Леван on 02.10.2016.
 */
public class NewsAdapter extends ArrayAdapter<News> {


    static class ViewHolder {
        TextView title;
        TextView section;
        TextView date;
        TextView time;
        TextView author;
    }

    ViewHolder holder = new ViewHolder();


    private static final String SEPARATOR = "T";

    public NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_list, parent, false);
        }

        News currentNews = getItem(position);

        holder.title = (TextView)listItemView.findViewById(R.id.title);
        holder.title.setText(currentNews.getWebTitle());

        holder.section = (TextView)listItemView.findViewById(R.id.section);
        holder.section.setText(currentNews.getSectionName());

        String fullDate = currentNews.getWebPublicationDate();
        String sepDate;
        String sepTime;

        String[] parts = fullDate.split(SEPARATOR);
        sepDate = parts[0];
        sepTime = parts[1];

        holder.date = (TextView)listItemView.findViewById(R.id.date);
        holder.date.setText(sepDate);
        holder.time = (TextView)listItemView.findViewById(R.id.time);
        holder.time.setText(sepTime);

        holder.author = (TextView)listItemView.findViewById(R.id.author);
        holder.author.setText(currentNews.getFullName());

        return listItemView;
    }
}