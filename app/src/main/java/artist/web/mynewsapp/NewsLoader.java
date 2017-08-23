package artist.web.mynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by User on 7/16/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsSegment>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private String newsUrl;

    public NewsLoader(Context context, String newsUrl) {
        super(context);
        this.newsUrl = newsUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<NewsSegment> loadInBackground() {

        if(newsUrl==null){
            return null;
        }
        // Perform network request, parse the response, and extract a list of news items
        List<NewsSegment> newsSegment = QueryUtils.fetchNewsItems(newsUrl, getContext());
        return newsSegment;
    }
}
