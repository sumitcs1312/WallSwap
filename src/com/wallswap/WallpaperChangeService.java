package com.wallswap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;

import com.google.android.gms.internal.bi;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

public class WallpaperChangeService extends Service implements Runnable {

	/* Declare a variable to store the time selected by user */
	private int time;
	private String imagespath;
	private Thread t;

	/* Declare two bitmap objects to store the wallpaper images */
	private Bitmap bitmap;
	ArrayList<String> imagePath = new ArrayList<String>();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flag, int startId) {
		super.onStartCommand(intent, flag, startId);
		/* Obtain the bundle sent along with the intent sent from the activity */
		Bundle bundle = intent.getExtras();
		/* Obtain the value of the time key */
		time = bundle.getInt("time");
		imagespath = bundle.getString("imagepath");
		String[] images = imagespath.split(",");
		for (int i=0;i<images.length-1;i++) {
			imagePath.add(images[i]);
			Log.i("WallpaperChangeService", "image path is "+images[i]);

		}
		
		//imagePath = getFileList(Environment.getExternalStorageDirectory()+File.separator+"WallSwap");

		/* Start a new thread to keep the service running in the background. */
		t = new Thread(WallpaperChangeService.this);
		t.start();
		return 0;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {		
			Log.i("WallpaperChangeService", time+"No of files "+imagePath.size());
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			String ct = dateFormat.format(date);
			Log.i("WallpaperChangeService", ct);
			while (true) {				
				for (int i = 0; i < imagePath.size(); i++) {
					BitmapFactory.Options opts=new BitmapFactory.Options();
			        opts.inDither=false;                     //Disable Dithering mode
			        opts.inSampleSize = 2;                    // scale image upto 1/8 of the original image.
			        opts.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
			        opts.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
			        opts.inTempStorage=new byte[16 * 1024];
			        
					if(bitmap!=null){
					bitmap.recycle();
					
					Log.i("WallpaperChangeService", imagePath.get(i));
					bitmap = BitmapFactory.decodeFile(imagePath.get(i));
					setWallpaper(bitmap);
					}else{
						File file = new File(imagePath.get(i));
						Log.i("WallpaperChangeServiceFile path", imagePath.get(i)+" exists "+file.exists());	

						bitmap = BitmapFactory.decodeFile(imagePath.get(i),opts);
						Log.i("WallpaperChangeService", "Is bitmap null "+bitmap.equals(null));
						if(!bitmap.equals(null)){
						setWallpaper(bitmap);
						bitmap.recycle();
						bitmap=null;
						}
					}
					
					Thread.sleep(1000 * time);
				}
			}
		} catch (Exception e) {
			Log.d("wallswap", "printing exception1");

			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getFileList(String srcPath) {
		// TODO Auto-generated method stub
		ArrayList<String> fileName = new ArrayList<String>();
		File folder = new File(srcPath);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles != null) {
			if (listOfFiles.length != 0) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						fileName.add(srcPath+File.separator+listOfFiles[i].getName());
					}
				}
				return fileName;
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

}
