package com.wallswap.pick;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GalleryAdapter extends BaseAdapter {

	private LayoutInflater infalter;
	private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
//	ImageLoader imageLoader;
	Context localContext;
	private boolean isActionMultiplePick;

	public GalleryAdapter(Context c) {
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		this.imageLoader = imageLoader;
      //  clearCache();
        this.localContext= c;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CustomGallery getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}

	public void selectAll(boolean selection) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isSeleted = selection;

		}
		notifyDataSetChanged();
	}

	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < data.size(); i++) {
			if (!data.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}

	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}

	public ArrayList<CustomGallery> getSelected() {
		ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				dataT.add(data.get(i));
			}
		}

		return dataT;
	}

	public void addAll(ArrayList<CustomGallery> files) {

		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			Log.d("wallswap", "printing exception3");

			e.printStackTrace();
		}

		notifyDataSetChanged();
	}

	public void changeSelection(View v, int position) {

		if (data.get(position).isSeleted) {
			data.get(position).isSeleted = false;
		} else {
			data.get(position).isSeleted = true;
		}

        ((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(data.get(position).isSeleted);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null) {

			convertView = infalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView
					.findViewById(R.id.imgQueueMultiSelected);

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        holder.imgQueue.setTag(position);

		
            holder.imgQueue.setImageResource(R.drawable.no_media);
//			imageLoader.displayImage("file://" + data.get(position).sdcardPath,
//					holder.imgQueue);
            try{
			Picasso.with(localContext).load("file://" + data.get(position).sdcardPath).resize(500, 500)
			.centerCrop().into(holder.imgQueue);
//			Picasso.Builder

            }catch (Exception e) {
            	e.printStackTrace();
            }		
			if (isActionMultiplePick) {

				holder.imgQueueMultiSelected
						.setSelected(data.get(position).isSeleted);

			}

//		}
		

		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		public ImageView imgQueueMultiSelected;
	}

//	public void clearCache() {
////		imageLoader.clearDiscCache();
////		imageLoader.clearMemoryCache();
//	}

	public void clearSelection() {
//		data.clear();
//		notifyDataSetChanged();
	}

//	public void clearSelection() {
//		data.clear();
//		notifyDataSetChanged();
//
//		
//	}

	public void setNewSelection(int position, boolean checked) {
		// TODO Auto-generated method stub
		data.get(position).isSeleted = checked;
		notifyDataSetChanged();
	}

	public void removeSelection(int position) {
		data.get(position).isSeleted = false;
		notifyDataSetChanged();
		
	}

	public boolean isPositionChecked(int position) {
		return data.get(position).isSeleted;
	}
}
