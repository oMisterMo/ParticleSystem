package particlesystem;

import common.Helper;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

/**
 * 26-Jul-2016, 01:12:17.
 *
 * @author Mo
 */
public class Particle extends DynamicGameObject {

    protected Point centerPoint;
    protected float width, height;
    //Particles lifespan in ms
    protected float age;

    //Vectors to represent a particle
    /*private Vector2D position;*/
//    protected Vector2D velocity;
//    protected Vector2D acceleration;
//    protected Vector2D gravity;
    //Range between 0.0 - 1.0 (ressistanse on x axis)
    protected float dampening;

    /*CHANGE BACK TO FLOATS*/
    //Rotation variables
    protected float rotation;
    protected float rotationVel;
    protected float rotationDamp;

    //Scale variables
    protected float scale;//or do I only scale one axis 0.0->scaleMax
    protected float scaleVel;
    protected float scaleAcc;
    protected float scaleMax;

    private Color color;
    private Color initColor;
    private Color finalColor;
    private int fadeAge;

    protected float startTime;
    protected boolean dead;

    public Particle(float x, float y, float width, float height,
            float age, float damp,
            float initRot, float initRotVel, float initRotDamp,
            float initScale, float initScaleVel, float initScaleAcc,
            float initScalemax) {
        super(x, y, width, height);
        //Width of our pixel (5)
        this.width = width;
        this.height = height;
        centerPoint = new Point();
        calculateCenter();

        color = new Color(
                Helper.Random(0, 256),
                Helper.Random(0, 256),
                Helper.Random(0, 256));

        //From GameObject
//        this.age = age;
//        this.position = pos;
//        this.velocity = vel;
//        this.acceleration = acc;
        this.age = age;
        dampening = damp;//between 0.0 -> 1.0

        this.rotation = initRot;
        this.rotationVel = initRotVel;
        this.rotationDamp = initRotDamp;

        this.scale = initScale;
        this.scaleVel = initScaleVel;
        this.scaleAcc = initScaleAcc;
        this.scaleMax = initScalemax;

        startTime = System.currentTimeMillis();
        dead = false;

//        printInfo();
    }

    private void printInfo() {
        System.out.println(position);
        System.out.println(velocity);
        System.out.println(acceleration);
        System.out.println("width: " + width);
        System.out.println("height: " + height);
        System.out.println(color);
    }

    public void mousePressed(MouseEvent e) {
//        position.x = e.getX();
//        position.y = e.getY();
//        System.out.println("Pressed");
    }

    public void mouseReleased(MouseEvent e) {
//        position.x = e.getX();
//        position.y = e.getY();
//        System.out.println("Released");

//        startTime = System.currentTimeMillis();
//        age = 3000;//ms
    }

    public void setState(boolean b) {
        dead = b;
    }

    public boolean isDead() {
        return dead;
    }

    private void updatePos(float deltaTime) {
        /* 
         vel (m/s) gravitiy (m/s), deltaTime in seconds(s), position(m)
         Everysecond we update the velocity
         */
        velocity.multBy(dampening);

        //Update velocities position by applying gravity (change in vel = accel)
        velocity.x += acceleration.x * deltaTime;
        velocity.y += acceleration.y * deltaTime;
//        System.out.println(velocity);

        //Update the particles position
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;
    }

    private void updateRot(float deltaTime) {
        rotation *= rotationDamp;
        rotation += rotationVel * deltaTime;
    }

    private void updateScale(float deltaTime) {
        scaleVel += scaleAcc * deltaTime;
        scale += scaleVel * deltaTime;
        scale = Helper.Clamp(scale, 0.0f, scaleMax);
//        System.out.println("scale = " + scale);
    }

    private void calculateCenter() {
        centerPoint.x = (int) (position.x + width / 2);
        centerPoint.y = (int) (position.y + height / 2);
    }

    @Override
    void gameUpdate(float deltaTime) {
        //dt = 0.016 (60fps) dt = 0.032 (30fps)
//        System.out.println("deltaTime: "+deltaTime);
        if (age < 0) {
            dead = true;
            return;
        }
        age -= deltaTime;  //in ms
//        System.out.println("Age: "+age);

        calculateCenter();
        updatePos(deltaTime);
        updateRot(deltaTime);
        updateScale(deltaTime);
    }

    @Override
    void gameRender(Graphics2D g) {
        if (age < 0) {
            return;
        }

        /*
         g.scale(x, y);
         g.rotate(rot);
         g.translate(x, y);
         */
        g.setColor(color);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform old = g.getTransform();
        AffineTransform trans = new AffineTransform();

        trans.translate(centerPoint.x, centerPoint.y);
        trans.scale(scale, scale);
        trans.rotate(Math.toRadians(rotation));
        trans.translate(-centerPoint.x, -centerPoint.y);
        g.setTransform(trans);

        g.fillRect((int) position.x, (int) position.y, (int) width, (int) height);
//        g.fillOval((int) position.x, (int) position.y, width, height);
        g.setTransform(old);
    }

}
