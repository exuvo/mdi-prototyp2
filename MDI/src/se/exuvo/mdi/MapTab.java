package se.exuvo.mdi;

import se.exuvo.mdi.Categories.CatDiff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MapTab extends Fragment implements CatDiff{
	
	public MapTab() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		
		cati(rootView);
		Categories.ev.add(this);
		Places.ev.add(this);
		
		return rootView;
	}
	
	private void cati(View rootView){
		ImageView im = (ImageView) rootView.findViewById(R.id.karta);
		ImageView i1 = (ImageView) rootView.findViewById(R.id.imagefood);
		ImageView i2 = (ImageView) rootView.findViewById(R.id.imagepizza);
		ImageView i3 = (ImageView) rootView.findViewById(R.id.imagecoffe);
		ImageView i4 = (ImageView) rootView.findViewById(R.id.imagemuseum);
		
		if(Places.destination != null){
			im.setImageResource(R.drawable.karta);
			i1.setVisibility(View.INVISIBLE);
			i2.setVisibility(View.INVISIBLE);
			i3.setVisibility(View.INVISIBLE);
			i4.setVisibility(View.INVISIBLE);
		}else{
			im.setImageResource(R.drawable.tomkarta);
			if(Categories.isActive("food")){
				i1.setVisibility(View.VISIBLE);
			}else{
				i1.setVisibility(View.INVISIBLE);
			}
			if(Categories.isActive("pizza")){
				i2.setVisibility(View.VISIBLE);
			}else{
				i2.setVisibility(View.INVISIBLE);
			}
			if(Categories.isActive("café")){
				i3.setVisibility(View.VISIBLE);
			}else{
				i3.setVisibility(View.INVISIBLE);
			}
			if(Categories.isActive("museum")){
				i4.setVisibility(View.VISIBLE);
			}else{
				i4.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void catu() {
		View w = getView();
		if(w != null){
			cati(w);
		}
//			cati(this.getView().findViewById(R.id.mapview));
	}
}
