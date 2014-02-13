package com.mobigeni.cars.motors.parts.search;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Share {

	public static void share(String wish, Context context)
	{
		 Intent sendIntent = new Intent(Intent.ACTION_SEND);
		 sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, wish);
			sendIntent.setType("text/plain");
         context.startActivity(Intent.createChooser(sendIntent, "Share image using"));
	}
}
