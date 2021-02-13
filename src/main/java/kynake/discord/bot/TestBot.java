package kynake.discord.bot;

// Internal
import kynake.discord.bot.handlers.CommandHandler;

// JDA
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

// Java
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import javax.security.auth.login.LoginException;

public class TestBot {
    public static JDA jda;

    public String getVersion() {
        return System.getProperty("java.version");
    }

    public static void main(String[] args) throws IOException, LoginException {
        System.out.println("Java version: " + new TestBot().getVersion());

        // Get Token
        String token = new String(Files.readAllBytes(Paths.get("token.txt")));


        // Create API Instance
        jda = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES).build();
        try {
            jda.awaitReady();
        } catch(InterruptedException | IllegalStateException e) {
            System.err.println("Connection to Discord via JDA failed");
            e.printStackTrace();
        }

        // Register API Event Handlers
        jda.addEventListener(new CommandHandler());

    }
}
