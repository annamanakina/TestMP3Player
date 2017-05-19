package com.manakina.home.mp3player.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.manakina.home.mp3player.interfaces.OnBackPressListener;


public class BackPressImpl implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressImpl(Fragment parentFragment) {
        this.parentFragment = parentFragment;
      //  Log.i("TAG", "class BackPressImpl конструктор");
    }

    @Override
    public boolean onBackPressed() {

        if (parentFragment == null) return false;

        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();
        //Log.i("TAG", "class BackPressImpl childCount- " + childCount);

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager.getFragments().get(0);


          //  Log.i("TAG", "class BackPressImpl childFragment - " + childFragment.toString());
          //  Log.i("TAG", "class BackPressImpl BOOLEAN childFragment.onBackPressed() - " + childFragment.onBackPressed());
            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
               // Log.i("TAG", "BackPressImpl childFragment.onBackPressed() - " + childFragment.onBackPressed());
                        // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate();

                //childFragmentManager.beginTransaction().attach(new FragmentFolders()).commit();
                //parentFragment.getView()..onResume();
                 /* FragmentManager manager = parentFragment.getFragmentManager();
                  FragmentTransaction transaction = manager.beginTransaction();
                  transaction.attach(parentFragment);*/
              //  Log.i("TAG", "class BackPressImpl BOOLEAN !childFragment.onBackPressed() " + childFragment.onBackPressed() );
                       // .popBackStack();
                        //.popBackStackImmediate();

                /*FragmentManager manager = parentFragment.getFragmentManager();
                int backStackEntryCount = manager.getBackStackEntryCount();
                Log.i("TAG", "BackPressImpl backStackEntryCount - "+ backStackEntryCount);
                Fragment fragment = manager.getFragments().get(backStackEntryCount);
                fragment.onResume();
                // parentFragment.getFragmentManager().getBackStackEntryAt(0);*/

            }
           /* FragmentManager manager = parentFragment.getFragmentManager();
            int backStackEntryCount = manager.getBackStackEntryCount();
            Fragment fragment = manager.getFragments().get(backStackEntryCount);
            fragment.onResume();*/
            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }

    }
}
