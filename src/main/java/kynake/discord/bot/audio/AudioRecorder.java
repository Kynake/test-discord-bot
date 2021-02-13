package kynake.discord.bot.audio;

// JDA
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.User;

// Java
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioRecorder implements AudioReceiveHandler {
  private Queue<byte[]> buffer = new ConcurrentLinkedQueue<>(); // buffer of byte chunks of 20ms of user Audio
  private long requesterID;
  private File outputFile;

  public AudioRecorder(User requester, String outputFileName) {
    requesterID = requester.getIdLong();
    outputFile = new File(outputFileName);
  }

  // Receiving
  @Override
  public boolean canReceiveUser() {
    return true;
  }

  @Override
  public void handleUserAudio(UserAudio userAudio) {
    if(userAudio.getUser().getIdLong() != requesterID) {
      return;
    }

    byte[] data = userAudio.getAudioData(1.0f);
    buffer.add(data);
  }

  public void writeWAVFile() {
    if(buffer.peek() == null) {
      System.out.println("No Audio was recorded");
      return;
    }

    byte[] combinedbuffer = new byte[buffer.peek().length * buffer.size()];
    int i = 0;

    for (byte[] bytes : buffer) {
      for (byte b : bytes) {
        combinedbuffer[i++] = b;
      }
    }

    ByteArrayInputStream byteStream = new ByteArrayInputStream(combinedbuffer);
    AudioInputStream audioStream = new AudioInputStream(byteStream, AudioReceiveHandler.OUTPUT_FORMAT, combinedbuffer.length);

    try {
      AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
