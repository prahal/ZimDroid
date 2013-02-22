package be.firewolf.zimdroid;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cx.ath.worm.zimdroid.ZimNotepad.ZimPage;

public class FolderViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private List<ZimPage> mPages = new ArrayList<ZimPage>();
	
	public static String iso (String s)	{
		byte bytes[] = EncodingUtils.getBytes(s,"iso-8859-1");
		return new String(bytes);
	}
	
	public static String utf (String s)	{
		byte bytes[] = EncodingUtils.getBytes(s,"utf-8");
		return new String(bytes);
	}
	
	public FolderViewAdapter(Context context, ArrayList<ZimPage> Pages) {
		mContext = context;
		mPages = Pages;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mPages.size();
	}

	@Override
	public Object getItem(int position) {
		return mPages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View row = convertView;
		if(row == null) {
			Log.i("ZimDroid", "Creating row layout...");
			row=mInflater.inflate(R.layout.row, null);
		}
		TextView pagename = (TextView)row.findViewById(R.id.txtPage);
		Button nextdir = (Button)row.findViewById(R.id.btnEnter);
		nextdir.setVisibility(Button.GONE);
		pagename.setLongClickable(true);
		pagename.setText(utf(mPages.get(position).getPrintName()));
		Log.i("ZimDroid", "Adding item: "+mPages.get(position).getName()+" / kids:"+mPages.get(position).getChildren());
		Log.i("ZimDroid","Adapter: "+mPages.get(position).getName()+" gC: "+mPages.get(position).getChildren());
		if(mPages.get(position).getChildren() != 0) {
			nextdir.setText(String.valueOf(mPages.get(position).getChildren()));
			nextdir.setVisibility(Button.VISIBLE);
		}
		pagename.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ZimDroid","ListClick: "+mPages.get(position).getPrintName());
				Intent intDisplayPage = new Intent(v.getContext(),display_page.class);
       			Bundle bundle = new Bundle();
       			bundle.putString("page_path", mPages.get(position).getPath().getPath());
       			bundle.putString("prev_view", mPages.get(position).getPath().getParent());
       			bundle.putString("notepad_path", mPages.get(position).getNotepadFile().getPath());
       			intDisplayPage.putExtras(bundle);
       			intDisplayPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		mContext.startActivity(intDisplayPage);
			}
		});
		
		pagename.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.i("ZimDroid","ListLongClick: "+mPages.get(position).getPrintName());
				Intent intEditPage = new Intent(v.getContext(),edit_page.class);
       			Bundle bundle = new Bundle();
       			bundle.putString("page_path", mPages.get(position).getPath().getPath());
       			bundle.putString("prev_view", mPages.get(position).getPath().getParent());
       			intEditPage.putExtras(bundle);
       			intEditPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		mContext.startActivity(intEditPage);
				return true;
			}
		});
		
		nextdir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ZimDroid","BtnClick: "+mPages.get(position).getPrintName());
				Intent intDisplayFolder = new Intent(v.getContext(),display_folder.class);
       			Bundle bundle = new Bundle();
       			bundle.putString("notepad_path", mPages.get(position).getNotepadFile().getPath());
       			bundle.putString("folder_inside", (mPages.get(position).getOwnFolder().getPath()));
       			bundle.putString("notepad_path", mPages.get(position).getNotepadFile().getPath());
       			intDisplayFolder.putExtras(bundle);
       			intDisplayFolder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		mContext.startActivity(intDisplayFolder);
			}
		});
		return row;
	}
	
}