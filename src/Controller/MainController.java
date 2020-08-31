package Controller;

import View.RayView;
import Model.Vector2D;
import View.WallView;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class MainController {

    @FXML
    private AnchorPane anchor_pane;
    private ArrayList<Line> rayViews;
    private double pane_width;
    private double pane_height;

    @FXML
    public void initialize() {
        pane_width = anchor_pane.getPrefWidth();
        pane_height = anchor_pane.getPrefHeight();

        RayView rayView = new RayView(new Vector2D(0, 0), new Vector2D(1, 0), 30);
        WallView wallView = new WallView(pane_width / 2, pane_height / 2);

//        rayView.setEndX(intersectPoint.getX());
//        rayView.setEndY(intersectPoint.getY());
        anchor_pane.getChildren().add(rayView);
        anchor_pane.getChildren().add(wallView);

        anchor_pane.setOnMouseMoved(mouseEvent -> {
            rayView.setEndX(mouseEvent.getX());
            rayView.setEndY(mouseEvent.getY());
            Vector2D intersectPoint = rayView.cast(wallView);
            if(intersectPoint != null) {
                System.out.println("Intersect point: " + intersectPoint.toString());
            }
//            rayView.setEndX(intersectPoint.getX());
//            rayView.setEndY(intersectPoint.getY());
        });

//        createRays();
//        anchor_pane.setOnMouseMoved(mouseEvent -> {
//            int i = 0;
//            for (Line line : rayViews) {
//                line.setTranslateX(mouseEvent.getX());
//                line.setTranslateY(mouseEvent.getY());
//                rayViews.set(i, line);
//                rayViews.set(i, line);
//                i++;
//            }
//        });
    }

    private void createRays() {
        int numberOfRays = 180;
        rayViews = new ArrayList<>(numberOfRays);
        for (int i = 0; i < numberOfRays; i++) {
            Line line = new Line(0, 0, pane_width * Math.cos((double) i * 2 * Math.PI / numberOfRays), pane_height * Math.sin((double) 2 * Math.PI * i / numberOfRays));
            line.setStroke(Color.ORANGE);
            line.setStrokeWidth(1);
            anchor_pane.getChildren().add(line);
            rayViews.add(line);
        }
    }
}
