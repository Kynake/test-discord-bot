package kynake.discord.bot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;


public class Shutdown implements Command {
  private String commandString = "shutdown";

  public String register() {
    System.out.println("Shutdown registered");
    return getCommandString();
  }

  public boolean execute(GuildMessageReceivedEvent event, String[] args) {
    System.out.println("Shutdown executed");
    System.out.println(Arrays.toString(args));

    event.getJDA().shutdownNow();
    System.exit(0);
    return true;
  }

  public String getCommandString() {
    return commandString;
  }
}
