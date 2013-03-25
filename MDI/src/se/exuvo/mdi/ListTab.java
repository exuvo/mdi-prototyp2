package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.mdi.Categories.CatDiff;
import se.exuvo.mdi.Categories.Category;
import se.exuvo.mdi.Places.Place;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListTab extends Fragment {
	private static ArrayList<Place> items = new ArrayList<Place>();

	public ListTab() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		final Context c = rootView.getContext();
		ListView list = (ListView) rootView.findViewById(R.id.list);
		ListAdapter ad = new ListAdapter(c);
		list.setAdapter(ad);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Places.destination = items.get(position);
				Places.notifyChanged();
				getActivity().getActionBar().setSelectedNavigationItem(getActivity().getActionBar().getTabCount()-1);
//				Toast.makeText(rootView.getContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
			}
		});
		return rootView;
	}

	public static class ListAdapter extends BaseAdapter implements CatDiff {
		private Context mContext;
		private ArrayList<Category> cats = new ArrayList<Category>();

		public ListAdapter(Context c) {
			mContext = c;
			catu();
			Categories.ev.add(this);
		}

		public void catu() {
			items.clear();
			cats.clear();
			List<Category> active = Categories.getActive();
			loop:
			for (Place p : Places.places) {
				for (String cat : p.cats)
					for (Category c : active) {
						if (c.name.equals(cat)) {
							items.add(p);
							cats.add(c);
							continue loop;
						}
					}
			}
			notifyDataSetChanged();
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
			View rowView;
			if (convertView == null) {  // if it's not recycled, initialize some attributes
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.fragment_list_row, parent, false);
			} else {
				rowView = convertView;
			}

			TextView textDist = (TextView) rowView.findViewById(R.id.list_dist);
			TextView textName = (TextView) rowView.findViewById(R.id.list_name);
			TextView textDesc = (TextView) rowView.findViewById(R.id.list_desc);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.list_icon);
			Place p = items.get(position);
			textName.setText(p.name);
			textDesc.setText(p.desc);
			textDist.setText("" + p.dist);
			imageView.setImageResource(cats.get(position).imgID);
			return rowView;
		}

	}

}
