package be.firewolf.zimdroid;

import java.io.File;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
import android.widget.ListView;
//import android.support.v4.app.NavUtils;

import be.firewolf.zimdroid.ZimNotepad;
//import be.firewolf.zimdroid.ZimNotepad.ZimPage;



public class display_folder extends Activity {
	
	FolderViewAdapter pages_adapter = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_folder);
    	ListView lstFiles = (ListView) findViewById(R.id.lstFiles);
    	lstFiles.setLongClickable(true);
        Log.i("ZimDroid", "inicjalizacja activity");
        Bundle bundle = this.getIntent().getExtras();
        String notepad_file = bundle.getString("notepad_path");
        Log.i("ZimDroid", "Notepad path is "+notepad_file);
        String folder_inside = bundle.getString("folder_inside");
        if(notepad_file == null)
        	finish();
        ZimNotepad notepad = new ZimNotepad(notepad_file);
        if(folder_inside == null) {
        	pages_adapter = new FolderViewAdapter(this, notepad.pages);       		
        }
        else {
        	Log.i("ZimDroid", "folder_inside:"+folder_inside);
        	String sciezka = folder_inside.substring((new File(notepad_file).getParent()+"/").length(), folder_inside.length());
        	pages_adapter = new FolderViewAdapter(this, notepad.getChildrenByPath(sciezka));
        }
        lstFiles.setAdapter(pages_adapter);
        Log.i("ZimDroid", "Pages in dir: "+String.valueOf(pages_adapter.getCount()));
        pages_adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_display_folder, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.add_page) {
    		Log.i("ZimDroid", "menuitem add page!");
    		//TODO: add new page dialog.
    	}
    	return true;
    }
    
}
