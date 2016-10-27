package io.reneses.tela;

import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.configuration.Configuration;
import io.reneses.tela.core.configuration.ConfigurationFactory;
import io.reneses.tela.modules.instagram.InstagramTelaModule;
import io.reneses.tela.modules.twitter.TwitterTelaModule;

public class App {

    /**
     * Basic app sample
     *
     * @param args Ignored
     */
    public static void main(String[] args) {

        Configuration config = ConfigurationFactory.create();
        config.setProperty(Configuration.Property.CACHE_TTL, 1000);
        config.setProperty(Configuration.Property.PORT, 80);

        TelaServer tela = Assembler.build(
                config,
                new InstagramTelaModule(),
                new TwitterTelaModule()
        );
        tela.start();
    }

}
