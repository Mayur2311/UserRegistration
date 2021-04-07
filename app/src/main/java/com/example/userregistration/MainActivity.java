package com.example.userregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    //Declarartion for login//
    EditText login_email, login_password;
    TextView logintxt, signuptxt, forgotpasstxt;
    Button btn_login;
    View singuplayout;

    //Declaration for Singup//
    EditText firstnameR,lastnameR, signupemailR, signuppasswordR,birthdateR,signupconfirmpasswordR;
    Button singupBtnR;

    //Firebase_Declaration
     FirebaseAuth firebaseAuth;
     FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singuplayout = findViewById(R.id.singUp_Layout);
        singuplayout.setVisibility(View.INVISIBLE);

        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //navController = Navigation.findNavController(this, R.id.host_fragment);



        //--Initialization for login--//
        login_email = findViewById(R.id.et_login_email);
        login_password = findViewById(R.id.et_login_password);
        signuptxt = findViewById(R.id.txt_signUp);
        logintxt = findViewById(R.id.textView);
        forgotpasstxt =  findViewById(R.id.txt_forgotPass);
        btn_login = findViewById(R.id.btn_login);


        //--Initialization for singup--//
        firstnameR =  findViewById(R.id.et_firstnameR);
        lastnameR =  findViewById(R.id.et_lastnameR);
        signupemailR = findViewById(R.id.et_signupR_email);
        signuppasswordR =  findViewById(R.id.et_signupR_password);
        birthdateR = findViewById(R.id.et_birthdateR);
        signupconfirmpasswordR = findViewById(R.id.et_signup_confirmpasswordR);
        singupBtnR = findViewById(R.id.btn_signupR);



        signuptxt.setOnClickListener(v -> {
           manageVisiblity();
        });

        //------------------------------SING_UP------------------------------------//

        singupBtnR.setOnClickListener(v -> {
               if(!checkEmptyField())
                {
                    if (signuppasswordR.getText().length() <6)
                    {
                        signuppasswordR.setError("Invalid Password, Password should be at least 6 charachters");
                        signuppasswordR.requestFocus();

                    }else
                    {
                        if (!signuppasswordR.getText().toString().equals(signupconfirmpasswordR.getText().toString()))
                        {
                            signupconfirmpasswordR.setError("Invalid password !, password should match");
                            signupconfirmpasswordR.requestFocus();
                        }
                        else
                        {
                            String firstname_var = firstnameR.getText().toString();
                            String lastname_var = lastnameR.getText().toString();
                            String email_var = signupemailR.getText().toString();
                            String password_var = signuppasswordR.getText().toString();
                            String bday_var =  birthdateR.getText().toString();
                            String cpassword_var = signupconfirmpasswordR.getText().toString();

                           // Person person = new Person(email_var, password_var, firstname_var, lastname_var,bday_var);
                            User user = new User(firstname_var, lastname_var, email_var, bday_var, password_var);

                            createUser(user);

                        }
                    }

                }
        });

        //-------------------------------LOG IN---------------------------------------//

        btn_login.setOnClickListener(v -> {

            if(!loginCheckEmptyFields())
            {
                String lemail = login_email.getText().toString();
                String lpassword = login_password.getText().toString();

                userLogin(lemail,lpassword);
            }
        });


    }

    private boolean checkEmptyField()
    {
        if(TextUtils.isEmpty(firstnameR.getText().toString()))
        {
            firstnameR.setError("firstname cannot be blank");
            firstnameR.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(lastnameR.getText().toString()))
        {
            lastnameR.setError("lastname cannot be blank");
            lastnameR.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(signupemailR.getText().toString()))
        {
            signupemailR.setError("email cannot be blank");
            signupemailR.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(signuppasswordR.getText().toString()))
        {
            signuppasswordR.setError("password cannot be blank");
            signuppasswordR.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(signupconfirmpasswordR.getText().toString()))
        {
            signupconfirmpasswordR.setError("confirm password cannot be blank");
            signupconfirmpasswordR.requestFocus();
            return true;
        }

        return false;
    }

    public void createUser(User user)
    {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful())
                    {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Toast.makeText(this,"Registration successful", Toast.LENGTH_SHORT).show();
                        writeFireStore(user, firebaseUser);
                    }else {
                        Toast.makeText(getApplicationContext(),"Registration Error!",Toast.LENGTH_LONG).show();
                    }

                });
    }

    public void writeFireStore(User user,FirebaseUser firebaseUser)
    {
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("firstname", user.getFirstName());
        userMap.put("lastname", user.getLastName());
        userMap.put("email",user.getEmail());
        userMap.put("birthdate", user.getBirthdate());
        userMap.put("password", user.getPassword());


        firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                .set(userMap).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(getApplicationContext(), "Registration Success!",Toast.LENGTH_LONG).show();
                firebaseAuth.signOut();
                singuplayout.setVisibility(View.INVISIBLE);


                logintxt.setVisibility(View.VISIBLE);
                login_email.setVisibility(View.VISIBLE);
                login_password.setVisibility(View.VISIBLE);
                forgotpasstxt.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                signuptxt.setVisibility(View.VISIBLE);


            }else{
                Toast.makeText(getApplicationContext(),"Firestore Error !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void manageVisiblity()
    {
        logintxt.setVisibility(View.INVISIBLE);
        login_email.setVisibility(View.INVISIBLE);
        login_password.setVisibility(View.INVISIBLE);
        forgotpasstxt.setVisibility(View.INVISIBLE);
        btn_login.setVisibility(View.INVISIBLE);
        signuptxt.setVisibility(View.INVISIBLE);

        singuplayout.setVisibility(View.VISIBLE);
    }
    public boolean loginCheckEmptyFields()
    {
        if(TextUtils.isEmpty(login_email.getText().toString()))
        {
            login_email.setError("Email cannot be empty!");
            login_email.requestFocus();
            return true;
        }else if (TextUtils.isEmpty(login_password.getText().toString()))
        {
            login_password.setError("Password cannot be empty!");
            login_password.requestFocus();
            return true;
        }
        return false;
    }

    public void userLogin(String email, String pass)
    {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task ->{
                    if(task.isSuccessful())
                    {
                        Toast.makeText(this.getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        updateUI(firebaseUser);

                    }else{
                        Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateUI(FirebaseUser user)
    {
        Bundle b = new Bundle();
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

       /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainact, new Dashboard()).commit();*/

        Intent intent = new Intent(this, Navigation.class);
        intent.putExtra("Users", user);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            updateUI(firebaseUser);
            Toast.makeText(this,"User Already Signed-in", Toast.LENGTH_LONG).show();
        }
    }


}