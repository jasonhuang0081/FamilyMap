package com.example.familymap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.net.URL;

import model.Person;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

public class LoginFragment extends Fragment {
    private String personID;
    private EditText serverHost;
    private EditText serverPort;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup radioButton;
    private Button signInButton;
    private Button registerButton;
    private String hostString;
    private String portString;
    private String userString;
    private String passwordString;
    private String firstNameString;
    private String lastNameString;
    private String emailString;
    private String gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        serverHost = v.findViewById(R.id.serverHost);
        serverPort = v.findViewById(R.id.serverPort);
        userName = v.findViewById(R.id.userName);
        password = v.findViewById(R.id.passWord);
        firstName = v.findViewById(R.id.firstName);
        lastName = v.findViewById(R.id.lastName);
        email = v.findViewById(R.id.email);
        radioButton =  v.findViewById(R.id.RGroup);
        signInButton = v.findViewById(R.id.signInButton);
        registerButton = v.findViewById(R.id.registerButton);

        hostString = serverHost.getText().toString();
        portString = serverPort.getText().toString();
        userString = userName.getText().toString();
        passwordString = password.getText().toString();
        firstNameString = firstName.getText().toString();
        lastNameString = lastName.getText().toString();
        emailString = email.getText().toString();
        gender = "m";

        checkSignIn();
        checkRegister();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new LoginTask().execute();


            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new RegisterTask().execute();


            }
        });

        radioButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.male:
                            gender = "m";
                        break;
                    case R.id.female:
                            gender = "f";
                        break;
                }
            }
        });

        serverHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hostString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                portString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastNameString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailString = s.toString();
                checkSignIn();
                checkRegister();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        return v;
    }

    private class LoginTask extends AsyncTask<Void,Void,Void> {
        String output;
        @Override
        protected Void doInBackground(Void... params) {
            Proxy proxy = new Proxy();
            try {
                URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
                LoginResult result = proxy.login(new LoginRequest(userString,passwordString),url);
                if (result.getMessage() == null)
                {
                    URL eventURL = new URL("http://" + hostString + ":" + portString + "/event");
                    URL personURL = new URL("http://" + hostString + ":" + portString + "/person");
                    String synchRes = proxy.getAllData(result.getToken(),eventURL,personURL);
                    if (synchRes.equals("Updated Succeed!"))
                    {
                        output = firstNameString + " " + lastNameString;
                        personID = result.getPersonID();
                        //// sign in
//                        Data.getInstance().setSignIn(true);
                    }
                    else
                    {
                        output = synchRes;
                    }
                }
                else
                {
                    output = result.getMessage();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
            if (output.equals(firstNameString + " " + lastNameString))
            {
                Data.getInstance().setHostString(hostString);
                Data.getInstance().setPortString(portString);
                Data.getInstance().setUserString(userString);
                Data.getInstance().setPasswordString(passwordString);
//                Person user = Data.getInstance().getPersonByID(personID);
//                Data.getInstance().setUser(user);
                Data.getInstance().processData(personID);
//                Person currentPerson = Data.getInstance().getPersonByID(personID);
//                Data.getInstance().setCurrentPerson(currentPerson);

                MainActivity activity = (MainActivity) getActivity();
                activity.SetMap();
            }
        }
    }
    private class RegisterTask extends AsyncTask<Void,Void,Void> {
        String output;

        @Override
        protected Void doInBackground(Void... params) {
            Proxy proxy = new Proxy();
            try {
                URL url = new URL("http://" + hostString + ":" + portString + "/user/register");
                RegisterResult result = proxy.register(new RegisterRequest(userString,passwordString,
                        emailString,firstNameString,lastNameString,gender),url);
                if (result.getMessage() == null)
                {
                    output = firstNameString + " " + lastNameString;
                }
                else
                {
                    output = result.getMessage();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
        }
    }
    private void checkRegister()
    {

        if (hostString.equals("") ||  portString.equals("") || userString.equals("") || passwordString.equals("")
        || firstNameString.equals("") || lastNameString.equals("") || emailString.equals(""))
        {
            registerButton.setEnabled(false);
        }
        else
        {
            registerButton.setEnabled(true);
        }

    }
    private void checkSignIn()
    {


        if (hostString.equals("") ||  portString.equals("") || userString.equals("") || passwordString.equals(""))
        {
            signInButton.setEnabled(false);
        }
        else
        {
            signInButton.setEnabled(true);
        }
    }


}
