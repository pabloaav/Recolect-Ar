package com.e.recolectar.adaptadores;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.e.recolectar.R;
import com.e.recolectar.fragmentos.SelectorFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * Esta clase permite:
 *  - Colocar los Titulos de las Tabs
 *  - Controlar el numero de Tabs que se muestran
 */
public class AdaptadorPaginas extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};
    private final Context mContext;

    /*Aqui el Context es el Activity Main (un activity es un Context, porque hereda de la clase Context). En la clase MainActivity se hace: new AdaptadorPaginas(this, getSupportFragmentManager());. Es decir, que this es MainActivity, y el segundo parametro es una funcion get que devuelve un Fragment Manager, que pertenece al un padre (FragmentActivity) de MainActivity, de la cual esta hereda ese metodo
     */
    public AdaptadorPaginas(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a SelectorFragment (defined as a static inner class below).
        return SelectorFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        /*Aqui se obtienen los nombres de las pestañas o tabs determinados en el array del atributo TAB_TITLES de tipo entero, que, dependiendo de la posicion, mostrara el nombre de la pestaña correspondiente.
        * */
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}