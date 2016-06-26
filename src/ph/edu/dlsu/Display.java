package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by ${AenonCunanan} on 26/06/2016.
 */

public class Display {

    MenuHBox menuBox;
    ScreenSize screen = new ScreenSize();
    double displayWidth = screen.getDisplayWidth();
    double displayHeight = screen.getDisplayHeight();

    private String inputCombination = "";

    String plateNumber = "";
    String trafficViolation = "";
    String vehicleClass = "";
    String vehicleColor = "";
    String date = "";
    String time = "";

    int sheetNumber = 0;

    final TableView table = new TableView();

    final ObservableList<Item> data =
            FXCollections.observableArrayList();

    public Display(TextField plate, ComboBox violation) {

        //no violation, no number
        if ((violation.getValue() == "Select a violation") && (plate.getText().isEmpty())){
            inputCombination = "00";
        }

        //no violation, with number
        else if ((violation.getValue() == "Select a violation") && (!plate.getText().isEmpty())){
            inputCombination = "01";
            plateNumber = plate.getText();
        }

        //with violation, no number
        else if ((violation.getValue() != "Select a violation") && (plate.getText().isEmpty())){
            inputCombination = "10";
            trafficViolation = (String) violation.getValue();
        }

        //with violation, with number
        else {
            inputCombination = "11";
            plateNumber = plate.getText();
            trafficViolation = (String) violation.getValue();
        }

    }

    public Parent main(){
        Pane rootNode = new Pane();
        rootNode.setPrefSize(displayWidth, displayHeight);

        ImageView imgBackground = Utils.loadImage2View("res/TraVIS.jpg", displayWidth, displayHeight);
        if (imgBackground != null) {
            rootNode.getChildren().add(imgBackground);
        }

        switch (inputCombination){
            case "00": // no violation, no plate number
//                Text violation00 = new Text();
//                violation00.setText("---");
//                violation00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(violation00, 0, 1);
//
//                Text number00 = new Text();
//                number00.setText("---");
//                number00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(number00, 1, 1);
//
//                Text vehicle00 = new Text();
//                vehicle00.setText("---");
//                vehicle00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(vehicle00, 2, 1);
//
//                Text color00 = new Text();
//                color00.setText("---");
//                color00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(color00, 3, 1);
//
//                Text date00 = new Text();
//                date00.setText("---");
//                date00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(date00, 4, 1);
//
//                Text time00 = new Text();
//                time00.setText("---");
//                time00.setTextAlignment(TextAlignment.CENTER);
//                grid.add(time00, 5, 1);
                break;
            case "01": // no violation, with plate number
                break;
            case "10": // with violation, no plate number
                break;
            case "11": // with violation, with plate number
                Boolean found = false;

                if (trafficViolation == "Speeding") {
                    sheetNumber = 0;
                }
                else if (trafficViolation == "Swerving"){
                    sheetNumber = 1;
                }
                else if (trafficViolation == "Drunk Driving"){
                    sheetNumber = 2;
                }
                else if (trafficViolation == "Counterflowing"){
                    sheetNumber = 3;
                }
                else if (trafficViolation == "Beating the red light"){
                    sheetNumber = 4;
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

                XSSFSheet mySheet = myWorkBook.getSheetAt(sheetNumber);

                Iterator<Row> rowIterator = mySheet.iterator();
                //Traverse each row
                while (rowIterator.hasNext() && !found){
                    Row row = rowIterator.next();

                    //For each row, traverse each column
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell = cellIterator.next();
                    if (cell.getStringCellValue().equals(plateNumber)){
                        while (cellIterator.hasNext()) {
                            cell = cellIterator.next();
                            vehicleClass = cell.getStringCellValue();
                            cell = cellIterator.next();
                            vehicleColor = cell.getStringCellValue();
                            cell = cellIterator.next();
                            date = cell.getStringCellValue();
                            cell = cellIterator.next();
                            time = cell.getStringCellValue();
                        }

                        data.add(new Item(trafficViolation, vehicleClass, plateNumber, vehicleColor, date, time));
                        found = true;
                    }
                }
                if (!found){
                    System.out.println("Plate number does not found in the list of " + trafficViolation + " violators.");
                }
                break;
        }

        final CustomMenuItem home = new CustomMenuItem("home");
        final CustomMenuItem facts = new CustomMenuItem("facts");
        final CustomMenuItem about = new CustomMenuItem("about");
        final CustomMenuItem close = new CustomMenuItem("close");

        home.setOnMouseClicked(event -> {
            Main.onHome();
        });

        facts.setOnMouseClicked(event -> {
            Main.onFacts();
        });

        about.setOnMouseClicked(event -> {
            Main.onAbout();
        });

        close.setOnMouseClicked(event ->{
            Main.onExit();
        });

        menuBox = new MenuHBox(home, facts, about, close);
//        menuBox.setTranslateX(725);
        menuBox.setTranslateX(485);
        menuBox.setTranslateY(630);

        TableColumn vio = new TableColumn("VIOLATION");
        vio.setCellValueFactory(new PropertyValueFactory<>("violation"));
        vio.setPrefWidth(150);

        TableColumn num = new TableColumn("PLATE NUMBER");
        num.setCellValueFactory(new PropertyValueFactory<>("number"));
        num.setPrefWidth(115);

        TableColumn vclass = new TableColumn("VEHICLE CLASS");
        vclass.setCellValueFactory(new PropertyValueFactory<>("vclass"));
        vclass.setPrefWidth(110);

        TableColumn color = new TableColumn("VEHICLE COLOR");
        color.setCellValueFactory(new PropertyValueFactory<>("color"));
        color.setPrefWidth(100);

        TableColumn date = new TableColumn("DATE VIOLATED");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setPrefWidth(107);

        TableColumn time = new TableColumn("TIME VIOLATED");
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        time.setPrefWidth(100);


        table.getColumns().addAll(vio, num, vclass, color, date, time);
        table.setItems(data);
        table.setPrefSize(displayWidth/2, displayHeight/3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        vbox.setTranslateX((displayWidth/2)-350);
        vbox.setTranslateY(350);

        rootNode.getChildren().addAll(menuBox, vbox);

        return rootNode;
    }

    public static class Item {

        private final SimpleStringProperty violation;
        private final SimpleStringProperty number;
        private final SimpleStringProperty vclass;
        private final SimpleStringProperty color;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;

        private Item(String violation, String number, String vclass, String color, String date, String time) {
            this.violation = new SimpleStringProperty(violation);
            this.number = new SimpleStringProperty(number);
            this.vclass = new SimpleStringProperty(vclass);
            this.color = new SimpleStringProperty(color);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
        }

        public String getViolation() {
            return violation.get();
        }

        public void setViolation(String vio) {
            violation.set(vio);
        }

        public String getVclass(){
            return vclass.get();
        }

        public void setVclass(String vio){
            vclass.set(vio);
        }

        public String getNumber() {
            return number.get();
        }

        public void setNumber(String vio) {
            number.set(vio);
        }

        public String getColor() {
            return color.get();
        }

        public void setColor(String vio) {
            color.set(vio);
        }

        public String getDate() {
            return date.get();
        }

        public void setDate(String vio) {
            date.set(vio);
        }

        public String getTime() {
            return time.get();
        }

        public void setTime(String vio) {
            time.set(vio);
        }

    }

}
