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
import android.os.Bundle;
import android.widget.ProgressBar;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
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

public class CardsActivity extends Activity {

	private CardUI mCardView;
	private CardStack mStack;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);
		
		mStack = new CardStack();
		mStack.setTitle("Job Offerings");
		mCardView.addStack(mStack);
		
		// add AndroidViews Cards
				
		mStack.add(new MyImageCard("Android Developer", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
				addItem("Android Developer");
			}}));

		mStack.add(new MyImageCard("iOS Developer", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
				addItem("iOS Developer");
			}}));
		
		mStack.add(new MyImageCard("Backend Software Engineer IV", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
				addItem("Backend Software Engineer IV");
			}}));
		mStack.add(new MyImageCard("D&B Software", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
				addItem("D&B Software");
			}}));
		
		mStack.add(new MyImageCard("Mobile Architect", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
				addItem("Mobile Architect");
			}}));
		/*
		
		MyCard androidViewsCard = new MyCard("www.androidviews.net");

		androidViewsCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://www.androidviews.net/"));
				startActivity(intent);

			}
		});
		/*
		androidViewsCard.setOnLongClickListener(new OnLongClickListener() {    		
    		
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(v.getContext(), "This is a long click", Toast.LENGTH_SHORT).show();
				return true;
			}
		
		});
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.androidviews.net/"));

		mCardView.addCardToLastStack(androidViewsCard);
		*/
		/*
		CardStack stackPlay = new CardStack();
		stackPlay.setTitle("GOOGLE PLAY CARDS");
		mCardView.addStack(stackPlay);
*/
		// add one card, and then add another one to the last stack.
		/*
		mCardView.addCard(new MyCard("Google Play Cards"));
		mCardView.addCardToLastStack(new MyCard("By Androguide & GadgetCheck"));

		mCardView.addCardToLastStack(new MyPlayCard("Google Play",
				"This card mimics the new Google play cards look", "#33b6ea",
				"#33b6ea", true, false));

		mCardView
				.addCardToLastStack(new MyPlayCard(
						"Menu Overflow",
						"The PlayCards allow you to easily set a menu overflow on your card.\nYou can also declare the left stripe's color in a String, like \"#33B5E5\" for the holo blue color, same for the title color.",
						"#e00707", "#e00707", false, true));

		// add one card
		mCardView
				.addCard(new MyPlayCard(
						"Different Colors for Title & Stripe",
						"You can set any color for the title and any other color for the left stripe",
						"#f2a400", "#9d36d0", false, false));

		mCardView
				.addCardToLastStack(new MyPlayCard(
						"Set Clickable or Not",
						"You can easily implement an onClickListener on any card, but the last boolean parameter of the PlayCards allow you to toggle the clickable background.",
						"#4ac925", "#222222", true, true));
		*/
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
