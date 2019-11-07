package com.example.mfrbmv10.Fragments.Muestreos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mfrbmv10.Fragments.Muestreos.MuestreoMostrar.PageMuestreoMostrarAdapter;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoMostrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoMostrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoMostrarFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String id_bitacora, id_muestreo, nombre_bitacora, forma, textura;
    private Muestreo m;


    public MuestreoMostrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoMostrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoMostrarFragment newInstance(String param1, String param2) {
        MuestreoMostrarFragment fragment = new MuestreoMostrarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View muestreo_mostrar_fragment = inflater.inflate(R.layout.fragment_muestreo_mostrar, container, false);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");

        tabLayout = (TabLayout) muestreo_mostrar_fragment.findViewById(R.id.tabLayout_mm);
        tabLayout.addTab(tabLayout.newTab().setText("Descripcion"));
        tabLayout.addTab(tabLayout.newTab().setText("Colores"));
        tabLayout.addTab(tabLayout.newTab().setText("Dimensiones"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) muestreo_mostrar_fragment.findViewById(R.id.pager_mm);
        PageMuestreoMostrarAdapter adapter = new PageMuestreoMostrarAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), id_bitacora, id_muestreo, nombre_bitacora, m);
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);

        return muestreo_mostrar_fragment;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
