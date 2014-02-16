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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
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

public class EmployeeActivity extends Activity {

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
				"www.github.com/iconcells",
				"@iconcells", 
				"415-633-6023", 
				"John_woo@gmail.com", 
				R.drawable.employee_tom, 
				this.getApplicationContext(), 
				new onDismissListener(){
					
					@Override
					public void onCardDismiss() {
						mStack.remove(mStack.getPosition());
						mCardView.refresh();
						addItem("John Woo");					
					}},
					
					new onDismissListener(){
						
						@Override
						public void onCardDismiss() {
							mStack.remove(mStack.getPosition());
							mCardView.refresh();
					}}
				));

//		mStack.add(new EmployeeCard("Peter Ma", R.drawable.employee_mark, this, new onDismissListener(){
//
//			@Override
//			public void onCardDismiss() {
//				mStack.remove(mStack.getPosition());
//				mCardView.refresh();
//				addItem("Peter Ma");
//			}}));

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
}
