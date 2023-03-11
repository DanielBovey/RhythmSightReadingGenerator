package com.dbov;

enum RhythmValue {
    
    QUAVER_REST(0.5f, true),
    QUAVER(0.5f, false),
    CROTCHET_REST(1, true),
    CROTCHET(1, false),
    MINIM_REST(2, true),
    MINIM(2, false),
    DOTTED_MINIM(3, false),
    SEMIBREVE(4, false);



    private final float length;
    private final boolean isRest;

    private RhythmValue(float length, boolean isRest){
        this.length = length;
        this.isRest = isRest;
    }

    public boolean isRest(){
        return isRest;
    }

    public float getLength() {
        return length;
    }

}
