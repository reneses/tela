package io.reneses.tela.modules.instagram;

import io.reneses.tela.modules.TelaModule;
import io.reneses.tela.core.databases.extensions.DatabaseExtension;
import io.reneses.tela.modules.instagram.actions.UserActions;
import io.reneses.tela.modules.instagram.databases.extensions.InstagramOrientDatabaseExtension;

import java.util.Arrays;

/**
 * Instagram module
 */
public class InstagramTelaModule extends TelaModule {

    /** Constant <code>NAME="instagram"</code> */
    public static final String NAME = "instagram";

    /**
     * Constructor for InstagramTelaModule.
     */
    public InstagramTelaModule() {
        super(NAME);
        Iterable<DatabaseExtension> extensions = Arrays.asList(new InstagramOrientDatabaseExtension());
        setExtensions(extensions);
        Iterable<Package> packages = Arrays.asList(UserActions.class.getPackage());
        setActionPackages(packages);
    }

}
