package be.firewolf.zimdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
//import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.webkit.WebView;
import android.widget.EditText;
//import android.widget.Scroller;
import android.widget.Toast;
//import android.support.v4.app.NavUtils;

public class edit_page extends Activity {
	
	File path;
	String Content;
	EditText edtPage;
	
	public boolean page_save(String content) {
		FileWriter writer;
		try {
			if(path.exists() && path.canWrite()) {
				writer = new FileWriter(path);
				writer.write(content);
				writer.close();
				Toast.makeText(this, "Page saved.", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this, "Cannot save page."+String.valueOf(path.canWrite()), Toast.LENGTH_LONG).show();
			}
		} catch (IOException e) {
			Log.i("ZimDroid", "Error while saving page:"+e.getLocalizedMessage());
		}

		return false;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ZimDroid", "przed");
        setContentView(R.layout.activity_edit_page);
        Log.i("ZimDroid", "po");
        Bundle bundle = this.getIntent().getExtras();
        path = new File(bundle.getString("page_path"));
        if(path == null)
        	finish();
        else {
        	edtPage = (EditText) findViewById(R.id.edtPage);
        	//edtPage.setScroller(new Scroller(getApplicationContext()));
        	//edtPage.setMaxLines(1);
        	//edtPage.setVerticalScrollBarEnabled(true);
        	//edtPage.setMovementMethod(new ScrollingMovementMethod());
        	
        	try {
        		Content = "";
        		BufferedReader reader = new BufferedReader(new FileReader(path));
				String line;
				while( (line = reader.readLine()) != null) {
					//omitting "not important" rows:
					if(line.contains("Content-Type") || line.contains("Wiki-Format") || line.contains("Creation-Date"))
						continue;
					if(Content.equals("") && line.equals("\n"))
						continue;
					line = line.replace("•", "*");
					//line = line.replaceAll("([*][*]([\\w: +-]+)[*][*])","<b>$2</b>");
					//line = line.replaceAll("([/][/]([\\w: +-]+)[/][/])","<em>$2</em>");
					//line = line.replaceAll("([=]{6}([\\w: +-]+)[=]{6})","<h3>$2</h3>");
					//line = line.replaceAll("(^[•] ([\\w: +-]+))","<li>$2</li>");
					//line = line.replaceAll("(^[*][*]{0} ([\\w: +-]+))","<li>$2</li>");
					Content+=line+"\n";
					//Content+="<br />";
				}
				//Ooh, so dirty:
				//Content = Content.replace("<br /><br />", "<br />");
				//Content = Content.replace("<br /><h3>", "<h3>");
				//Content = Content.replace("</h3><br />", "</h3>");
				reader.close();
				//edtPage.getSettings().setDefaultTextEncodingName("utf-8");
				edtPage.setText(Content);
        	}
        	catch(IOException e) {
        		Log.i("ZimDroid", "Cannot read file:"+path.getPath());
        	}
        	
        }      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_page, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.Save) {
    		Log.i("ZimDroid", "menuitem save!"+edtPage.getText().toString());
    		page_save(edtPage.getText().toString());
    	}
    	return true;
    }
    
}
