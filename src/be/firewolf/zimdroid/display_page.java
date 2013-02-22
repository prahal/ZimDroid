package be.firewolf.zimdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.NavUtils;

public class display_page extends Activity {

	public static String iso (String s)	{
		byte bytes[] = EncodingUtils.getBytes(s,"iso-8859-1");
		return new String(bytes);
	}
	
	public static String utf (String s)	{
		byte bytes[] = EncodingUtils.getBytes(s,"utf-8");
		return new String(bytes);
	}
	
	public String LinkHandler(String text) {
		if(text.contains("|")) {
			// link with description:
			String[] divide = text.split("|");
			return "<a href='"+divide[0]+"'>"+divide[1]+"</a>";					
		}
		if(text.contains(":") && !text.contains("")) {
			//link to notepad page:
			
		}
		return text;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);
        Bundle bundle = this.getIntent().getExtras();
        File path = new File(bundle.getString("page_path"));
        File zimfile = new File(bundle.getString("notepad_path"));
        if(path == null)
        	finish();
        else {
        	WebView mdView = (WebView) findViewById(R.id.mdView);
        	mdView.setWebViewClient(new WebViewClient()
            {
        		@Override
        		public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        		    if(url.contains("mailto:")){
        		    	Intent intent = null;
        		    	intent = new Intent(Intent.ACTION_SEND);
        		    	intent.setType("plain/text");
        		    	intent.putExtra(Intent.EXTRA_EMAIL, url.substring(6));
        		    	startActivity(Intent.createChooser(intent, ""));
        		    }
        		    else if(url.contains("zim:")) {
        		    	
        		    }
        		    return true;
        		}                      
        	});
        	try {
        		String Content = "";
        		if(!(path.exists())) {
        			
        		}
        		else {
        			BufferedReader reader = new BufferedReader(new FileReader(path));
        			String line;
        			while( (line = reader.readLine()) != null) {
        				//omitting "not important" rows:
        				if(line.contains("Content-Type") || line.contains("Wiki-Format") || line.contains("Creation-Date"))
        					continue;
        				line = line.replaceAll("([*][*]([\\w: +-]+)[*][*])","<b>$2</b>"); //for bold text
        				//line = line.replaceAll("([/][/]([\\w: +-]+)[/][/])","<em>$2</em>"); //for italics
        				line = line.replaceAll("([=]{6}([\\w: +-]+)[=]{6})","<h3>$2</h3>"); //for headers
        				line = line.replaceAll("(^[•] ([\\w: +-]+))","<li>$2</li>"); //for lists TODO: see below.
        				line = line.replaceAll("(^[*] ([\\w: +-]+))","<li>$2</li>");
        				line = line.replaceAll("(^[*][*]{0} ([\\w: +-]+))","<li>$2</li>"); //for list TODO: set <ul>/<ol> counter
        				line = line.replaceAll("([\\[][\\[]([\\p{Print}]+)[\\]][\\]])", LinkHandler("$2")); //detect wiki and regular links
        				line = line.replaceAll("([a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+)","<a href='mailto:$1'>$1</a>"); //email to mailto link.
        				line = line.replaceAll("", "");
        				Content+=line;
        				Content+="<br />";
        			}
        			//Ooh, so dirty:
        			//Content = Content.replace("<br /><br />", "<br />");
        			Content = Content.replace("<br /><h3>", "<h3>");
        			Content = Content.replace("</h3><br />", "</h3>");
        			Content = Content.replaceAll("([/][/]([\\w: +-]+)[/][/])","<em>$2</em>"); //for italics
        			reader.close();
        			mdView.getSettings().setDefaultTextEncodingName("utf-8");
        			mdView.loadData(Content, "text/html", "utf-8");
        		}
        	}
        	catch(IOException e) {
        		Log.i("ZimDroid", "Cannot read file:"+path.getPath());
        	}
        	
        }       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_display_page, menu);
        return true;
    }

    
}
