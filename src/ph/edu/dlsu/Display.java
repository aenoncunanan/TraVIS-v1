package ph.edu.dlsu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

    int sheetNumber = 0;

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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text vTitle = new Text();
        vTitle.setText("VIOLATION");
        vTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(vTitle, 0, 0);

        Text nTitle = new Text();
        nTitle.setText("PLATE NUMBER");
        nTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(nTitle, 1, 0);

        Text vcTitle = new Text();
        vcTitle.setText("VEHICLE CLASS");
        vcTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(vcTitle, 2, 0);

        Text cTitle = new Text();
        cTitle.setText("VEHICLE COLOR");
        cTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(cTitle, 3, 0);

        Text dTitle = new Text();
        dTitle.setText("DATE VIOLATED");
        dTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(dTitle, 4, 0);

        Text tTitle = new Text();
        tTitle.setText("TIME VIOLATED");
        tTitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(tTitle, 5, 0);

        switch (inputCombination){
            case "00": // no violation, no plate number
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
                            Text violation = new Text();
                            violation.setText(trafficViolation);
                            violation.setTextAlignment(TextAlignment.CENTER);
                            grid.add(violation, 0, 1);

                            Text number = new Text();
                            number.setText(cell.getStringCellValue());
                            number.setTextAlignment(TextAlignment.CENTER);
                            grid.add(number, 1, 1);

                            cell = cellIterator.next();

                            Text vehicle = new Text();
                            vehicle.setText(cell.getStringCellValue());
                            vehicle.setTextAlignment(TextAlignment.CENTER);
                            grid.add(vehicle, 2, 1);

                            cell = cellIterator.next();

                            Text color = new Text();
                            color.setText(cell.getStringCellValue());
                            color.setTextAlignment(TextAlignment.CENTER);
                            grid.add(color, 3, 1);

                            cell = cellIterator.next();

                            Text date = new Text();
                            date.setText(cell.getStringCellValue());
                            date.setTextAlignment(TextAlignment.CENTER);
                            grid.add(date, 4, 1);

                            cell = cellIterator.next();

                            Text time = new Text();
                            time.setText(cell.getStringCellValue());
                            time.setTextAlignment(TextAlignment.CENTER);
                            grid.add(time, 5, 1);
                        }

                        found = true;
                    }
                }
                if (!found){
                    System.out.println("Plate number does not found in the list of " + trafficViolation + " violators.");
                }
                break;
        }

        grid.setTranslateX(420);
        grid.setTranslateY(325);

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

        rootNode.getChildren().addAll(menuBox, grid);

        return rootNode;
    }

}
