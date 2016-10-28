package io.reneses.tela.modules;

import io.reneses.tela.core.databases.extensions.DatabaseExtension;

import java.util.ArrayList;

/**
 * Tela Module
 * A module is a registrable component which adds actions to Tela
 */
public abstract class TelaModule {

    private String name;
    private Iterable<DatabaseExtension> extensions;

    /**
     * Constructor for TelaModule.
     *
     * @param name Module name
     */
    public TelaModule(String name) {
        this.name = name;
        this.extensions = new ArrayList<>(0);
    }

    /**
     * Getter for the field <code>name</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the field <code>name</code>.
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the field <code>extensions</code>.
     *
     * @return a {@link java.lang.Iterable} object.
     */
    public Iterable<DatabaseExtension> getExtensions() {
        return extensions;
    }

    /**
     * Setter for the field <code>extensions</code>.
     *
     * @param extensions a {@link java.lang.Iterable} object.
     */
    protected void setExtensions(Iterable<DatabaseExtension> extensions) {
        this.extensions = extensions;
    }

}
