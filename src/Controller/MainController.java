package Controller;

import View.RayView;
import Model.Vector2D;
import View.WallView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;


import java.util.ArrayList;
import java.util.Random;

public class MainController {

    @FXML
    private AnchorPane anchor_pane;
    private ArrayList<RayView> rayViews;
    private double pane_width;
    private double pane_height;

    private ArrayList<WallView> walls;

    @FXML
    public void initialize() {
        pane_width = anchor_pane.getPrefWidth();
        pane_height = anchor_pane.getPrefHeight();
        walls = new ArrayList<>();

        createWalls();
        createRays();
        handleAnchorPaneOnMouseMoved();
    }

    private void handleAnchorPaneOnMouseMoved() {
        anchor_pane.setOnMouseMoved(mouseEvent -> {
            int i = 0;
            for (RayView rayView : rayViews) {
                rayView.setStartX(rayView.getInitialStartPoint().getX()  + mouseEvent.getX());
                rayView.setStartY(rayView.getInitialStartPoint().getY() + mouseEvent.getY());
                rayView.setEndX(rayView.getInitialEndPoint().getX() + mouseEvent.getX());
                rayView.setEndY(rayView.getInitialEndPoint().getY() + mouseEvent.getY());

                rayViews.set(i, rayView);
                for (WallView wallView : walls) {
                    Vector2D intersectPoint = rayView.cast(wallView);
                    if (intersectPoint != null) {   // there is a hit
                        rayView.setEndX(intersectPoint.getX());
                        rayView.setEndY(intersectPoint.getY());
                        rayViews.set(i, rayView);
                    }
                }
                i++;
            }
        });
    }

    private void createWalls() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            WallView wallView = new WallView(rand.nextDouble() * pane_width, rand.nextDouble() * pane_height, rand.nextDouble() * 300, rand.nextDouble() * 100);
            walls.add(wallView);
            anchor_pane.getChildren().add(wallView);
        }
    }

    private void createRays() {
        int numberOfRays = 360;
        rayViews = new ArrayList<>(numberOfRays);
        for (int i = 0; i < numberOfRays; i++) {
            RayView rayView = new RayView(new Vector2D(0, 0), new Vector2D(Math.cos((double) i * 2 * Math.PI / numberOfRays), Math.sin((double) 2 * Math.PI * i / numberOfRays)), 250);
            anchor_pane.getChildren().add(rayView);
            rayViews.add(rayView);
        }
    }
}
