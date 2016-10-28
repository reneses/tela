package io.reneses.tela;

import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.modules.instagram.InstagramTelaModule;
import io.reneses.tela.modules.twitter.TwitterTelaModule;

public class App {

    /**
     * Basic app sample
     *
     * @param args Ignored
     */
    public static void main(String[] args) {

        TelaServer tela = Assembler.build(
                new InstagramTelaModule(),
                new TwitterTelaModule()
        );
        tela.start();
    }

}
