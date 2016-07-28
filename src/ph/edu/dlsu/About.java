package ph.edu.dlsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import java.util.Set;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class About {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    private static AudioInputStream audio = null;
    private static AudioPlayer player = null;

    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ImageView imgBackground = Utils.loadImage2View("res/TraVIS.jpg", displayWidth, displayHeight);
        if (imgBackground != null) {
            rootNode.getChildren().add(imgBackground);
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        player = new AudioPlayer();
        player.cancel();

        Text message = new Text();
//        message.setText("Number of people having cars are increasing nowadays, making the roads more busy." +
//                "\nBusy road can cause drivers to commit traffic violation whether it is intentional or not." +
//                "\nTo help people review the violations that have been committed, the traffic violation" +
//                "\ninformation system or TraVIS, can be used. TraVIS is a system that interacts with a" +
//                "\nuser using a graphical user interface (GUI) to help them monitor the violations that" +
//                "\nhave been committed in a particular day. TraVIS can show the user what type of vehicle" +
//                "\ncommitted the violation, plate number, color of the vehicle, and the date and time of" +
//                "\nwhen it was committed." +
//                "\n\nThis program was made by a Computer Engineering students of De La Salle University, " +
//                "\nScience and Technology Complex that serves as a partial completion of the course, System" +
//                "\nAnalysis and Design.");
        message.setText("Hi! I'm Travis! Your Traffic Violation Information System!" +
                "\nI can help you monitor or review the traffic violations that have been committed." +
                "\nI can show you the information like the plate number of the vehicle, color of the vehicle," +
                "\nclass or type of the vehicle, the date of when the violation was committed, the place or" +
                "\nlocation of where the violation was committed, and the corresponding penalty given to the" +
                "\nviolator. I can show those information through a table. I can also show the number of violations" +
                "\ncommitted in a particular month, week, or day, or even all of the violations committed from the" +
                "\nstart, through the use of a graph." +
                "\n\nI was developed by a Computer Engineering students of De La Salle University, " +
                "\nScience and Technology Complex that serves as their partial completion of their course, System" +
                "\nAnalysis and Design.");
        message.setTextAlignment(TextAlignment.CENTER);
        grid.add(message, 0, 1);

        Button speakBtn = new Button("speak");
        HBox speakHBox = new HBox();
        speakHBox.setAlignment(Pos.BOTTOM_RIGHT);
        speakHBox.getChildren().add(speakBtn);
        grid.add(speakHBox, 0, 2);

        grid.setTranslateX(420);
        grid.setTranslateY(325);

        speakBtn.setOnAction(event -> {
            say(message.getText());
        });

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem graph = new CustomMenuItem("graph");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            if (player.isAlive()){
                player.cancel();
            }
            Main.onHome();
        });

        facts.setOnMouseClicked(event -> {
            if (player.isAlive()){
                player.cancel();
            }
            Main.onFacts();
        });

        graph.setOnMouseClicked(event -> {
            if (player.isAlive()){
                player.cancel();
            }
            Main.onGraph();
        });

        close.setOnMouseClicked(event ->{
            if (player.isAlive()){
                player.cancel();
            }
            Main.onExit();
        });

        menuBox = new MenuHBox(home, facts, graph, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, grid);

        return rootNode;
    }

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static boolean inited = false;
    private static MaryInterface marytts = null;

    private static void init() throws MaryConfigurationException {
        marytts = new LocalMaryInterface();
        Set<String> voices = marytts.getAvailableVoices();
        marytts.setVoice(voices.iterator().next());

//        Set<String> voices = marytts.getAvailableVoices();
//        for(String v : voices){
//            System.out.println("Voice available: " + v);
//        }
//
//        marytts.setVoice("bits1-hsmm");
//        System.out.println("\nVOICE: " + voices.iterator().next() + "\n");

        inited = true;
    }

    public static void say(String comment) {

        try {
            if (!inited) {
                init();
            }

            if (comment.isEmpty()) {
                return;
            }

            audio = marytts.generateAudio(comment);
            player = new AudioPlayer(audio);

//            AudioInputStream audio = marytts.generateAudio(comment);
//            AudioPlayer player = new AudioPlayer(audio);
            player.start();
        } catch (MaryConfigurationException |
                SynthesisException e) {
            logger.error("Error synthesizing text to voice", e);
        }
    }

}
