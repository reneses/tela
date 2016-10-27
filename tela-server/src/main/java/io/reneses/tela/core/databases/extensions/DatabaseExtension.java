package io.reneses.tela.core.databases.extensions;

/**
 * Database extension
 *
 * Extensions are  used to generate the database model required by the database, so that modules can be easily
 * added without manual updates.
 */
public interface DatabaseExtension<T>  {

    /**
     * Check if the extension has already been initiated
     *
     * @return True if the extension has already been initiated, false otherwise
     * @param database a T object.
     */
    boolean isInitiated(T database);

    /**
     * Init the extension, creating the required schema
     *
     * @param database a T object.
     */
    void init(T database);

}
