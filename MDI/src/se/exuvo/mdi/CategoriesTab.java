package se.exuvo.mdi;

import java.util.ArrayList;

import se.exuvo.mdi.Categories.Category;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class CategoriesTab extends Fragment {

	public CategoriesTab() {};

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_TOPCAT = "topcat";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cat, container, false);
		final Context c = rootView.getContext();
		GridView grid = (GridView) rootView.findViewById(R.id.grid);
		ImageAdapter ad = new ImageAdapter(c, (Category) getArguments().get(ARG_TOPCAT));
		grid.setAdapter(ad);

		return rootView;
	}
	
	public static class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private ArrayList<Category> items = new ArrayList<Category>();
		
		public ImageAdapter(Context c, Category topCat) {
			mContext = c;
			items.clear();
			if(topCat == null){
				for(Category cat : Categories.cats){
					items.add(cat);
				}
			}else{
				for(Category cat : topCat.subs){
					items.add(cat);
				}
			}
		}

		public int getCount() {
	        return items.size();
		}

		public Object getItem(int position) {
			return items.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ImageButton view;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
//	            imageView = new ImageView(mContext);
				view = new ImageButton(mContext);
				view.setLayoutParams(new GridView.LayoutParams(200, 200));
	            view.setScaleType(ImageButton.ScaleType.CENTER_CROP);
//				view.setPadding(8, 8, 8, 8);
	            if(items.get(position).selected){
	            	view.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
	            	view.invalidate();
				}else{
					view.getBackground().clearColorFilter();
					view.invalidate();
				}
	            view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Category c = items.get(position);
						Toast.makeText(v.getContext(), c.name, Toast.LENGTH_SHORT).show();
						c.selected = !c.selected;
						if(c.selected){
							v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
		                    v.invalidate();
						}else{
							v.getBackground().clearColorFilter();
		                    v.invalidate();
						}
						Places.destination = null;
						Categories.notifyChanged();
					}
				});
			} else {
				view = (ImageButton) convertView;
			}

	        view.setImageResource(items.get(position).imgID);
			return view;
		}

	}

}
