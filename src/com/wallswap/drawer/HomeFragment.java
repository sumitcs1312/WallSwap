package com.wallswap.drawer;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wallsawp.data.TimeUtils;
import com.wallswap.Callbacks;
import com.wallswap.ProcessImages;
import com.wallswap.QustomDialogBuilder;
import com.wallswap.WallpaperChangeService;
import com.wallswap.pick.CustomGallery;
import com.wallswap.pick.GalleryAdapter;
import com.wallswap.pick.R;


public class HomeFragment extends Fragment  {

	private GridView gridGallery;
	private Handler handler;
	private GalleryAdapter adapter;

	private ImageView imgNoMedia;
	//private Button btnGalleryOk;

	//	private Picasso imageLoader;
	private ProcessImages mProcessImages;
	private String[] allPath;
	private int screenWidth;
	private InterstitialAd interstitial;
	private Callbacks mCallbacks = sDummyCallbacks;
	protected boolean mActionModeIsActive;

	public HomeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.gallery, container, false);
		screenWidth = determineScreenSize();
		mProcessImages = new ProcessImages();
		//		initImageLoader();
		init(rootView);

		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(getActivity());
		// Insert the Ad Unit ID
		interstitial.setAdUnitId("ca-app-pub-123456789/123456789");

		//Locate the Banner Ad in activity_main.xml
		final AdView adView = (AdView)rootView.findViewById(R.id.adView);

		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()

		// Add a test device to show Test Ads
		//         .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		//         .addTestDevice("CC5F2C72DF2B356BBF0DA198")
		.build();

		// Load ads into Banner Ads
		adView.loadAd(adRequest);

		//   adView.setVisibility(View.GONE);
		// Load ads into Interstitial Ads
		interstitial.loadAd(adRequest);
		// Prepare an Interstitial Ad Listener
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				adView.setVisibility(View.VISIBLE);
				adView.setEnabled(true);
				displayInterstitial();
			}
		});

		mCallbacks=(Callbacks)getActivity();
//		ActionMode mMode = MyActivityClass.this.startActionMode(some implementation);
		
//		doneButton.setOnClickListener(new View.OnClickListener() {
////
//		    @Override
//		    public void onClick(View v) {
//
//				mActionModeIsActive=false;
//				ArrayList<CustomGallery> selected = adapter.getSelected();
//				allPath = new String[selected.size()];
//				for (int i = 0; i < allPath.length; i++) {
//					allPath[i] = selected.get(i).sdcardPath;
//					Log.d("sdcard paths", allPath[i]);
//				}
//				if(allPath.length>0){
//					LongOperation mLongOperation = new LongOperation(getActivity());
//					mLongOperation.execute("");
//					mCallbacks.openDrawer(true);
//				}
////				else{
////					Toast.makeText(getActivity(), "No image selected.",
////							Toast.LENGTH_SHORT).show();
////				}
//			//	nr=0;
//				adapter.selectAll(false);
//				// setResult(RESULT_OK, data);
//
//		    }
//		});
		return rootView;
	}
	
	
	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	public int determineScreenSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
		.getMetrics(metrics);
		return metrics.widthPixels;
	}

	//	private void initImageLoader() {
	//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	//				.cacheOnDisc().imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	//				.bitmapConfig(Bitmap.Config.RGB_565).build();
	//		Picasso.Builder builder = new Picasso.Builder(
	//				getActivity()).defaultDisplayImageOptions(defaultOptions);
	//				
	//
	//		Picasso config = builder.build();
	//		imageLoader = Picasso.singleton;
	//		imageLoader.init(config);
	//	}

	private void init(View rootView) {

		handler = new Handler();
		gridGallery = (GridView) rootView.findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getActivity().getApplicationContext());
		rootView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) { 
				if(mActionModeIsActive) {
		        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
		            // handle your back button code here
		            return true; // consumes the back key event - ActionMode is not finished
		         }
		     }
		     return getActivity().dispatchKeyEvent(event);}
		});

		//		rootView.findViewById(R.id.llBottomContainer).setVisibility(
		//				View.VISIBLE);
		//		gridGallery.setOnItemClickListener(mItemMulClickListener);
		//		gridGallery.setMultiChoiceModeListener(listener)

		//CAB 
		gridGallery.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//		@Override
