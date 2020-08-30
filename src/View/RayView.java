package View;

import Model.Tuple;
import Model.Vector2D;
import javafx.scene.shape.Line;

public class RayView extends Line {

    private Vector2D position;
    private Vector2D direction;

    public RayView() {
        this(new Vector2D(0, 0), new Vector2D(1, 0), 40);
    }

    public RayView(Vector2D position, Vector2D direction, double t) {
        this.position = position;
        this.direction = direction;

        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.setEndX(position.getX() + direction.getX() * t);
        this.setEndY(position.getY() + direction.getY() * t);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public Tuple<Vector2D, Double> intersects(Line line) {
        double x1 = this.getStartX();
        double y1 = this.getStartY();
        double x2 = this.getEndX();
        double y2 = this.getEndY();

        double x3 = line.getStartX();
        double y3 = line.getStartY();
        double x4 = line.getEndX();
        double y4 = line.getEndY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if (denominator < 0) {
            return null;
        }
        double numerator = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
        double t = numerator / denominator;

        return new Tuple<>(new Vector2D(x1 + t * (x2 - x1), y1 + t * (y2 - y1)), t);
    }

    Tuple<Vector2D, Double> min(Tuple<Vector2D, Double> a, Tuple<Vector2D, Double> b) {
        return a.y < b.y ? a : b;
    }

    public Vector2D cast(WallView wallView) {
        Tuple<Vector2D, Double> intersectPointLeft = intersects(wallView.getLeftSide());
        Tuple<Vector2D, Double> intersectPointTop = intersects(wallView.getTopSide());
        Tuple<Vector2D, Double> intersectPointRight = intersects(wallView.getRightSide());
        Tuple<Vector2D, Double> intersectPointBottom = intersects(wallView.getBottomSide());


        return min(min(min(intersectPointLeft, intersectPointTop), intersectPointRight), intersectPointBottom).x;
    }
}
