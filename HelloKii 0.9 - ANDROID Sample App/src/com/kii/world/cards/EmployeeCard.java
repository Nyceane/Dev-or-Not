package com.kii.world.cards;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.cardsui.objects.RecyclableCard;
import com.kii.world.EmployeeActivity.onDismissListener;
import com.kii.world.R;

public class EmployeeCard extends RecyclableCard {
	Context mContext;
	String name;
	String _github, _twitter, _phone, _email;
	onDismissListener _yesDismisser, _noDismisser;
	
	public EmployeeCard(
			String title, 
			String name, 
			String description, 
			String github, 
			String twitter, 
			String phone, 
			String email, 
			int image, 
			Context context, 
			onDismissListener yesDismisser, 
			onDismissListener noDismisser){
		super(title, description, image);
		mContext = context;
		_github = github;
		_twitter = twitter;
		_phone = phone;
		_email = email;
		_yesDismisser = yesDismisser;
		_noDismisser = noDismisser;
	}
	

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_employee;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((ImageView) convertView.findViewById(R.id.imageView1)).setImageResource(image);
		((TextView) convertView.findViewById(R.id.github)).setText(_github);
		((TextView) convertView.findViewById(R.id.twitter)).setText(_twitter);
		((TextView) convertView.findViewById(R.id.phone)).setText(_phone);
		((TextView) convertView.findViewById(R.id.email)).setText(_email);
		
		
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
		_yesDismisser.onCardDismiss();
	}
	
	public void setOnNoClicked()
	{
		this.OnSwipeCard();
		_noDismisser.onCardDismiss();
	}
}