//		public boolean dispatchKeyEvent(KeyEvent event) {
//		    if(mActionModeIsActive) {
//		        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//		           // handle your back button code here
//		           return true; // consumes the back key event - ActionMode is not finished
//		        }
//		    }
//		    return super.dispatchKeyEvent(event);
//		}
		

		gridGallery.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			private int nr = 0;

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				mActionModeIsActive=true;
				return false;
			}

			

			@Override
			public void onDestroyActionMode(ActionMode mode) {
//				mActionModeIsActive=false;
//				ArrayList<CustomGallery> selected = adapter.getSelected();
//				allPath = new String[selected.size()];
//				for (int i = 0; i < allPath.length; i++) {
//					allPath[i] = selected.get(i).sdcardPath;
//					Log.d("sdcard paths", allPath[i]);
//				}
//				if(allPath.length>0){
//					LongOperation mLongOperation = new LongOperation(getActivity());
//					mLongOperation.execute("");
//					mCallbacks.openDrawer(true);
//				}
////				else{
////					Toast.makeText(getActivity(), "No image selected.",
////							Toast.LENGTH_SHORT).show();
////				}
//				nr=0;
//				adapter.selectAll(false);
//				// setResult(RESULT_OK, data);

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				nr = 0;
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.contextual_menu, menu);
				//   mode.setCustomView(view)
				return true;
			}

			@Override
			public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
				Log.d("Test","test");
				// TODO Auto-generated method stub
				//                switch (item.getItemId()) {
				//                 
				////                    case R.id.item_delete:
				//                        adapter.clearSelection();
				//                        mode.finish();
				//                }

				switch (item.getItemId()) {

				case R.id.select_all:
					adapter.selectAll(true);
					nr=adapter.getCount();
					mode.setTitle(nr + " selected");
					return false;
				case R.id.action_deselect_all:
					adapter.selectAll(false);
					mode.setTitle(0 + " selected");
					nr=0;
					onDestroyActionMode(mode);
					return false;
					
				case R.id.action_setWallPaper:
					mActionModeIsActive=false;
					final QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(getActivity()).
							setTitle("WallSwap Time").
							setTitleColor("#FF7F27").
							setDividerColor("#FF7F27").
							
							//setMessage("Wallswap change Duration.").
							setCustomView(R.layout.example_ip_address_layout, getActivity()).
							setIcon(getResources().getDrawable(R.drawable.icon));
					
					qustomDialogBuilder.show();
					
//					View.OnClickListener listener= new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							
//						}
//					};
					ArrayList<CustomGallery> selected = adapter.getSelected();
					allPath = new String[selected.size()];
					for (int i = 0; i < allPath.length; i++) {
						allPath[i] = selected.get(i).sdcardPath;
						Log.d("sdcard paths", allPath[i]);
					}
					qustomDialogBuilder.getfulldialogview().findViewById(R.id.done).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							View v1 = qustomDialogBuilder.getfulldialogview();
						 EditText v2 = (EditText)v1.findViewById(R.id.ip_edit_text);
						 if(v2.getText().equals(null)||v2.getText().toString().isEmpty()){
						return;
					}
						TimeUtils.setChangeTime(Integer.parseInt(v2.getText().toString()));
							if(allPath.length>0){
								LongOperation mLongOperation = new LongOperation(getActivity());
								mLongOperation.execute("");
											}
							nr=0;
							adapter.selectAll(false);
							((View) qustomDialogBuilder.getfulldialogview().getRootView()).setVisibility(View.GONE);
							mode.finish();
						}
					});
					// setResult(RESULT_OK, data);
					return false;
				default:
					adapter.selectAll(true);
					nr=adapter.getCount();
					mode.setTitle(nr + " selected");
					return false;
				}
			}

				
			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
					long id, boolean checked) {
				// TODO Auto-generated method stub
				if (checked) {
					nr++;
					adapter.setNewSelection(position, checked);                    
				} else {
					nr--;
					adapter.removeSelection(position);                 
				}
				mode.setTitle(nr + " selected");

			}
		});

		
		int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
		View doneButton = gridGallery.findViewById(doneButtonId);
		gridGallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				gridGallery.setItemChecked(position, !adapter.isPositionChecked(position));
				return false;
			}
		});
		//CAB
		adapter.setMultiplePick(true);

		gridGallery.setAdapter(adapter);
		imgNoMedia = (ImageView) rootView.findViewById(R.id.imgNoMedia);

		//	btnGalleryOk = (Button) rootView.findViewById(R.id.btnGalleryOk);
		//	btnGalleryOk.setOnClickListener(mOkClickListener);

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};

		}.start();

	}

	private void checkImageStatus() {
		if (adapter.isEmpty()) {
			imgNoMedia.setVisibility(View.VISIBLE);
		} else {
			imgNoMedia.setVisibility(View.GONE);
		}
	}

	View.OnClickListener mOkClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			ArrayList<CustomGallery> selected = adapter.getSelected();
			allPath = new String[selected.size()];
			for (int i = 0; i < allPath.length; i++) {
				allPath[i] = selected.get(i).sdcardPath;
				Log.d("sdcard paths", allPath[i]);
			}
			if(allPath.length>0){
				LongOperation mLongOperation = new LongOperation(getActivity());
				mLongOperation.execute("");
			}else{
				Toast.makeText(getActivity(), "No image selected.",
						Toast.LENGTH_SHORT).show();
			}
			// setResult(RESULT_OK, data);
		}
	};

	//	AdapterView.OnItemClickListener mItemMulClickListener = new AdapterView.OnItemClickListener() {
	//		@Override
	//		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
	//			adapter.changeSelection(v, position);
	//		}
	//	};

	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			@SuppressWarnings("deprecation")
			Cursor imagecursor = getActivity().managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					CustomGallery item = new CustomGallery();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);
					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			Log.d("wallswap", "printing exception1");
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;
		int cnt = 0;
		private Activity activity;
		private Context context;

		public LongOperation(Activity arg2) {
			this.activity = arg2;
			this.context = arg2;
			this.dialog = new ProgressDialog(this.context);
		}

		protected String doInBackground(String... param) {
			Intent localIntent = new Intent(activity,
					WallpaperChangeService.class);
			activity.stopService(localIntent);
			//			cleanUpWallSwapImageStore(Environment.getExternalStorageDirectory()
			//					+ File.separator + "WallSwap");
			final int len = allPath.length;
			String selectImages = "";
			for (int i = 0; i < len; i++) {
				cnt++;
				//				String processedPath = mProcessImages.processImage(allPath[i],
				//						screenWidth);
				selectImages = selectImages + allPath[i] + ",";
			}

			return selectImages;
		}

		protected void onPostExecute(String paramString) {
			if (cnt != 0) {
				if (this.dialog.isShowing())
					this.dialog.dismiss();
				int i = TimeUtils.changeTime;
//				Toast.makeText(getActivity(),
//						cnt + " images selected. Wallpaper will Change in " + i,
//						Toast.LENGTH_SHORT).show();
				//btnGalleryOk.setEnabled(true);
				Intent localIntent = new Intent(activity,
						WallpaperChangeService.class);
				Bundle localBundle = new Bundle();
				localBundle.putInt("time", i);
				localBundle.putString("imagepath", paramString);
				localIntent.putExtras(localBundle);
				activity.startService(localIntent);
			} else {
				Toast.makeText(getActivity(), "No image selected.",
						Toast.LENGTH_SHORT).show();
				if (this.dialog.isShowing())
					this.dialog.dismiss();
			}

		}

		protected void onPreExecute() {

			this.dialog.setMessage("Setting Images...");
			this.dialog.setCanceledOnTouchOutside(false);
			this.dialog.show();
			//			btnGalleryOk.setEnabled(false);
		}

		protected void onProgressUpdate(Void... param) {
		}
	}

	//	public static boolean cleanUpWallSwapImageStore(String srcPath) {
	//		// TODO Auto-generated method stub
	//		File folder = new File(srcPath);
	//		File[] listOfFiles = folder.listFiles();
	//		if (listOfFiles != null) {
	//			if (listOfFiles.length != 0) {
	//				for (int i = 0; i < listOfFiles.length; i++) {
	//					if (listOfFiles[i].isFile()) {
	//						listOfFiles[i].delete();
	//					}
	//				}
	//				return true;
	//			} else {
	//				return false;
	//			}
	//		} else {
	//			return false;
	//		}
	//
	//	}

//	@Override
//	public void onItemSelected(boolean id) {
//		adapter.selectAll(id);
//
//	}
	  /**
		 * A dummy implementation of the {@link Callbacks} interface that does
		 * nothing. Used only when this fragment is not attached to an activity.
		 */
		private static Callbacks sDummyCallbacks = new Callbacks() {
			@Override
			public void openDrawer(boolean id) {
				
			}
		};

}
