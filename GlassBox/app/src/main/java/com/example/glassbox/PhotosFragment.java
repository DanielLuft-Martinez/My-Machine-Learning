package com.example.glassbox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import static com.example.glassbox.HomeActivity.SelectedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.glassbox.HomeActivity.jsonObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photos, container, false);
        logo_fade_in();
        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        Button b_OK = (Button) view.findViewById(R.id.button_ok);
        b_OK.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity().getApplicationContext(), ChooseModelActivity.class);
                startActivity(intent);
            }
        });

        Button B_showdata = (Button) view.findViewById(R.id.button_show_selected_Data);
        B_showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ShowSelectedDataActivity.class);
                String all_fruit_breed = "FRUIT: ";
                String all_ball_breed = "BALL: ";
                String all_dog_breed = "DOG: ";
                String all_cat_breed = "CAT: ";
                String all_other_animals_breed = "OTHER ANIMALS: ";
                String all_object_breed = "OBJECT: ";
                String all_vehicle_breed = "VEHICLE: ";
                String value = "";
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    try {
                        value = jsonObject.getString(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (key.charAt(0) == '0') {
                        if (all_fruit_breed == "FRUIT: ") {
                            all_fruit_breed = all_fruit_breed   + value;
                        }
                        else{
                            all_fruit_breed = all_fruit_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '1'){
                        if (all_ball_breed == "BALL: ") {
                            all_ball_breed = all_ball_breed   + value;
                        }
                        else{
                            all_ball_breed = all_ball_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '2'){
                        if (all_dog_breed == "DOG: ") {
                            all_dog_breed = all_dog_breed   + value;
                        }
                        else{
                            all_dog_breed = all_dog_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '3'){
                        if (all_cat_breed == "CAT: ") {
                            all_cat_breed = all_cat_breed   + value;
                        }
                        else{
                            all_cat_breed = all_cat_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '4'){
                        if (all_other_animals_breed == "OTHER ANIMALS: ") {
                            all_other_animals_breed = all_other_animals_breed   + value;
                        }
                        else{
                            all_other_animals_breed = all_other_animals_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '5' || key.charAt(0) == '7'){
                        if (all_object_breed == "OBJECT: ") {
                            all_object_breed = all_object_breed   + value;
                        }
                        else{
                            all_object_breed = all_object_breed  + ", " + value;
                        }
                    }
                    else if (key.charAt(0) == '6'){
                        if (all_vehicle_breed == "VEHICLE: ") {
                            all_vehicle_breed = all_vehicle_breed   + value;
                        }
                        else{
                            all_vehicle_breed = all_vehicle_breed  + ", " + value;
                        }
                    }
                }
                intent.putExtra("FRUIT", all_fruit_breed);
                intent.putExtra("BALL", all_ball_breed);
                intent.putExtra("Dog", all_dog_breed);
                intent.putExtra("Cat", all_cat_breed);
                intent.putExtra("OTHER ANIMALS", all_other_animals_breed);
                intent.putExtra("OBJECT", all_object_breed);
                intent.putExtra("VEHICLE", all_vehicle_breed);
                startActivity(intent);
            }
        });
        return view;
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

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("FRUIT");
        listDataHeader.add("BALL");
        listDataHeader.add("DOG");
        listDataHeader.add("CAT");
        listDataHeader.add("OTHER ANIMALS");
        listDataHeader.add("OBJECT");
        listDataHeader.add("VEHICLE");

        // Adding child data
        List<String> dog = new ArrayList<String>();
        dog.add("chihuahua");
        dog.add("corgi");
        dog.add("husky");

        List<String> cat = new ArrayList<String>();
        cat.add("persian");
        cat.add("tabby");
        cat.add("siamese");

        List<String> fruit = new ArrayList<String>();
        fruit.add("apple");
        fruit.add("banana");

        List<String> ball = new ArrayList<String>();
        ball.add("baseball");
        ball.add("basketball");
        ball.add("soccerball");

        List<String> other_animals = new ArrayList<String>();
        other_animals.add("rabbit");
        other_animals.add("horse");
        other_animals.add("shark");
        other_animals.add("spider");

        List<String> object = new ArrayList<String>();
        object.add("watch");
        object.add("table");
        object.add("lamp");
        object.add("computer");
        object.add("bag");
        object.add("sneaker");
        object.add("piano");
        object.add("bench");
        object.add("mug");
        object.add("cellphone");
        object.add("toaster");

        List<String> vehicle = new ArrayList<String>();
        vehicle.add("ambulance");
        vehicle.add("firetruck");
        vehicle.add("jet");

        listDataChild.put(listDataHeader.get(0), fruit); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ball);
        listDataChild.put(listDataHeader.get(2), dog);
        listDataChild.put(listDataHeader.get(3), cat);
        listDataChild.put(listDataHeader.get(4), other_animals);
        listDataChild.put(listDataHeader.get(5), object);
        listDataChild.put(listDataHeader.get(6), vehicle);
    }

    public void logo_fade_in(){
        ImageView img = (ImageView)view.findViewById(R.id.logo);
        final Animation animFadeIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.fade_in);
        img.startAnimation(animFadeIn);
    }
}
