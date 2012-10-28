package com.sastra.app.timetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.viewpagerindicator.TitlePageIndicator;

public class TimetableActivity extends FragmentActivity implements OnClickListener
{
	
	private static String TAG = "AB";
	
	private static Intent intent;
	private static AlertDialog.Builder builder;
	private static AlertDialog.Builder builder2;
	private static AlertDialog alert;
	private static AlertDialog alert2;
	private static InternalDB data;
	 private static String action = "insert";
	 private static Dialog addDialog;
		private static ListView currentLW;
		 static Button b;

		private static ArrayList<HashMap<String, Object>> results;
	    private static SimpleAdapter simpleAdapter;
		private static String[] from = {"SName","SCode","STeacher","HType","HClass","HStart","HEnd"};
	    private static int[] to = {R.id.SName,R.id.SCode,R.id.STeacher,R.id.HType,R.id.HClass,R.id.HStart,R.id.HEnd};
	    private static String[] from2 = {"SName","SColor"};
	    private static int[] to2 = {R.id.text1,R.id.colorLabel2};
	    static int currentDay;

	    private static EditText HClass;
	    private static EditText HType;
	    private static String[] arraydays;
	    private static ArrayAdapter<String> Sarrayadapter;
	    private static ArrayAdapter<String> arrayadapter;
	    private static String selectedDay;
	    private static TimePickerDialog fromDialog;
	    private static TimePickerDialog toDialog;
	    private static TextView start;
	    private static TextView end;
	    private static String[] arraySubjects;
	    private static Spinner SName;
	    private static Spinner HDay;
	    private static String selectedSubject;
	    private static String colorVet[],colo[];

	    private static String OLDSName;
	    private static String OLDHType;
	    private static String OLDHClass;
	    private static int OLDHDay;
	    private static String OLDHStart;
	    private static String OLDHEnd;

	private ViewPager mPager;
	private TitlePageIndicator mIndicator;
	private MainPagerAdapter mAdapter;
	private List<Fragment> mFragments;
	
