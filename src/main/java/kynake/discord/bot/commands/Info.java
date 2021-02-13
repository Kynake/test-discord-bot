package kynake.discord.bot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;


public class Info implements Command {
  private String commandString = "info";

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

  public String getCommandString() {
    return commandString;
  }
}
