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

import common.Helper;
import common.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Represents a single firework instance and the following explosion
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Firework {

    public static final int EXPLODE_LIMIT = 20;

    public boolean exploaded;
    private Particle firework;
    public ArrayList<Particle> explosion;

    private Vector2D center;
    private Vector2D gravity;
    private Vector2D wind;

    public boolean complete = false;

    private final Color startCol;
    private final Color finalCol;
    private final Color fadeCol;

    private static final int TRAIL_LIMIT = 10;
    private Vector2D[] history;
    private int count;

    /**
     * Creates a firework with set variables at a random location below the game
     * screen.
     *
     * The firework also stores the information relating to the following
     * explosion.
     */
    public Firework() {
        //Set center of screen position
        center = new Vector2D(GamePanel.GAME_WIDTH / 2, GamePanel.GAME_HEIGHT / 2);
        //Set gravity to negative 50 units
        gravity = new Vector2D(0, 50);
        wind = new Vector2D(-50, 0);
        exploaded = false;
        explosion = new ArrayList<>(EXPLODE_LIMIT);
//        lifespan = 200;

        float age = 1f;  //sec
        float size = 4;
//        float age = Helper.Random(2, 4);
        int randR = Helper.Random(0, 255);
        int randG = Helper.Random(0, 255);
        int randB = Helper.Random(0, 255);
        startCol = new Color(Helper.Random(0, 255), Helper.Random(0, 255),
                Helper.Random(0, 255), 255);
        finalCol = new Color(randR, randG, randB, 255);
        fadeCol = new Color(randR, randG, randB, 0);

        this.firework = new Particle(
                Helper.Random(0, GamePanel.GAME_WIDTH), //x
                GamePanel.GAME_HEIGHT + size, //y
                size, size, //w, h
                age, 1f, //age, damp
                0f, 0f, 1f, //rotation
                1f, 0f, 0f, 1f, //scale
                startCol, finalCol, //rgba
                age / 3f //fade age
        );
//        float speed = 80;
        firework.velocity.set(0, Helper.Random(-100, -250));
        firework.velocity.mult(2);

//        //test below
//        history = new Vector2D[TRAIL_LIMIT];
//        for (int i = 0; i < history.length; i++) {
//            history[i] = new Vector2D(-100, -100);
//        }
//        count = 0;
    }

    private void explode() {
        for (int i = 0; i < EXPLODE_LIMIT; i++) {
            float age = 1.5f;
            float size = 4f;
            Particle p = new Particle(
                    firework.position.x, firework.position.y,
                    size, size, //w, h
                    age, 1f, //age, damp
                    0f, Helper.Random(-400, 400), 1f, //rotation
                    1f, 0f, 0f, 1f, //scale
                    finalCol, fadeCol, //rgba
                    age / 2 //fade age
            );
            p.velocity.set(Helper.Random(-100, 100), Helper.Random(-100, 100));
//            p.velocity.normalize();
            p.velocity.mult(0.4f);
            explosion.add(p);
        }
    }

    /**
     * Incomplete
     */
    private void updateTrail() {
        if (count >= TRAIL_LIMIT) {
            count = 0;
        }
//        System.out.println("count: "+count);
//        System.out.println("pos: " + firework.position);
//        history[count] = firework.position;
        history[count] = new Vector2D(firework.position);
        count++;
    }

    /**
     * Incomplete
     *
     * @param g
     */
    private void drawTrail(Graphics2D g) {
//        g.setColor(startCol);
        g.setColor(firework.color);
//        for (int i = history.length - 1; i >= 0; i--) {
        for (int i = 0; i < history.length; i++) {
            int w = 2;
            int h = 2;
            Vector2D pos = history[i];
            g.fillOval((int) (pos.x + firework.width / 2) - w / 2,
                    (int) (pos.y + firework.height / 2) - h / 2,
                    w, h);
        }
    }

    public void update(float deltaTime) {
        if (!exploaded) {
            //Update single firework travelling up
            firework.applyForce(gravity);
//            firework.applyForce(wind);
            firework.gameUpdate(deltaTime);
//            updateTrail();

            if (firework.dead) {
                exploaded = true;
                explode();
                System.out.println("EXPLODE");
            }
        } else {
            //Firework has exploded, update EXPLOSION particles
            for (int i = explosion.size() - 1; i >= 0; i--) {
                Particle p = explosion.get(i);
                if (p.dead) {
                    //If one explosion particle is complete, whole firework has ended
                    complete = true;
                }
                p.applyForce(gravity);
                p.gameUpdate(deltaTime);
            }
        }
    }

    public void draw(Graphics2D g) {
        if (!exploaded) {
//            drawTrail(g);
            firework.gameRender(g);

        } else {
            //Firework has exploded, draw EXPLOSION particles
            for (int i = explosion.size() - 1; i >= 0; i--) {
                Particle p = explosion.get(i);
                p.gameRender(g);
            }
        }
    }
}
