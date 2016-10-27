package io.reneses.tela.modules.instagram.actions;


import io.reneses.tela.modules.instagram.api.AbstractInstagramApiWrapper;
import io.reneses.tela.modules.instagram.api.InstagramApiWrapperFactory;

/**
 * Common functionality for Instagram actions
 */
abstract class InstagramActions {

    protected AbstractInstagramApiWrapper api;

    /**
     * Constructor with DI
     *
     * @param api API Wrapper
     */
    InstagramActions(AbstractInstagramApiWrapper api) {
        this.api = api;
    }

    /**
     * Default constructor, required by the action dispatcher
     */
    public InstagramActions() {
        this(InstagramApiWrapperFactory.create());
    }

}
