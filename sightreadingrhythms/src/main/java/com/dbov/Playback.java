package com.dbov;

import javax.sound.midi.*;
import static javax.sound.midi.ShortMessage.*;

import java.util.List;


public class Playback {
    private Sequencer sequencer;
    public Playback(){
        try {
        sequencer = MidiSystem.getSequencer();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    public void play(List<RhythmValue> rhythms){
        try {
            
            sequencer.open();
            Sequence seq = new Sequence(Sequence.PPQ, 4);
            Track track = seq.createTrack();
            int currentTick = 1;
            float totalBeats = 0;

            for(RhythmValue value : rhythms) {
                totalBeats += value.getLength();
                if(!value.isRest()){
                    track.add(makeEvent(NOTE_ON, 1, 44,100,currentTick));
                    currentTick += (int) (value.getLength()*4);
                    track.add(makeEvent(NOTE_OFF, 1, 44, 100, currentTick));
                } else {
                    currentTick += (int) (value.getLength()*4);
                }
            }

            //reset currentTick to add the click track
            currentTick = 1;
            track.add(makeEvent(PROGRAM_CHANGE, 2, 115, 0, 0));
            //adding woodblock ticks
            while(currentTick < (totalBeats*4)+1) {
                track.add(makeEvent(NOTE_ON, 2, 60, 100, currentTick));
                track.add(makeEvent(NOTE_OFF, 2, 60, 100, currentTick+1));
                currentTick +=4;
            }


            sequencer.setSequence(seq);
            sequencer.setTempoInBPM(120);
            sequencer.start();


        

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private static MidiEvent makeEvent(int cmd, int chnl, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(cmd, chnl, one, two);
            event = new MidiEvent(msg, tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}


