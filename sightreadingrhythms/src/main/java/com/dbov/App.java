package com.dbov;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene mainScene;
    private static int WIDTH = 1200;
    private static int HEIGHT = 600;
    private static Canvas canvas = new Canvas(WIDTH, 400);
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    private static ArrayList<RhythmValue> rhythms = new ArrayList<>();
    private static ArrayList<Image> rhythmImages = new ArrayList<>();
    private static final Image CROTCHET;
    private static final Image QUAVER;
    private static final Image QUAVER_REST;
    private static final Image MINIM;
    private static final Image MINIM_REST;
    private static final Image DOTTED_MINIM;
    private static final Image SEMIBREVE;
    private static final Image CROTCHET_REST;
    private static final Image FOUR_FOUR_TIME_SIGNATURE;
    private static final Image THREE_FOUR_TIME_SIGNATURE;
    private static final Image BARLINE;
    private static final Image DOUBLE_BARLINE;
    private static final Image TWO_QUAVERS;
    private static Image currentTimeSignatureImage;
    private static Playback playback = new Playback();

    static {
        ClassLoader classLoader = App.class.getClassLoader();
        CROTCHET = new Image(classLoader.getResource("newcrotchet.png").toExternalForm());
        QUAVER = new Image(classLoader.getResource("newquaver.png").toExternalForm());
        QUAVER_REST = new Image(classLoader.getResource("newquaverrest.png").toExternalForm());
        MINIM = new Image(classLoader.getResource("newminim.png").toExternalForm());
        MINIM_REST = new Image(classLoader.getResource("newminimrest.png").toExternalForm());
        DOTTED_MINIM = new Image(classLoader.getResource("newdottedminim.png").toExternalForm());
        SEMIBREVE = new Image(classLoader.getResource("newsemibreve.png").toExternalForm());
        CROTCHET_REST = new Image(classLoader.getResource("newcrotchetrest.png").toExternalForm());
        FOUR_FOUR_TIME_SIGNATURE = new Image(classLoader.getResource("new44timesig.png").toExternalForm());
        THREE_FOUR_TIME_SIGNATURE = new Image(classLoader.getResource("new34Timesig.png").toExternalForm());
        BARLINE = new Image(classLoader.getResource("newbarline.png").toExternalForm());
        DOUBLE_BARLINE = new Image(classLoader.getResource("newdoublebarline.png").toExternalForm());
        TWO_QUAVERS = new Image(classLoader.getResource("newtwoquavers.png").toExternalForm());
    }

    private static float timeSignature;
    private static float bars = 2;

    private RhythmGenerator generator = new RhythmGenerator();

    @Override
    public void start(Stage stage) throws IOException {

        Button newRhythmButton = new Button("New Rhythm");
        Button playbackButton = new Button("Play Rhythm");
        Button menuButton = new Button("Menu");

        StackPane buttonsPane = new StackPane(canvas, newRhythmButton, menuButton, playbackButton);
        buttonsPane.setStyle("-fx-background-color: white;");

        playbackButton.setDisable(true);
        
        StackPane.setAlignment(menuButton, Pos.BOTTOM_LEFT);      
        StackPane.setAlignment(newRhythmButton, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(playbackButton, Pos.BOTTOM_RIGHT);

        StackPane.setMargin(menuButton, new Insets(0,0,40,60));
        StackPane.setMargin(playbackButton, new Insets(0,60,40,0));
        StackPane.setMargin(newRhythmButton, new Insets(0,0,40,0));

        Button backButton = new Button("Back");
        ToggleButton crotchetButton = new ToggleButton("Crotchets");
        ToggleButton quaverButton = new ToggleButton("Quavers");
        ToggleButton minimButton = new ToggleButton("Minims");
        ToggleButton semibreveButton = new ToggleButton("Semibreves");
        ToggleButton quaverRestButton = new ToggleButton("Quaver Rests");
        ToggleButton crotchetRestButton = new ToggleButton("Crotchet Rests");
        ToggleButton minimRestButton = new ToggleButton("Minim Rests");
        ToggleButton dottedMinimButton = new ToggleButton("Dotted Minims");

        quaverRestButton.setSelected(true);
        quaverButton.setSelected(true);
        crotchetRestButton.setSelected(true);
        crotchetButton.setSelected(true);
        minimRestButton.setSelected(true);
        minimButton.setSelected(true);
        semibreveButton.setSelected(true);
        dottedMinimButton.setSelected(true);

        GridPane menuGridPane = new GridPane();
        menuGridPane.add(backButton, 0,0);
        menuGridPane.add(quaverButton, 0,1);
        menuGridPane.add(crotchetButton, 1, 1);
        menuGridPane.add(minimButton, 2, 1);
        menuGridPane.add(dottedMinimButton, 3, 1);
        menuGridPane.add(semibreveButton, 4, 1);
        menuGridPane.add(quaverRestButton, 0, 2);
        menuGridPane.add(crotchetRestButton, 1, 2);
        menuGridPane.add(minimRestButton, 2, 2);
        menuGridPane.setHgap(10);
        menuGridPane.setVgap(10);

        ToggleButton fourFourTime = new ToggleButton("4/4");
        ToggleButton threeFourTime = new ToggleButton("3/4");
        ToggleGroup timeSignatureGroup = new ToggleGroup();
        fourFourTime.setToggleGroup(timeSignatureGroup);
        threeFourTime.setToggleGroup(timeSignatureGroup);
        timeSignatureGroup.selectToggle(fourFourTime);

        menuGridPane.add(threeFourTime, 0, 3);
        menuGridPane.add(fourFourTime, 1, 3);
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.drawImage(FOUR_FOUR_TIME_SIGNATURE, 10, 150);


        backButton.setOnAction(e -> {
            buttonsPane.getChildren().remove(menuGridPane); 
            buttonsPane.getChildren().add(canvas);
            buttonsPane.getChildren().add(newRhythmButton);
            buttonsPane.getChildren().add(menuButton);
            buttonsPane.getChildren().add(playbackButton);
        });

        newRhythmButton.setOnAction(e -> {
            if(playbackButton.isDisabled()) {
                playbackButton.setDisable(false);
            }
            if(fourFourTime.isSelected()){
                timeSignature = 4;
            } else if(threeFourTime.isSelected()) {
                timeSignature = 3;
                
            }
            clearRhythm();
            generateRhythmAndImages(quaverRestButton.isSelected(),quaverButton.isSelected(),crotchetRestButton.isSelected()
            ,crotchetButton.isSelected(),minimRestButton.isSelected(),minimButton.isSelected(),dottedMinimButton.isSelected()
            ,semibreveButton.isSelected());
            displayRhythm();
        });

        playbackButton.setOnAction(e -> {
            playback.play(rhythms);
        });

        fourFourTime.setOnAction(e -> {
            semibreveButton.setDisable(false);
        });

        threeFourTime.setOnAction(e -> {
            semibreveButton.setSelected(false);
            semibreveButton.setDisable(true);
        });

        menuButton.setOnAction(e-> {
            buttonsPane.getChildren().add(menuGridPane);
            buttonsPane.getChildren().remove(canvas);
            buttonsPane.getChildren().remove(newRhythmButton);
            buttonsPane.getChildren().remove(menuButton);
            buttonsPane.getChildren().remove(playbackButton);
        });
     
        mainScene = new Scene(buttonsPane, WIDTH, HEIGHT);
        mainScene.setFill(Color.WHITE);
        stage.setTitle("Rhythm Generator");
        stage.setScene(mainScene);
        stage.show();
    }

    private void clearRhythm() {
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            rhythmImages.clear();
            rhythms.clear();
            currentTimeSignatureImage = timeSignature==4 ? FOUR_FOUR_TIME_SIGNATURE : THREE_FOUR_TIME_SIGNATURE;
            gc.drawImage(currentTimeSignatureImage, 10, 150);
    }

    private void generateRhythmAndImages(boolean quaverRests, boolean quavers, 
    boolean crotchetRests, boolean crotchets, boolean minimRests, boolean minims, boolean dottedMinims, boolean semibreves) { //this list must correspond to RhythmValue enum's values array
        boolean[] included = {quaverRests, quavers, crotchetRests, crotchets, minimRests, minims, dottedMinims, semibreves};
        rhythms = generator.getRhythm(timeSignature, bars, included);
        float timeRemainingInBar = timeSignature;

        for(int i = 0; i<rhythms.size(); i++) {
            if(timeRemainingInBar >= 1 && timeRemainingInBar%1==0 
            && rhythms.get(i).equals(RhythmValue.QUAVER) && rhythms.get(i+1).equals(RhythmValue.QUAVER)){
                rhythmImages.add(TWO_QUAVERS);
                timeRemainingInBar -= 0.5f;
                i++;
            } else {
            rhythmImages.add(rhythmValueToImage(rhythms.get(i)));
            }
            timeRemainingInBar -= rhythms.get(i).getLength();
            if(timeRemainingInBar == 0 && i != rhythms.size()-1){
                rhythmImages.add(BARLINE);
                timeRemainingInBar = timeSignature;
            }
        }

        rhythmImages.add(DOUBLE_BARLINE);     
    }

    private void displayRhythm() {
        double xOfNextImage = 10 + currentTimeSignatureImage.getWidth();
        for(Image image : rhythmImages) {
            gc.drawImage(image, xOfNextImage, 150);
            xOfNextImage += image.getWidth();
        }
    }

    private Image rhythmValueToImage(RhythmValue value) {
        switch (value) {
            case QUAVER_REST:
                return QUAVER_REST;               
            case QUAVER:
                return QUAVER;
            case CROTCHET_REST:
                return CROTCHET_REST;
            case CROTCHET:
                return CROTCHET;
            case MINIM:
                return MINIM;
            case MINIM_REST:
                return MINIM_REST;
            case DOTTED_MINIM:
                return DOTTED_MINIM;
            case SEMIBREVE:
                return SEMIBREVE;
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        launch();
        
    }

}