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

        //no violation, with number //all violation, with number
        else if (((violation.getValue() == "Select a violation") || (violation.getValue() == "All Violations")) && (!plate.getText().isEmpty())){
            inputCombination = "01";
            plateNumber = plate.getText();
        }

        //with violation, no number
        else if (((violation.getValue() != "Select a violation") && (violation.getValue() != "All Violations")) && (plate.getText().isEmpty())){
            inputCombination = "10";
            trafficViolation = (String) violation.getValue();
        }

        //all violation, no number
        else if (violation.getValue() == "All Violations" && plate.getText().isEmpty()){
            inputCombination = "110";
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

        Integer sheetCount = 0;

        switch (inputCombination){
            case "00": // no violation, no plate number
                break;
            case "01": // no violation, with plate number // all violation, with number
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

                    XSSFSheet mySheet01 = myWorkBook.getSheetAt(sheetCount);

                    Iterator<Row> rowIterator01 = mySheet01.iterator();
                    //Traverse each row
                    while (rowIterator01.hasNext()) {
                        Row row = rowIterator01.next();

                        //For each row, traverse each column
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Cell cell = cellIterator.next();
                        if (cell.getStringCellValue().equals(plateNumber)) {
                            while (cellIterator.hasNext()) {
                                plateNumber = cell.getStringCellValue();
                                cell = cellIterator.next();
                                vehicleClass = cell.getStringCellValue();
                                cell = cellIterator.next();
                                vehicleColor = cell.getStringCellValue();
                                cell = cellIterator.next();
                                date = cell.getStringCellValue();
                                cell = cellIterator.next();
                                time = cell.getStringCellValue();
                            }
                            data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time));
                        }
                    }
                    sheetCount++;
                }
                break;
            case "10": // with violation, no plate number
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
                else if (trafficViolation == "Color Coding"){
                    sheetNumber = 5;
                }

                XSSFSheet mySheet10 = myWorkBook.getSheetAt(sheetNumber);

                Iterator<Row> rowIterator10 = mySheet10.iterator();
                //Traverse each row
                rowIterator10.next();   //to skip the first row
                while (rowIterator10.hasNext()){
                    Row row = rowIterator10.next();

                    //For each row, traverse each column
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Cell cell = cellIterator.next();
                        while (cellIterator.hasNext()) {
                            plateNumber = cell.getStringCellValue();
                            cell = cellIterator.next();
                            vehicleClass = cell.getStringCellValue();
                            cell = cellIterator.next();
                            vehicleColor = cell.getStringCellValue();
                            cell = cellIterator.next();
                            date = cell.getStringCellValue();
                            cell = cellIterator.next();
                            time = cell.getStringCellValue();
                        }

                        data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time));
                }

                break;
            case "11": // with violation, with plate number

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
                else if (trafficViolation == "Color Coding"){
                    sheetNumber = 5;
                }

                XSSFSheet mySheet11 = myWorkBook.getSheetAt(sheetNumber);

                Iterator<Row> rowIterator11 = mySheet11.iterator();
                //Traverse each row
                while (rowIterator11.hasNext()){
                    Row row = rowIterator11.next();

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

                        data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time));
                    }
                }
                break;
            case "110":// All violations, no number

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
                        //For each row, traverse each column
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Cell cell = cellIterator.next();
                            while (cellIterator.hasNext()) {
                                plateNumber = cell.getStringCellValue();
                                cell = cellIterator.next();
                                vehicleClass = cell.getStringCellValue();
                                cell = cellIterator.next();
                                vehicleColor = cell.getStringCellValue();
                                cell = cellIterator.next();
                                date = cell.getStringCellValue();
                                cell = cellIterator.next();
                                time = cell.getStringCellValue();
                            }
                        data.add(new Item(trafficViolation, plateNumber, vehicleClass, vehicleColor, date, time));
                    }
                    sheetCount++;
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
