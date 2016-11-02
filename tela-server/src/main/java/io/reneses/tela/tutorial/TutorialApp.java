package io.reneses.tela.tutorial;

import io.reneses.tela.Assembler;
import io.reneses.tela.core.configuration.Configuration;
import io.reneses.tela.core.configuration.ConfigurationFactory;

/**
 * Simple app for the tutorial
 */
public class TutorialApp {

    /**
     * Configure the scheduler, a temporal OrientDB database and assemble and run the server
     *
     * @param args Ignored
     */
    public static void main(String[] args) {
        Configuration config = ConfigurationFactory.create();
        config.setProperty(Configuration.Property.SCHEDULER_DELAY, 5);
        config.setProperty(Configuration.Property.ORIENTDB_MODE, Configuration.OrientDbMode.MEMORY);
        Assembler.build(config, new TutorialTelaModule()).start();
    }

}
