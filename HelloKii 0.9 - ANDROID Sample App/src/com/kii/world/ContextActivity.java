//
//
// Copyright 2012 Kii Corporation
// http://kii.com
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
//

package com.kii.world;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.sendgrid.SendGrid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import at.tomtasche.contextio.ContextIO;

public class ContextActivity extends Activity {
	TextView txtStuff;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.context);

		txtStuff = (TextView)findViewById(R.id.txtStuff);
		new stuffTask().execute();
	}
	
	  
		// Send an email with SendGrid's Web API, using our SendGrid Java Library
		// https://github.com/sendgrid/sendgrid-java
		private class stuffTask extends AsyncTask<Void, Void, String> {

			@Override
			protected String doInBackground(Void... params) {
				// set our view to the xml in res/layout/main.xml
				ContextIO dokdok = new ContextIO("w46q8fv4", "gaEcCXx1cvjmuvSi");
				
				Map<String, String> params1 = new HashMap<String, String>();
				params1.put("since", "0");
				
				Log.e("stuff", dokdok.allMessages("mitopma@gmail.com", params1).rawResponse.getBody());
				return dokdok.allMessages("mitopma@gmail.com", params1).rawResponse.getBody();
			}
			
		     protected void onPostExecute(String result) {
		    	 try {
		    		 String stuff = "";
					JSONObject json = new JSONObject(result);
					if(json.has("data"))
					{
						JSONArray array = json.getJSONArray("data");
						for(int i = 0; i < array.length(); i++)
						{
							if(array.getJSONObject(i).getString("subject").contains("Dinder"))
							{
								stuff += (array.getJSONObject(i).getString("subject") + "\n");
							}
						}
					}
					
			    	 txtStuff.setText(stuff);
			    	 
			    	 if(stuff.isEmpty()){
			    		 txtStuff.setText("No Job Offering");
			    	 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	 
		     }
			
		}
	
}
