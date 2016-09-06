package com.timsnky.mymoney;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;
	
	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return !navDrawerItems.get(position).getSection();
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(isEnabled(position)){
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.navigation_drawer, null);
	        }
	         
	        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
	        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
	         
	        imgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
	        txtTitle.setText(navDrawerItems.get(position).getTitle());
			
		}else{
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)
	                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.navigation_drawer_section, null);
	        }
	        TextView txtTitle = (TextView) convertView.findViewById(R.id.sectionTitle);      
	        txtTitle.setText(navDrawerItems.get(position).getTitle());			
		}
		
        
        // displaying count
        // check whether it set visible or not     
        return convertView;
	}

}
