package ph.edu.dlsu;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static javafx.scene.paint.Color.FIREBRICK;

public class Main extends Application{

    private static double displayWidth;
    private static double displayHeight;

    static Boolean updated = false;
    static Boolean internet = false;

    static Stage stage;
    static Scene homeScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        onUpdate();
        initializeScreenSize();
        homeScene = new Scene(createHomeContent());
        stage = primaryStage;
        stage.setTitle("TraVIS: Traffic Information System");
        stage.setScene(homeScene);
        stage.setFullScreen(true);
//        stage.setFullScreenExitHint("");
        stage.show();

    }

    private void initializeScreenSize() {
        ph.edu.dlsu.ScreenSize screen = new ph.edu.dlsu.ScreenSize();
        displayWidth = screen.getDisplayWidth();
        displayHeight = screen.getDisplayHeight();
    }

    private Parent createHomeContent(){

        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ph.edu.dlsu.MenuHBox menuBox;

        ImageView imgBackground = ph.edu.dlsu.Utils.loadImage2View("res/TraVIS.jpg", displayWidth, displayHeight);
        if(imgBackground != null){
            rootNode.getChildren().add(imgBackground);
        }

        final CustomMenuItem about = new CustomMenuItem("About");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem graph = new CustomMenuItem("graph");
        final CustomMenuItem close = new CustomMenuItem("close");

        about.setOnMouseClicked(event -> {
            onAbout();
        });

        facts.setOnMouseClicked(event -> {
            onFacts();
        });

        graph.setOnMouseClicked(event -> {
            onGraph();
        });

        close.setOnMouseClicked(event ->{
            onExit();
        });

        menuBox = new ph.edu.dlsu.MenuHBox(about, facts, graph, close);
//        menuBox.setTranslateX(725); //Use this if menus are to be located beside the school logo
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button Sbtn = new Button("search");
        HBox ShbBtn = new HBox();
        ShbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        ShbBtn.getChildren().add(Sbtn);
        grid.add(ShbBtn, 3, 1);

        TextField plate = new TextField();
        plate.setPromptText("Enter a plate number");
        grid.add(plate, 1, 1);

        final ComboBox violation = new ComboBox();
        violation.getItems().addAll(
                "Speeding",
                "Swerving",
                "Drunk Driving",
                "Counterflowing",
                "Beating the red light",
                "Color Coding",
                "All Violations"
        );
        violation.setValue("Select a violation");
        grid.add(violation, 2, 1);

        Sbtn.setOnAction(event ->{
            Display display = new Display(plate, violation);
            stage.setTitle("TraVIS: Results");
            stage.setScene(
                    new Scene(display.main(), displayWidth, displayHeight)
            );
            stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
            stage.setFullScreenExitHint("");
        });

        grid.setTranslateX(455);
        grid.setTranslateY(325);

        if (updated == true && internet == true){
            Label updateMessage = new Label();
            updateMessage.setText("Database is up to date!");
            updateMessage.setTranslateX(620);
            updateMessage.setTranslateY(480);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setTextFill(FIREBRICK);
            FadeTransition fader = createFader(updateMessage);

            SequentialTransition fade = new SequentialTransition(
                    updateMessage,
                    fader
            );

            fade.play();
            updated = false;
            internet = false;
            rootNode.getChildren().addAll(grid, menuBox, updateMessage);
        }
        else {
            Label updateMessage = new Label();
            updateMessage.setText("Cannot update the database!\nNo Internet Connection!");
            updateMessage.setTranslateX(620);
            updateMessage.setTranslateY(480);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setTextFill(FIREBRICK);
            FadeTransition fader = createFader(updateMessage);

            SequentialTransition fade = new SequentialTransition(
                    updateMessage,
                    fader
            );

            fade.play();
            rootNode.getChildren().addAll(grid, menuBox, updateMessage);
        }

        return rootNode;
    }

    public static void onHome(){
        stage.setTitle("TraVIS: Traffic Information System");
        stage.setScene(homeScene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    public static void onAbout(){
        ph.edu.dlsu.About about = new ph.edu.dlsu.About();
        stage.setTitle("TraVIS: About");
        stage.setScene(
                new Scene(about.main(), displayWidth, displayHeight)
        );
        stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
        stage.setFullScreenExitHint("");
    }

    public static void onFacts(){
        ph.edu.dlsu.Facts facts = new ph.edu.dlsu.Facts();
        stage.setTitle("TraVIS: Facts");
        stage.setScene(
                new Scene(facts.main(), displayWidth, displayHeight)
        );
        stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
        stage.setFullScreenExitHint("");
    }

    public static void onGraph(){
        ph.edu.dlsu.Graph graph = new ph.edu.dlsu.Graph();
        stage.setTitle("TraVIS: Graph");
        stage.setScene(
                new Scene(graph.main(), displayWidth, displayHeight)
        );
        stage.setFullScreen(true);                                  //Set the stage in fullscreen mode
        stage.setFullScreenExitHint("");
    }

    public static void onUpdate() throws IOException {

        hasInternet();

        if(internet){
            updated = true;
            internet = true;
        }
        else{
            updated = false;
            internet = false;
        }

    }

    public static void hasInternet(){
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            internet = true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            internet = false;
        }
    }

    public static void onExit() {
        Platform.exit();
    }

    private static FadeTransition createFader(Node node){
        FadeTransition fade = new FadeTransition(Duration.seconds(10), node);
        fade.setFromValue(100);
        fade.setToValue(0);

        return fade;
    }

    public static void main(String[] args) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

}

