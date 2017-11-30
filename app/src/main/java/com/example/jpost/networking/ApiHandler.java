package com.example.jpost.networking;

import com.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mindorks.androidjpost.JPost;
import com.mindorks.jpost.core.OnMessage;
import com.mindorks.jpost.exceptions.JPostNotRunningException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by antlap on 22/11/2017.
 * GIT_REPO_URL is a defines to facilitate the referencing to the api endpoint from any class.
 * API_CHANNEL_ID is the int id, we want to attach to a private channel we are creating through JPost.
 * JPost provides three kinds of channel. Default, Public and Private, Private channel is used to communicate with the subscribers in a controlled way.
 * Only Creator can add subscribers on this channel and only subscribers of this channel can post messages over it.
 * We have init method to instantiate the ApiHandler class.
 * The ApiHandler() constructor creates a private channel with channel id.
 * doGetApiCall method provides an interface to make api call.
 * It sends a message over the api channel to make the api call and process it in a non UI thread.
 * “@OnMessage” is from JPost. It binds a method to receive message on the channel.
 * processGitRepoGet method creates HttpURLConnection by opening the connection and makes get request on the provided url.
 * It gets the api response in an input stream. From the input stream it creates a StringBuffer to be used to parse the json.
 * Through GsonBuilder we gets a gson object and use it’s fromJson method to get an array of parsed GitRepo objects.
 * The array is converted into List using Arrays.asList method.
 * The GitRepoMsg is instantiated and send on the DEFAULT GLOBAL channel of the JPost.
 * Default global channel sends and receives messages for all the subscribes.
 * For documnetation on JPost see:
 * https://github.com/janishar/JPost
 */

public class ApiHandler {
    private static final String LOG_PREFIX = "antlap";
    private static final String LOG_TAG = LOG_PREFIX + ApiHandler.class.getSimpleName();

    // Two different API to invoke = two different JSON to parse
    public static String GIT_REPO_URL_1 = "https://api.github.com/users/mralexgray/repos";
    public static String GIT_REPO_URL_2 = "https://api.github.com/search/repositories?q=android&sort=stars";

    private static final int API_CHANNEL_ID = 1000;
    private static ApiHandler apiHandler;

    public static void init() {
        apiHandler = new ApiHandler();
    }

    public ApiHandler() {
        try {

            // Create a Private Channel
            JPost.getBroadcastCenter().createPrivateChannel(this, API_CHANNEL_ID);

            // Create a Public Channel and subscribe to it
//            JPost.getBroadcastCenter().createPublicChannel(API_CHANNEL_ID);
//            JPost.getBroadcastCenter().addSubscriber(API_CHANNEL_ID, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send private async msg to process GitHub request in a separate Thread
     *
     */
    public static void doGetApiCall(String msg) {
        Log.d(LOG_TAG, "doGetApiCall: currentThread = " + Thread.currentThread());
        try {

            // Send asynchrone broadcast message in Private Channel
            JPost.getBroadcastCenter().broadcastAsync(apiHandler, API_CHANNEL_ID, msg);

            // Send broadcast message in Public Channel
//            JPost.getBroadcastCenter().broadcastAsync(API_CHANNEL_ID, url);

        } catch (JPostNotRunningException e) {
            e.printStackTrace();
        }
    }

    @OnMessage(channelId = API_CHANNEL_ID)
    public void processGitHubRequestUrl1(String msg) throws IOException {
        Log.d(LOG_TAG, "processGitHubRequest: currentThread = " + Thread.currentThread() + ", url = " + GIT_REPO_URL_1);
        HttpURLConnection urlConnection = null;
        try {
            URL myUrl = new URL(GIT_REPO_URL_1);
            urlConnection = (HttpURLConnection) myUrl.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            int responseCode = urlConnection.getResponseCode();
            Log.d(LOG_TAG, "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    String response = scanner.next();

                    Log.d(LOG_TAG, "Response2 : " + response);
                    Gson gson = new GsonBuilder().create();

                    // Parse JSON Response
                    GitRepo[] gitRepoArray = gson.fromJson(response, GitRepo[].class);
                    GitRepoMsg gitRepoMsg = new GitRepoMsg(Arrays.asList(gitRepoArray));
                    Log.d(LOG_TAG, "ResponseObj : " + gitRepoMsg.getGitRepoList());
                    JPost.getBroadcastCenter().broadcastAsync(gitRepoMsg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JPostNotRunningException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    @OnMessage(channelId = API_CHANNEL_ID)
    private void processGitHubRequestUrl2(String msg) {
        Log.d(LOG_TAG, "processGitHubRequest: currentThread = " + Thread.currentThread() + ", url = " + GIT_REPO_URL_2);
        try {
            URL obj = new URL(GIT_REPO_URL_2);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            Log.d(LOG_TAG, "Response Code : " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d(LOG_TAG, "Response : " + response);
                Gson gson = new GsonBuilder().create();

                // Parse JSON Response
                GitReposContainer gitRepoContainer = gson.fromJson(response.toString(), GitReposContainer.class);
                Log.d(LOG_TAG, "gitRepoContainer = " + gitRepoContainer);
                List<GitRepo> gitRepoArray = gitRepoContainer.getItems();
                GitRepoMsg gitRepoMsg = new GitRepoMsg(gitRepoArray);
                Log.d(LOG_TAG, "ResponseObj : " + gitRepoMsg.getGitRepoList());
                JPost.getBroadcastCenter().broadcastAsync(gitRepoMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JPostNotRunningException e) {
            e.printStackTrace();
        }
    }

}