package controllers;

import javax.sound.sampled.Clip;
import java.util.Map;
import javax.sound.sampled.FloatControl;
public class SoundController {

    private Map<String, Clip> soundClips;

    public SoundController(Map<String, Clip> soundClips) {
        this.soundClips = soundClips;

    }

    public void playSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loopSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void setVolume(String name, float volume) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float newVolume = Math.max(min, Math.min(max, volume)); 
            volumeControl.setValue(newVolume);
        }
    }
}
