package com.kii.world.cards;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.cardsui.objects.RecyclableCard;
import com.kii.world.R;

public class MyImageCard extends RecyclableCard {
	Context mContext;
	
	public MyImageCard(String title, int image, Context context){
		super(title, image);
		mContext = context;
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_picture;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(image);

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
		Toast.makeText(mContext, "doh", Toast.LENGTH_LONG).show();
		this.OnSwipeCard();
	}
	
	public void setOnNoClicked()
	{
		this.OnSwipeCard();
	}
}
