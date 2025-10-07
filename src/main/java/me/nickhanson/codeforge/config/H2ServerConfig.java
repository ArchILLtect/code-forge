package me.nickhanson.codeforge.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.h2.tools.Server;

import java.sql.SQLException;

@Configuration
@ConditionalOnProperty(name = "app.h2.tcp.enabled", havingValue = "true")
@ConditionalOnClass(Server.class)
public class H2ServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        // Expose in-memory DB to external tools (IntelliJ) via TCP
        // Connect with: jdbc:h2:tcp://localhost:9092/mem:codeforge_dev
        return Server.createTcpServer("-tcp", "-tcpPort", "9092");
    }
}
