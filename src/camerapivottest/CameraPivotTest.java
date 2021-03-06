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
    BooleanProperty aDown = new SimpleBooleanProperty(false);
    BooleanProperty leftDown = new SimpleBooleanProperty(false);
    BooleanProperty movingLeft = new SimpleBooleanProperty();
    {
        movingLeft.bind(leftDown.or(aDown));
    }
    
    BooleanProperty wDown = new SimpleBooleanProperty(false);
    BooleanProperty upDown = new SimpleBooleanProperty(false);
    BooleanProperty movingUp = new SimpleBooleanProperty();
    {
        movingUp.bind(wDown.or(upDown));
    }
    
    
    
    

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        Pane sprite = new Pane();
        sprite.setPrefWidth(100);
        sprite.setPrefHeight(100);
        sprite.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        sprite.setTranslateX(300);
        sprite.setTranslateY(100);
        sprite.setTranslateZ(00);
        root.getChildren().add(sprite);

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
        redCenterX.bind(sprite.translateXProperty().add(sprite.widthProperty().divide(2)));

        DoubleProperty redCenterY = new SimpleDoubleProperty();
        redCenterY.bind(sprite.translateYProperty().add(sprite.heightProperty().divide(2)));

        Camera c = new PerspectiveCamera(true);
        c.translateXProperty().bind(redCenterX);
        c.translateYProperty().bind(redCenterY);
        c.translateZProperty().bind(sprite.translateZProperty());

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
                if(movingLeft.get())
                {
                    sprite.setTranslateX(sprite.getTranslateX() - 1);
                }
                if(movingUp.get())
                {
                    sprite.setTranslateY(sprite.getTranslateY() - 1);
                }
            }
        };
        
        
        
        BooleanProperty running = new SimpleBooleanProperty(false);
        scene.onKeyPressedProperty().set(e
                -> {
            switch (e.getCode()) {
                case SPACE:
                    if (running.get()) {
                        t.stop();
                        running.set(false);
                    } else {
                        t.start();
                        running.set(true);
                    }
                    break;
                case A:
                    aDown.setValue(true);
                    break;
                case LEFT:
                    leftDown.setValue(true);
                    break;
                case W:
                    wDown.set(true);
                case UP:
                    upDown.set(true);
                    
                default:
                    break;
            }

        });
        scene.onKeyReleasedProperty().set(e
                -> {
            switch (e.getCode()) {
                case A:
                    aDown.setValue(false);
                    break;
                case LEFT:
                    leftDown.setValue(false);
                    break;
                    case W:
                    wDown.set(false);
                case UP:
                    upDown.set(false);
                default:
                    break;
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




