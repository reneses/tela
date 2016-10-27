package io.reneses.tela.modules.instagram.repositories;

import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;

/**
 * Common OrientDB repository which retrieves an instance of a OrientGraphWrapper
 */
abstract class AbstractOrientRepository {

    protected final OrientGraphWrapper telaGraph = OrientGraphWrapperFactory.get();

}
