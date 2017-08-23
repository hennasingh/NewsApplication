package artist.web.mynewsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.Fragment;
import android.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;


/**
 * Created by User on 7/16/2017.
 */

public class NewsFragment extends Fragment implements LoaderCallbacks<List<NewsSegment>>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = NewsFragment.class.getName();

    /** Guardian API Base URL */
    private static final String API_BASE_URL = "http://content.guardianapis.com/search?q=";
    private static final String API_SEARCH_URL = "https://content.guardianapis.com/search?section=";

    private NewsAdapter newsAdapter;
    private TextView emptyStateTextView;
    public static List<NewsSegment> newsList;
    private ListView newsListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int loaderId;
    private View homeView;

    public static NewsFragment newInstance(String newsSection, int menuPosition){
        NewsFragment newsFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString("section", newsSection);
        args.putInt("position", menuPosition);
        newsFragment.setArguments(args);
        return newsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeView = inflater.inflate(R.layout.news_list_item,container,false);

        // Activate SwipeRefreshLayout feature so news list is updated when screen in swiped
        swipeRefreshLayout = (SwipeRefreshLayout) homeView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        //find a reference to the list in the layout
        newsListView = (ListView)homeView.findViewById(R.id.list_news);

        //set Empty View
        emptyStateTextView = (TextView)homeView.findViewById(R.id.text_emptyView);
        newsListView.setEmptyView(emptyStateTextView);

        //create a new adapter that takes list as input
        newsList = new ArrayList<NewsSegment>();
        newsAdapter = new NewsAdapter(this.getContext(),newsList);
        newsListView.setAdapter(newsAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        getActivity().getSystemService(CONNECTIVITY_SERVICE);

        // If there is a network connection, fetch data
        if (isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getActivity().getLoaderManager();

            // Get loaderId from menu position, so that a different loaderId is assigned for each section
            loaderId = getMenuPosition();
            loaderManager.initLoader(loaderId, null, this);

        } else {
            // Hide loading indicator and show empty state view
            View progressIndicator = homeView.findViewById(R.id.progress_indicator);
            emptyStateTextView.setText("Unfortunately your device does not have any internet connection");
            progressIndicator.setVisibility(View.GONE);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsSegment currentNews = newsAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getNewsUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
        return homeView;
    }

    @Override
    public Loader<List<NewsSegment>> onCreateLoader(int i, Bundle bundle) {

        String section = getSection();
        String url = "";

        if (section.equals(getString(R.string.menu_home).toLowerCase())) {
            url = API_BASE_URL;
        } else {
            url = API_SEARCH_URL + section;
        }

        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new NewsLoader(getContext(),uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsSegment>> loader, List<NewsSegment> newsItems) {

        swipeRefreshLayout.setRefreshing(false);

        // Hide progress indicator because the data has been loaded
        View progressIndicator = homeView.findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.GONE);

        // Check if connection is still available, otherwise show appropriate message
        if (isConnected()) {

            // Set empty state text when no news found
            if (newsItems == null || newsItems.size() == 0) {
                emptyStateTextView.setVisibility(View.VISIBLE);
                emptyStateTextView.setText("No news item found that matched your search criteria");
            } else {
                emptyStateTextView.setVisibility(View.GONE);
            }

            newsList.clear();

            // If there is a valid list of {@link Book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (newsItems != null && !newsItems.isEmpty()) {
                newsList.addAll(newsItems);
                newsAdapter.notifyDataSetChanged();
            }
        } else {
            emptyStateTextView.setText("Unfortunately your device does not have any internet connection");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsSegment>> loader) {
        newsAdapter.clear();
    }


    public void onRefresh() {
        getActivity().getLoaderManager().restartLoader(loaderId, null, this);
    }

    /**
     * Method to get News Section Name
     * @return section
     */
    public String getSection() {
        return getArguments().getString("section", "");
    }

        /**
         * Method to get Selected Menu Position
         * @return menu position
         */
    public int getMenuPosition() {
        return getArguments().getInt("position", 0);
    }

    /**
     * Method to check network connectivity
     * @return true/false
     */
    public boolean isConnected() {
        boolean hasNetwork;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            hasNetwork = true;
        } else {
            hasNetwork = false;
        }

        return hasNetwork;
    }
}
