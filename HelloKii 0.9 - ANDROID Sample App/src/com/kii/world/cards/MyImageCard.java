package com.kii.world.cards;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.cardsui.objects.RecyclableCard;
import com.kii.world.CardsActivity.onDismissListener;
import com.kii.world.R;

public class MyImageCard extends RecyclableCard {
	Context mContext;
	onDismissListener mYesListener;
	onDismissListener mNoListener;
	String mName;
	
	public MyImageCard(String title, String name, String desc, int image, Context context, onDismissListener yeslistener, onDismissListener nolistener){
		super(title, desc, image);
		mContext = context;
		mYesListener = yeslistener;
		mNoListener = nolistener;
		mName = name;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_picture;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(image);

		((TextView) convertView.findViewById(R.id.company)).setText(mName);
		((TextView) convertView.findViewById(R.id.description)).setText(desc);
		
		
		((ImageView) convertView.findViewById(R.id.btnYes)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setOnYesClicked();
			}
		});
		
		((ImageView) convertView.findViewById(R.id.btnNo)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setOnNoClicked();
			}
		});
	}

	public void setOnYesClicked()
	{
		this.OnSwipeCard();
		mYesListener.onCardDismiss();
	}
	
	public void setOnNoClicked()
	{
		this.OnSwipeCard();
		mNoListener.onCardDismiss();
	}
}
