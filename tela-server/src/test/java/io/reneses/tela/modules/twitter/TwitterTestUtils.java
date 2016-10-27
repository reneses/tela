package io.reneses.tela.modules.twitter;


import io.reneses.tela.core.util.HttpClient;
import org.apache.commons.io.IOUtils;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TwitterTestUtils {

    private TwitterTestUtils() {
    }


    // We are U1
    // U2 -> U1-> U3
//    public static InstagramApi mockInstagramApi(String u1AccessToken, User u1, User u2, User u3) throws InstagramException {
//
//        InstagramApi instagramApi = mock(InstagramApi.class);
//
//        when(instagramApi.self(eq(u1AccessToken))).thenReturn(u1);
//
//        when(instagramApi.search(eq(u1AccessToken), anyString())).thenThrow(new UserNotExistsException("Test"));
//        when(instagramApi.search(eq(u1AccessToken), eq(u1.getUsername()))).thenReturn(u1);
//        when(instagramApi.search(eq(u1AccessToken), eq(u2.getUsername()))).thenReturn(u2);
//        when(instagramApi.search(eq(u1AccessToken), eq(u3.getUsername()))).thenReturn(u3);
//        when(instagramApi.search(not(eq(u1AccessToken)), anyString())).thenThrow(new InvalidAccessTokenException());
//
//        when(instagramApi.user(eq(u1AccessToken), anyInt())).thenThrow(new UserNotExistsException("Test"));
//        when(instagramApi.user(eq(u1AccessToken), eq(u1.getId()))).thenReturn(u1);
//        when(instagramApi.user(eq(u1AccessToken), eq(u2.getId()))).thenReturn(u2);
//        when(instagramApi.user(eq(u1AccessToken), eq(u3.getId()))).thenReturn(u3);
//        when(instagramApi.user(not(eq(u1AccessToken)), anyInt())).thenThrow(new InvalidAccessTokenException());
//
//        when(instagramApi.followers(eq(u1AccessToken), anyInt())).thenReturn(Arrays.asList(u2));
//        when(instagramApi.followers(eq(u1AccessToken))).thenReturn(Arrays.asList(u2));
//
//        when(instagramApi.following(eq(u1AccessToken), anyInt())).thenReturn(Arrays.asList(u3));
//        when(instagramApi.following(eq(u1AccessToken))).thenReturn(Arrays.asList(u3));
//
//        return instagramApi;
//
//    }

    public static HttpClient mockTwitterHttpClient() throws Exception {
        HttpClient httpClient = mock(HttpClient.class);

        String selfRes = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/self.json"));
        when(httpClient.authorizedGetRequest(
                anyString(), eq("https://api.twitter.com/1.1/account/verify_credentials.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                anyString(), anyString())
        ).thenReturn(selfRes);

        String userRes11 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-1-1.json"));
        String userRes12 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-1-2.json"));
        String userRes2 = IOUtils.toString(TwitterTestUtils.class.getResourceAsStream("/twitter/relationship-2.json"));

        when(httpClient.authorizedGetRequest(
                anyString(), eq("https://api.twitter.com/1.1/followers/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("-1"),
                anyString(), anyString(),
                anyString(), anyString())
        ).thenReturn(userRes11);

        when(httpClient.authorizedGetRequest(
                anyString(), eq("https://api.twitter.com/1.1/followers/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("1549260762313340700"),
                anyString(), anyString(),
                anyString(), anyString())
        ).thenReturn(userRes2);

        when(httpClient.authorizedGetRequest(
                anyString(), eq("https://api.twitter.com/1.1/friends/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("-1"),
                anyString(), anyString(),
                anyString(), anyString())
        ).thenReturn(userRes12);

        when(httpClient.authorizedGetRequest(
                anyString(), eq("https://api.twitter.com/1.1/friends/list.json"),
                anyString(), anyString(),
                anyString(), anyString(),
                eq("cursor"), eq("1549260762313340700"),
                anyString(), anyString(),
                anyString(), anyString())
        ).thenReturn(userRes2);

        return httpClient;
    }

}
