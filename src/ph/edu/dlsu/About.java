package ph.edu.dlsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class About {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

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

        Text message = new Text();
        message.setText("Number of people having cars are increasing nowadays, making the roads more busy." +
                "\nBusy road can cause drivers to commit traffic violation whether it is intentional or not." +
                "\nTo help people review the violations that have been committed, the traffic violation" +
                "\ninformation system or TraVIS, can be used. TraVIS is a system that interacts with a" +
                "\nuser using a graphical user interface (GUI) to help them monitor the violations that" +
                "\nhave been committed in a particular day. TraVIS can show the user what type of vehicle" +
                "\ncommitted the violation, plate number, color of the vehicle, and the date and time of" +
                "\nwhen it was committed." +
                "\n\nThis program was made by a Computer Engineering students of De La Salle University, " +
                "\nScience and Technology Complex that serves as a partial completion of the course, System" +
                "\nAnalysis and Design.");
        message.setTextAlignment(TextAlignment.CENTER);
        grid.add(message, 0, 1);

        grid.setTranslateX(420);
        grid.setTranslateY(325);

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem update = new CustomMenuItem("update");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        facts.setOnMouseClicked(event -> {
            Main.onFacts();
        });

        update.setOnMouseClicked(event -> {
            try {
                Main.onUpdate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, facts, update, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX(485);
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, grid);

        return rootNode;
    }

}
