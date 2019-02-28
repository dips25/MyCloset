package my.closet.fashion.style;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import my.closet.fashion.style.adapters.Lookbookadapter;
import my.closet.fashion.style.modesl.Looks;


public class Lookbook_fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    GridView gridView;
    Lookbookadapter lookbookadapter;
    Realm realm;
    ArrayList<Looks> dresses=new ArrayList<>();



    public Lookbook_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Lookbook_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Lookbook_fragment newInstance(String param1, String param2) {
        Lookbook_fragment fragment = new Lookbook_fragment();
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

        View view=inflater.inflate(R.layout.lookbook,container,false);
        gridView=(GridView) view.findViewById(R.id.grid);
        lookbookadapter=new Lookbookadapter(Objects.requireNonNull(getActivity()),R.layout.single_gridview,fetchItems());
        gridView.setAdapter(lookbookadapter);

        return view;
    }



    private ArrayList<Looks> fetchItems() {

        realm=Realm.getDefaultInstance();
        RealmResults<Looks> looksRealmResults=realm.where(Looks.class).findAll();
        for (Looks looks : looksRealmResults){

            dresses.add(looks);
        }

        return dresses;
    }

    // TODO: Rename method, update argument and hook method into UI event



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

}
