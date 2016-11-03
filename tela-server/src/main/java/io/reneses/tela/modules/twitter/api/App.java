package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;

public class App {

    /**
     * Basic app sample
     *
     * @param args Ignored
     */
    public static void main(String[] args) throws TwitterException {

//        TelaServer tela = Assembler.build(
//                new InstagramTelaModule(),
//                new TwitterTelaModule()
//        );
//        tela.start();

//
//
//            final OAuth10aService service = new ServiceBuilder()
//                .apiKey("Pn6lNhPr2tTNqqTGlexOwCRms")
//                .apiSecret("44zlKDHs6L8SDMkrgPiQ7voyjeP3r0ivDK1aMUPdRkazJ0EU0E")
//                .build(TwitterApi.instance());
//            final OAuth10aService service = new ServiceBuilder()
//                .apiKey("ndntTg6mkN7eNkcTP1ADHtt6t")
//                .apiSecret("ouzu4YtQGv3iOPCKpRjagK9wcuIJv9wblUe0afe59JfqPhCpFr")
//                .build(TwitterApi.instance());
////
////
////            final OAuth1RequestToken requestToken = service.getRequestToken();
////            System.out.println(requestToken);
////            String authUrl = service.getAuthorizationUrl(requestToken);
//////
////            System.out.println(authUrl);
////
////            String response = "HOLA";
////            final OAuth1AccessToken accessToken = service.getAccessToken(requestToken,response);
////            System.out.println(accessToken);
//
//            final OAuth1AccessToken accessToken = new OAuth1AccessToken("150829277-pG8UQIQLJatOSKtsqevpwhg7oqhVvOuZ0Ktk6OuJ", "3uFce18iD4qRZZCSCZQHj7iXGgpSpM96szbBTeN2ZtY6D");
////
//            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", service);
//            service.signRequest(accessToken, request); // the access token from step 4
//            final Response response = request.send();
//            System.out.println(response.getBody());


            TwitterApi api = new TwitterApiImpl();

            // 2HkAqQAAAAAAxu1pAAABWCdM9c4 BDwGRl59hNmvgsbAISGsTG1d1ptxjJD0

//            User user = api.self("Pn6lNhPr2tTNqqTGlexOwCRms", "44zlKDHs6L8SDMkrgPiQ7voyjeP3r0ivDK1aMUPdRkazJ0EU0E", "150829277-pG8UQIQLJatOSKtsqevpwhg7oqhVvOuZ0Ktk6OuJ", "3uFce18iD4qRZZCSCZQHj7iXGgpSpM96szbBTeN2ZtY6D");
            User user = api.self("Pn6lNhPr2tTNqqTGlexOwCRms", "44zlKDHs6L8SDMkrgPiQ7voyjeP3r0ivDK1aMUPdRkazJ0EU0E", "2HkAqQAAAAAAxu1pAAABWCdM9c4", "BDwGRl59hNmvgsbAISGsTG1d1ptxjJD0");


            System.out.println(user);
    }

}
