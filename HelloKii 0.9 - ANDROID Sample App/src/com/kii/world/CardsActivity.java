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

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
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
import com.kii.world.cards.MyImageCard;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class CardsActivity extends ParentActivity {

	private static final String LOGTAG = "CardsActivity";
	private int BROWSERNOTIFICATION = 400050;

	
	private CardUI mCardView;
	private CardStack mStack;
	
	private int[] companies = {
			R.drawable.company1,
			
			R.drawable.company3,
			R.drawable.company4,
			R.drawable.company5,
			R.drawable.company2,
			R.drawable.company6,
			R.drawable.company7,
			R.drawable.company8,
			
			R.drawable.company10,
			R.drawable.company9
	};
	
	private String[] companyNames = {
			"Context.io",
			
			"Evernote",
			"Gnip",
			"Kii",
			"Dun and Bradstreet",
			"MemSQL",
			"Microsoft",
			"Neo Technology",
			
			"Sendgrid",
			"Parse + Facebook",
	};
	
	private String[] companyJobTitle = {
			"Android Developer",
			"iOS Developer",
			
			"Backend Software Engineer IV",
			"Front End UX Engineer",
			"Mobile Architect",
			"Android/iOS Developer",
			"Windows Phone Dev",
			"Python Developer",
			"php, LAMP develoer",
			".net/C# developer"
	};
	
	private String[] companyInfo = {
			"Context.IO, a Return Path company, is leading a new wave of innovation on email. Conversations, collaboration and document exchange happens in email on a daily basis. We provide a unique email API that makes it easy for application developers to retrieve that information and leverage it in applications such as CRM, document management, collaboration, productivity tools and project management. With Context.IO, developers focus on what's unique to their business while we manage the technical details of integrating with arcane email server protocols.",
			"Evernote allows users to capture, organize, and find information across multiple platforms. Users can take notes, clip webpages, snap photos using their mobile phones, create to-dos, and record audio. All data is synchronized with the Evernote web service and made available to clients on Windows, Mac, Web, and mobile devices. Additionally, the Evernote web service performs image recognition on all incoming notes, making printed or handwritten text found within images searchable.",
			"Gnip is the world’s largest and most trusted provider of social data. Gnip’s customers deliver social media analytics to more than 95% of the Fortune 500. Gnip delivers more than 120 billion realtime social data activities each month, providing access to data from dozens of sources including Twitter, Tumblr, Foursquare, WordPress, Disqus and more.",
			"Kii provides end-to-end partnerships to mobile developers who want to maximize revenue, gain global distribution, and turn their apps into full-fledged businesses. It offers a unique combination of cloud backend, global distribution and monetization services.",
			"Dun and Bradstreet (NYSE:DNB) is the world’s leading source of commercial information and insight on businesses, enabling companies to Decide with Confidence for more than 172 years. Today, Dun and Bradstreet’s global commercial database contains more than 225 million business records. The database is enhanced by Dun and Bradstreet’s proprietary DUNSRight Quality Process, which provides our customers with quality business information. This quality information is the foundation of our global solutions that customers rely on to make critical business decisions.",
			"MemSQL is a next generation database that removes the most common bottleneck most applications hit today: disk. By offering a familiar relational interface to an in-memory data tier, MemSQL empowers developers with the technology web-scale companies use to cope with massive traffic and growth. MemSQL offers orders of magnitude improvements in write and read performance and greatly simplifies application development and maintenance.",
			"Microsoft Corporation is engaged in developing, licensing and supporting a range of software products and services. The Company operates in five segments: Windows & Windows Live Division (Windows Division), Server and Tools, Online Services Division (OSD), Microsoft Business Division (MBD), and Entertainment and Devices Division (EDD). The Company’s products include operating systems for personal computers (PCs), servers, phones, and other intelligent devices; server applications for distributed computing environments; productivity applications; business solution applications; desktop and server management tools; software development tools; video games, and online advertising.",
			"Graphs are everywhere. From websites adding social capabilities to Telco’s providing personalized customer services to innovative bioinformatics research, organizations are adopting graph databases as the best way to model and query connected data. Neo4j researchers have pioneered graph databases since 2000 and have been instrumental in bringing the power of the graph to numerous organizations worldwide, including 25 Global 2000 customers, such as Cisco, Accenture, Deutsche Telekom, and Telenor. Serving customers in production for over a decade, Neo4j is the world’s leading graph database with the largest ecosystem of partners and tens of thousands of successful deployments.",
			"SendGrid is a cloud-based email infrastructure and delivery service. We help companies communicate with their customers, through transactional and marketing email channels.   As an email infrastructure provider, we deliver value through increased email deliverability, low-cost and efficient scalability, business intelligence (advanced email analytics), and APIs for flexible and customizable implementation.",
			"Parse is the cloud app platform for Windows 8, Windows Phone 8, iOS, Android, JavaScript, and OS X. With Parse, you can add a scalable and powerful backend in minutes and launch a full-featured mobile or web app in record time without ever worrying about server management. Parse offers push notifications, social integration, data storage, and the ability to add rich custom logic to your app’s backend with Cloud Code. Build more with Parse.",
	};
	
	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	private MobileServiceTable<DevJobs> mToDoTable;
	

	  /**
	   * ************************************************************************
	   * You MUST change the following values to run this sample application.    *
	   * *************************************************************************
	   */

	/**
	 * Progress spinner to use for table operations
	 */
	private ProgressBar mProgressBar;
	
	private static final String OBJECT_KEY = "jobString";
	private static final String BUCKET_NAME = "myBucket";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);
		
		mStack = new CardStack();
		mStack.setTitle("Job Offerings");
		mCardView.addStack(mStack);
		
		
		// add AndroidViews Cards
		mStack.add(new MyImageCard(companyJobTitle[0], companyNames[0], companyInfo[0], companies[0], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(0);
				mCardView.refresh();
				addItem(companyJobTitle[0]);
				
				//put the note evernote
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(0);
					mCardView.refresh();
				}}));

		mStack.add(new MyImageCard(companyJobTitle[1], companyNames[1], companyInfo[1], companies[1], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(1);
				mCardView.refresh();
				addItem(companyJobTitle[1]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(1);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[2], companyNames[2], companyInfo[2], companies[2], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(2);
				mCardView.refresh();
				addItem(companyJobTitle[2]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(2);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[3], companyNames[3], companyInfo[3], companies[3], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(3);
				mCardView.refresh();
				addItem(companyJobTitle[3]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(3);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[4],companyNames[4], companyInfo[4], companies[4], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(4);
				mCardView.refresh();
				addItem(companyJobTitle[4]);
				simPushNotification(companyNames[4], companyJobTitle[4] + "\n" + companyInfo[4] + "\n" + "Dun and Bradstreet: Metrics: FY2013 (Dec.) revenues of $1.7bn and Net Income of $259,000,000 market cap.: $3.7bn.");

			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(4);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[5],companyNames[5], companyInfo[5],  companies[5], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(5);
				mCardView.refresh();
				addItem(companyJobTitle[5]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(5);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[6], companyNames[6], companyInfo[6], companies[6], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(6);
				mCardView.refresh();
				addItem(companyJobTitle[6]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(6);
					mCardView.refresh();
				}}
			));
		
		mStack.add(new MyImageCard(companyJobTitle[7], companyNames[7], companyInfo[7], companies[7], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(7);
				mCardView.refresh();
				addItem(companyJobTitle[7]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(7);
					mCardView.refresh();
				}}
				));
		
		mStack.add(new MyImageCard(companyJobTitle[8], companyNames[8], companyInfo[8],  companies[8], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(8);
				mCardView.refresh();
				addItem(companyJobTitle[8]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(8);
					mCardView.refresh();
				}}));
		
		mStack.add(new MyImageCard(companyJobTitle[9], companyNames[9], companyInfo[9], companies[9], this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(9);
				mCardView.refresh();
				addItem(companyJobTitle[9]);
			}},
			new onDismissListener(){

				@Override
				public void onCardDismiss() {
					mStack.remove(9);
					mCardView.refresh();
				}}));
		
		
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
	
	public void simPushNotification(final String title, final String desc)
	{
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		        Notification noti = new NotificationCompat.Builder(CardsActivity.this)
		         .setContentTitle(title)
		         .setContentText(desc)
		         .setOngoing(false)
		         .setSmallIcon(R.drawable.dev_icon_small)
		         .setLargeIcon(BitmapFactory.decodeResource(CardsActivity.this.getResources(),
		        		 R.drawable.dev_icon_big))
		         .build();
		        nm.notify(BROWSERNOTIFICATION, noti);
		        
				saveNote(title, desc);	
				
				CardsActivity.this.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						
						Hashtable<String,String> params = new Hashtable<String,String>();
						String result = null;
						
						// Get the values from the form
						String to = "Nyceane@gmail.com";
						params.put("to", to);	
						
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

				
			}}, 3000);
		


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
				sendgrid.setFrom(h.get("from"));
				sendgrid.setSubject(h.get("subject"));
				sendgrid.setText(h.get("text"));
				String response = sendgrid.send();
				return response;
			}
		}
}
