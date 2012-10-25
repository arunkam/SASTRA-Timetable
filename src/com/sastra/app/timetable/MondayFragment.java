package com.sastra.app.timetable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;




public  class MondayFragment extends Fragment {
	private static ListView mList;
	private static ArrayList<HashMap<String, Object>> results;
	private static InternalDB datax;
    private static String colorVet[];
    private static SimpleAdapter simpleAdapter;
    private static String[] from = {"SName","SCode","STeacher","HType","HClass","HStart","HEnd"};
    private static int[] to = {R.id.SName,R.id.STeacher,R.id.SCode,R.id.HType,R.id.HClass,R.id.HStart,R.id.HEnd};
    


	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup grp, Bundle icicle) {
		View v = inf.inflate(R.layout.timetable_monday_fragment, grp, false);
		mList = (ListView) v.findViewById(R.id.monday); 
		
		if(simpleAdapter != null){
		simpleAdapter.notifyDataSetChanged();
		return v;
		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		datax = new InternalDB(getActivity());
		registerForContextMenu(mList);
		
		
	update();
	}
	
	



private void update() {
	datax.open();
	results = datax.selectAllFromDay(0);
	datax.close();
	colorVet = new String[results.size()];
    for(int i=0;i<results.size();i++) {
    	HashMap<String, Object> color = new HashMap<String, Object>();
    	color = results.get(i);
    	colorVet[i] = (String) color.get("SColor");
    }
    simpleAdapter = new myAdapter(getActivity(), results, R.layout.timetable_daylist, from, to, colorVet);
    mList.setAdapter(simpleAdapter); 
    simpleAdapter.notifyDataSetChanged();
}

public class myAdapter extends SimpleAdapter{
    
	String[] colors;
    public myAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, String[] col) {
        	super(context, data, resource, from, to);
            colors = col;
    	}
        @Override
		public View getView(int position, View convertView, ViewGroup parent ) {
        	View view = super.getView(position, convertView, parent);
        	View label = view.findViewById(R.id.colorLabel);
            label.setBackgroundColor(Color.parseColor(colors[position]));
            return view;
        }
}


	
}







