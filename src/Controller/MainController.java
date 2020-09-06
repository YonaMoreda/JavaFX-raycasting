package Controller;

import View.RayView;
import Model.Vector2D;
import View.WallView;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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

    private int numberOfRays = 2 * 180;

    @FXML
    public void initialize() {
        pane_width = anchor_pane.getPrefWidth();
        pane_height = anchor_pane.getPrefHeight();
        walls = new ArrayList<>();

        createWalls();
        createRays();
        handleAnchorPaneOnMouseMoved();
        populateRenderHBox();
        Stop[] stops = {new Stop(0, Color.BLACK), new Stop(0.5, Color.BLACK), new Stop(1, Color.DARKGRAY)};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        render_HBox.setBackground(new Background(new BackgroundFill(lg, null, null)));
    }

    private void populateRenderHBox() {
        for (int i = 0; i < numberOfRays; i++) {
            Rectangle rectangleSlice = new Rectangle(0, 0, render_HBox.getPrefWidth() / numberOfRays, render_HBox.getPrefHeight());
            rectangleSlice.setFill(Color.rgb(255, 255, 255));
            render_HBox.getChildren().add(rectangleSlice);
        }
    }

    private void handleAnchorPaneOnMouseMoved() {
        anchor_pane.setOnMouseMoved(mouseEvent -> {
            renderRayIntersections(mouseEvent.getX(), mouseEvent.getY(), true);
        });
    }

    private void renderRayIntersections() {
        renderRayIntersections(0, 0, false);
    }

    private void renderRayIntersections(double x, double y, boolean translateFromInitial) {
        int rayIndex = 0;
        for (RayView rayView : rayViews) {
            if (translateFromInitial) {
                rayView.translateStartEndPointsFromInitial(x, y);
                rayViews.set(rayIndex, rayView);
            }
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
                int colorValue = (int) ((150) - rayView.getProjectedLength() * 150 / pane_width);
                rectangleSlice.setFill(Color.rgb(colorValue, colorValue, colorValue));
            } else {
                rectangleSlice.setHeight(0);
            }
            rayIndex++;
        }
    }

    private void createWalls() {
        createBoundaryWalls();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            WallView wallView = new WallView(rand.nextDouble() * pane_width, rand.nextDouble() * pane_height, 100 + rand.nextDouble() * 300, 50 + rand.nextDouble() * 50);
            while (collidesWithOtherWalls(wallView)) {
                wallView = new WallView(rand.nextDouble() * pane_width, rand.nextDouble() * pane_height, 100 + rand.nextDouble() * 300, 50 + rand.nextDouble() * 50);
            }
            walls.add(wallView);
            anchor_pane.getChildren().add(wallView);
        }
    }

    private boolean collidesWithOtherWalls(WallView wallView) {
        for (WallView wallView1 : walls) {
            if (wallView.hasAxisAlignedCollision(wallView1)) {
                return true;
            }
        }
        return false;
    }

    private void createBoundaryWalls() {
        WallView wallViewLeft = new WallView(0, 0, 5, pane_height);
        WallView wallViewTop = new WallView(0, 0, pane_width, 5);
        WallView wallViewRight = new WallView(pane_width - 10, 0, 5, pane_height);
        WallView wallViewBottom = new WallView(0, pane_height - 7, pane_width, 5);

        walls.add(wallViewLeft);
        walls.add(wallViewTop);
        walls.add(wallViewRight);
        walls.add(wallViewBottom);
        anchor_pane.getChildren().addAll(wallViewLeft, wallViewTop, wallViewRight, wallViewBottom);
    }

    private void createRays() {
        rayViews = new ArrayList<>(numberOfRays);
        for (int i = 0; i < numberOfRays; i++) {
            double angleFromCameraView = 2 * Math.PI * i / (6 * numberOfRays) - (Math.PI / 6);
            RayView rayView = new RayView(new Vector2D(0, 0), new Vector2D(Math.cos(angleFromCameraView), Math.sin(angleFromCameraView)), pane_width, angleFromCameraView);//250
            anchor_pane.getChildren().add(rayView);
            rayViews.add(rayView);
        }
    }


    private void rotateRays(double angle) {
        for (int i = 0; i < numberOfRays; i++) {
            RayView rayView = rayViews.get(i);
            double oldAngle = Math.atan2(rayView.getDirection().getY(), rayView.getDirection().getX());
            rayView.setDirection(new Vector2D(Math.cos(angle + oldAngle), Math.sin(angle + oldAngle)));
            rayViews.set(i, rayView);
        }
    }

    public void anchor_pane_key_pressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT, A -> rotateRays(-Math.PI / 180);
            case RIGHT, D -> rotateRays(3 * Math.PI / 180);
            case UP, W -> translateStartEndPointsRays(10 * Math.cos(findCameraViewAngle()), 10 * Math.sin(findCameraViewAngle()));
            case DOWN, S -> translateStartEndPointsRays(-10 * Math.cos(findCameraViewAngle()), -10 * Math.sin(findCameraViewAngle()));
        }
        renderRayIntersections();
    }

    private void translateStartEndPointsRays(double x, double y) {
        for (int i = 0; i < numberOfRays; i++) {
            RayView rayView = rayViews.get(i);
            rayView.translateStartEndPoints(x, y);
            rayViews.set(i, rayView);
        }
    }

    private double findCameraViewAngle() {
        int midIndex = rayViews.size() / 2;
        RayView midRayView = rayViews.get(midIndex);
        return Math.atan2(midRayView.getEndY() - midRayView.getStartY(), midRayView.getEndX() - midRayView.getStartX());
    }
}
