/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesystem;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Remove rotation -> add alpha compositing
 * 
 * 26-Jul-2016, 01:12:17.
 *
 * @author Mo
 */
public class Particle extends GameObject {
    private Random r;
    
    //Particles lifespan in ms
    protected float age;
    
    //Vectors to represent a particle
    /*private Vector2D position;*/
    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected Vector2D gravity;
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
    
    private Composite blendMode;
    
    private long start;
    

    
    public Particle(float age, Vector2D pos, Vector2D vel, Vector2D acc,
            float damp, float initRot, float initRotVel, float initRotDamp,
            float initScale, float initScaleVel, float initScaleAcc, 
            float initScalemax
            ) {
        //Width of our pixel (5)
        width = 15;
        height = 15;
        r = new Random();
        color = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        //Should be between 0 -> 10 otherwise too large

        //From GameObject
        this.age = age;
        position = pos;
        velocity = vel;
        acceleration = acc;
        gravity = new Vector2D(0, 9.8);//defult gravity
        dampening = damp;//between 0.0 -> 1.0
        
        this.rotation = initRot;
        this.rotationVel = initRotVel;
        this.rotationDamp = initRotDamp;
        
        this.scale = initScale;
        this.scaleVel = initScaleVel;
        this.scaleAcc = initScaleAcc;
        this.scaleMax = initScalemax;
        
        start = System.currentTimeMillis();
        
        blendMode = AlphaComposite.Src;
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
        start = System.currentTimeMillis();
        age = 3000;//ms
    }

    
    private void updatePos(float deltaTime){
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
    
    private void updateRot(float deltaTime){
        rotation *= rotationDamp;
        rotation += rotationVel* deltaTime;
    }
    
    private void updateScale(float deltaTime){
        scaleVel += scaleAcc * deltaTime; 
        scale += scaleVel * deltaTime;
        scale = Helper.Clamp((float) scale, 0.0f, scaleMax);
        System.out.println("scale = " + scale);
    }

    @Override
    void gameUpdate(float deltaTime) {
        //dt = 0.016 (60fps) dt = 0.032 (30fps)
//        System.out.println("deltaTime: "+deltaTime);
        
        if(age<0){
            return;
        }
//        long num = (System.currentTimeMillis() - start);
//        System.out.println("dt: "+deltaTime);
//        System.out.println("elapsed: "+num);
        age -= deltaTime*1000;  //in ms
//        System.out.println("Age: "+age);
        
        updatePos(deltaTime);
        updateRot(deltaTime);
        updateScale(deltaTime);
//        System.out.println(position.y);
        
//        //If we reach the ground
//        if(position.y>GamePanel.GAME_HEIGHT-height){
//            velocity.x=0;
//            float elapsedTime = System.currentTimeMillis() - start;
//            System.out.println("\nTime taken to reach ground: "+elapsedTime/1000);
////            System.out.println("xPos: "+position.x);
//            position.y = GamePanel.GAME_HEIGHT-height;
//        }
        
        
        //Update velocities position by applying gravity
//        velocity = velocity.add(gravity.x*deltaTime, gravity.y*deltaTime);
//        //Update the particles position
//        position.add(velocity.x*deltaTime, velocity.y*deltaTime);
    }

    @Override
    void gameRender(Graphics2D g) {
        if(age<0){
            return;
        }
        g.setComposite(blendMode);
        g.setColor(color);
        
        /*
        g.scale(x, y);
        g.rotate(rot);
        g.translate(x, y);
        */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
        AffineTransform pre = g.getTransform();
        AffineTransform tran = new AffineTransform();

//        tran.translate(GamePanel.GAME_WIDTH/2, GamePanel.GAME_HEIGHT/2);
        tran.translate(position.x+width/2, position.y+height/2);
        tran.rotate(Math.toRadians(rotation));
//        tran.scale(scale, scale);
        tran.translate(-position.x+width/2, -position.y+height/2);
//        tran.translate(-GamePanel.GAME_WIDTH/2, -GamePanel.GAME_HEIGHT/2);

        g.setTransform(tran);
        g.fillRect((int)position.x, (int)position.y, width, height);
//        g.fillOval((int) position.x, (int) position.y, width, height);
        g.setTransform(pre);
    }


}

//    //Move when key is pressed
//    public void keyPressed(KeyEvent e) {
//        int key = e.getKeyCode();
//
//        //Update current state depending on movement
//        if (key == KeyEvent.VK_UP) {
//            moveUp();
//        } else if (key == KeyEvent.VK_LEFT) {
//            moveLeft();
//        } else if (key == KeyEvent.VK_RIGHT) {
//            moveRight();
//        } else if (key == KeyEvent.VK_DOWN) {
//            moveDown();
//        }
//
////        if(key == KeyEvent.VK_R){
////            world.resetWorld();
////            //resetWorld();
////        }
//    }
//    
//    void keyReleased(KeyEvent e) {
//        int key = e.getKeyCode();
//
//        //Update current state depending on movement
//        if (key == KeyEvent.VK_UP) {
//            velocity.setY(0);
//        } else if (key == KeyEvent.VK_LEFT) {
//            velocity.setX(0);
//        } else if (key == KeyEvent.VK_RIGHT) {
//            velocity.setX(0);
//        } else if (key == KeyEvent.VK_DOWN) {
//            velocity.setY(0);
//        }
//    }
//
//    private void moveUp() {
//        //System.out.println("UP");
//        velocity.setY(-movementSpeed);
//    }
//
//    private void moveLeft() {
//        //System.out.println("LEFT");
//        velocity.setX(-movementSpeed);
//    }
//
//    private void moveRight() {
//        //System.out.println("RIGHT");
//        velocity.setX(movementSpeed);
//    }
//
//    private void moveDown() {
//        //System.out.println("DOWN");
//        velocity.setY(movementSpeed);
//    }