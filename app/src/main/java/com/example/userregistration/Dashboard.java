package com.example.userregistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Dashboard extends Fragment {

    Button btn_signOut;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    NavController navController;
    TextView txt_welcome;


    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebaseUser = getArguments().getParcelable("user");
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // btn_signOut = view.findViewById(R.id.singout);
        txt_welcome =  view.findViewById(R.id.txt_welcome);
        navController = Navigation.findNavController(getActivity(), R.id.host_fragment);

        readFireStore();

//        btn_signOut.setOnClickListener(view1 -> {
//            FirebaseAuth.getInstance().signOut();
//
//            Intent intent = new Intent(getContext(),MainActivity.class);
//            startActivity(intent);
//        });
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
                    txt_welcome.setText("Welcome "+doc.get("firstname")+"!");
                }
            }
        });
    }
}