package com.example.familymap.loginFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.familymap.model.Data;
import com.google.gson.Gson;

import requests.EventAllRequest;
import requests.LoginRequest;
import requests.PersonAllRequest;
import requests.RegisterRequest;
import results.EventAllResult;
import results.LoginResult;
import results.PersonAllResult;
import results.RegisterResult;

public class Proxy {

    public RegisterResult register(RegisterRequest request, URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        RegisterResult result = new RegisterResult(null,null,null,null);
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            String reqData =
                    "{" +
                            "\"userName\": \"" + request.getUserName() + "\"," +
                            "\"password\": \"" + request.getPassword() + "\"," +
                            "\"email\": \"" + request.getEmail() + "\"," +
                            "\"firstName\": \"" + request.getFirstName() + "\"," +
                            "\"lastName\": \"" + request.getLastName() + "\"," +
                            "\"gender\": \"" + request.getGender() + "\"" +
                            "}";
            OutputStream reqBody = connection.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                Reader reader = new InputStreamReader(respBody);
                Gson gson = new Gson();
                result = gson.fromJson(reader,RegisterResult.class);

            }
            else {
                result = new RegisterResult(null,null,null,
                        "ERROR: " + connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return result;
    }
    public LoginResult login(LoginRequest request, URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        LoginResult result = new LoginResult(null,null,null,null);
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            String reqData =
                    "{" +
                            "\"userName\": \"" + request.getUserName() + "\"," +
                            "\"password\": \"" + request.getPassword() + "\"" +
                            "}";
            OutputStream reqBody = connection.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                Reader reader = new InputStreamReader(respBody);
                Gson gson = new Gson();
                result = gson.fromJson(reader,LoginResult.class);
            }
            else {
                result = new LoginResult(null,null,null,
                        "ERROR: " + connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return result;
    }
    public String getAllData(String token, URL eventUrl, URL personUrl) throws IOException
    {
        EventAllRequest eventReq = new EventAllRequest(token);
        PersonAllRequest personReq = new PersonAllRequest(token);
        EventAllResult eventRes = getAllEvents(eventReq,eventUrl);
        PersonAllResult personRes = getAllPeople(personReq, personUrl);
        if (eventRes.getMessage() != null)
        {
            return eventRes.getMessage();
        }
        else if(personRes.getMessage() != null)
        {
            return personRes.getMessage();
        }
        else
        {
            Data.getInstance().setEventList(eventRes.getData());
            Data.getInstance().setPersonList(personRes.getData());
            return "Updated Succeed!";
        }
    }
    private EventAllResult getAllEvents(EventAllRequest request, URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        EventAllResult result = new EventAllResult(null,null);
        try {
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", request.getToken());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                Reader reader = new InputStreamReader(respBody);
                Gson gson = new Gson();
                result = gson.fromJson(reader,EventAllResult.class);
            }
            else {
                result = new EventAllResult(null,
                        "ERROR: " + connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return result;
    }
    private PersonAllResult getAllPeople(PersonAllRequest request, URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        PersonAllResult result = new PersonAllResult(null,null);
        try {
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.addRequestProperty("Authorization", request.getToken());
            connection.addRequestProperty("Accept", "application/json");
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = connection.getInputStream();
                Reader reader = new InputStreamReader(respBody);
                Gson gson = new Gson();
                result = gson.fromJson(reader,PersonAllResult.class);
            }
            else {
                result = new PersonAllResult(null,
                        "ERROR: " + connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        return result;
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
