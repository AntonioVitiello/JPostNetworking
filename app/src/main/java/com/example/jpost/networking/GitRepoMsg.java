package com.example.jpost.networking;

import java.util.List;

/**
 * Created by antlap on 22/11/2017.
 */

public class GitRepoMsg {

    private List<GitRepo> gitRepoList;

    public GitRepoMsg(List<GitRepo> gitRepoList) {
        this.gitRepoList = gitRepoList;
    }

    public List<GitRepo> getGitRepoList() {
        return gitRepoList;
    }

    public void setGitRepoList(List<GitRepo> gitRepoList) {
        this.gitRepoList = gitRepoList;
    }
}