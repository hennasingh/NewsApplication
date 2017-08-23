package artist.web.mynewsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String STATE_NEWS_SECTION = "section";
    private static final String STATE_MENU_ID = "menuId";
    private static final String STATE_MENU_SELECTED_POSITION = "menuPosition";

    final Context mContext = this;
    private String mNewsSection;
    private int mMenuId;
    private int mSelectedPosition;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mMenuId = R.id.nav_home;
            displaySectionContent();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_MENU_ID, mMenuId);
        outState.putString(STATE_NEWS_SECTION, mNewsSection);
        outState.putInt(STATE_MENU_SELECTED_POSITION, mSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        if (inState != null) {
            mMenuId = inState.getInt(STATE_MENU_ID);
            mSelectedPosition = inState.getInt(STATE_MENU_SELECTED_POSITION);
            mNewsSection = inState.getString(STATE_NEWS_SECTION);
            displaySectionContent();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Method to handle individual navigation item clicks
     * @param item
     * @return boolean flag
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mMenuId = item.getItemId();

        displaySectionContent();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    /**
     * Method to open appropriate news section based on menu item clicked
     */
    public void displaySectionContent() {
        switch (mMenuId) {
            case R.id.nav_home:
                mNewsSection = getString(R.string.menu_home).toLowerCase();
                mSelectedPosition = 1;
                break;
            case R.id.nav_business:
                mNewsSection = getString(R.string.menu_business).toLowerCase();
                mSelectedPosition = 2;
                break;
            case R.id.nav_technology:
                mNewsSection = getString(R.string.menu_technology).toLowerCase();
                mSelectedPosition = 3;
                break;
            case R.id.nav_world:
                mNewsSection = getString(R.string.menu_world).toLowerCase();
                mSelectedPosition = 4;
                break;
            default:
                mNewsSection = getString(R.string.menu_home).toLowerCase();
                mSelectedPosition = 1;
                break;
        }

        NewsFragment mNewsFragment = NewsFragment.newInstance(mNewsSection, mSelectedPosition);
        mFragmentManager.beginTransaction()
                .replace(R.id.frame_content, mNewsFragment)
                .addToBackStack(null)
                .commit();

    }

}
