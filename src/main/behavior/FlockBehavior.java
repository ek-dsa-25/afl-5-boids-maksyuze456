package main.behavior;

import java.util.List;

import main.model.Boid;
import main.simulation.Forces;
import main.simulation.Vector2D;

public class FlockBehavior implements BehaviorStrategy {

    @Override
    public Forces calculateForces(Boid boid, List<Boid> neighbors) {
        if (neighbors.isEmpty()) {
            return new Forces();
        }

        FlockWeights weights = boid.getFlockWeights();

        Vector2D sep = calculateSeparation(boid, neighbors, weights);
        Vector2D ali = calculateAlignment(boid, neighbors, weights);
        Vector2D coh = calculateCohesion(boid, neighbors, weights);

        return new Forces(sep, ali, coh);
    }

    private Vector2D calculateSeparation(Boid boid, List<Boid> neighbors, FlockWeights weights) {
        double x = boid.getX();
        double y = boid.getY();
        double vx = boid.getVx();
        double vy = boid.getVy();

        double steerX = 0, steerY = 0;
        int count = 0;

        for (Boid neighbor : neighbors) {
            double distance = boid.distanceTo(neighbor);
            if (distance > 0 && distance < 25) {
                double diffX = x - neighbor.getX();
                double diffY = y - neighbor.getY();

                diffX /= distance;
                diffY /= distance;

                steerX += diffX;
                steerY += diffY;
                count++;
            }
        }

        if (count > 0) {
            steerX /= count;
            steerY /= count;

            double magnitude = Math.sqrt(steerX * steerX + steerY * steerY);
            if (magnitude > 0) {
                steerX = (steerX / magnitude) * 2.0;
                steerY = (steerY / magnitude) * 2.0;

                steerX -= vx;
                steerY -= vy;

                double force = Math.sqrt(steerX * steerX + steerY * steerY);
                if (force > 0.03) {
                    steerX = (steerX / force) * 0.03;
                    steerY = (steerY / force) * 0.03;
                }
            }
        }

        return new Vector2D(steerX * weights.separation(), steerY * weights.separation());
    }
    private Vector2D calculateAlignment(Boid boid, List<Boid> neighbors, FlockWeights weights) {
        double vx = boid.getVx();
        double vy = boid.getVy();

        double avgVx = 0, avgVy = 0;
        int count = 0;

        for (Boid neighbor : neighbors) {
            double distance = boid.distanceTo(neighbor);
            if (distance > 0 && distance < 50) {
                avgVx += neighbor.getVx();
                avgVy += neighbor.getVy();
                count++;
            }
        }

        if (count > 0) {
            avgVx /= count;
            avgVy /= count;

            double magnitude = Math.sqrt(avgVx * avgVx + avgVy * avgVy);
            if (magnitude > 0) {
                avgVx = (avgVx / magnitude) * 2.0;
                avgVy = (avgVy / magnitude) * 2.0;

                double steerX = avgVx - vx;
                double steerY = avgVy - vy;

                double force = Math.sqrt(steerX * steerX + steerY * steerY);
                if (force > 0.03) {
                    steerX = (steerX / force) * 0.03;
                    steerY = (steerY / force) * 0.03;
                }

                return new Vector2D(steerX * weights.alignment(), steerY * weights.alignment());
            }
        }

        return Vector2D.ZERO;
    }
    private Vector2D calculateCohesion(Boid boid, List<Boid> neighbors, FlockWeights weights) {
        double x = boid.getX();
        double y = boid.getY();
        double vx = boid.getVx();
        double vy = boid.getVy();

        double centerX = 0, centerY = 0;
        int count = 0;

        for (Boid neighbor : neighbors) {
            double distance = boid.distanceTo(neighbor);
            if (distance > 0 && distance < 50) {
                centerX += neighbor.getX();
                centerY += neighbor.getY();
                count++;
            }
        }

        if (count > 0) {
            centerX /= count;
            centerY /= count;

            double steerX = centerX - x;
            double steerY = centerY - y;

            double magnitude = Math.sqrt(steerX * steerX + steerY * steerY);
            if (magnitude > 0) {
                steerX = (steerX / magnitude) * 2.0;
                steerY = (steerY / magnitude) * 2.0;

                steerX -= vx;
                steerY -= vy;

                double force = Math.sqrt(steerX * steerX + steerY * steerY);
                if (force > 0.03) {
                    steerX = (steerX / force) * 0.03;
                    steerY = (steerY / force) * 0.03;
                }

                return new Vector2D(steerX * weights.cohesion(), steerY * weights.cohesion());
            }
        }

        return Vector2D.ZERO;
    }
}
