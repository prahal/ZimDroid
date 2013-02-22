package be.firewolf.zimdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.format.Time;
import android.util.Log;

public class ZimNotepad {
	

	
	class ZimPage {
		private String Name = "";
		private String Content = "";
		private File Location;
		private File Notepad;
		private ArrayList<ZimPage> children = new ArrayList<ZimPage>();
		
		ZimPage(String name, File location, File notepad_file) {
			File fullPath = new File(location.getPath()+"/"+name+".txt");
			Location = fullPath;
			Name = name;
			Notepad = notepad_file;
			try {
				if(fullPath.exists()) {
					Log.i("ZimDroid",fullPath.getPath()+" exists!");
					BufferedReader reader = new BufferedReader(new FileReader(fullPath));
					String line;
					while( (line = reader.readLine()) != null) {
						Content += line;
						Content += "\n";
					}
					reader.close();
				}
				else {
					Log.i("ZimDroid", "ZimPage: attempt to write: "+fullPath.getPath());
					fullPath.createNewFile();
					Content = getDefaultContent();
					FileWriter writer = new FileWriter(fullPath);
					writer.append(Content);
					writer.close();
				}
			}
			catch(IOException e) {
				Log.i("ZimDroid", "ZimPage:Failed to read page "+fullPath+"::"+e.getLocalizedMessage());
			}
			}
			
		public int getChildren() {
			File kat = new File(Location.getParent()+"/"+Name+"/");
			Log.i("ZimDroid", "gC: "+kat.getPath());
			children.clear();
			if(kat.exists() && kat.isDirectory()) {
				if(kat.listFiles().length == 0) {
					//folder is empty
					kat.delete();
					return 0;
				}
				String[] pliki = kat.list();
				for(String apped : pliki) {
					if(apped.contains(".txt")) {
						children.add(new ZimPage(apped.substring(0,apped.length()-4), kat, Notepad));
						Log.i("ZimDroid","Child added: "+apped.substring(0,apped.length()-4));
					}
				}
				return children.size();
			}
			else
				return 0;
			}
		
		public String getDefaultContent() {
			Time today = new Time(Time.getCurrentTimezone());
			today.setToNow();
			return("==="+Name+"===\n"+"Created on "+today.format("%d %B %G")+"\n");
		}
		
		public String getName() {
			return Name;
		}
		
		public String getPrintName() {
			return Name.replace("_", " ");
		}
		
		public File getPath() {
			return Location;
		}
		
		public File getOwnFolder() {
			if(children.size() > 0) {
				File own = new File(Location.getParent()+"/"+Name);
				return own;
			}
			else
				return null;
		}
		
		public File getNotepadFile() {
			return Notepad;
		}
	}
		
	public String name = null;
	public String version = null;
	public String home = null;
	public File zimFile;
	public ArrayList<ZimPage> pages = new ArrayList<ZimNotepad.ZimPage>();
	
	ZimNotepad(String PathToFile) {
		zimFile = new File(PathToFile);
		try {
			//loading *.zim file, reading settings.
			BufferedReader reader = new BufferedReader(new FileReader(zimFile));
			Map<String,String> prefs = new HashMap<String, String>();
			String line;
			while((line = reader.readLine()) != null) {
				if(line.contains("=") && line.split("=").length > 1) {
					prefs.put(line.split("=")[0], line.split("=")[1]);
				}
			}
			reader.close();
			name = prefs.get("name");
			version = prefs.get("version");
			home = prefs.get("home");
			
			//loading pages at top level:
			File kat = zimFile.getParentFile();
			if(kat.exists() && kat.isDirectory()) {
				String[] pliki = kat.list();
				for(String apped : pliki) {
					if(apped.contains(".txt")) {
						pages.add(new ZimPage(apped.substring(0,apped.length()-4), kat, zimFile));
						Log.i("ZimDroid", "Page added: "+apped.substring(0,apped.length()-4));
					}
				}
			}
		}
		catch(FileNotFoundException e) {
			Log.i("ZimDroid", "EXC: Cannot find "+zimFile.getPath()+". ");
		}
		catch(IOException e) {
			Log.i("ZimDroid", "EXC: IOException while reading zim file. ");
		}
	}
	
	public ZimPage getPageByName(String Nazwa, ArrayList<ZimPage> pagez) {
		for(ZimPage akt : pagez) {
			if(akt.Name.equals(Nazwa)) {
				Log.i("ZimDroid","PageByName found:"+akt.getName());
				return akt;
			}

		}
		return null;
	}
	
	public ArrayList<ZimPage> getChildrenByPath(String spath) {
		Log.i("ZimDroid", "gCBP:"+spath+" / NC: "+zimFile.getParent());
		ArrayList<ZimPage> lista = pages;
		ZimPage testing;
		if(spath.contains("/")) {
			String[] whole_path = spath.split("/");
			for(String onepath : whole_path) {
				if(getPageByName(onepath, lista) != null) {
					testing = getPageByName(onepath, lista);
					testing.getChildren();
					lista = testing.children;
					Log.i("ZimDroid", "Children:"+lista.size());
				}	
			}
		}
		else {
			testing = getPageByName(spath, lista);
			testing.getChildren();
			lista = testing.children;
			Log.i("ZimDroid", "Children:"+lista.size());
		}
		return lista;
	}
	
	public boolean addPage(String Name, File location) {

		if(location.exists() && location.canWrite()) {
			File fullpath = new File(location.getPath()+"/"+Name+".txt");
			if(fullpath.exists()) {
				return false;
			}
			else {
				//ZimPage nowa = new ZimPage(Name, location, this.zimFile);
				return true;
			}
		}
		else{
			Log.i("ZimDroid", "New page failed.");
			return false;
		}
	}
}
