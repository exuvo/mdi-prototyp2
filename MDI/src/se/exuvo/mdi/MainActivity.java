package se.exuvo.mdi;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory. If this becomes too
	 * memory intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	TabsAdapter mTabsAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

//		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mViewPager, getSupportFragmentManager());
		Categories.load();
		mTabsAdapter.addTab(actionBar.newTab().setText("Categories"), CategoriesTab.class, new Bundle());
		Bundle b = new Bundle();
		b.putInt(CategoriesTab.ARG_TOPCAT, 0);
		mTabsAdapter.addTab(null, CategoriesTab.class, b);
		mTabsAdapter.addTab(actionBar.newTab().setText("List"), ListTab.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Map"), MapTab.class, null);

//		return getString(R.string.title_cat).toUpperCase(l);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummyFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummyFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
//			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	/**
	 * This is a helper class that implements the management of tabs and all details of connecting a ViewPager with associated TabHost. It
	 * relies on a trick. Normally a tab host has a simple API for supplying a View or Intent that each tab will show. This is not
	 * sufficient for switching between pages. So instead we make the content part of the tab host 0dp high (it is not shown) and the
	 * TabsAdapter supplies its own dummy view to show as the tab content. It listens to changes in tabs, and takes care of switch to the
	 * correct paged in the ViewPager whenever the selected tab changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(Activity activity, ViewPager pager, FragmentManager fm) {
			super(fm);
			mContext = activity;
			mActionBar = activity.getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			if (tab != null) {
				tab.setTag(info);
				tab.setTabListener(this);
				mActionBar.addTab(tab);
			}
			mTabs.add(info);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		static int old = 0;

		@Override
		public void onPageSelected(int position) {
			if (position == 0 || position == 1) {
				Places.destination = null;
				if (ListTab.ad != null) {
					ListTab.ad.filter(null);
				}
			}
			
			if (position == 0) {// Main cat
				mActionBar.setSelectedNavigationItem(0);
			} else if (position == mTabs.size() - 2) {// List
				mActionBar.setSelectedNavigationItem(1);
			} else if (position == mTabs.size() - 1) {// Map
				mActionBar.setSelectedNavigationItem(2);
			} else {// Subcats
				if (Categories.isActive()) {
					mActionBar.getTabAt(0).setTabListener(new TabListener() {
						@Override
						public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

						@Override
						public void onTabSelected(Tab tab, FragmentTransaction ft) {}

						@Override
						public void onTabReselected(Tab tab, FragmentTransaction ft) {}
					});
					mActionBar.setSelectedNavigationItem(0);
					mActionBar.getTabAt(0).setTabListener(this);
				} else {
					if (old > position) {
						mViewPager.setCurrentItem(0);
						position = 0;
					} else {
						mViewPager.setCurrentItem(mTabs.size() - 2);
						position = mTabs.size() - 2;
					}
				}
			}
			old = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
			int position = tab.getPosition();
			if (position == 0) {
				Places.destination = null;
				if (ListTab.ad != null) {
					ListTab.ad.filter(null);
				}
			}
//        	mViewPager.setCurrentItem(position);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	}

}
