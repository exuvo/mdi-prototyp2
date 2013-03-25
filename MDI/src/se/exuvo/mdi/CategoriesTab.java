package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.exuvo.mdi.Categories.CatDiff;
import se.exuvo.mdi.Categories.Category;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class CategoriesTab extends Fragment {
	public static final List<ImageAdapter> cattab = new ArrayList<ImageAdapter>();
	
	public CategoriesTab() {};

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_TOPCAT = "topcat";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_cat, container, false);
		GridView grid = (GridView) rootView.findViewById(R.id.grid);
		ImageAdapter imad = new ImageAdapter(rootView.getContext(), (Integer) getArguments().get(ARG_TOPCAT));
		cattab.add(imad);
		grid.setAdapter(imad);

		return rootView;
	}
	
	public class ImageAdapter extends BaseAdapter implements CatDiff{
		private Context mContext;
		public ArrayList<Category> items = new ArrayList<Category>();
		public ImageAdapter topCat;
		
		public ImageAdapter(Context c, Integer topCatID) {
			mContext = c;
			items.clear();
			if(topCatID == null){
				for(Category cat : Categories.cats){
					items.add(cat);
				}
			}else{
				topCat = cattab.get(topCatID);
				catu();
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
	            final Object t = this;
	            view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Category c = items.get(position);
						c.selected = !c.selected;
						if(c.selected){
							v.getBackground().setColorFilter(0xe0f47521,PorterDuff.Mode.SRC_ATOP);
		                    v.invalidate();
						}else{
							v.getBackground().clearColorFilter();
		                    v.invalidate();
						}
						Categories.notifyChanged();
						Iterator<ImageAdapter> it = cattab.iterator();
						while(it.next() != t){}
						while(it.hasNext()){
							ImageAdapter ia = it.next();
							ia.catu();
							ia.notifyDataSetChanged();
						}
//						Toast.makeText(v.getContext(), c.name, Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				view = (ImageButton) convertView;
			}

	        view.setImageResource(items.get(position).imgID);
			return view;
		}

		@Override
		public void catu() {
			if(topCat != null){
				items.clear();
				for(Category cat : topCat.items){
					if(cat.selected){
						for(Category c : cat.subs){
							items.add(c);
							notifyDataSetChanged();
						}
					}
				}
			}
		}

	}

}
