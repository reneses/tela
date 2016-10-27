package io.reneses.tela.modules.twitter;

import io.reneses.tela.modules.TelaModule;
import io.reneses.tela.core.databases.extensions.DatabaseExtension;
import io.reneses.tela.modules.twitter.actions.UserActions;
import io.reneses.tela.modules.twitter.database.extensions.TwitterOrientDatabaseExtension;
import java.util.Arrays;

/**
 * Tela Module for Twitter
 */
public class TwitterTelaModule extends TelaModule {

    /** Constant <code>NAME="twitter"</code> */
    public static final String NAME = "twitter";

    /**
     * Constructor for TwitterTelaModule.
     */
    public TwitterTelaModule() {
        super(NAME);
        Iterable<DatabaseExtension> extensions = Arrays.asList(new TwitterOrientDatabaseExtension());
        setExtensions(extensions);
        Iterable<Package> packages = Arrays.asList(UserActions.class.getPackage());
        setActionPackages(packages);
    }

}
