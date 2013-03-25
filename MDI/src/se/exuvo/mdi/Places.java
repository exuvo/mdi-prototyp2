package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.exuvo.mdi.Categories.CatDiff;

public class Places {
	public static final ArrayList<CatDiff> ev = new ArrayList<CatDiff>();
	public static final List<Place> places = Arrays.asList(
			new Place[] {
				new Place("Kebaberiet", "Kebabs", 123, new String[] { "food" }),
				new Place("Pizza Hut", "Pizzas", 40, new String[] { "food", "pizza" }),
				new Place("Al Dente", "Pizzas", 242, new String[] { "food","pizza" }),
				new Place("Café gräs", "Tea", 9, new String[] { "food" }),
				new Place("Uffes te", "tea", 2, new String[] { "food", "café" }),
				new Place("Humlans Café", "Cofee", 5, new String[] { "food", "café" }),
				new Place("Vatten muséet", "Water", 150, new String[] { "museum" }),
				new Place("Stone muséet", "Stones", 4, new String[] { "museum" }),
				new Place("Fisk Museum", "Fish", 4, new String[] { "museum" })
			}
	);
	
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
