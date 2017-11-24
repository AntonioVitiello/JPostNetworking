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

import java.util.ArrayList;

/**
 * Created by antlap on 22/11/2017.
 * MainActivity subscribes to JPost’s Default Channel using addSubscriber(object) method
 * “@OnUiThread” : Annotation is provided by JPost to run the code on the Android UI thread.
 * It receives the response message through the default global channel and updates the listview
 */

public class MainActivity extends AppCompatActivity {
    private static final String KEY_ADAPTER_CONTENT = "gitReposJson";
    private static final String KEY_CURRENT_THEME_RES_ID = "currentThemeResId";
    private static final int[] mThemesResId = new int[]{R.style.AppThemeGreen, R.style.AppThemeRed, R.style.AppThemeYellow, R.style.AppThemeBlue};

    private ListView mRepoListView;
    private RepoListAdapter mRepoListAdapter;
    private int mCurrThemeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change Theme and colors (before setContentView !!)
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mCurrThemeIndex = extras.getInt(KEY_CURRENT_THEME_RES_ID);
            setTheme(mThemesResId[mCurrThemeIndex]);
            savedInstanceState = extras;
        }

        setContentView(R.layout.activity_main);

        mRepoListView = (ListView) findViewById(R.id.repoListView);
        mRepoListAdapter = new RepoListAdapter(this, new ArrayList<GitRepo>());
        mRepoListView.setAdapter(mRepoListAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADAPTER_CONTENT)){
            String gitReposJson = savedInstanceState.getString(KEY_ADAPTER_CONTENT);
            mRepoListAdapter.addAll(gitReposJson);
            setTheme(savedInstanceState.getInt(KEY_CURRENT_THEME_RES_ID));
        }

        // Register MainActivity as Subscriber
        try {
            JPost.getBroadcastCenter().addSubscriber(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnUiThread
    @OnMessage
    private void onGitRepoList(GitRepoMsg msg){
        if(msg.getGitRepoList() != null) {
//            mRepoListAdapter.setGitRepoList(msg.getGitRepoList());
            mRepoListAdapter.addAll(msg.getGitRepoList());
            Toast.makeText(this, "GitHub repositories Loaded (" + mRepoListAdapter.getCount() + ")", Toast.LENGTH_SHORT).show();
        }
    }

    public void startClick(View view) {
        // Post start message for worker Thread
        ApiHandler.doGetApiCall(ApiHandler.GIT_REPO_URL);
        Toast.makeText(this, "Loading GitHub repositories info", Toast.LENGTH_SHORT).show();
    }

    public void cleanClick(View view) {
        mRepoListAdapter.clear();
        Toast.makeText(this, "Clean all GitHub repositories info", Toast.LENGTH_SHORT).show();
    }

    public void colorClick(View view) {
        mCurrThemeIndex = ++mCurrThemeIndex % mThemesResId.length;
        Toast.makeText(this, "Change Theme color", Toast.LENGTH_SHORT).show();

        finish();
        Bundle bundle = new Bundle();
        putActivityState(bundle);
        Intent intent = getIntent();
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        putActivityState(outState);
    }

    private void putActivityState(Bundle outState) {
        outState.putInt(KEY_CURRENT_THEME_RES_ID, mCurrThemeIndex);
        mRepoListAdapter.putGitReposJson(outState, KEY_ADAPTER_CONTENT);
    }
}