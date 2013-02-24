package be.firewolf.zimdroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
//import android.support.v4.app.NavUtils;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
//import java.util.Set;

public class select_notebook extends Activity {
	final String PREFS_NAME = "ZimDroidSetv1";
	final String PREFS_LIST = "list_of_notebooks";
	public static SharedPreferences settings;
	
	final ArrayList<String> listNotebooks = new ArrayList<String>();
	BaseAdapter lst_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_notepad);
        
    	final ListView list = (ListView) findViewById(R.id.lstNotepads);
        //Adapter for ListView:
        lst_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listNotebooks);
    	list.setAdapter(lst_adapter);
    	LoadNotepads();
    	lst_adapter.notifyDataSetChanged();
    	list.setLongClickable(true);
    	list.setClickable(true);
       	list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
    		public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
    			AlertDialog.Builder adb=new AlertDialog.Builder(select_notebook.this);
    			adb.setTitle("Delete notebook?");
            	adb.setMessage("Do you want to delete '" + listNotebooks.get(position)+"'?");
            	final int positionToRemove = position;
            	adb.setNegativeButton("Cancel", null);
            	adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
            		public void onClick(DialogInterface dialog, int which) {
            			listNotebooks.remove(positionToRemove);
            			lst_adapter.notifyDataSetChanged();
            			SaveNotepads(listNotebooks);
            			LoadNotepads();
            		}
            	});
            adb.show();
            return true;
            }
        });
       	
       	list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
       		public void onItemClick(AdapterView<?> a, View v, int position, long id) {
       			//Calling activity_display_folder (with parameters):
       			Intent intBrowseNotepad = new Intent(v.getContext(),display_folder.class);
       			Bundle bundle = new Bundle();
       			bundle.putString("notepad_path", listNotebooks.get(position));
       			intBrowseNotepad.putExtras(bundle);
        		startActivityForResult(intBrowseNotepad, 0);
       		}
		});
        final Button btn_add_notepad = (Button) findViewById(R.id.btnAddNotepad);
        btn_add_notepad.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		//Calling activity_add_notepad:
        		Intent intSelectFile = new Intent(v.getContext(),select_file.class);
        		startActivityForResult(intSelectFile, 0);
        	}
				
		});
    }
    
    public void onResume(Bundle savedInstanceState) {
    	Log.i("ZimDroid", "onResume");
    	LoadNotepads();
    }
    
	public void LoadNotepads() {
    	Log.i("ZimDroid", "Run LoadNotepads()");
    	settings = getSharedPreferences(PREFS_NAME, 0);
    	String temp = null;
       	temp = settings.getString(PREFS_LIST, "NONE");
       	if(temp.equals("NONE") || temp.equals("")) {
       		Log.i("ZimDroid", "No rows found");
       		Toast info = Toast.makeText(select_notebook.this.getBaseContext(), "No notepads available. Add existing or create new.", Toast.LENGTH_LONG);
       		info.show();
       	}
       	else {
       		Log.i("ZimDroid", "Find row");
       		temp = temp.replace(";;", ";");
       		String[] Notepads = temp.split(";");
       		listNotebooks.clear();
       		for (String position : Notepads) {
       			Log.i("ZimDroid", "Loop");
       			if(position.equals(""))
       				continue;
				listNotebooks.add(position);
				lst_adapter.notifyDataSetChanged();
       		}
       	}
    }
    
    public void SaveNotepads(ArrayList<String> listNotepads) {
    	SharedPreferences.Editor editor = settings.edit();
    	StringBuilder sb = new StringBuilder();
    	int i=0;
    	for (String ss : listNotepads) {
    	    sb.append(ss).append(";");
    	    i++;
    	}
    	Toast info = Toast.makeText(select_notebook.this.getBaseContext(), Integer.toString(i)+" to save.", Toast.LENGTH_SHORT);
    	info.show();
    	editor.putString(PREFS_LIST, sb.toString());
    	editor.commit();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_notepad, menu);
        return true;
    }

    
}
