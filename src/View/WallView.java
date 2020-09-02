package View;


import Model.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class WallView extends Rectangle {

    public WallView() {
        this(0, 0, 140, 30);
    }

    public WallView(double x, double y, double width, double height) {
        super(x, y, width, height);
        setStroke(Color.BLACK);
        setFill(Color.GRAY);
    }

    //TODO:: ROTATIONS NEED TO BE TAKEN INTO ACCOUNT
    public Line getLeftSide() {
        return new Line(getX(), getY(), getX(), getY() + getHeight());
    }

    public Line getTopSide() {
        return new Line(getX(), getY(), getX() + getWidth(), getY());
    }

    public Line getRightSide() {
        return new Line(getX() + getWidth(), getY(), getX() + getWidth(), getY() + getHeight());
    }

    public Line getBottomSide() {
        return new Line(getX(), getY() + getHeight(), getX() + getWidth(), getY() + getHeight());
    }
}
