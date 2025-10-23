package me.nickhanson.codeforge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.h2.tools.Server;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

/**
 * Configuration class to set up an H2 TCP server if enabled in application properties.
 * This allows external tools (like IntelliJ) to connect to the in-memory H2 database.
 */
@Profile("dev")
@Configuration
@ConditionalOnProperty(name = "app.h2.tcp.enabled", havingValue = "true")
@ConditionalOnClass(Server.class)
public class H2ServerConfig {

    /**
     * Creates and starts an H2 TCP server.
     * The server can be accessed via JDBC URL: jdbc:h2:tcp://localhost:9092/mem:codeforge_dev
     *
     * @return the H2 TCP server instance
     * @throws SQLException if there is an error starting the server
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        // Expose in-memory DB to external tools (IntelliJ) via TCP
        // Connect with: jdbc:h2:tcp://localhost:9092/mem:codeforge_dev
        return Server.createTcpServer("-tcp", "-tcpPort", "9092");
    }
}
