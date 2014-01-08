package com.example.myfirstapp;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class CircleActivity extends Activity {

	Button circle;
	
	Canvas canvas;
	Paint paint;
	Bitmap bg;
	int width;
	int height;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle);
		
		circle = (Button) findViewById(R.id.Button_Circle);
		
		//get window width and height for the device
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;
		height = metrics.heightPixels;
		
		//set up the canvas and paint it white before drawing on it
		paint = new Paint();
        paint.setColor(Color.WHITE);
        bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg); 
        canvas.drawPaint(paint);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.Layout);
        rl.setBackgroundDrawable(new BitmapDrawable(bg));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.circle, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void onClickCircle(View v) {
		//draw a circle on the canvas at a random position and with a random size
		selectColor();
		canvas.drawCircle((float)(Math.random()*width), (float)(Math.random()*height), (float)(Math.random()*150+10), paint); 
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.Layout);
        rl.setBackgroundDrawable(new BitmapDrawable(bg));
	}
	
	public void onClickSave(View v) {
		if (saveToExternalStorage(bg)) {
			
		}
		else if (saveToInternalStorage(bg)) {
			//method not implemented!
		}
		else {
			
		}
	}
	
	private void selectColor() {
		int color = (int) (Math.random()*6+1);
		switch(color) {
		case 1:
			paint.setColor(Color.RED);
			break;
		case 2:
			paint.setColor(Color.CYAN);
			break;
		case 3:
			paint.setColor(Color.GREEN);
			break;
		case 4:
			paint.setColor(Color.MAGENTA);
			break;
		case 5:
			paint.setColor(Color.BLUE);
			break;
		}
	}
	
	private boolean saveToExternalStorage(Bitmap img) {
		String APP_PATH_SD_CARD = "/CircleApp/";
		String APP_THUMBNAIL_PATH_SD_CARD = "SavedImages";
		String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD +
				APP_THUMBNAIL_PATH_SD_CARD;
		try {
			File dir = new File(fullPath);
			if (!dir.exists())
				dir.mkdirs();
			
			int numberOfImages = dir.listFiles().length;
			
			final File filename = new File(dir, "image" + Integer.toString(numberOfImages+1) + ".png");
			
			FileOutputStream out = new FileOutputStream(filename);
			img.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			
			new AlertDialog.Builder(this)
				.setTitle("Saved")
				.setMessage("Successfully saved to internal storage!")
				.setNeutralButton("Close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// clicked!
					}
				})
				.setPositiveButton("Open", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						File file = new File(filename.getAbsolutePath());
						Uri path = Uri.fromFile(file);
						Intent pngOpenIntent = new Intent(Intent.ACTION_VIEW);
						pngOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pngOpenIntent.setDataAndType(path, "image/png");
						try {
				            startActivity(pngOpenIntent);
				        } catch (ActivityNotFoundException e) {

				        }
					}
				})
				.setIcon(R.drawable.ic_launcher)
				.show();
				
			return true;
			
		} catch (Exception e) {
			//handle exceptions
			alert("Error", "Unable to save to internal storage.");
			return false;
		}			
	}
	
	private boolean saveToInternalStorage(Bitmap img) {
		return true;
	}
	
	private void alert(String title, String str) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(str)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// clicked!
				}
			})
			.setIcon(R.drawable.ic_launcher)
			.show();
	}
	

}
