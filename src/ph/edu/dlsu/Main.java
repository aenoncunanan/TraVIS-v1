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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import static javafx.scene.paint.Color.FIREBRICK;

public class Main extends Application{

    private static double displayWidth;
    private static double displayHeight;

    static Boolean updated = false;

    static Stage stage;
    static Scene homeScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        final CustomMenuItem update = new CustomMenuItem("update");
        final CustomMenuItem close = new CustomMenuItem("close");

        about.setOnMouseClicked(event -> {
            onAbout();
        });

        facts.setOnMouseClicked(event -> {
            onFacts();
        });

        update.setOnMouseClicked(event -> {
            try {
                onUpdate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        close.setOnMouseClicked(event ->{
            onExit();
        });

        menuBox = new ph.edu.dlsu.MenuHBox(about, facts, update, close);
//        menuBox.setTranslateX(725); //Use this if menus are to be located beside the school logo
        menuBox.setTranslateX(485);
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
                "Beating the red light"
        );
        violation.setValue("Select a violation");
        grid.add(violation, 2, 1);

        Sbtn.setOnAction(event ->{
            try {
                onSearchBtn(plate, violation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        grid.setTranslateX(455);
        grid.setTranslateY(325);

        Label updateMessage = new Label();
        if (updated == true){
            updateMessage.setText("Database is up to date!" +
                    "\nData as of: mm/dd/yy");
            updateMessage.setTranslateX(620);
            updateMessage.setTranslateY(500);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setTextFill(FIREBRICK);
            FadeTransition fader = createFader(updateMessage);

            SequentialTransition fade = new SequentialTransition(
                    updateMessage,
                    fader
            );

            fade.play();
            updated = false;
        }

        rootNode.getChildren().addAll(grid, menuBox, updateMessage);

        return rootNode;
    }

    private void onSearchBtn(TextField plate, ComboBox violation) throws IOException {

        //no violation, no number
        if ((violation.getValue() == "Select a violation") && (plate.getText().isEmpty())){
            System.out.println("No violation and plate number is detected");
        }

        //no violation, with number
        else if ((violation.getValue() == "Select a violation") && (!plate.getText().isEmpty())){
            System.out.println("No violation is detected but with plate number");
        }

        //with violation, no number
        else if ((violation.getValue() != "Select a violation") && (plate.getText().isEmpty())){
            System.out.println("Violation is detected but no plate number");
        }

        //with violation, with number
        else {
            System.out.println("Violation and number is detected");

                Boolean found = false;

                int sheetNumber = 0;

                if (violation.getValue() == "Speeding") {
                    sheetNumber = 0;
                }
                else if (violation.getValue() == "Swerving"){
                    sheetNumber = 1;
                }
                else if (violation.getValue() == "Drunk Driving"){
                    sheetNumber = 2;
                }
                else if (violation.getValue() == "Counterflowing"){
                    sheetNumber = 3;
                }
                else if (violation.getValue() == "Beating the red light"){
                    sheetNumber = 4;
                }

                File myFile = new File("dat/Violation-Database.xlsx");

                FileInputStream fis = new FileInputStream(myFile);

                XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

                XSSFSheet mySheet = myWorkBook.getSheetAt(sheetNumber);

                Iterator<Row> rowIterator = mySheet.iterator();
                //Traverse each row
                while (rowIterator.hasNext() && !found){
                    Row row = rowIterator.next();

                    //For each row, traverse each column
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell = cellIterator.next();
                    if (cell.getStringCellValue().equals(plate.getText())){
                        while (cellIterator.hasNext()) {
                            System.out.println("Violation       : " + violation.getValue());
                            System.out.println("Plate Number    : " + cell.getStringCellValue());
                            cell = cellIterator.next();
                            System.out.println("Vehicle Class   : " + cell.getStringCellValue());
                            cell = cellIterator.next();
                            System.out.println("Vehicle Color   : " + cell.getStringCellValue());
                            cell = cellIterator.next();
                            System.out.println("Date Committed  : " + cell.getStringCellValue());
                            cell = cellIterator.next();
                            System.out.println("Time Committed  : " + cell.getStringCellValue());
                        }

                        found = true;
                    }
                }
                if (!found){
                    System.out.println("Plate number does not found in the list of " + violation.getValue() + " violators.");
                }

        }
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

    public static void onUpdate() throws IOException {

        updated = true;
        onHome();

    }

    public static void onExit() {
        Platform.exit();
    }

    private static FadeTransition createFader(Node node){
        FadeTransition fade = new FadeTransition(Duration.seconds(5), node);
        fade.setFromValue(100);
        fade.setToValue(0);

        return fade;
    }

    public static void main(String[] args) {
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

}

