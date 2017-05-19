package com.manakina.home.mp3player.musicadapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.manakina.home.mp3player.R;
import com.manakina.home.mp3player.fragments.FragmentAlbums;
import com.manakina.home.mp3player.fragments.FragmentArtists;
import com.manakina.home.mp3player.fragments.FragmentFolders;
import com.manakina.home.mp3player.fragments.FragmentGenres;
import com.manakina.home.mp3player.fragments.FragmentSongs;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int ITEM_SONGS = 0;
    private static final int ITEM_FOLDERS = 1;
    private static final int ITEM_ARTISTS = 2;
    private static final int ITEM_GENRES = 3;
    private static final int ITEM_ALBUMS = 4;
    private static final int FRAGMENT_AMOUNT = 5;


    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Create pager adapter
     *
     * @param //resources
     * @param fm
     */
    public ViewPagerAdapter(Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;

        switch (position) {
            case ITEM_SONGS:
                // First Fragment of First Tab
                result = new FragmentSongs();
            //    Log.i("TAG", "ViewPagerAdapter FragmentSongs");
                break;
            case ITEM_FOLDERS:
                result = new FragmentFolders();
              //  Log.i("TAG", "ViewPagerAdapter FragmentFolders");
                break;
            case ITEM_ARTISTS:
                result = new FragmentArtists();
               // Log.i("TAG", "ViewPagerAdapter FragmentArtists ");
                break;
            case ITEM_GENRES:
                result = new FragmentGenres();
              //  Log.i("TAG", "ViewPagerAdapter FragmentGenres ");
                break;
           case ITEM_ALBUMS:
                result = new FragmentAlbums();
             //   Log.i("TAG", "ViewPagerAdapter FragmentAlbums ");
                break;

            default:
                result = null;
              //  Log.i("TAG", "ViewPagerAdapter default");
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return FRAGMENT_AMOUNT;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case ITEM_SONGS:
                return resources.getString(R.string.fragment_all_tracks);
            case ITEM_FOLDERS:
                return resources.getString(R.string.fragment_folders);
            case ITEM_ARTISTS:
                return resources.getString(R.string.fragment_artists);
            case ITEM_GENRES:
                return resources.getString(R.string.fragment_genres);
            case ITEM_ALBUMS:
                return resources.getString(R.string.fragment_albums);
            default:
                return null;
        }

        //  return "OBJECT " + (position + 1);
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        //этот метод срабатывает когда перелистываются swipe страницы в пейджере
        //к удалению верхнего фрагмента (список песен) он отношения не имеет
        // Log.i("TAG", "ViewPagerAdapter destroyItem");
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}
