package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.apache.commons.lang.WordUtils;
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

    final ObservableList<Item> data =
            FXCollections.observableArrayList();

    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ImageView imgBackground = Utils.loadImage2View("res/TraVIS_Others.jpg", displayWidth, displayHeight);
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

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);

        bc.setTitle("Traffic Violation Summary");
        bc.setLegendVisible(false);
        xAxis.setLabel("Violation");
        yAxis.setLabel("Number");

        XYChart.Series series = new XYChart.Series();

        for (int sheetCount = 0; sheetCount < myWorkBook.getNumberOfSheets(); sheetCount++){
            int count = 0;
            XSSFSheet mySheet110 = myWorkBook.getSheetAt(sheetCount);

            Iterator<Row> rowIterator110 = mySheet110.iterator();

            //Traverse each row
            rowIterator110.next();
            while (rowIterator110.hasNext()) {
                Row row = rowIterator110.next();
                count++;
            }

            data.add(new Item(myWorkBook.getSheetName(sheetCount)));
            series.getData().add(new XYChart.Data(WordUtils.capitalizeFully(myWorkBook.getSheetName(sheetCount)),count));
        }

        bc.getData().addAll(series);
        bc.setTranslateX((displayWidth/2) - ((displayWidth/2)/2));
//        bc.setTranslateY((displayHeight/4) + (150));
        bc.setTranslateY((displayHeight/7) + (150));
//        bc.setPrefSize(displayWidth/2, displayHeight/2.6);
        bc.setPrefSize(displayWidth/2, displayHeight-390);

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

    public static class Item {
        private final SimpleStringProperty violation;

        private Item (String violation){
            this.violation = new SimpleStringProperty(violation);
        }

        public String getViolation(){
            return violation.get();
        }

        public void setViolation(String vio){
            violation.set(vio);
        }

    }

}
