package com.dbov;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RhythmGenerator {
    private static ArrayList<RhythmValue> rhythm = new ArrayList<>();
    private static Random random = new Random();

    

    

    public ArrayList<RhythmValue> getRhythm(float timeSignature, float bars, boolean[] rhythmsIncluded) { //give this method parameters which will be the timeSig, number of bars, values to include, set by the buttons on the GUI
        float totalTimeRemaining = timeSignature * bars;
        float timeRemainingInBar = timeSignature;
        RhythmValue nextValue;
        List<RhythmValue> possibleValues = new ArrayList<>();

        
        
        for(int i = 0; i<rhythmsIncluded.length; i++) { //populate the list with the chosen rhythms
            if(rhythmsIncluded[i]) { //the rhythmsIncluded array must correspond to the RhythmValue enum's ordinal values
                possibleValues.add(RhythmValue.values()[i]);
            }
        }

        while(totalTimeRemaining > 0) {
            
            if(timeRemainingInBar == 4) {
                nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                if(nextValue.isRest()){ //if the value generated is a rest, re-reroll to create a bias in favor of notes, not rests
                    nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                }
            } else if (timeRemainingInBar >=3) {
                possibleValues.removeIf(r -> r.getLength()>3);
                nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                if(nextValue.isRest()){
                    nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                }
            

            }else if (timeRemainingInBar >= 2) {
                possibleValues.removeIf(r -> r.getLength()>2);
                                       
                nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                if(nextValue.isRest()){
                    nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                }
            } else if (timeRemainingInBar >= 1) {
                possibleValues.removeIf(r -> r.getLength()>1);
                nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                if(nextValue.isRest()){
                    nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                }
            } else {
                possibleValues.removeIf(r -> r.getLength()>0.5);
                nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                if(nextValue.isRest()){
                    nextValue = possibleValues.get(random.nextInt(possibleValues.size()));
                }
            }

            rhythm.add(nextValue);

            timeRemainingInBar = timeRemainingInBar - nextValue.getLength();
            if(timeRemainingInBar == 0) {
                timeRemainingInBar = timeSignature;
                for(int i = 0; i<rhythmsIncluded.length; i++) { 
                    if(rhythmsIncluded[i] && !(possibleValues.contains(RhythmValue.values()[i]))) { 
                        possibleValues.add(RhythmValue.values()[i]);
                    }
                }
            }


            totalTimeRemaining = totalTimeRemaining - nextValue.getLength();
        }


        return rhythm;

    }

    
}


