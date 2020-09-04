package Controller;

import View.RayView;
import Model.Vector2D;
import View.WallView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class MainController {
    @FXML
    private HBox render_HBox;
    @FXML
    private AnchorPane anchor_pane;
    private ArrayList<RayView> rayViews;
    private double pane_width;
    private double pane_height;

    private ArrayList<WallView> walls;

    private int numberOfRays = 180;

    @FXML
    public void initialize() {
        pane_width = anchor_pane.getPrefWidth();
        pane_height = anchor_pane.getPrefHeight();
        walls = new ArrayList<>();

        createWalls();
        createRays();
        handleAnchorPaneOnMouseMoved();
        handleAnchorPaneOnKeyPressed();
        populateRenderHBox();
        render_HBox.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
    }

    private void handleAnchorPaneOnKeyPressed() {
//        anchor_pane.setOnKeyPressed(keyEvent -> {
//            System.out.println("HM");
//            switch (keyEvent.getCode()) {
//                case A -> {
//                    System.out.println("LEFT PRESSED");
//                    rotateRays(-Math.PI / 180);
//                }
//                case RIGHT -> rotateRays(Math.PI / 180);
//            }
//        });
        anchor_pane.setOnMouseClicked(keyEvent -> {
            rotateRays(-Math.PI / 180);
        });
    }

    private void populateRenderHBox() {
        for (int i = 0; i < numberOfRays; i++) {
            Rectangle rectangleSlice = new Rectangle(0, 0, render_HBox.getPrefWidth() / numberOfRays, render_HBox.getPrefHeight());
            rectangleSlice.setFill(Color.DARKGRAY);
            rectangleSlice.setStroke(Color.DARKGRAY);
            rectangleSlice.setStrokeWidth(0);
//            rectangleSlice.setStroke(Color.BLUEVIOLET);
            render_HBox.getChildren().add(rectangleSlice);
        }
    }

    private void handleAnchorPaneOnMouseMoved() {
        anchor_pane.setOnMouseMoved(mouseEvent -> {
            int rayIndex = 0;
            for (RayView rayView : rayViews) {
                rayView.setStartX(rayView.getInitialStartPoint().getX() + mouseEvent.getX());
                rayView.setStartY(rayView.getInitialStartPoint().getY() + mouseEvent.getY());
                rayView.setEndX(rayView.getInitialEndPoint().getX() + mouseEvent.getX());
                rayView.setEndY(rayView.getInitialEndPoint().getY() + mouseEvent.getY());

                rayViews.set(rayIndex, rayView);
                Rectangle rectangleSlice = (Rectangle) render_HBox.getChildren().get(rayIndex);
                boolean thereIsIntersection = false;
                for (WallView wallView : walls) {
                    Vector2D intersectPoint = rayView.cast(wallView);
                    if (intersectPoint != null) {   // there is a hit
                        thereIsIntersection = true;
                        rayView.setEndX(intersectPoint.getX());
                        rayView.setEndY(intersectPoint.getY());

                        rayViews.set(rayIndex, rayView);

                    }
                }
                if (thereIsIntersection) {
                    rectangleSlice.setHeight(20000 / rayView.getProjectedLength());
                } else {
                    rectangleSlice.setHeight(0);
                }
                rayIndex++;
            }
        });
    }

    private void createWalls() {
        createBoundaryWalls();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            WallView wallView = new WallView(rand.nextDouble() * pane_width, rand.nextDouble() * pane_height, 100 + rand.nextDouble() * 300, 50 + rand.nextDouble() * 100);
            walls.add(wallView);
            anchor_pane.getChildren().add(wallView);
        }
    }

    private void createBoundaryWalls() {
        WallView wallViewLeft = new WallView(0, 0, 5, pane_height);
        WallView wallViewTop = new WallView(0, 0, pane_width, 5);
        WallView wallViewRight = new WallView(pane_width - 10, 0, 5, pane_height);
        WallView wallViewBottom = new WallView(0, pane_height - 5, pane_width, 5);
        walls.add(wallViewLeft);
        walls.add(wallViewTop);
        walls.add(wallViewRight);
        walls.add(wallViewBottom);
        anchor_pane.getChildren().addAll(wallViewLeft, wallViewTop, wallViewRight, wallViewBottom);
    }

    private void createRays() {
        rayViews = new ArrayList<>(numberOfRays);
        for (int i = 0; i < numberOfRays; i++) {
            RayView rayView = new RayView(new Vector2D(0, 0), new Vector2D(Math.cos((double) i * 2 * Math.PI / (6 * numberOfRays) - (Math.PI / 6)), Math.sin((double) 2 * Math.PI * i / (6 * numberOfRays) - (Math.PI / 6))), pane_width);//250
            anchor_pane.getChildren().add(rayView);
            rayViews.add(rayView);
        }
    }

    private void rotateRays(double angle) {
//        System.out.println("TODO:: TO BE IMPLEMENTED");
        for (int i = 0; i < numberOfRays; i++) {
            RayView rayView = rayViews.get(i);
            rayView.setDirection(new Vector2D(Math.cos(Math.acos(rayView.getDirection().getX()) + angle), Math.sin(Math.asin(rayView.getDirection().getY()) + angle)));
            rayViews.set(i, rayView);
        }
    }
}
