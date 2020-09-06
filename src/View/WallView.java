package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class WallView extends Rectangle {

    public WallView() {
        this(0, 0, 140, 30);
    }

    public WallView(double x, double y, double width, double height) {
        super(x, y, width, height);
        setStroke(Color.GRAY);
        setStrokeWidth(5);
        setFill(Color.DARKGRAY);
//        setRotate(10);
    }

    private double centerToCornerDistance() {
        return Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2)) / 2;
    }

    //TODO:: ROTATIONS NEED TO BE TAKEN INTO ACCOUNT
    public Line getLeftSide() {
        Line line = new Line(getX(), getY(), getX(), getY() + getHeight());
        line.setRotate(getRotate());
        return line;
    }


    public Line getTopSide() {
//        double r = centerToCornerDistance();
//        double x = r * Math.cos(Math.toRadians(-getRotate()));
//        double y = r * Math.sin(Math.toRadians(-getRotate()));
//        Line line = new Line(getX() + x, getY() + y, getX() + getWidth() - x, getY() - y);
//        line.setRotate(getRotate());
        return new Line(getX(), getY(),getX() + getWidth(), getY());
    }

    public Line getRightSide() {
        Line line = new Line(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight());
        line.setRotate(getRotate());
        return line;
    }

    public Line getBottomSide() {
        Line line = new Line(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
        line.setRotate(getRotate());
        return line;
    }

    public boolean hasAxisAlignedCollision(WallView wallView) {
        return getX() < wallView.getX() + wallView.getWidth() &&
                getX() + getWidth() > wallView.getX() &&
                getY() < wallView.getY() + wallView.getHeight() &&
                getY() + getHeight() > wallView.getY();
    }
}
