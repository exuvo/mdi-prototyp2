package se.exuvo.mdi;

import java.util.ArrayList;
import java.util.List;


public class Categories {
	public static final ArrayList<Category> cats = new ArrayList<Category>();
	public static final ArrayList<CatDiff> ev = new ArrayList<CatDiff>();
	
	public static interface CatDiff{
		public void catu();
	}
	
	public static void load(){
		cats.add(new Category("museum", R.drawable.museum));
		cats.add(new Category("food", R.drawable.restaurant).addSub(new Category("pizza", R.drawable.pizza)).addSub(new Category("café", R.drawable.coffe)));
	}
	
	public static int getIMG(String cat){
		for(Category c : getActive()){
			if(cat.equals(c.name)){
				return c.imgID;
			}
		}
		return 0;
	}
	
	public static Category getCat(String cat){
		for(Category c : cats){
			if(cat.equals(c.name)){
				return c;
			}
		}
		return null;
	}
	
	public static boolean isActive(String cat){
		for(Category c : getActive()){
			if(cat.equals(c.name)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isActive(){
		for(Category c : cats){
			if(c.selected && c.subs.size()>0){
				return true;
			}
		}
		return false;
	}
	
	private static boolean getActive(Category c, List<Category> l){
		boolean ret = false;
		for(Category cat : c.subs){
			if(cat.selected){
				if(!getActive(cat, l)){
					l.add(cat);
				}
				ret = true;
			}
		}
		return ret;
	}
	
	public static List<Category> getActive(){
		List<Category> l = new ArrayList<Category>();
		for(Category c : cats){
			if(c.selected){
				if(!getActive(c, l)){
					l.add(c);
				}
			}
		}
		return l;
	}
	
	public static void notifyChanged(){
		for(CatDiff e : ev){
			e.catu();
		}
	}

	static final class Category {
		final String name;
		final Integer imgID;
		boolean selected = false;
		final ArrayList<Category> subs = new ArrayList<Category>();
		
		public Category(String _name, Integer _imgID){
			name = _name;
			imgID = _imgID;
		}
		
		public Category addSub(Category c){
			subs.add(c);
			return this;
		}
	}
}
