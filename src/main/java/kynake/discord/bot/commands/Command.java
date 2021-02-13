package kynake.discord.bot.commands;

// JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface Command {
  public String register();

  public boolean execute(GuildMessageReceivedEvent event, String[] args);

  public String getCommandString();
}
