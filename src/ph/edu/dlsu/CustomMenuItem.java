package ph.edu.dlsu;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by ${AenonCunanan} on 21/06/2016.
 */
public class CustomMenuItem extends StackPane {

    public CustomMenuItem(String name){                                         //This class is made to design the menu base

        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#e9e779")),
                new Stop(0.1, Color.web("#f7ca18")),
                new Stop(0.9, Color.web("#f7ca18")),
                new Stop(1, Color.web("#e9e779"))
        );

        Rectangle bg = new Rectangle(245, 40);
        bg.setArcHeight(100);
        bg.setArcWidth(100);
        bg.setWidth(100);
        bg.setHeight(100);
        bg.setFill(Color.web("#f7ca18"));
        bg.setOpacity(0.4);

        Text text = new Text(name);                                             //Font and color of text
        text.setFill(Color.web("#2c3e50"));
        text.setFont(Font.font("Asimov", FontWeight.SEMI_BOLD, 30));

        setAlignment(Pos.CENTER);                                               //Align to center

        getChildren().addAll(bg, text);

        setOnMouseEntered(event -> {
            bg.setFill(gradient);
            text.setFill(Color.LIGHTGOLDENRODYELLOW);
        });

        setOnMouseExited(event -> {
            bg.setFill(Color.web("#f7ca18"));
            text.setFill(Color.web("#2c3e50"));
        });

        setOnMousePressed(event -> {
            bg.setFill(Color.DARKGREEN);
        });

        setOnMouseReleased(event -> {
            bg.setFill(gradient);
        });

    }

}
