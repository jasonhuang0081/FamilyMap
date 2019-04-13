package com.example.familymap;

import com.example.familymap.loginFunction.Proxy;
import com.example.familymap.model.Data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProxyTest {
    private String hostString;
    private String portString;
    private String userString;
    private String passwordString;
    private String firstNameString;
    private String lastNameString;
    private String output;
    private String emailString;
    private String gender;
    @Before
    public void setUp() {
        hostString = "localhost";
        portString = "8080";


    }
    @After
    public void tearDown()
    {
        Data.getInstance().reset();
    }
    @Test
    public void registerFail()
    {
        userString = "sheila";
        passwordString = "1234";
        firstNameString = "jason";
        lastNameString = "H";
        emailString = "jason@something.com";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/register");
            RegisterResult result = proxy.register(new RegisterRequest(userString,passwordString,
                    emailString,firstNameString,lastNameString,gender),url);
            if (result.getMessage() == null)
            {
                output = result.getUserName();
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
        assertEquals(output, "Username already taken by another user");
    }
    @Test
    public void registerPass()
    {
        userString = "jason112";
        passwordString = "1234";
        firstNameString = "jason";
        lastNameString = "H";
        emailString = "jason@something.com";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/register");
            RegisterResult result = proxy.register(new RegisterRequest(userString,passwordString,
                    emailString,firstNameString,lastNameString,gender),url);
            if (result.getMessage() == null)
            {
                output = result.getUserName();  // get the user name from result to verify it register
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
        assertEquals(output, userString);
    }
    @Test
    public void loginFail()
    {
        userString = "notExist";
        passwordString = "parker";
        firstNameString = "jason";
        lastNameString = "H";
        emailString = "jason@something.com";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
            LoginResult result = proxy.login(new LoginRequest(userString,passwordString),url);
            if (result.getMessage() == null)
            {
                output = result.getUserName();  // get the username from database to confirm

            }
            else
            {
                output = result.getMessage(); // if not exist, it will have the warning message
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertEquals(output, "Invalid password or User name does not exist");
    }

    @Test
    public void loginPass()
    {
        userString = "ja";
        passwordString = "parker";
        firstNameString = "jason";
        lastNameString = "H";
        emailString = "jason@something.com";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
            LoginResult result = proxy.login(new LoginRequest(userString,passwordString),url);
            if (result.getMessage() == null)
            {
                output = result.getUserName();  // get the username from database to confirm
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
        assertEquals(output, userString);

    }
    @Test
    public void retrieveEventsAndPeoplePass()
    {
        userString = "ja";
        passwordString = "parker";
        firstNameString = "jason";
        lastNameString = "H";
        emailString = "jason@something.com";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
            LoginResult result = proxy.login(new LoginRequest(userString, passwordString), url);
            if (result.getMessage() == null) {
                URL eventURL = new URL("http://" + hostString + ":" + portString + "/event");
                URL personURL = new URL("http://" + hostString + ":" + portString + "/person");
                String synchRes = proxy.getAllData(result.getToken(), eventURL, personURL);
                if (synchRes.equals("Updated Succeed!")) {
                    output = "success";
//                    personID = result.getPersonID();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertEquals(output, "success");
        // getAllData function will store all events and person in Data singleton
        assertNotNull(Data.getInstance().getEventList());
        assertNotNull(Data.getInstance().getPersonList());
        assertEquals(Data.getInstance().getEventList().size(),91); // all 4 generations + user's number of events
        assertEquals(Data.getInstance().getPersonList().size(),31); // all 4 generations + user
    }
    @Test
    public void retrieveEventsAndPeopleFail()
    {
        userString = "random";
        passwordString = "password";
        firstNameString = "firstname";
        lastNameString = "lastname";
        emailString = "email";
        gender = "m";
        Proxy proxy = new Proxy();
        try {
            URL url = new URL("http://" + hostString + ":" + portString + "/user/login");
            LoginResult result = proxy.login(new LoginRequest(userString, passwordString), url);
            if (result.getMessage() == null) {
                URL eventURL = new URL("http://" + hostString + ":" + portString + "/event");
                URL personURL = new URL("http://" + hostString + ":" + portString + "/person");
                String synchRes = proxy.getAllData(result.getToken(), eventURL, personURL);
                if (synchRes.equals("Updated Succeed!")) {
                    output = "success";
//                    personID = result.getPersonID();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertEquals(output, "success");
        // getAllData function will store all events and person in Data singleton
        assertNotNull(Data.getInstance().getEventList());
        assertNotNull(Data.getInstance().getPersonList());
        // testing if
        // this person only has no event and only himself to see if zero events will break things
        assertEquals(Data.getInstance().getEventList().size(),0); //
        assertEquals(Data.getInstance().getPersonList().size(),1); //
    }
}
