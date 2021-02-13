package kynake.discord.bot.audio;

// JDA
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;

public class AudioRecorder implements AudioReceiveHandler {
  // Receiving
  @Override
  public boolean canReceiveUser() {
    return true;
  }

  @Override
  public void handleUserAudio(UserAudio userAudio) {

  }
}
