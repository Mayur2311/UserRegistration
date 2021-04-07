package com.example.userregistration;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;


public class Profile extends Fragment {

    TextView fname, lname, email, birthdate;

    String firebaseUser;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    NavController navController;

    Random rnd;
    int color;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getUid();
        firestore = FirebaseFirestore.getInstance();

        rnd = new Random();
        color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        fname = view.findViewById(R.id.txt_fname);
        lname = view.findViewById(R.id.txt_lname);
        email = view.findViewById(R.id.txt_email);
        birthdate =  view.findViewById(R.id.txt_birthdate);

        fname.setTextColor(color);
        lname.setTextColor(color);
        email.setTextColor(color);
        birthdate.setTextColor(color);

        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);



        readFireStore();
    }

    public void readFireStore()
    {
        DocumentReference docRef = firestore.collection("Users").document(firebaseAuth.getUid());

        docRef.get().addOnCompleteListener(task ->  {
            if(task.isSuccessful())
            {
                DocumentSnapshot doc = task.getResult();

                if(doc.exists())
                {
                  fname.setText(doc.get("firstname").toString());
                  lname.setText(doc.get("lastname").toString());
                  birthdate.setText(doc.get("birthdate").toString());
                  email.setText(doc.get("email").toString());

                }
            }
        });
    }

}