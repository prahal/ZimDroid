package be.firewolf.zimdroid;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.support.v4.app.NavUtils;

public class select_file extends ListActivity {
	String PREFS_NAME = "ZimDroidSetv1";
	String PREFS_LIST = "list_of_notebooks";
	TextView txtPath;
	ArrayAdapter<String> lista = null;
	String path = Environment.getExternalStorageDirectory().getPath(); //just /sdcard.
	List<String> name = null;
	List<String> location = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        txtPath = (TextView) findViewById(R.id.txtPath);
        getDir(path);
        
    }
    
    private void getDir(String Path) {
    	name = new ArrayList<String>();
    	location = new ArrayList<String>();
    	txtPath.setText(Path);
    	File f = new File(Path);
     	File[] files = f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.isDirectory())
					return true;
				String fname = pathname.getName();
				int mid = fname.lastIndexOf(".");
				String fex = fname.substring(mid+1,fname.length());
				if(fex.equals("zim") || fex.equals("ZIM"))
					return true;
				return false;
			}
		});
    	Log.i("ZimDroid", files.toString());
    	if(!Path.equals("/")) {
    		name.add("/");
    		location.add("/");
    		name.add("../");
    		location.add(f.getParent());
    	}
    	Log.i("ZimDroid", "przed for()");
    	for(File loc : files) {
    		location.add(loc.getPath());
    		if(loc.isDirectory())
    			name.add(loc.getName() + "/");
    		else{
    			name.add(loc.getName());
    		}
    	}
        lista = new ArrayAdapter<String>(this, R.layout.rowfile, name);
        setListAdapter(lista);
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
    	File f = new File(location.get(position));
    	if(f.isDirectory() && f.canRead()){
    			getDir(location.get(position));
    	}
    	else{
    			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    			SharedPreferences.Editor editor = settings.edit();
    			String temp;
    			temp = settings.getString("list_of_notebooks", "NONE");
    			if(temp.equals(""))
    				editor.putString("list_of_notebooks", location.get(position));
    			else
    				editor.putString("list_of_notebooks", temp+";"+location.get(position));
    			editor.commit();
    			Intent refresh = new Intent(v.getContext(), select_notebook.class);
    			startActivity(refresh);
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_file, menu);
        return true;
    }
}
