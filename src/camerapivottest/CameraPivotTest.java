/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package camerapivottest;

import eu.lestard.advanced_bindings.api.MathBindings;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author pramukh
 */
public class CameraPivotTest extends Application {

    DoubleProperty distance = new SimpleDoubleProperty(700);

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        Pane obviousRedPane = new Pane();
        obviousRedPane.setPrefWidth(100);
        obviousRedPane.setPrefHeight(100);
        obviousRedPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        obviousRedPane.setTranslateX(300);
        obviousRedPane.setTranslateY(100);
        obviousRedPane.setTranslateZ(00);
        root.getChildren().add(obviousRedPane);

        GridPane grid = grid(10);
        grid.setGridLinesVisible(true);
        grid.setMinSize(1000, 1000);
        root.getChildren().add(grid);

        GridPane grid2 = grid(10);
        grid2.setGridLinesVisible(true);
        grid2.setMinSize(1000, 1000);
        grid2.getTransforms().add(new Rotate(-90, Rotate.X_AXIS));
        root.getChildren().add(grid2);

        GridPane grid3 = grid(10);
        grid3.setGridLinesVisible(true);
        grid3.setMinSize(1000, 1000);
        grid3.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        root.getChildren().add(grid3);

        DoubleProperty redCenterX = new SimpleDoubleProperty();
        redCenterX.bind(obviousRedPane.translateXProperty().add(obviousRedPane.widthProperty().divide(2)));

        DoubleProperty redCenterY = new SimpleDoubleProperty();
        redCenterY.bind(obviousRedPane.translateYProperty().add(obviousRedPane.heightProperty().divide(2)));

        Camera c = new PerspectiveCamera(true);
        c.translateXProperty().bind(redCenterX);
        c.translateYProperty().bind(redCenterY);
        c.translateZProperty().bind(obviousRedPane.translateZProperty());

        c.setFarClip(3000);

        Rotate xRotate = new Rotate();

        DoubleProperty angle = new SimpleDoubleProperty(0);
        xRotate.angleProperty().bind(angle);
        xRotate.setAxis(Rotate.X_AXIS);

        Translate translate = new Translate();
        translate.yProperty().bind(distance.multiply(MathBindings.sin(MathBindings.toRadians(angle))));
        translate.zProperty().bind(distance.multiply(MathBindings.cos(MathBindings.toRadians(angle))).negate());

        c.getTransforms().addAll(translate, xRotate);

        Scene scene = new Scene(root, 300, 250);

        AnimationTimer t = new AnimationTimer() {
            @Override
            public void handle(long now) {
                angle.set(angle.get() + 0.4);
            }
        };
        BooleanProperty running = new SimpleBooleanProperty(false);
        scene.onKeyPressedProperty().set(e
                -> {
            if (e.getCode() == KeyCode.SPACE) {
                if (running.get()) {
                    t.stop();
                    running.set(false);
                } else {
                    t.start();
                    running.set(true);
                }
            }
        });
        t.start();

        scene.setCamera(c);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private GridPane grid(double size) {
        GridPane temp = new GridPane();
        List<ColumnConstraints> cList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100 / size);
            cList.add(cc);
        }
        temp.getColumnConstraints().addAll(cList);

        List<RowConstraints> rList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100 / size);
            rList.add(rc);
        }
        temp.getRowConstraints().addAll(rList);

        return temp;
    }
}







