package com.example.jpost.networking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * Created by antlap on 22/11/2017.
 */

public class RepoListAdapter extends BaseAdapter {
    private List<GitRepo> mGitRepoList;
    private Context mContext;

    
    public RepoListAdapter(Context context, List<GitRepo> gitRepoList) {
        this.mContext = context;
        mGitRepoList = gitRepoList;
    }

    @Override
    public int getCount() {
        return mGitRepoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGitRepoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.repo_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.repoIdTxt = (TextView)convertView.findViewById(R.id.repoIdTxt);
        holder.repoNameTxt = (TextView)convertView.findViewById(R.id.repoNameTxt);
        holder.repoUrlTxt = (TextView)convertView.findViewById(R.id.repoUrlTxt);
        holder.repoSizeTxt = (TextView)convertView.findViewById(R.id.repoSizeTxt);
        holder.repoDescTxt = (TextView)convertView.findViewById(R.id.repoDescTxt);

        GitRepo repo = mGitRepoList.get(position);
        holder.position = position;
        holder.repoIdTxt.setText(String.valueOf(repo.getId()));
        holder.repoNameTxt.setText(repo.getName());
        holder.repoUrlTxt.setText(repo.getUrl());
        holder.repoSizeTxt.setText(String.valueOf(repo.getSize()));
        holder.repoDescTxt.setText(String.valueOf(repo.getDescription()));

        return convertView;
    }

    private class ViewHolder implements View.OnClickListener{
        TextView repoIdTxt;
        TextView repoNameTxt;
        TextView repoUrlTxt;
        TextView repoSizeTxt;
        TextView repoDescTxt;
        int position;

        public ViewHolder(View itemView) {
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Toast.makeText(mContext, "List item index = " + (position + 1) + " of " + mGitRepoList.size(), Toast.LENGTH_LONG).show();
        }
    }

//    public void setGitRepoList(List<GitRepo> gitRepoList) {
//        mGitRepoList = gitRepoList;
//        notifyDataSetChanged();
//    }

    public void addAll(List<GitRepo> gitRepoList) {
        mGitRepoList.addAll(gitRepoList);
        notifyDataSetChanged();
    }

    public void addAll(String gitReposJson) {
        Gson gson = new GsonBuilder().create();
        GitRepo[] gitRepoArray = gson.fromJson(gitReposJson, GitRepo[].class);
        mGitRepoList.addAll(Arrays.asList(gitRepoArray));
        notifyDataSetChanged();
    }

    public void clear() {
        mGitRepoList.clear();
        notifyDataSetChanged();
    }

    public void putGitReposJson(Bundle bundle, String key) {
        GitRepo[] gitReposArray = mGitRepoList.toArray(new GitRepo[mGitRepoList.size()]);
        Gson gson = new GsonBuilder().create();
        String gitReposJson = gson.toJson(gitReposArray, GitRepo[].class);
        bundle.putString(key, gitReposJson);
    }

}