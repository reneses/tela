package io.reneses.tela.core.api.server;

import io.reneses.tela.core.api.controllers.GeneralController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.net.BindException;
import java.net.SocketException;
import java.util.EnumSet;

class JettyTelaServer extends AbstractTelaServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private Server server;

    /**
     * Constructor admitting a port
     *
     * @param serverPort Port to use
     */
    JettyTelaServer(int serverPort) {
        super(serverPort);
        init();
    }

    /**
     * Configure and instantiate the server
     */
    private void init() {

        // Register resources
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(GeneralController.class.getPackage().getName());    // Core controllers
        resourceConfig.register(JacksonFeature.class);                              // Json utility
        resourceConfig.register(NotFoundExceptionMapper.class);                     // Custom 404 mapper

        // CORS API Filter
        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,OPTIONS,DELETE");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        holder.setName("cross-origin");

        // Set context
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        context.addFilter(holder, "*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));

        // Instantiate the server
        server = new Server(getPort());
        server.setHandler(context);
        server.setStopAtShutdown(true);

    }

    /** {@inheritDoc} */
    @Override
    public JettyTelaServer start() {
        try {
            LOGGER.info("[Tela] Starting server at port {}", getPort());
            server.start();
            LOGGER.info("[Tela] Server started at 127.0.0.1:{} / {}:{}", getPort(), getLocalIp(), getPort());
            System.out.println("\n" +
                    "      ___           ___           ___       ___     \n" +
                    "     /\\  \\         /\\  \\         /\\__\\     /\\  \\    \n" +
                    "     \\:\\  \\       /::\\  \\       /:/  /    /::\\  \\   \n" +
                    "      \\:\\  \\     /:/\\:\\  \\     /:/  /    /:/\\:\\  \\  \n" +
                    "      /::\\  \\   /::\\~\\:\\  \\   /:/  /    /::\\~\\:\\  \\ \n" +
                    "     /:/\\:\\__\\ /:/\\:\\ \\:\\__\\ /:/__/    /:/\\:\\ \\:\\__\\\n" +
                    "    /:/  \\/__/ \\:\\~\\:\\ \\/__/ \\:\\  \\    \\/__\\:\\/:/  /\n" +
                    "   /:/  /       \\:\\ \\:\\__\\    \\:\\  \\        \\::/  / \n" +
                    "   \\/__/         \\:\\ \\/__/     \\:\\  \\       /:/  /  \n" +
                    "                  \\:\\__\\        \\:\\__\\     /:/  /   \n" +
                    "                   \\/__/         \\/__/     \\/__/    \n" +
                    "\n" +
                    "STARTED AT: 127.0.0.1:" + getPort() + " / " + getLocalIp() + ":" + getPort() + "\n");
        } catch (SocketException e) {
            LOGGER.error("[Tela] The port " + getPort() + " is already in use");
            LOGGER.error("[Tela] Exiting...");
            System.exit(-1);
        } catch (InterruptedException e) {
            LOGGER.error("[Tela] The server has been interrupted", e);
            LOGGER.error("[Tela] Exiting...");
            System.exit(-1);
        } catch (Exception e) {
            LOGGER.error("[Tela] Unknown internal server problem", e);
            LOGGER.error("[Tela] Exiting...");
            System.exit(-1);
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public boolean stop() {
        try {
            server.stop();
            return true;
        } catch (Exception e) {
            LOGGER.error("GLOBAL] The server could not be stopped", e);
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        return server.isRunning();
    }

}
