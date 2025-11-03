package me.nickhanson.codeforge.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Starts an H2 TCP server so external tools (IntelliJ) can connect to the in-memory DB
 * used by the application. For dev only.
 * Connect with: jdbc:h2:tcp://localhost:<port>/mem:codeforge_dev  (user=sa, blank password)
 * @author Nick Hanson
 */
@WebListener
public class H2TcpServerListener implements ServletContextListener {
    private static final Logger log = LogManager.getLogger(H2TcpServerListener.class);

    private Server tcpServer;

    /**
     * Starts the H2 TCP server.
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String enabledStr = paramOrSysProp(ctx, "h2.tcp.enabled", "H2_TCP_ENABLED", "true");
        boolean enabled = Boolean.parseBoolean(enabledStr);
        if (!enabled) {
            log.info("H2 TCP server disabled (h2.tcp.enabled=false)");
            return;
        }
        String port = paramOrSysProp(ctx, "h2.tcp.port", "H2_TCP_PORT", "9092");
        try {
            tcpServer = Server.createTcpServer("-tcp", "-tcpPort", port, "-tcpDaemon").start();
            log.info("H2 TCP server started on port {}. URL: jdbc:h2:tcp://localhost:{}/mem:codeforge_dev", port, port);
        } catch (Exception e) {
            log.error("Failed to start H2 TCP server: {}", e.toString(), e);
        }
    }

    /**
     * Stops the H2 TCP server.
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (tcpServer != null) {
            try {
                tcpServer.stop();
                log.info("H2 TCP server stopped");
            } catch (Exception e) {
                log.warn("Error stopping H2 TCP server: {}", e.toString());
            }
        }
    }

    /**
     * Retrieves a configuration value from either a servlet context parameter or a system property.
     * @param ctx the servlet context
     * @param ctxParam the name of the servlet context parameter
     * @param sysProp the name of the system property
     * @param defVal the default value if neither is set
     * @return the configuration value
     */
    private static String paramOrSysProp(ServletContext ctx, String ctxParam, String sysProp, String defVal) {
        String v = System.getProperty(sysProp);
        if (v != null && !v.isBlank()) return v;
        v = ctx.getInitParameter(ctxParam);
        if (v != null && !v.isBlank()) return v;
        return defVal;
    }
}

