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


import android.app.Activity;
import android.os.Bundle;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.kii.world.cards.MyImageCard;

public class CardsActivity extends Activity {

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
		mStack.setTitle("Job Offerings");
		mCardView.addStack(mStack);
		
		// add AndroidViews Cards
				
		mStack.add(new MyImageCard("Android Developer", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
			}}));

		mStack.add(new MyImageCard("iOS Developer", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
			}}));
		
		mStack.add(new MyImageCard("Backend Software Engineer IV", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
			}}));
		mStack.add(new MyImageCard("D&B Software", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
			}}));
		
		mStack.add(new MyImageCard("Mobile Architect", R.drawable.img1, this, new onDismissListener(){

			@Override
			public void onCardDismiss() {
				mStack.remove(mStack.getPosition());
				mCardView.refresh();
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
	}
	
	public interface onDismissListener
	{
        public void onCardDismiss();
	}
}
