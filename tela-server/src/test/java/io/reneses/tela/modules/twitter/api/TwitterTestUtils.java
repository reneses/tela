package io.reneses.tela.modules.twitter.api;


import org.apache.commons.io.IOUtils;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class TwitterTestUtils {

    private TwitterTestUtils() {
    }

    static TwitterApi mockTwitterApi() throws Exception {

        TwitterApiImpl twitterApi = spy(TwitterApiImpl.class);

        String selfRes = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/self.json"));
        doReturn(selfRes).when(twitterApi).oauthGetRequest(
                eq("1"), eq("2"), eq("3"), eq("4"),
                eq("https://api.twitter.com/1.1/account/verify_credentials.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                anyString(), anyString());

        String userRes11 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-1-1.json"));
        String userRes12 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-1-2.json"));
        String userRes2 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-2.json"));

        doReturn(userRes11).when(twitterApi).oauthGetRequest(
                eq("1"), eq("2"), eq("3"), eq("4"),
                eq("https://api.twitter.com/1.1/followers/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("-1"),
                anyString(), anyString(),
                anyString(), anyString());

        doReturn(userRes2).when(twitterApi).oauthGetRequest(
                eq("1"), eq("2"), eq("3"), eq("4"),
                eq("https://api.twitter.com/1.1/followers/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("1549260762313340700"),
                anyString(), anyString(),
                anyString(), anyString());

        doReturn(userRes12).when(twitterApi).oauthGetRequest(
                eq("1"), eq("2"), eq("3"), eq("4"),
                eq("https://api.twitter.com/1.1/friends/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("-1"),
                anyString(), anyString(),
                anyString(), anyString());

        doReturn(userRes2).when(twitterApi).oauthGetRequest(
                eq("1"), eq("2"), eq("3"), eq("4"),
                eq("https://api.twitter.com/1.1/friends/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("1549260762313340700"),
                anyString(), anyString(),
                anyString(), anyString());

        return twitterApi;
    }

}
