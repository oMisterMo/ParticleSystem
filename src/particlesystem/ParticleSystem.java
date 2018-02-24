package particlesystem;

import common.Helper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * 16-Dec-2016, 15:46:00.
 *
 * @author Mo
 */
public class ParticleSystem extends GameObject {

    private final static int MAX_SIZE = 30;  //100

    private ArrayList<Particle> particles;
    private Point center;

    public ParticleSystem() {
        particles = new ArrayList<>(MAX_SIZE);
        center = new Point(GamePanel.GAME_WIDTH / 2, GamePanel.GAME_HEIGHT / 2);

        for (int i = 0; i < MAX_SIZE; i++) {
//            addWaterfallParticle();
//            addFireParticle();
//            testEffect();
            testColEffect();
        }
    }

    private void testEffect() {
        Particle p = new Particle(
                center.x,
                center.y,
                8f, 8f, //w, h
                0.8f, 1f, //age, damp
                0f, 0f, 1f, //rotation
                1f, 0f, 0f, 1f //scale
        );
//        float speed = 80;
        p.velocity.set(Helper.Random(-100, 100), Helper.Random(-100, 100));
        p.velocity.normalize();
        p.velocity.mult(110);
        p.acceleration.set(0, 0);
        particles.add(p);
    }

    private void testColEffect() {
        Particle p = new Particle(
                center.x,
                center.y,
                8f, 8f, //w, h
                2f, 1f, //age, damp
                0f, 0f, 1f, //rotation
                1f, 0f, 0f, 1f, //scale
                Color.RED, Color.BLUE, new Color(0, 255, 0, 0), 1f
        );
//        float speed = 80;
        p.velocity.set(Helper.Random(-100, 100), Helper.Random(-100, 100));
        p.velocity.normalize();
        p.velocity.mult(110);
//        p.acceleration.set(0, 0);
        particles.add(p);
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Pressed");

        center.x = e.getX();
        center.y = e.getY();

        for (int i = 0; i < MAX_SIZE; i++) {
//            addWaterfallParticle();
//            addFireParticle();
//            testEffect();
            testColEffect();
        }
    }

    /* FIX ME WITH NEW CONSTRUCTOR */
    private void addFireParticle() {
//        int wh = Helper.random(2, 25);
//        particles.add(
//                new Particle(
//                        r.nextInt(1 * 1000) + 1500, //age
//                        wh, wh, //width, height
//                        new Vector2D(centerX, centerY), //pos
//                        new Vector2D(Helper.random(-100, 100), Helper.random(-100, 100)), //vel
//                        new Vector2D(0, 30f), //acc
//                        1.0f, //damp
//                        0f, 0f, 1.0f, //initial rotation, rotVel, rotDamp
//                        1.0f, 0f, 0f, 3.0f //initial scale, scaleVel, scaleAcc, scalemax
//                )
//        );
    }

    private void resetParticle(Particle p) {
//        Particle particle = p;
//        int wh = Helper.Random(2, 25);
//
//        particle.age = Helper.Random(0, (1 * 1000) + 1500);
//        particle.width = wh;
//        particle.height = wh;
//        particle.position.x = center.x;
//        particle.position.y = center.y;
//        particle.velocity.x = Helper.Random(-100, 100);
//        particle.velocity.y = Helper.Random(-100, 100);
//        particle.acceleration.y = 30f;
//        particle.setState(false);

    }

    /**
     * Loop through list backwards to avoid skipping elements!
     *
     * @param deltaTime
     */
    @Override
    void gameUpdate(float deltaTime) {
        //If time is up change position of center
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.gameUpdate(deltaTime);

            /* Memory efficient, resets position, doesn't create new particle */
            if (p.isDead()) {
                particles.remove(p);
            }
//            if (p.isDead()) {
//                resetParticle(p);
//            }
        }
    }

    @Override
    void gameRender(Graphics2D g) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.gameRender(g);
        }
    }

}
