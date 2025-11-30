package main.behavior;

import main.model.Boid;
import main.simulation.Forces;
import main.simulation.Vector2D;

import java.util.List;

public class RandomBehavior implements BehaviorStrategy{

    @Override
    public Forces calculateForces(Boid boid, List<Boid> neighbors) {


        double angle = (Math.random() - 0.5) * 0.6;
        double speed = 0.5;

        double steerX = Math.cos(angle) * speed;
        double steerY = Math.sin(angle) * speed;

        Vector2D randomForce = new Vector2D(steerX, steerY);

        return new Forces(randomForce, Vector2D.ZERO, Vector2D.ZERO);
    }
}
