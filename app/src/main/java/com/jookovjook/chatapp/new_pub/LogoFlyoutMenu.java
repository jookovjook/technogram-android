package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

import com.jookovjook.chatapp.R;

import org.zakariya.flyoutmenu.FlyoutMenuView;

public class LogoFlyoutMenu {

	private static Drawable getDrawableByURL(Logo logo, Context context){
		switch (logo){
			case youtube: return context.getResources().getDrawable(R.drawable.logo_youtube);
			case facebook: return context.getResources().getDrawable(R.drawable.logo_fb);
			case github: return context.getResources().getDrawable(R.drawable.logo_github);
			case bitbucket: return context.getResources().getDrawable(R.drawable.logo_bb);
			default: return context.getResources().getDrawable(R.drawable.logo_web);
		}

	}

	private final static int half_size = 16;

	private  static int dpToPx(int dp)
	{
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	private static int pxToDp(int px)
	{
		return (int) (px / Resources.getSystem().getDisplayMetrics().density);
	}

	public static class MenuItem extends FlyoutMenuView.MenuItem {

		Logo logo;
		Context context;

		public MenuItem(int id, Logo logo, Context context) {
			super(id);
			this.logo = logo;
			this.context = context;
		}

		public Logo getLogo(){
			return this.logo;
		}

		@Override
		public void onDraw(Canvas canvas, RectF bounds, float degreeSelected) {
			Drawable d = getDrawableByURL(logo, context);
			d.setBounds((int) bounds.centerX() - dpToPx(half_size), (int) bounds.centerY() - dpToPx(half_size),
					(int) bounds.centerX() + dpToPx(half_size), (int) bounds.centerY() + dpToPx(half_size));
			d.draw(canvas);
		}
	}

	public static class ButtonRenderer extends FlyoutMenuView.ButtonRenderer {

		Context context;
		Logo logo;

		public ButtonRenderer(Logo logo, Context context) {
			super();
			this.logo = logo;
			this.context = context;
		}

		public void setLogo(Logo logo) {
			this.logo = logo;
		}

		@Override
		public void onDrawButtonContent(Canvas canvas, RectF buttonBounds, @ColorInt int buttonColor, float alpha) {
			Drawable d = getDrawableByURL(this.logo, context);
			d.setBounds((int) buttonBounds.centerX() - dpToPx(half_size), (int) buttonBounds.centerY() - dpToPx(half_size),
					(int) buttonBounds.centerX() + dpToPx(half_size), (int) buttonBounds.centerY() + dpToPx(half_size));
			d.draw(canvas);
		}
	}
}