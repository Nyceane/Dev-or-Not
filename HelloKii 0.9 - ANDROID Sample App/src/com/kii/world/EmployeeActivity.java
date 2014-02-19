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


import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.github.sendgrid.SendGrid;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.KiiUser;
import com.kii.world.cards.EmployeeCard;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class EmployeeActivity extends ParentActivity {
	private int BROWSERNOTIFICATION = 400050;
	private static final String LOGTAG = "CardsActivity";

		/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private MobileServiceTable<DevJobs> mToDoTable;
	

	/**
	 * Progress spinner to use for table operations
	 */
	private ProgressBar mProgressBar;
	
	private static final String OBJECT_KEY = "jobString";
	private static final String BUCKET_NAME = "myBucket";
	
	private CardUI mCardView;
	private CardStack mStack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		
		mStack = new CardStack();
		mStack.setTitle("Applicants");
		mCardView.addStack(mStack);
		
		//---------------------------------------
		
		// add AndroidViews Cards
		
		mStack.add(new EmployeeCard(
				"Developer", 
				"John Woo", 
				"developer with 5 year experience",
				"www.github.com/markcells",
				"@iconYooo",
				"www.linkedin.com/in/JohnWoo",
				"415-611-6222", 
				"John_woo@gmail.com", 
				R.drawable.employee_tom, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(0);
						mCardView.refresh();
						addItem("John Woo");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(0);
							mCardView.refresh();
					}}
				));

		mStack.add(new EmployeeCard(
				"Designer", 
				"Steve Job", 
				"Designer with 15 years in Mobile",
				"www.github.com/iconcells",
				"@steve Job", 
				"www.linkedin.com/in/SteveJob",
				"415-622-6023", 
				"Steve_Job@gmail.com", 
				R.drawable.employee_steve, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(1);
						mCardView.refresh();
						addItem("Steve Job");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(1);
							mCardView.refresh();
					}}
				));
		
		mStack.add(new EmployeeCard(
				"Hacker", 
				"Peter Ma", 
				"Developer with 10 years in Social Network",
				"www.github.com/MarkSucks",
				"@MarkS", 
				"www.linkedin.com/in/MarkS",
				"415-622-4122", 
				"MarkS@gmail.com", 
				R.drawable.empolyee_peter, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(2);
						mCardView.refresh();
						addItem("Peter Ma");
						simPushNotification("Hacker, Peter Ma", "Developer with 10 years in Social Network\nGithub: www.github.com/MarkSucks\nLinkedIN: www.linkedin.com/in/MarkS\nTwitter:@MarkS\nphone:415-622-4122\nemail:MarkS@gmail.com");
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(2);
							mCardView.refresh();
					}}
				));

		mStack.add(new EmployeeCard(
				"Heathcare.gov", 
				"Michelle Snyder", 
				"Director with 30 years in Health",
				"www.github.com/MarkSucks",
				"@MarkS", 
				"www.linkedin.com/in/MarkS",
				"415-622-4122", 
				"MarkS@gmail.com", 
				R.drawable.empolyee_mich, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(3);
						mCardView.refresh();
						addItem("Michelle Snyder");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(3);
							mCardView.refresh();
					}}
				));
	
		mStack.add(new EmployeeCard(
				"Hacker", 
				"Kenneth Ng", 
				"Developer with 10 years in Mobile and Health App",
				"www.github.com/MarkSucks",
				"@MarkS", 
				"www.linkedin.com/in/MarkS",
				"415-622-4122", 
				"MarkS@gmail.com", 
				R.drawable.employee_kng, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(4);
						mCardView.refresh();
						addItem("Kenneth Ng");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(4);
							mCardView.refresh();
					}}
				));	
		
		mStack.add(new EmployeeCard(
				"Hacker", 
				"Mark Sucks", 
				"Developer with 10 years in Social Network",
				"www.github.com/MarkSucks",
				"@MarkS", 
				"www.linkedin.com/in/MarkS",
				"415-622-4122", 
				"MarkS@gmail.com", 
				R.drawable.employee_mark, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(5);
						mCardView.refresh();
						addItem("Mark Sucks");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(5);
							mCardView.refresh();
					}}
				));
		

		// draw cards
		mCardView.refresh();
		
		try {
		mClient = new MobileServiceClient(
				"https://devhack2014.azure-mobile.net/",
				"iAAjAYnuCELJXSputqzBZsFuignUXR83",
				this).withFilter(new ProgressFilter());

		// Get the Mobile Service Table instance to use
		mToDoTable = mClient.getTable(DevJobs.class);
		} catch (MalformedURLException e) {
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	}
	

	public void addItem(String jobString)
	{
		KiiBucket bucket = KiiUser.getCurrentUser().bucket(BUCKET_NAME);
		KiiObject obj = bucket.object();
		obj.set(OBJECT_KEY, jobString);
		
		if (mClient == null) {
			return;
		}

		// Create a new item
		DevJobs item = new DevJobs();

		item.setText("Peter Ma");
		item.setContact("test@test.com");
		item.setComplete(false);
		item.setJobTitle(jobString);
		// Insert the new item
		mToDoTable.insert(item, new TableOperationCallback<DevJobs>() {

			public void onCompleted(DevJobs entity, Exception exception, ServiceFilterResponse response) {
				
				if (exception == null) {
					if (!entity.isComplete()) {
						//mAdapter.add(entity);
					}
				} else {
					createAndShowDialog(exception, "Error");
				}

			}
		});
	}
	public interface onDismissListener
	{
        public void onCardDismiss();
	}
	
	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
	private class ProgressFilter implements ServiceFilter {
		
		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});
			
			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
				
				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});
					
					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}
	

	public void simPushNotification(final String title, final String desc)
	{

				PendingIntent pendingIntent;
		        Intent intent = new Intent();
		        intent.setClass(EmployeeActivity.this, ContextActivity.class);
		        pendingIntent =  PendingIntent.getActivity(EmployeeActivity.this, 0, intent, 0);
				
				NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		        Notification noti = new NotificationCompat.Builder(EmployeeActivity.this)
		         .setContentTitle(title)
		         .setContentText(desc)
		         .setOngoing(false)
		         .setSmallIcon(R.drawable.dev_icon_small)
		         .setContentIntent(pendingIntent)
		         .setLargeIcon(BitmapFactory.decodeResource(EmployeeActivity.this.getResources(),
		        		 R.drawable.dev_icon_big))
		         .build();
		        nm.notify(BROWSERNOTIFICATION, noti);
		        
				//saveNote(title, desc);	
				
				EmployeeActivity.this.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						
						Hashtable<String,String> params = new Hashtable<String,String>();
						String result = null;
						
						// Get the values from the form
						String to = "Nyceane@gmail.com";
						params.put("to", to);
						
						String to2 = "ngkenneth@gmail.com";
						params.put("to2", to2);
						
						String from = "peter@spotvite.com";
						params.put("from", from);
						
						String subject = "Dinder Match: " + title;
						params.put("subject", subject);
						
						String text = desc;
						params.put("text", text);
						
						// Send the Email
						SendEmailWithSendGrid email = new SendEmailWithSendGrid();
						try {
							result = email.execute(params).get();
							Log.e("result", result);
						} catch (InterruptedException e) {
							// TODO Implement exception handling
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Implement exception handling
							e.printStackTrace();
						}						
					}});

	}
	
	
	  /**
	   * Saves text field content as note to selected notebook, or default notebook if no notebook select
	   */
	  public void saveNote(String title, String content) {
	    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
	      Toast.makeText(getApplicationContext(), R.string.empty_content_error, Toast.LENGTH_LONG).show();
	    }
	    Log.e("title", title);
	    Log.e("content", content);
	    Note note = new Note();
	    note.setTitle(title);

	    //TODO: line breaks need to be converted to render in ENML
	    note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);

	    try {
	      mEvernoteSession.getClientFactory().createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {
	        @Override
	        public void onSuccess(Note data) {
	          Toast.makeText(getApplicationContext(), R.string.note_saved, Toast.LENGTH_LONG).show();
	        }

	        @Override
	        public void onException(Exception exception) {
	          Log.e(LOGTAG, "Error saving note", exception);
	          Toast.makeText(getApplicationContext(), R.string.error_saving_note, Toast.LENGTH_LONG).show();
	        }
	      });
	    } catch (TTransportException exception) {
	      Log.e(LOGTAG, "Error creating notestore", exception);
	      Toast.makeText(getApplicationContext(), R.string.error_creating_notestore, Toast.LENGTH_LONG).show();
	    }

	  }
		// Send an email with SendGrid's Web API, using our SendGrid Java Library
		// https://github.com/sendgrid/sendgrid-java
		private class SendEmailWithSendGrid extends AsyncTask<Hashtable<String,String>, Void, String> {

			@Override
			protected String doInBackground(Hashtable<String,String>... params) {
				Hashtable<String,String> h = params[0];
				Utils creds = new Utils();
				SendGrid sendgrid = new SendGrid(creds.getUsername(),creds.getPassword());
				sendgrid.addTo(h.get("to"));
				sendgrid.addTo(h.get("to2"));
				sendgrid.setFrom(h.get("from"));
				sendgrid.setSubject(h.get("subject"));
				sendgrid.setText(h.get("text"));
				String response = sendgrid.send();
				return response;
			}
		}
}