	private static final String MONDAYFRAGMENT = MondayFragment.class.getName();
	private static final String TUESDAYFRAGMENT = TuesdayFragment.class.getName();
	private static final String WEDNESDAYFRAGMENT = WednesdayFragment.class.getName();
	private static final String THURSDAYFRAGMENT = ThursdayFragment.class.getName();
	private static final String FRIDAYFRAGMENT = FridayFragment.class.getName();
	private static final String SATURDAYFRAGMENT = SaturdayFragment.class.getName();


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable_main);
		
		Log.d(TAG,"Entered On Create");
		
		ActionBar Bar = getActionBar();
		Bar.setLogo(R.drawable.ic_launcher_1);
		//Bar.setTitle("");
		 // String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/SASTRA Timetable/files/test.txt";
		
		 
		 
		 SharedPreferences mPreferences = this.getSharedPreferences("AB",MODE_PRIVATE);
		 
		 
		 boolean firstTime = mPreferences.getBoolean("firstTime", true);
		 if (firstTime) 
		 {
			 
		     SharedPreferences.Editor editor = mPreferences.edit();
		     editor.putBoolean("firstTime", false);
		     editor.commit();
		     Dialog d2 = new Dialog(this);
           	d2.setTitle("HELP");
           	d2.setCanceledOnTouchOutside(true);
              	TextView tv = new TextView(this);
           	tv.setText("This application can be used to store your timetable.First,click on Manage Subjects Icon(Wrench Icon) inorder to add all your subjects.Click on Add Subjects on top and enter the details of your subject.The fields that appear are optional and not manditory.Once you have added all the subjects,go back to the main screen and click on Add Classes icon(Plus Icon) to add the different classes to your timetable.The fields that appear are again optional and all of them need not be filled.You can long press on any particular subject to edit or delete them.You can delete all the informations using Delete All(Garbage Icon) option.");
           	ScrollView sav = new ScrollView(this);
           	sav.addView(tv);
              	LayoutParams lap = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
              	d2.addContentView(sav,lap);
              	d2.show();
		 }
		
		
		 boolean enabled = true;
		
		Bar.setHomeButtonEnabled(enabled);
		
        //File database=getApplicationContext().getDatabasePath("massey.db");
        // if (database.exists()) {
         data = new InternalDB(this);
        // }
        	 
         
		// add fragments
		mFragments = new ArrayList<Fragment>();
		
		
		mFragments.add(Fragment.instantiate(this, MONDAYFRAGMENT));
		mFragments.add(Fragment.instantiate(this, TUESDAYFRAGMENT));
		mFragments.add(Fragment.instantiate(this, WEDNESDAYFRAGMENT));
		mFragments.add(Fragment.instantiate(this, THURSDAYFRAGMENT));
		mFragments.add(Fragment.instantiate(this, FRIDAYFRAGMENT));
		mFragments.add(Fragment.instantiate(this, SATURDAYFRAGMENT));
		
		// adapter
		mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
		
		// pager
		mPager = (ViewPager) findViewById(R.id.view_pager);
		
		mPager.setAdapter(mAdapter);
		
		// indicator
		mIndicator = (TitlePageIndicator) findViewById(R.id.title_indicator);
		
		//mIndicator.setBackgroundColor(0x0106000e);
		//mIndicator.setFooterColor(0xffffff);
		mIndicator.setTextColor(0xFF000000);
		mIndicator.setSelectedColor(0xFF000000);
		mIndicator.setViewPager(mPager);
		
		
		intent = new Intent(getApplicationContext(), AddSubjects.class);//intent = new Intent(getApplicationContext(), AddSubjects.class);
	    
    	builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all lessons?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   data.open();
                	   data.deleteAllHours();
                	   data.close();
                      // update();
                	   mAdapter.notifyDataSetChanged();
                	   mAdapter.finishUpdate(mPager);
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        alert = builder.create();
        
    	builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("Are you sure you want to delete this lesson?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   data.open();
                	   data.deleteHour(OLDSName, OLDHDay, OLDHType, OLDHClass, OLDHStart, OLDHEnd);
                	   data.close();
                	   //update();
                	   mAdapter.notifyDataSetChanged();
                	   mAdapter.finishUpdate(mPager);
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        alert2 = builder2.create(); 
        
        //added
        
    	
    	addDialog = new Dialog(this);
	    addDialog.setCancelable(false);
	    action = "insert";
	    addDialog.setTitle("Add Class");
	    addDialog.setContentView(R.layout.timetable_addhour);
	    
	    HType = (EditText) addDialog.findViewById(R.id.typeEdit);
	    HClass = (EditText) addDialog.findViewById(R.id.classroomEdit);
	    arraydays = new String []{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        HDay = (Spinner) addDialog.findViewById(R.id.day);
        arrayadapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, arraydays);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HDay.setAdapter(arrayadapter);
        HDay.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {                    
                 selectedDay = arraydays[position];               
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
        });
        
        fromDialog = new TimePickerDialog(this, fromTimeSetListener, 12, 0, true);
        toDialog = new TimePickerDialog(this, toTimeSetListener, 12, 0, true);
        
        
        //This place is important.
        
        SName = (Spinner) addDialog.findViewById(R.id.SName);
        SName.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {                    
                 selectedSubject = (String) arraySubjects[position];
                 Log.d("AB","We are inside the setOnItemSelectedListener for Sname : 264");
                 Log.d("AB",selectedSubject);
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
        });
        
        Button buttonFrom = (Button) addDialog.findViewById(R.id.buttonFrom);
        buttonFrom.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				fromDialog.show();
			}
		});
        
        Button buttonTo = (Button) addDialog.findViewById(R.id.buttonTo);
        buttonTo.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				toDialog.show();
			}
		}); 
	    
        start = (TextView) addDialog.findViewById(R.id.start);
	    end = (TextView) addDialog.findViewById(R.id.end);
	    
	    Button cancel = (Button) addDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
                       
        	public void onClick(View v) {
        		addDialog.cancel();
        		fromDialog.updateTime(12, 0);
        		toDialog.updateTime(12, 0);
        		start.setText("--:--");
        		end.setText("--:--");
        		HType.setText(null);
        		HClass.setText(null);
            }
        	
        });
        
        Button ok = (Button) addDialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
                       
        	public void onClick(View v) {                              
        		int HDay = SDay2IDay(selectedDay);
        		if(action.equals("insert")){
        			data.open();
        			data.insertIntoHours(selectedSubject, HDay, HType.getText().toString(), HClass.getText().toString(), start.getText().toString(), end.getText().toString());
        			data.close();}
        		else if(action.equals("edit")) {
        			data.open();
        			data.updateHours(OLDSName, OLDHType, OLDHClass, OLDHDay, OLDHStart, OLDHEnd, selectedSubject, HDay, HType.getText().toString(), HClass.getText().toString(), start.getText().toString(), end.getText().toString());
        			data.close();}
        		addDialog.cancel();
        		fromDialog.updateTime(12, 0);
        		toDialog.updateTime(12, 0);
        		start.setText("--:--");
        		end.setText("--:--");
        		HType.setText(null);
        		HClass.setText(null);
        		mAdapter.notifyDataSetChanged();
        		//update();
        		mAdapter.finishUpdate(mPager);
            }	
        });
        
	}
	

	//remove
	protected void update() {
    	data.open();
    	results = data.selectAllFromDay(currentDay);
    	data.close();
		colorVet = new String[results.size()];
	    for(int i=0;i<results.size();i++) {
	    	HashMap<String, Object> color = new HashMap<String, Object>();
	    	color = results.get(i);
	    	colorVet[i] = (String) color.get("SColor");
	    }
	    simpleAdapter = new myAdapter(this, results, R.layout.timetable_daylist, from, to, colorVet);
	    currentLW.setAdapter(simpleAdapter); 
    }
	  
		public class myAdapter extends SimpleAdapter{
	        
			String[] colors;
	        public myAdapter(Context context, List<? extends Map<String, ?>> data,
	        	int resource, String[] from, int[] to, String[] col) {
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
		

		public class mySpinnerAdapter extends SimpleAdapter{
	        
			String[] colors;
	        public mySpinnerAdapter(Context context, List<? extends Map<String, ?>> data,
	        	int resource, String[] from, int[] to, String[] col) 
	        {
	            	super(context, data, resource, from, to);
	            	Log.d("AB","And now we are in the constructor. :374");
	                colors = col;
	        	}
	            @Override
				public View getView(int position, View convertView, ViewGroup parent ) 
	            {
	            	Log.d("AB","We are inside getView :380");
	            	Log.d("AB","This has been called already :"+String.valueOf(position));
	            	Log.d("AB",colors[position]);
	            	View view = super.getView(position, convertView, parent);
	            	View label = view.findViewById(R.id.colorLabel2);
	            	if(label.equals(null))
	            	{
	            		Log.d("AB","Null label!!");
	            	}
	                label.setBackgroundColor(Color.parseColor(colors[position]));
	                return view;
	            }
	    }
	
		public class newSpinnerAdapter extends ArrayAdapter<String>
{
	        
			//String[] colors;
			
			

	        public newSpinnerAdapter(Context context, int textViewResourceId,String[] objects)
	        {
	        	
	            super(context, textViewResourceId, objects);
	            Log.d(TAG,"We are inside My Adapter now : 407");
	        }
	 
	        @Override
	        public View getDropDownView(int position, View convertView,ViewGroup parent) 
	        {
	        	Log.d(TAG,"We are inside getDropDownView now : 413");
	            return getCustomView(position, convertView, parent);
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) 
	        {	
	        	Log.d(TAG,"We are inside getView now : 420");
	            return getCustomView(position, convertView, parent);
	        }
	 
	        public View getCustomView(int position, View convertView, ViewGroup parent) 
	        {
	        	Log.d(TAG,"Custom View : 426");
	            LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.timetable_spinner_layout, parent, false);
	            View label=(View)row.findViewById(R.id.colorLabel2);
	            label.setBackgroundColor(Color.parseColor(colo[position]));
	 
	            TextView sub=(TextView)row.findViewById(R.id.text1);
	            sub.setText(arraySubjects[position]);
	 
	           
	            return row;
	            }
	      
	    }
	
		
		
		
		
		
		
	  private int SDay2IDay(String selectedDay) {
	    	
	    	if(selectedDay.equals("Tuesday"))
	    		return 1;
	    	else if(selectedDay.equals("Wednesday"))
	    		return 2;
	    	else if(selectedDay.equals("Thursday"))
	    		return 3;
	    	else if(selectedDay.equals("Friday"))
	    		return 4;
	    	else if(selectedDay.equals("Saturday"))
	    			return 5;
	    	
	    	return 0;
	    }
	
    private TimePickerDialog.OnTimeSetListener fromTimeSetListener =
        	new TimePickerDialog.OnTimeSetListener() {

    			public void onTimeSet(TimePicker view, int hour,
    					int minute) {
    				
    				StringBuilder sb = new StringBuilder();
    				
    				if(hour<10)
    					sb.append("0");
    				sb.append(hour+":");
    				if(minute<10)
    					sb.append("0");
    				sb.append(minute);
    				
    				start.setText(sb);
    			}

        };
        
        private TimePickerDialog.OnTimeSetListener toTimeSetListener =
        	new TimePickerDialog.OnTimeSetListener() {

    			public void onTimeSet(TimePicker view, int hour,
    					int minute) {
    				
    				StringBuilder sb = new StringBuilder();
    				
    				if(hour<10)
    					sb.append("0");
    				sb.append(hour+":");
    				if(minute<10)
    					sb.append("0");
    				sb.append(minute);
    				
    				end.setText(sb);
    			}
        };
 
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		  boolean result = super.onCreateOptionsMenu(menu);
		  	
	        menu.add(0, 0, 0, "Manage Subjects").setIcon(android.R.drawable.ic_menu_manage).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        menu.add(1, 1, 1, "Add Class").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        menu.add(1, 2, 2, "Delete All").setIcon(android.R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        menu.add(1, 3, 3, "About").setIcon(android.R.drawable.ic_menu_info_details).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        menu.add(1,4,4,"Help").setIcon(android.R.drawable.ic_menu_help).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	        return result;
		
	}
	//startActivity(intent);
	 //(com.actionbarsherlock.view.MenuItem item)
	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case 0:
	            	startActivityForResult(intent, 0);
	                break;
	            case 1:
	            	action = "insert";
	            	data.open();
	            	ArrayList<HashMap<String, Object>> arrayS = data.selectSubjects2();
	            	data.close();
	            	arraySubjects = new String [arrayS.size()];
	                colo = new String [arrayS.size()];
	            	
	                Iterator<HashMap<String, Object>> it = arrayS.iterator();
	                int i = 0;
	                while(it.hasNext()) {
	                	HashMap<String, Object> hm = it.next();
	                	arraySubjects[i] = hm.get("SName").toString();
	                	colo[i] = hm.get("SColor").toString();
	                	Log.d("AB","This is inside the onOptionsItemSelected Function :485");
	                	Log.d("AB",arraySubjects[i]);
	                	Log.d("AB",colo[i]);
	                	i++;
	                }
	                
	                newSpinnerAdapter ma = new newSpinnerAdapter(TimetableActivity.this,R.id.text1,arraySubjects);
	                SName.setAdapter(ma);
	                
	                //mySpinnerAdapter msa = new mySpinnerAdapter(this,arrayS,R.layout.timetable_spinner_layout,from2, to2,colo );
	                //msa.setDropDownViewResource(R.layout.timetable_spinner_layout);
	                //  SName.setAdapter(msa);
	                
	                
	                addDialog.setTitle("Add Class");
	            	addDialog.show();
	            	
//	            	data.open();
//	            	results2 = data.selectSubjects2();
//	            	data.close();
//	            	colorVet2 = new String[results2.size()];
//	        		 for(int i=0;i<results2.size();i++) {
//	        	    	HashMap<String, Object> color = new HashMap<String, Object>();
//	        	    	
//	        	    	color = results2.get(i);
//	        	    	colorVet2[i] = (String) color.get("SColor");
//	        	    }
//	                
	            	
	               // Sarrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySubjects);
	                //Sarrayadapter = new ArrayAdapter<String>(this,R.layout.timetable_spinner_layout, arraySubjects);
	                //Sarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	                
	              
	            	break;
	            case 2:
	            	alert.show();
	            	break;
	            case 3:
	            	
	            	Dialog d = new Dialog(this);
	            	d.setCanceledOnTouchOutside(true);
	            	d.setTitle("ABOUT");
	               	TextView tv2 = new TextView(this);
	            	tv2.setText("This application was developed by R.R.Arun Balaji,a student of SASTRA University.Special Thanks to J.Sivaguru from SASTRA.\n\nLICENSE INFORMATION : \nSASTRA TimeTable\nThis program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.This program is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.This program makes use of the following libary ViewPageIndicator licensed under Apache License 2.0 and modifies the StudentTimeTable application made by Mazzarelli Alessio and Hopstank,distributed under GNU GPL v3.");
	            	ScrollView sv = new ScrollView(this);
	            	sv.addView(tv2);
	               	LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	               	d.addContentView(sv,lp);
	            	d.setTitle("About");
	            	d.show();
	            	break;
	            case 4:
	            	Dialog d2 = new Dialog(this);
	            	d2.setTitle("HELP");
	            	d2.setCanceledOnTouchOutside(true);
	               	TextView tv = new TextView(this);
	            	tv.setText("This application can be used to store your timetable.\nFirst,click on Manage Subjects Icon(Wrench Icon) inorder to add all your subjects.Click on Add Subjects on top and enter the details of your subject.The fields that appear are optional and not manditory.\n\nOnce you have added all the subjects,go back to the main screen and click on Add Classes icon(Plus Icon) to add the different classes to your timetable.The fields that appear are again optional and all of them need not be filled.You can long press on any particular subject to edit or delete them.You can delete all the informations using Delete All(Garbage Icon) option.");
	            	ScrollView sav = new ScrollView(this);
	            	sav.addView(tv);
	               	LayoutParams lap = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	               	d2.addContentView(sav,lap);
	               	d2.show();
	            	break;
	            
	            
	        }
	        return false;
	    }
		
	  
	    @Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    	super.onCreateContextMenu(menu, v, menuInfo);
	    	menu.add(0, 1, 0, "Edit");
	    	menu.add(0, 2, 0, "Delete");
	    	menu.setHeaderTitle("Options");	
	    }
	    
	    public  boolean onContextItemSelected(android.view.MenuItem item) {
	    	data.open();
	    	int day = currentDay = mPager.getCurrentItem();
	    	System.out.println(day);
	    	results = data.selectAllFromDay(day);
	    	data.close();
	    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    	String course = results.get(info.position).get("SName").toString();
	    	String type = results.get(info.position).get("HType").toString();
	    	String classroom = results.get(info.position).get("HClass").toString();
	    	String startH = results.get(info.position).get("HStart").toString();
	    	String endH = results.get(info.position).get("HEnd").toString();
	    	
	    	
	    	switch (item.getItemId()) 
	    	{
	        	case 1:
	        		
	        		action = "edit";
	        		data.open();
	            	ArrayList<HashMap<String, Object>> arrayS = data.selectSubjects();
	            	data.close();
	            	arraySubjects = new String [arrayS.size()];
	            	colo = new String[arrayS.size()];
	                Iterator<HashMap<String, Object>> it = arrayS.iterator();
	                int i = 0;
	                while(it.hasNext()) {
	                	HashMap<String, Object> hm = it.next();
	                	arraySubjects[i] = hm.get("SName").toString();
	                	colo[i] = hm.get("SColor").toString();
	                	i++;
	                }
	                
	                newSpinnerAdapter maa = new newSpinnerAdapter(this,R.id.text1,arraySubjects);
	                SName.setAdapter(maa);
//	                Sarrayadapter = new ArrayAdapter<String>(this,R.layout.timetable_spinner_layout,arraySubjects);
//	                Sarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	                SName.setAdapter(Sarrayadapter);
	        		SName.setSelection(maa.getPosition(course));
	        		String dayS = IDay2SDay(day);
	        		HDay.setSelection(arrayadapter.getPosition(dayS));
	            	CharSequence fhour = startH.subSequence(0, 2);
	            	CharSequence fminutes = startH.subSequence(3, 5);
	            	CharSequence thour = endH.subSequence(0, 2);
	            	CharSequence tminutes = endH.subSequence(3, 5);
	            	int fhourInt;
	            	int fminutesInt;
	            	try{
	                	fhourInt = Integer.parseInt((String)fhour);
	                	fminutesInt = Integer.parseInt((String)fminutes);
	            	} catch(Exception e) {
	            		fhourInt = 12;
	                	fminutesInt = 0;
	            	}
	            	int thourInt;
	            	int tminutesInt;
	            	try{
	                	thourInt = Integer.parseInt((String)thour);
	                	tminutesInt = Integer.parseInt((String)tminutes);
	            	} catch(Exception e) {
	            		thourInt = 12;
	                	tminutesInt = 0;
	            	}
	        		fromDialog.updateTime(fhourInt, fminutesInt);
	        		toDialog.updateTime(thourInt, tminutesInt);
	        		start.setText(startH);
	        		end.setText(endH);
	        		HType.setText(type);
	        		HClass.setText(classroom);
	        		addDialog.setTitle("Edit Class");
	        		OLDSName = course;
	        		OLDHType = type;
	        		OLDHClass = classroom;
	        		OLDHDay = day;
	        		OLDHStart = startH;
	        		OLDHEnd = endH;
	        		addDialog.show();
	        		break;
	        	case 2:
	        		OLDSName = course;
	        		OLDHType = type;
	        		OLDHClass = classroom;
	        		OLDHDay = day;
	        		OLDHStart = startH;
	        		OLDHEnd = endH;
	        		alert2.show();
	        		break;
	    	}
	    	
	    	
	    	mAdapter.notifyDataSetChanged();
	    	mAdapter.finishUpdate(mPager);
	    	//update();
	    	return true;
	    }
	    
	    private String IDay2SDay(int selectedDay) {
	    	
	    	if(selectedDay == 1)
	    		return "Tuesday";
	    	else if(selectedDay == 2)
	    		return "Wednesday";
	    	else if(selectedDay == 3)
	    		return "Thursday";
	    	else if(selectedDay == 4)
	    	  		return "Friday";
	    	else if(selectedDay==5)
	    		return "Saturday";
	    	
	    	return "Monday";
	    }
	    
	    
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			mAdapter.notifyDataSetChanged();
			
			//update();
		}


		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			if(v==b)
			setContentView(R.layout.timetable_main);
			
		}
	    
	   
	    
	
}