package io.reneses.tela.modules.instagram;


import io.reneses.tela.core.util.HttpClient;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstagramTestUtils {

    private InstagramTestUtils() {
    }

    private static String readFile(String filename) throws IOException {
        return IOUtils.toString(InstagramTestUtils.class.getResourceAsStream(filename));
    }

    public static HttpClient mockInstagramHttpClient() throws Exception {

        HttpClient httpClient = mock(HttpClient.class);

        String self = readFile("/instagram/user.json");
        String search = readFile("/instagram/search.json");
        String relationship11 = readFile("/instagram/relationship-1-1.json");
        String relationship12 = readFile("/instagram/relationship-1-2.json");
        String relationship2 = readFile("/instagram/relationship-2.json");
        String media = readFile("/instagram/media.json");
        String comments1 = readFile("/instagram/comments-1.json");
        String comments2 = readFile("/instagram/comments-2.json");
        String likes1 = readFile("/instagram/likes-1.json");
        String likes2 = readFile("/instagram/likes-2.json");
        String userRelationship = readFile("/instagram/user-relationship.json");

        String urlSelf = "https://api.instagram.com/v1/users/self";
        when(httpClient.getRequest(eq(urlSelf), anyString(), anyString())).thenReturn(self);

        String urlUsers1 = "https://api.instagram.com/v1/users/1";
        when(httpClient.getRequest(eq(urlUsers1), anyString(), anyString())).thenReturn(self);

        String urlSearch = "https://api.instagram.com/v1/users/search";
        when(httpClient.getRequest(eq(urlSearch), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(search);

        String urlFollowers = "https://api.instagram.com/v1/users/self/followed-by";
        when(httpClient.getRequest(eq(urlFollowers), anyString(), anyString(), eq("cursor"), eq("")))
                .thenReturn(relationship11);
        when(httpClient.getRequest(eq(urlFollowers), anyString(), anyString(), eq("cursor"), eq("2")))
                .thenReturn(relationship2);

        String urlFollowing = "https://api.instagram.com/v1/users/self/follows";
        when(httpClient.getRequest(eq(urlFollowing), anyString(), anyString(), eq("cursor"), eq("")))
                .thenReturn(relationship12);
        when(httpClient.getRequest(eq(urlFollowing), anyString(), anyString(), eq("cursor"), eq("2")))
                .thenReturn(relationship2);

        String urlMedia1 = "https://api.instagram.com/v1/users/self/media/recent?access_token=&count=2147483647";
        String urlMedia2 = "https://api.instagram.com/v1/users/self/media/recent?access_token=&count=1";
        when(httpClient.getRequest(eq(urlMedia1))).thenReturn(media);
        when(httpClient.getRequest(eq(urlMedia2))).thenReturn(media);

        String urlComments = "https://api.instagram.com/v1/media/1/comments?access_token=";
        when(httpClient.getRequest(eq(urlComments))).thenReturn(comments1);
        when(httpClient.getRequest(eq("http://next-comment-url.com"))).thenReturn(comments2);

        String urlLikes = "https://api.instagram.com/v1/media/1/likes?access_token=";
        when(httpClient.getRequest(eq(urlLikes))).thenReturn(likes1);
        when(httpClient.getRequest(eq("http://next-likes.com"))).thenReturn(likes2);

        String urlRelationship = "https://api.instagram.com/v1/users/1/relationship";
        when(httpClient.getRequest(eq(urlRelationship), anyString(), anyString())).thenReturn(userRelationship);

        return httpClient;
    }


}
