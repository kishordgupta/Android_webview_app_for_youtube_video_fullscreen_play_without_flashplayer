package com.mobigeni.cars.motors.parts.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class StackParser extends Activity {
    /** Called when the activity is first created. */
	public static String url="";
	public Context context=null;
	public static String datas="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     context=this;
       
				 if(GetNetworkStatus.isNetworkAvailable(context))
			       	{
					 
			           pd = ProgressDialog.show(StackParser.this,"wait ","wait please", true, false);
			           new ParseSite().execute(urlmaker(datas));
			       	}
				 else
				 {finish();
					 Toast.makeText(context, "Need a working Internet Connection", Toast.LENGTH_LONG).show();
				 }
		
    }

    public String urlmaker(String data)
    {
    	String Data = data.replace("  "," ");
        Data = data.replace(" ","+");
    	Data = url+Data;
    	return Data;
    	
    }
    private ProgressDialog pd;

   
  
    private class ParseSite extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... arg) {
          String output = "";
            try 
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(arg[0]);
                ResponseHandler<String> resHandler = new BasicResponseHandler();
                String page = httpClient.execute(httpGet, resHandler);
                String[] a = null;
                if(page.contains("youtube.com/")){
              a = page.split("youtube.com/");}
                   String[] b =a[1].split("\"") ;
                output = "http://m.youtube.com/"+b[0];
          
            }
            catch (ClientProtocolException e)
            {
                e.printStackTrace();
             
            }
            catch (IOException e)
            {
                e.printStackTrace();
               
            }


            return output;
        }

        protected void onPostExecute(String output) {

        	
            pd.dismiss();
            String id="";
            //Imageurl.newyearsvalues=(ArrayList<String>) output;
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(output);

            if(matcher.find()){
            	id= matcher.group();
            }
            else
            {
            	
            }
           Toast.makeText(context,id, Toast.LENGTH_LONG).show();
           try{
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
               startActivity(intent);                 
               }catch (ActivityNotFoundException ex){
                   Intent intent=new Intent(Intent.ACTION_VIEW, 
                   Uri.parse("http://www.youtube.com/watch?v="+id));
                   startActivity(intent);
               }
          /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(output));
           
           // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + output));
            startActivity(intent);*/
        finish();    
        }
        
        
    }
}