package kynake.discord.bot.commands;

// Internal
import kynake.discord.bot.audio.AudioRecorder;

// JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;

// Java
import java.util.Arrays;


public class Shutdown implements Command {
  private String commandString = "shutdown";

  public String getCommandString() {
    return commandString;
  }

  public String register() {
    System.out.println("Shutdown registered");
    return getCommandString();
  }

  public boolean execute(GuildMessageReceivedEvent event, String[] args) {
    System.out.println("Shutdown executed");
    System.out.println(Arrays.toString(args));

    AudioReceiveHandler recorder = event.getGuild().getAudioManager().getReceivingHandler();
    if(recorder instanceof AudioRecorder) {
      ((AudioRecorder) recorder).writeWAVFile();
    }

    event.getJDA().shutdownNow();
    System.exit(0);
    return true;
  }
}
