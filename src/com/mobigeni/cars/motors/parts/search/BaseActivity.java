package com.mobigeni.cars.motors.parts.search;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BaseActivity extends Activity 
{
	protected void ShowDialog(Context context,String title,String[] items)
	{	
		final Dialog dialog;
		dialog = new Dialog(context);
		ListView modeList = new ListView(this);
		String[] stringArray = items;
		final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
		modeList.setAdapter(modeAdapter);
		dialog.setContentView(modeList);
		dialog.setTitle(title);
		dialog.setCancelable(true);
		modeList.setOnItemClickListener(new OnItemClickListener() {

			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,long id) 
			{
				// TODO Auto-generated method stub
			//	Globals.Device.SELECTED_DEVICE = modeAdapter.getItem(position);
			//	Log.d("Selected Device", Globals.Device.SELECTED_DEVICE);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	
	
	protected void ShowMessage(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
}
