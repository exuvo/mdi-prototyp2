package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.exuvo.mdi.Categories.CatDiff;

public class Places {
	public static final ArrayList<CatDiff> ev = new ArrayList<CatDiff>();
	public static final List<Place> places = Arrays.asList(new Place[] { new Place("Pizza Hut", "Pizzas", 40, new String[] { "food", "pizza" }),
			new Place("A", "a", 1, new String[] { "touhou" }), new Place("F", "f", 23, new String[] { "food" }),
			new Place("B", "b", 2, new String[] { "café" }), new Place("G", "g", 9, new String[] { "food" }),
			new Place("C", "c", 3, new String[] { "touhou" }), new Place("H", "h", 150, new String[] { "food" }),
			new Place("D", "d", 4, new String[] { "museum" }), new Place("I", "i", 242, new String[] { "pizza" }),
			new Place("E", "e", 5, new String[] { "café" }), new Place("J", "j", 123, new String[] { "food" }) });
	
	public static Place destination;
	
	public static void notifyChanged(){
		for(CatDiff e : ev){
			e.catu();
		}
	}

	public static class Place {
		String name, desc;
		int dist;
		List<String> cats;

		public Place(String _name, String _desc, int _dist, String[] _cats) {
			name = _name;
			desc = _desc;
			dist = _dist;
			cats = Arrays.asList(_cats);
		}
	}

}
