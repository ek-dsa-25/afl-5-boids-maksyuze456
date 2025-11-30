package main;

import main.simulation.FlockSimulation;
import main.spatial.*;

public class Microbench {

    public static void main(String[] args) {

        int width = 1200;
        int height = 800;

        int boidCount = 2000;
        int warmupIterations = 50;
        int measureIterations = 200;
        double radius = 50;


        SpatialIndex[] indices = {
                new NaiveSpatialIndex(),
                new KDTreeSpatialIndex(),
                new QuadTreeSpatialIndex(width, height),
                new SpatialHashIndex(width, height, 50)
        };

        for (SpatialIndex index : indices) {
            System.out.println("=== " + index.getName() + " ===");

            FlockSimulation sim = new FlockSimulation(width, height);
            sim.setSpatialIndex(index);
            sim.setNeighborRadius(radius);
            sim.setBoidCount(boidCount);

            for (int i = 0; i < warmupIterations; i++)
                sim.update();

            double total = 0;
            for (int i = 0; i < measureIterations; i++) {
                sim.update();
                total += sim.getLastIterationTimeMs();
            }

            double avg = total / measureIterations;
            System.out.println("Average iteration time: " + avg + " ms\n");
        }
    }
}
