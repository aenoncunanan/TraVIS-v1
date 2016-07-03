package ph.edu.dlsu;

import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by ${AenonCunanan} on 03/07/2016.
 */
public class Graph {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    String trafficViolation = "";

    int sheetCount = 0;
    int speedCount = 0;
    int swervingCount = 0;
    int drunkCount = 0;
    int counterflowingCount = 0;
    int beatingCount = 0;
    int colorCount = 0;


    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ImageView imgBackground = Utils.loadImage2View("res/TraVIS.jpg", displayWidth, displayHeight);
        if (imgBackground != null) {
            rootNode.getChildren().add(imgBackground);
        }

        File myFile = new File("dat/Violation-Database.xlsx");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XSSFWorkbook myWorkBook = null;
        try {
            myWorkBook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (sheetCount < 6) {
            if (sheetCount == 0){
                trafficViolation = "Speeding";
            }
            else if (sheetCount == 1){
                trafficViolation = "Swerving";
            }
            else if (sheetCount == 2){
                trafficViolation = "Drunk Driving";
            }
            else if (sheetCount == 3){
                trafficViolation = "Counterflowing";
            }
            else if (sheetCount == 4){
                trafficViolation = "Beating the red light";
            }
            else if (sheetCount == 5){
                trafficViolation = "Color Coding";
            }

            XSSFSheet mySheet110 = myWorkBook.getSheetAt(sheetCount);

            Iterator<Row> rowIterator110 = mySheet110.iterator();

            //Traverse each row
            rowIterator110.next();
            while (rowIterator110.hasNext()) {
                Row row = rowIterator110.next();
                if (sheetCount == 0){
                    speedCount++;
                }
                else if (sheetCount == 1){
                    swervingCount++;
                }
                else if (sheetCount == 2){
                    drunkCount++;
                }
                else if (sheetCount == 3){
                    counterflowingCount++;
                }
                else if (sheetCount == 4){
                    beatingCount++;
                }
                else if (sheetCount == 5){
                    colorCount++;
                }
            }
            sheetCount++;
        }

        final String speeding = "Speeding";
        final String swerving = "Swerving";
        final String drunk = "Drunk Driving";
        final String counterflowing = "Counterflowing";
        final String beating = "Beating the red light";
        final String color = "Color Coding";

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);

        bc.setTitle("Traffic Violation Summary");
        bc.setLegendVisible(false);
        xAxis.setLabel("Violation");
        yAxis.setLabel("Number");

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data(speeding, speedCount));
        series.getData().add(new XYChart.Data(swerving, swervingCount));
        series.getData().add(new XYChart.Data(drunk, drunkCount));
        series.getData().add(new XYChart.Data(counterflowing, counterflowingCount));
        series.getData().add(new XYChart.Data(beating, beatingCount));
        series.getData().add(new XYChart.Data(color, colorCount));

        bc.getData().addAll(series);
        bc.setTranslateX((displayWidth/2) - ((displayWidth/2)/2));
        bc.setTranslateY((displayHeight/4) + (150));
        bc.setPrefSize(displayWidth/2, displayHeight/2.6);

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        about.setOnMouseClicked(event -> {
            Main.onAbout();
        });

        facts.setOnMouseClicked(event -> {
            Main.onFacts();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, about, facts, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX((displayWidth/2) - (200));
        menuBox.setTranslateY(630);

        rootNode.getChildren().addAll(menuBox, bc);

        return rootNode;
    }

}
