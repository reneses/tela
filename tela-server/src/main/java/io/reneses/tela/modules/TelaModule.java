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
    private Iterable<Class<?>> actionClasses;
    private Iterable<Package> actionPackages;

    /**
     * Constructor for TelaModule.
     *
     * @param name Module name
     */
    public TelaModule(String name) {
        this.name = name;
        this.extensions = new ArrayList<>(0);
        this.actionClasses = new ArrayList<>(0);
        this.actionPackages = new ArrayList<>(0);
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

    /**
     * Getter for the field <code>actionClasses</code>.
     *
     * @return a {@link java.lang.Iterable} object.
     */
    public Iterable<Class<?>> getActionClasses() {
        return actionClasses;
    }

    /**
     * Setter for the field <code>actionClasses</code>.
     *
     * @param classes a {@link java.lang.Iterable} object.
     */
    protected void setActionClasses(Iterable<Class<?>> classes) {
        this.actionClasses = classes;
    }

    /**
     * Getter for the field <code>actionPackages</code>.
     *
     * @return a {@link java.lang.Iterable} object.
     */
    public Iterable<Package> getActionPackages() {
        return actionPackages;
    }

    /**
     * Setter for the field <code>actionPackages</code>.
     *
     * @param packages a {@link java.lang.Iterable} object.
     */
    protected void setActionPackages(Iterable<Package> packages) {
        this.actionPackages = packages;
    }

}
