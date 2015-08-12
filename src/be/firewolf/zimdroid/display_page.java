package be.firewolf.zimdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.List;

import org.apache.http.util.EncodingUtils;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
//import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
//import android.support.v4.app.NavUtils;

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


	/*
	 * http://www.java2s.com/Code/Android/File/FileNameExtensionUtils.htm
	 */
	private static String getRemovedExtensionName(String name){
		String baseName;
		if(name.lastIndexOf(".")==-1){
			baseName=name;

		}else{
			int index=name.lastIndexOf(".");
			baseName=name.substring(0,index);
		}
		return baseName;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);
        Bundle bundle = this.getIntent().getExtras();
        File path = new File(bundle.getString("page_path"));
        //File zimfile = new File(bundle.getString("notepad_path"));
        if(path != null) {
        	//finish();
        //else {
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
        		String Content = "<style type='text/css'>" +
						"a          { text-decoration: none      }" +
						"a:hover    { text-decoration: underline }" +
						"a:active   { text-decoration: underline }" +
						"strike     { color: grey                }" +
						"u          { text-decoration: none;" +
						" background-color: yellow   }" +
						"tt         { color: #2e3436;            }" +
						"pre        { color: #2e3436;" +
						" margin-left: 20px          }" +
						"h1         { text-decoration: underline;" +
						" color: #4e9a06; margin-bottom: 0 }" +
						"h2         { color: #4e9a06; margin-bottom: 0 }" +
						"h3         { color: #4e9a06; margin-bottom: 0 }" +
						"h4         { color: #4e9a06; margin-bottom: 0 }" +
						"h5         { color: #4e9a06; margin-bottom: 0 }" +
						"p          { margin-top: 0              }" +
						"span.zim-tag {" +
						"color: #ce5c00;" +
						"}" +
						"div.zim-object {" +
						"border-style:solid;" +
						"border-width:1px;" +
						"}" +
						".checked-box {list-style-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAANOgAADMQBiN+4gQAAAAd0SU1FB9gKGQ8sMEGsKGkAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAEBUlEQVRIx62V22tdRRTGf7Nn73P2ybntnNOe3NqkPTGgLTVUUZF6QatSLOKTPgqCIqLgQ0H/A1sQQbBYCBb1QfAxiC8tSO1FqHkwJVKtjdTGNraUmObsc9nXmfGh7cGYpM1D5nHWzPetteZb3wg2eB2YqYm4zSadsMtoboiNBH/3TE0awx6j+MRoxoTg/IYRvP19TQrJS0bzhdHGSyKFkLTtjSKwMjyiEz43ynhtP6bdjBCWyFobAf7eT7VhNF/q1FRbjYjmUohlCVPwnB+6FUxMTJipqSmUUhhjEGKd3bMT4ks/Y6oLBK2Yth8hHYtCJXOix7Nf7xLMzc0xOzvLzp078TyPNE3viW3QJPXzhNWbxFFKHCmMhoLn/FHodd48vGfhapdAacXQlkFK5dL6wIUm6fuTZPuvqDQhaMUYYyiVyuQr6rXDexYuAdi3tSv1ZJNs/R/CaszzT+1na88uXFnCEnJVgivBNN8uTJKmHQI/ptOOcXNZzMz9mOqFs90OHpipWcYwlo5P4ebnuOkrvr5wgrH+h3im7y36MzuwRXYZeKha/OhP0EkadFoxQSdGSotedR/+XwMc2XvKdNUFOFqZx6LKZWIiwjgkikNmLp/hm8sH+K1zjFTHXfBYdTi+eJArzXM0GxFxoBDCopLvo/fqEwi1XPkWkGqjFo2TgB1jOYZUKZTS/D1/ncmLh7jon0IbRWoiTi59ymzzJEEQE3cStNZsGxqlfPE57MBbOR8fP3hDGalOO9fq2DlBvmZw8xa2IxACGn6TydlD/O6f5OzSV/zif0cYhLQaEXGkKBbz7Ov/AOlXV1cxgBJRI3fuSTrpTawt18kWIZN1CFuaONI0w0WOXfsI43YIggh/KUSlhqxrMz74AkOZcWBm9QkH+Gw8NDLuITi+m0yzhluSyJzBLcpblUhFxywSRAEtPwQjsKVN30CNh0uvYuOubSHLtN3J0TO1j0pmBNuFbFWRK0gyPRZpktL2I5JQkclKakNlnh54g6ocvevUr/Ai2a7wineEkcJupA3S1Wg0nVZM2E6wbEF5U5G9Q++wI7sfR7h3N8HVNstykBfzH+KJEZwiWD0aIwxCgJ0R1Mu7GXOeJSuK93bZtQIle4D9pUNU5DC5jEsu55AvZakM5NicGyEj8uuz8bUCQgj67QfY671P3vEoeC69gy695U1U7NG7XV0pUwBjDJa1/JJlWWxzHuflzQe5FJ/GsgUVuZ2t8lEkTvfc0aNHb72flBhjVicQQqCUuvM3/M+WDVguWBrMVdDXEGZlBVEUrVCU9d9s5+fnaTQa2PZyPxEIhJaI1EEoZwX4ncynp6fXrmB4eJjR0VFarRbNZnP9P9rt9gohqNVq1Ov1ZbF/AZGev3hLJ2/zAAAAAElFTkSuQmCC)}" +
						".xchecked-box {list-style-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAANOgAADMQBiN+4gQAAAAd0SU1FB9gKGQ8bDYnDxEwAAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAEK0lEQVRIx9WVS2hTWRjHf/eR3CY1nbxMH2YiZRQS6qO13YlMVxY3SnVcuNIBFezGpSADLoQqLu1sHJCqdCFSXFpw4YOCSH3BtFqttTNamabX3DS5bfO6uffMoglja3RGcDMHzuac7/z/53++//cd+L8P6VuCPQYZ8ADNgBd4J31DcDcQs+GnHByRocEDv0kfBSjAOlYCs11Q+gpwDegS8LMJ+3QIK0ATzEhV8Odnz5bzw8P4dJ25aJQ/WlvJ1df/K7hSLtOcTNI+Pk69rpMTgqIQhCDvh1/VSpw79+gRrRMTmLZNezLJJsPg+a5dmOEwQlFqg1sWG16/Jv7sGWXDwBACFQjC9HcwIMONKkGp4PGAJGEDS0IQmZlhnWnye3c3eiyGo6qr3WHbrJ+dJf7gAXI6zSIr72T7/fgzmT4FHnTBsgrQBfYvTU0km5vxz86iADnAm0rRPTWFt7cXZccOJJcLAGHb2K9ekT93jmwmwwdAAFpjI6Ntbfxw5879ag7l6o1sr5eHHR3IsRgeQK/M4sQE+YEB7JcvEY6zAj45SWFgAPPxYwzHoQxIkQjTPT0kIxE+Noj8sexFn4/xnh58iQTeSpHkHAdrbIzi0BCOrmNPTpK/eJHM3bt8sCyKQCiR4NWePWSiUZw1+ZLXJm4pFKLhzBlCsRh2RUXacVgeGaF47RrL58+zcP8+RrmMkCQinZ1EL1zAjERqmkH+tLYl1G3bCJw4QUjT0IA0MJfLMX/5MqmHD0nZNiUgtGULG/r7ccXjINWuWbnmqsuFu7sb/4EDNLlcBIEioNs2KUAFGmMxmk6dQm1tRZI+3xBqEkiShBQOox05Ql1nJ26gvuIUAWiKQnj/ftStW5Fk+YuF+NldsbBA4cYN9KdPmaso8Fc62ZJtk7l1C2t0FGdxESHE1xE4hkHh6lX0oSHSpRIewC/LrPf7CSgKNpCamkI/fZr8pUuIZBIcpyaBunahPp1mub+fDyMjGKUSChCsq6Nh717q9u2jbnSU0uAgRrFIwTThyhUCqRS+hgZKLS1fJvDm87SNjZGcnsYUAjcQ8vsJ9/Xh7u1FDgRQN20iks3iDA+zZFmYhQLqzZtsj8WY3L0baY2Sf55ICCKpFHUzM2SEQAJCHg+hY8fQDh5EDgRWDoRC1J88SePRo2geD0XAcBy8b98Sv3ePYDZbbf2rFQjLIphMsmDbaEBQVQkePox26BCSz7e6i4bDrDt+nGYhmBscpFAskheC4Js3bPR4qHphFYEnlcI7P4/jdqNpGu8TCe4oCsXr1z//F2ga3+/cSfTJE0qmSVYIsKzaOZDcbjKyzFIiwfvt21kMBLA07YsetzWNd+3tLLW0sH5igvT8PH9Go/z44kX+E4LGjg7GDYOcy4XlOEgLC//5P/5LCFzxOPLmzWyIx+m6fduu7v0NVGqyTSycKksAAAAASUVORK5CYII=)}" +
						".unchecked-box {list-style-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAAAXNSR0IArs4c6QAAAAZiS0dEAP8A/wD/oL2nkwAAAAlwSFlzAAANOgAADMQBiN+4gQAAAAd0SU1FB9gKGQ8qAt8h3m8AAAAZdEVYdENvbW1lbnQAQ3JlYXRlZCB3aXRoIEdJTVBXgQ4XAAAA60lEQVRIx+2VsQqDMBRF70sCLg5OLoKgjk7+lJ/hh+STXBwcnRz8ArMEkrxOFktbaC3tULzTg5e8k5vADXDq70VbobXmvu/hvQczg4heHrJfXxQFuq67blZbMc8zpmlCXddIkgTOuZcBUko45zCOI6y1Nz2xFSEEZFmGOI7fGg4A3nsQEZqmuXOu9jallACAtm3fvmutNaIoAjM/dkBECCF89KCbk4eAb+kEnIAT8EsAM0OIz3hSyrssUvss8t5fg+uIrLXPs0gIgWVZYIyBUurQyYdheO4gz3NUVQVjDNZ1PfSjpWmKsixvehfB9GBZ3NndrgAAAABJRU5ErkJggg==)}" +
						"ul {list-style-image: none}" +
						"/* ul rule needed to reset style for sub-bullets */" +
						"</style>";
        		if(!(path.exists())) {
        			
        		}
        		else {
        			BufferedReader reader = new BufferedReader(new FileReader(path));
        			String line;
					Boolean first_empty = true;
					String relpath = "";
					Uri uri;
					String alt = "";
					MatchResult mr;
					Pattern p;
					Matcher m;
					File file;
					while( (line = reader.readLine()) != null) {
        				//omitting "not important" rows:
        				if(line.contains("Content-Type") || line.contains("Wiki-Format") || line.contains("Creation-Date"))
        					continue;
						if (line.contentEquals("")) {
							first_empty = false;
							continue;
						}
						p = Pattern.compile("([{][{]([^|{]+)[|]*(.*)[}][}])");
						m = p.matcher(line.toString());
						while (m.find()) {
							mr = m.toMatchResult();
							relpath = mr.group(2);
							alt = mr.group(3);
							file = new File(getRemovedExtensionName(path.getAbsolutePath()), relpath);
							uri = Uri.fromFile(file.getAbsoluteFile());
							line = line.replaceFirst("([{][{]([^|{]+)[|]*(.*)[}][}])", "<img src=\"" + uri.toString() +"\" alt=\""+ alt.toString() +"\" />"); //for images
						}
						line = line.replaceAll("([*][*]([\\w'’\\?,\\/«»: +-]+)[*][*])","<b>$2</b>"); //for bold text
						line = line.replaceAll("([_][_]([\\w'’\\?,\\/«»: +-]+)[_][_])","<u>$2</u>"); //for underlined text
        				//line = line.replaceAll("([/][/]([\\w'\?,’,«»\/: +-]+)[/][/])","<em>$2</em>"); //for italics
        				line = line.replaceAll("([=]{5}=*([\\w'’\\?,\\/«»: +-]+)=*[=]{5}=*)","<h1>$2</h1>"); //for headers
						line = line.replaceAll("([=]{4}([\\w'’\\?,\\/«»: +-]+)[=]{4})","<h2>$2</h2>"); //for headers
						line = line.replaceAll("([=]{3}([\\w'’\\?,\\/«»: +-]+)[=]{3})","<h3>$2</h3>"); //for headers
						line = line.replaceAll("([=]{2}([\\w'’\\?,\\/«»: +-]+)[=]{2})","<h4>$2</h4>"); //for headers
						line = line.replaceAll("([=]{1}([\\w'’\\?,\\/«»: +-]+)[=]{1})","<h5>$2</h5>"); //for headers
						line = line.replaceAll("(^[•] ([\\w'’\\?,\\/v»: +-]+))","<li>$2</li>"); //for lists TODO: see below.
        				line = line.replaceAll("(^[*] ([\\w'’\\?,\\/«»: +-]+))", "<li>$2</li>");
						line = line.replaceAll("(^[*][*]{0} ([\\w'’\\?,\\/«»: +-]+))", "<li>$2</li>"); //for list TODO: set <ul>/<ol> counter
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
        			Content = Content.replaceAll("([/][/]([\\w': +-]+)[/][/])","<em>$2</em>"); //for italics
        			reader.close();
        			mdView.getSettings().setDefaultTextEncodingName("utf-8");
        			mdView.loadDataWithBaseURL("fake/",Content, "text/html", "utf-8", null);
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
