import Model.Tuple;
import Model.Vector2D;
import View.RayView;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RayViewTest {

    @Test
    public void intersectsTest() {
        RayView rayView = new RayView();
        rayView.setStartX(0);
        rayView.setStartY(0);
        rayView.setEndX(400);
        rayView.setEndY(250);

        Tuple<Vector2D, Double> intersectionPoint = rayView.intersects(new Line(300, 200, 440, 200));
        System.out.println("Intersection point " + intersectionPoint.x + ", " + intersectionPoint.y);
        assertNotNull(intersectionPoint);
    }
}