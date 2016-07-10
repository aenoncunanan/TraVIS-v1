package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

    final TableView table = new TableView();

    final ObservableList<Item> data =
            FXCollections.observableArrayList();

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
        message.setText("Source: http://www.mmda.gov.ph/index.php/20-faq/298-traffic-violations-and-penalties | As of: July 2016");
        message.setFont(Font.font (8));
        message.setTextAlignment(TextAlignment.CENTER);
        grid.add(message, 0, 1);

        grid.setTranslateX((displayHeight/2) + 90);
        grid.setTranslateY((displayHeight/2) + 200);

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

        table.setPrefSize(displayWidth/2, displayHeight/3);

        TableColumn violation = new TableColumn("VIOLATIONS");
        violation.setCellValueFactory(new PropertyValueFactory<>("violation"));
        violation.setPrefWidth((table.getPrefWidth()/2) - 50);
        violation.setSortable(false);

        TableColumn penalty = new TableColumn("PENALTIES");
        penalty.setCellValueFactory(new PropertyValueFactory<>("penalty"));
        penalty.setPrefWidth((table.getPrefWidth()/2) + 50);

        TableColumn first = new TableColumn("First Offense");
        first.setCellValueFactory(new PropertyValueFactory<>("first"));
        first.setPrefWidth(penalty.getPrefWidth()/3);
        first.setSortable(false);

        TableColumn second = new TableColumn("Second Offense");
        second.setCellValueFactory(new PropertyValueFactory<>("second"));
        second.setPrefWidth(penalty.getPrefWidth()/3);
        second.setSortable(false);

        TableColumn third = new TableColumn("Third Offense");
        third.setCellValueFactory(new PropertyValueFactory<>("third"));
        third.setPrefWidth(penalty.getPrefWidth()/3);
        third.setSortable(false);

        penalty.getColumns().addAll(first, second, third);

        final ViolationList list = new ViolationList(data);

        table.getColumns().addAll(violation, penalty);
        table.setItems(data);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        vbox.setTranslateX((displayWidth/2)-350);
        vbox.setTranslateY(350);

        rootNode.getChildren().addAll(menuBox, vbox, grid);

        return rootNode;
    }

    public static class Item{

        private final SimpleStringProperty violation;
        private final SimpleStringProperty first;
        private final SimpleStringProperty second;
        private final SimpleStringProperty third;

        public Item(String violation, String first, String second, String third){
            this.violation = new SimpleStringProperty(violation);
            this.first = new SimpleStringProperty(first);
            this.second = new SimpleStringProperty(second);
            this.third = new SimpleStringProperty(third);
        }

        public String getViolation(){
            return violation.get();
        }

        public void setViolation(String vio){
            violation.set(vio);
        }

//        public String getPenalty(){
//            return penalty.get();
//        }
//
//        public void setPenalty(String vio){
//            penalty.set(vio);
//        }

        public String getFirst(){
            return first.get();
        }

        public void setFirst(String vio){
            first.set(vio);
        }

        public String getSecond(){
            return second.get();
        }

        public void setSecond(String vio){
            second.set(vio);
        }

        public String getThird(){
            return third.get();
        }

        public void setThird(String vio){
            third.set(vio);
        }

    }

}
