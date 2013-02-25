package be.firewolf.zimdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class activity_about extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_about);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_about, menu);
        return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	    	case R.id.menu_close:
	    		Intent intSelectFile = new Intent(activity_about.this.getBaseContext(),select_notebook.class);
	            startActivityForResult(intSelectFile, 0);
	    		return(true);
	    }
    	return(super.onOptionsItemSelected(item));
    }
	
}
