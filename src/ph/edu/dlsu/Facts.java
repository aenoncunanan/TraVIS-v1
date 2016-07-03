package ph.edu.dlsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by ${AenonCunanan} on 24/06/2016.
 */
public class Facts {

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
        message.setText("Facts about traffic violations will appear here. Version of database should also appear here.");
        message.setTextAlignment(TextAlignment.CENTER);
        grid.add(message, 0, 1);

        grid.setTranslateX(420);
        grid.setTranslateY(325);

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem graph = new CustomMenuItem("graph");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        about.setOnMouseClicked(event -> {
            Main.onAbout();
        });

        graph.setOnMouseClicked(event -> {
            Main.onGraph();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, about, graph, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, grid);

        return rootNode;
    }

}
