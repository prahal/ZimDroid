package be.firewolf.zimdroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PageListActivity extends FragmentActivity
        implements PageListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_list);

        if (findViewById(R.id.page_detail_container) != null) {
            mTwoPane = true;
            ((PageListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.page_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(PageDetailFragment.ARG_ITEM_ID, id);
            PageDetailFragment fragment = new PageDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.page_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, PageDetailActivity.class);
            detailIntent.putExtra(PageDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
