/* 
 * Copyright (C) 2019 Mohammed Ibrahim
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package particlesystem;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Fireworks particle system
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Fireworks {

    public static final int NUM_OF_FIREWORKS = 30;
    private final ArrayList<Firework> fireworks;

    /**
     * Default constructor creates an empty array that holds fireworks with an
     * initial capacity of NUM_OF_FIREWORKS
     */
    public Fireworks() {
//        fireworks = new Firework[100];
        fireworks = new ArrayList<>(NUM_OF_FIREWORKS);
    }

    /**
     * Adds a new firework particle to the list of particles
     *
     * @param e mouse event
     */
    public void mousePressed(MouseEvent e) {
        System.out.println("Adding new firework");
        fireworks.add(new Firework());
        System.out.println("Size: " + fireworks.size());
    }

    /**
     * Update all game objects
     *
     * @param deltaTime time since last frame
     */
    public void update(float deltaTime) {
        //Randomly add a new firework
//        if (Helper.Random() < 0.005f) {
//            fireworks.add(new Firework());
//        }
        //Render a single firework
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.update(deltaTime);
        }
    }

    /**
     * Render all particles
     *
     * @param g graphics object used to render shapes, images and fonts
     */
    public void draw(Graphics2D g) {
        for (int i = fireworks.size() - 1; i >= 0; i--) {
            Firework f = fireworks.get(i);
            f.draw(g);

            if (f.complete) {
                System.out.println("WE DONE ERE!!!!!");
                f.explosion.clear();    //clear explosion array
                fireworks.remove(f);    //remove single firework from fireworks
            }
        }
    }
}
