package kynake.discord.bot.commands;

// JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

// Java
import java.util.Arrays;

public class Info implements Command {
  private String commandString = "info";
  
  public String getCommandString() {
    return commandString;
  }

  public String register() {
    System.out.println("Info registered");
    return getCommandString();
  }

  public boolean execute(GuildMessageReceivedEvent event, String[] args) {
    System.out.println("Info executed");
    System.out.println(Arrays.toString(args));

    event.getChannel().sendMessage("Info command received and executed").queue();
    return true;
  }
}
