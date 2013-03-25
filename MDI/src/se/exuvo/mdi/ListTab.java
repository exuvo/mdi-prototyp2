package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class ListTab extends Fragment {
	private static ArrayList<Item> items = new ArrayList<Item>();

	public static class Item {
		Place p;
		Category cat;

		public Item(Place _p, Category _cat) {
			p = _p;
			cat = _cat;
		}
	}

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
				Places.destination = items.get(position).p;
				Places.notifyChanged();
				getActivity().getActionBar().setSelectedNavigationItem(getActivity().getActionBar().getTabCount() - 1);
//				Toast.makeText(rootView.getContext(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
			}
		});
		return rootView;
	}

	public static class ListAdapter extends BaseAdapter implements CatDiff {
		private Context mContext;

		public ListAdapter(Context c) {
			mContext = c;
			catu();
			Categories.ev.add(this);
		}

		public void catu() {
			items.clear();
			List<Category> active = Categories.getActive();
			if (active.size() == 0) {
				for (Place p : Places.places) {
					items.add(new Item(p, Categories.getCat(p.cats.get(0))));
				}
			} else {
				loop:
				for (Place p : Places.places) {
					for (String cat : p.cats)
						for (Category c : active) {
							if (c.name.equals(cat)) {
								items.add(new Item(p, c));
								continue loop;
							}
						}
				}
			}

			Collections.sort(items, new Comparator<Item>() {
				@Override
				public int compare(Item lhs, Item rhs) {
					return lhs.p.dist - rhs.p.dist;
				}
			});
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
			Place p = items.get(position).p;
			textName.setText(p.name);
			textDesc.setText(p.desc);
			textDist.setText("" + p.dist + "m");
			imageView.setImageResource(items.get(position).cat.imgID);
			return rowView;
		}

	}

}
