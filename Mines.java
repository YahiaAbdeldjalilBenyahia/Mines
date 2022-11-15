/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author DELL
 */
public class Mines extends Application {

    public static int buttonSize = 40;
    public static int space = 15;
    public static int width = 10, height = 10;
    public static int frameWidth = width * buttonSize + space, frameHeight = height * buttonSize + space;
    static boolean[][] grid;
    static List<Button> list = new ArrayList<>();
    static Map<Button, Integer> map = new HashMap<>();
    static boolean[][] border;
    static boolean revealMines = false;
    public static final Group root = new Group();
    @Override
    public void start(Stage primaryStage) {
        grid = new boolean[height][width];
        border = new boolean[height][width];
        Scene scene = new Scene(root, frameWidth, frameHeight, Color.BLACK);
        SetupViruses(10);
        SetupBorder();
        SetupButtons(root);
        fillBorder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print((grid[i][j] ? 1 : 0) + " ");
            }
            System.out.println();
        }
        System.out.println("///////");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print((border[i][j] ? 1 : 0) + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < list.size(); i++) {
            Button b = list.get(i);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    reveal(b);
                }
            });
        }
        primaryStage.setTitle("Mines");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    static Button getWithIndex(int ind) {
        for (Map.Entry<Button, Integer> ent : map.entrySet()) {
            if (ent.getValue() == ind) {
                return ent.getKey();
            }
        }
        return null;
    }

    static void fillBorder() {
        for (int i = 0; i < width * height; i++) {
            if (!grid[i / height][i % width]) {
                Button b = getWithIndex(i);
                int x = i % width, y = i / height, ca = 0;
                for (int j = y - 1; j <= y + 1; j++) {
                    for (int k = x - 1; k <= x + 1; k++) {
                        try {
                            if (grid[j][k]) {
                                ca++;
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
                b.setText(String.valueOf(ca));
                b.setTextFill(new Color(0, 0, 0, 0));
            }
        }
    }

    static void Lose() {
        JOptionPane.showMessageDialog(null, "you lose", "you lose", 0);
    }

    static void reveal(Button b) {
        if (b.getText().equals("X")) {
            Lose();
        }
        if (!b.getText().equals("0")) {
            b.setTextFill(Color.BLACK);
            return;
        }
        int ind = map.get(b);
        check(ind);
//        if(Won())
//        {
//            Win();
//        }
        
    }
    static void Win(){
        JOptionPane.showMessageDialog(null, "you won", "you won", 2);
    }
    static boolean Won() {
        for (int i = 0; i < grid[0].length * grid.length; i++) {
            Button b = getWithIndex(i);
            if (b.getText().equals(" ")&&!b.getStyle().equals("-fx-background-color: #08E79C;")) {
                return false;
            }
        }
        return true;
    }

    static void check(int ind) {
        Button bt = getWithIndex(ind);
        bt.setStyle("-fx-background-color: #08E79C;");
        int x = ind % width, y = ind / height;
        for (int i = y - 1; i <= y + 1; i++) {
            for (int j = x - 1; j <= x + 1; j++) {
                if ((i != y || j != x) && i >= 0 && i < height && j >= 0 && j < height) {
                    Button b = getWithIndex(i * height + j);
                    if (!border[i][j] && !b.getText().equals(" ")) {
                        b.setText(" ");
                        b.setTextFill(Color.BLACK);
                        check(i * height + j);
                    } else if (border[i][j]) {
                        b.setTextFill(Color.BLACK);
                    }
                }
            }
        }
    }

    static void SetupBorder() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j]) {
                    try {
                        border[i][j + 1] = true;

                    } catch (Exception ex) {
                    }
                    try {
                        border[i][j - 1] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i - 1][j - 1] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i - 1][j] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i - 1][j + 1] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i + 1][j - 1] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i + 1][j] = true;
                    } catch (Exception ex) {
                    }
                    try {
                        border[i + 1][j + 1] = true;
                    } catch (Exception ex) {
                    }

                }
            }
        }
    }

    static void SetupViruses(int count) {
        int len = (width) * (height);
        for (int i = 0; i < count; i++) {

            int rn = (int) (Math.random() * width * height);
            while (grid[rn / height][rn % width]) {
                rn = (int) (Math.random() * (len));
            }
            grid[rn / height][rn % width] = true;
        }

    }

    static void SetupButtons(Group root) {
        // 8x8
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Button b = new Button(" ");
                b.setScaleX(2.3);
                b.setScaleY(2);
                b.setShape(new Rectangle(10, 10));
                b.setTranslateY(i * buttonSize + buttonSize / 2);
                b.setTranslateX(j * buttonSize + buttonSize / 2);

                if (grid[i][j]) {
                    b.setText("💣");
                    b.setTextFill(new Color(0, 0, 0, revealMines ? .3 : 0));
                }
                list.add(b);
                map.put(b, (i * height + j));
                root.getChildren().add(b);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
