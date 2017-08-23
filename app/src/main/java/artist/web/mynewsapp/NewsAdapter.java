package artist.web.mynewsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 7/17/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsSegment> {

    private static final String LOG_TAG = NewsAdapter.class.getName();
    private List<NewsSegment> newsList;
    Context context;

    public NewsAdapter(Context context, List<NewsSegment> newsList){
      super(context,0,newsList);

    }

    public class NewsViewHolder {
        TextView newsTitleView;
        TextView newsSectionView;
        TextView newsDateView;
        TextView newsAuthorView;

        public NewsViewHolder(View itemView){
            newsTitleView = (TextView)itemView.findViewById(R.id.newsTitletv);
            newsSectionView = (TextView)itemView.findViewById(R.id.newsSectiontv);
            newsAuthorView = (TextView)itemView.findViewById(R.id.newsAuthortv);
            newsDateView = (TextView)itemView.findViewById(R.id.newsDatetv);

        }
    }

    /**
     * Returns a list item view that displays information about the news at a given position
     * in the list of news.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String title = "";
        String section = "";
        String newsDate = "";
        String author = "";
        NewsViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_layout, parent, false);
            holder = new NewsViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NewsViewHolder) convertView.getTag();
        }
        // First check if position is not greater than size of news feed
        if (position < getCount()) {

            // Find news at the given position in the list
            NewsSegment currentNews = getItem(position);

            /** Set data to respective views within ListView */

            // Set Title
            if ((currentNews.getNewsTitle() != null) && (currentNews.getNewsTitle().length() > 0)) {
                title = currentNews.getNewsTitle();
            }

            // Set Section Name
            if ((currentNews.getNewsSection() != null) && (currentNews.getNewsSection().length() > 0)) {
                section = currentNews.getNewsSection();
            }

            // Set Author
            if ((currentNews.getNewsAuthor() != null) && (currentNews.getNewsAuthor().length() > 0)) {
                author = currentNews.getNewsAuthor();
            }

            // Set Date
            if ((currentNews.getNewsPublishedDate() != null) && (currentNews.getNewsPublishedDate().length() > 0)) {
                String date = currentNews.getNewsPublishedDate();
                newsDate = formatDate(date);
            }

        }
        holder.newsTitleView.setText(title);
        holder.newsSectionView.setText(section);
        holder.newsAuthorView.setText(author);
        holder.newsDateView.setText(newsDate);

        return convertView;
    }
    /**
     * Method to check if Published Date exists, then format it if the extracted date is a valid date
     */
    public String formatDate(String date) {

        String dateFormatted = "";
        String dateNew = date.substring(0, 10); // gets date in yyyy-mm-dd format from timestamp

        // Format dateNew
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date dt = inputFormat.parse(dateNew);
            dateFormatted = newFormat.format(dt);
        }
        catch(ParseException pe) {
            Log.e(LOG_TAG, "Problem formatting input Date", pe);
        }

        return dateFormatted;
    }


}
