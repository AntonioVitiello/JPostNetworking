package com.example.jpost.networking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mindorks.androidjpost.JPost;
import com.mindorks.androidjpost.droid.OnUiThread;
import com.mindorks.jpost.core.OnMessage;
import com.mindorks.jpost.exceptions.InvalidSubscriberException;
import com.mindorks.jpost.exceptions.NoSuchChannelException;
import com.mindorks.jpost.exceptions.NullObjectException;
import com.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antlap on 22/11/2017.
 * MainActivity subscribes to JPost’s Default Channel using addSubscriber(object) method
 * “@OnUiThread” : Annotation is provided by JPost to run the code on the Android UI thread.
 * It receives the response message through the default global channel and updates the listview
 */

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String KEY_REPOS_JSON = "gitReposJson";
    private static final String KEY_REPOS_PARCELABLE = "gitReposParcelable";
    private static final String KEY_CURRENT_THEME_ID = "currentThemeResId";
    private static final int[] mThemesResId = new int[]{R.style.AppThemeGreen, R.style.AppThemeRed, R.style.AppThemeYellow, R.style.AppThemeBlue};

    private ListView mRepoListView;
    private RepoListAdapter mRepoListAdapter;
    private int mThemeIndex = 0;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String gitReposJson = null;
        List<GitRepo> gitRepos = null;
        Bundle extras = getIntent().getExtras();

        /* On screen rotation or app became foreground from background use savedInstanceState and get Parcelable
         * while onClick on COLOR Button use extras and get JSON string */
        if(savedInstanceState != null){
            mThemeIndex = savedInstanceState.getInt(KEY_CURRENT_THEME_ID);
            gitRepos = savedInstanceState.getParcelableArrayList(KEY_REPOS_PARCELABLE);
            Log.d(LOG_TAG, "onCreate: loaded state from savedInstanceState (Parcelable!)" + ", mThemeIndex = " + mThemeIndex + ", gitRepos = " + gitRepos);
        } else if(extras != null) {
            mThemeIndex = extras.getInt(KEY_CURRENT_THEME_ID);
            gitReposJson = extras.getString(KEY_REPOS_JSON);
            Log.d(LOG_TAG, "onCreate: loaded state from extras (JSON string!)" + ", mThemeIndex = " + mThemeIndex + ", gitReposJson = " + gitReposJson);
        }

        // Set Theme before setContentView !!
        setTheme(mThemesResId[mThemeIndex]);
        setContentView(R.layout.activity_main);

        mRepoListView = (ListView) findViewById(R.id.repoListView);
        mRepoListAdapter = new RepoListAdapter(this, new ArrayList<GitRepo>());
        mRepoListView.setAdapter(mRepoListAdapter);

        // load all repos from a JSON string
        mRepoListAdapter.addAll(gitReposJson);

        // load all repos from a Parcelable ArrayList
        mRepoListAdapter.addAll(gitRepos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Register MainActivity as Subscriber
            JPost.getBroadcastCenter().addSubscriber(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // Unregister MainActivity as Subscriber
            JPost.getBroadcastCenter().removeSubscriber(this);
        } catch (InvalidSubscriberException e) {
            e.printStackTrace();
        } catch (NoSuchChannelException e) {
            e.printStackTrace();
        } catch (NullObjectException e) {
            e.printStackTrace();
        }
    }

    @OnUiThread
    @OnMessage
    private void onGitRepoList(GitRepoMsg msg){
        if(msg.getGitRepoList() != null) {
//            mRepoListAdapter.setGitRepoList(msg.getGitRepoList());
            mRepoListAdapter.addAll(msg.getGitRepoList());
            showToast("GitHub repositories Loaded (" + mRepoListAdapter.getCount() + ")");
        }
    }

    private void showToast(String msg) {
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public void startClick(View view) {
        // Post start message for worker Thread
//        ApiHandler.doGetApiCall(ApiHandler.GIT_REPO_URL_1);
//        ApiHandler.doGetApiCall(ApiHandler.GIT_REPO_URL_2);
        ApiHandler.doGetApiCall("put here an object instance as parameter");
        showToast("Loading GitHub repositories info");
    }

    public void cleanClick(View view) {
        mRepoListAdapter.clear();
        showToast("Clean all GitHub repositories info");
    }

    // onClick on Color button: Persist state with a JSON string
    public void colorClick(View view) {
        mThemeIndex = ++mThemeIndex % mThemesResId.length;
        showToast("Change Theme color");

        finish();
        Bundle bundle = new Bundle();
        putActivityStateJson(bundle); // or putActivityStateParcelable(outState);
        Intent intent = getIntent();
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Persist state with Parcelable ArrayList
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        putActivityStateParcelable(outState); // or putActivityStateJson(outState);
    }

    /**
     * Put MainActivity state in outState Bundle like a JSON string
     * @param extras put in this Bundle
     */
    private void putActivityStateJson(Bundle extras) {
        extras.putInt(KEY_CURRENT_THEME_ID, mThemeIndex);
        mRepoListAdapter.putGitReposJson(extras, KEY_REPOS_JSON);
        Log.d(LOG_TAG, "putActivityStateJson: Persist state with a JSON string!");
    }

    /**
     * Put MainActivity state in outState Bundle like a Parcelable
     * @param outState put in this Bundle
     */
    private void putActivityStateParcelable(Bundle outState) {
        getIntent().replaceExtras(new Bundle());
        outState.putInt(KEY_CURRENT_THEME_ID, mThemeIndex);
        mRepoListAdapter.putGitReposParcelable(outState, KEY_REPOS_PARCELABLE);
        Log.d(LOG_TAG, "putActivityStateParcelable: Persist state with Parcelable!");
    }
}