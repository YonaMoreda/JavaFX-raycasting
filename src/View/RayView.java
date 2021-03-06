package View;

import Model.Tuple;
import Model.Vector2D;
import javafx.scene.shape.Line;

public class RayView extends Line {

    private Vector2D position;
    private Vector2D direction;
    private Vector2D initialStartPoint;
    private Vector2D initialEndPoint;
    private double length;
    private double angleFromCameraView;


    public RayView() {
        this(new Vector2D(0, 0), new Vector2D(1, 0), 40, 0);
    }

    public RayView(RayView rayView) {
        this.position = rayView.position;
        this.direction = rayView.direction;
        this.initialStartPoint = rayView.initialStartPoint;
        this.initialEndPoint = rayView.initialEndPoint;
        this.length = rayView.length;
        this.angleFromCameraView = rayView.angleFromCameraView;
    }

    public RayView(Vector2D position, Vector2D direction, double length, double angleFromCameraView) {
        this.position = position;       //origin
        this.direction = direction;
        this.setTranslateX(position.getX()); //FIXME:: <- REMOVE THIS?
        this.setTranslateY(position.getY()); //FIXME:: <- REMOVE THIS?
        this.length = length;
        this.angleFromCameraView = angleFromCameraView;
        this.setEndX(position.getX() + direction.getX() * length);
        this.setEndY(position.getY() + direction.getY() * length);


        this.initialStartPoint = new Vector2D(position.getX(), position.getY());
        this.initialEndPoint = new Vector2D(position.getX() + direction.getX() * length, position.getY() + direction.getY() * length);
    }

    public Vector2D getInitialStartPoint() {
        return initialStartPoint;
    }

    public Vector2D getInitialEndPoint() {
        return initialEndPoint;
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
        this.setEndX(this.getStartX() + direction.getX() * length);
        this.setEndY(this.getStartY() + direction.getY() * length);
        this.initialStartPoint = new Vector2D(position.getX(), position.getY());
        this.initialEndPoint = new Vector2D(position.getX() + direction.getX() * length, position.getY() + direction.getY() * length);
    }

    private boolean isInBetween(double pt, double a, double b) {
        if (a <= pt && pt <= b) {
            return true;
        }
        return a >= pt && pt >= b;
    }

    public Tuple<Vector2D, Double> intersects(Line line) {
        double x1 = this.getStartX();   // 0
        double y1 = this.getStartY();   // 0
        double x2 = this.getEndX();     // 400
        double y2 = this.getEndY();     // 250

        double x3 = line.getStartX();   // 300
        double y3 = line.getStartY();   // 200
        double x4 = line.getEndX();     // 440
        double y4 = line.getEndY();     // 200

        double denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));

        if (denominator == 0) {
            return null;
        }
        double numerator = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
        double numeratorU = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
        double t = numerator / denominator;
        double u = -numeratorU / denominator;
        if (t < 0 || t > 1 || u < 0 || u > 1) {
            return null;
        }
        double intersectPointX = x1 + t * (x2 - x1);
        double intersectPointY = y1 + t * (y2 - y1);
        return new Tuple<>(new Vector2D(intersectPointX, intersectPointY), t);
    }

    Tuple<Vector2D, Double> min(Tuple<Vector2D, Double> a, Tuple<Vector2D, Double> b) {
        if (a != null && b == null) {
            return a;
        }
        if (a == null && b != null) {
            return b;
        }
        if (a == null) {
            return null;
        }
        return a.y < b.y ? a : b;
    }

    public Vector2D cast(WallView wallView) {
        Tuple<Vector2D, Double> intersectPointLeft = intersects(wallView.getLeftSide());
        Tuple<Vector2D, Double> intersectPointTop = intersects(wallView.getTopSide());
        Tuple<Vector2D, Double> intersectPointRight = intersects(wallView.getRightSide());
        Tuple<Vector2D, Double> intersectPointBottom = intersects(wallView.getBottomSide());

        Tuple<Vector2D, Double> intersectPoint = min(min(min(intersectPointLeft, intersectPointTop), intersectPointRight), intersectPointBottom);
        if (intersectPoint == null) {
            return null;
        }
        return intersectPoint.x;
    }

    public double getEuclideanLength() {
        return Math.sqrt(Math.pow(getStartX() - getEndX(), 2) + Math.pow(getStartY() - getEndY(), 2));
    }

    public double getProjectedLength() {
        return getEuclideanLength() * Math.cos(angleFromCameraView);
    }

    public void translateStartEndPointsFromInitial(double x, double y) {
        this.setStartX(initialStartPoint.getX() + x);
        this.setStartY(initialStartPoint.getY() + y);
        this.setEndX(initialEndPoint.getX() + x);
        this.setEndY(initialEndPoint.getY() + y);
    }

    public void translateStartEndPoints(double x, double y) {
        setStartX(getStartX() + x);
        setStartY(getStartY() + y);
        position = new Vector2D(getStartX(), getStartY());
        setEndX(position.getX() + direction.getX() * length);
        setEndY(position.getY() + direction.getY() * length);
    }
}
