package particlesystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 16/05/2016
 * 
 * @author Mo
 */
public class GamePanel extends JPanel implements Runnable {

    //GAMES WIDTH & HEIGHT
    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 600;

    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;

    private final int FPS = 60;
    
    private long averageFPS;
    //dont need -> Checks to see if game loop sleeps for negative time
    private int counter = 0;


    //GAME VARIABLES HERE-------------------------------------------------------
    private final static int NO_OF_PARTICLES = 53;
    
    private Color backgroundColor;    //Represents colour of background
//    private Particle[] p;
    private ArrayList<Particle> fireworks;
    
    private Particle particle;

    

    //CONSTRUCTOR
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GAME_WIDTH-10, GAME_HEIGHT-10));
        setFocusable(true);
        //System.out.println(requestFocusInWindow());
        requestFocus(); //-> platform dependant
        
        //initialise varialbes
        init();
    }
    
    private void init(){
        backgroundColor = new Color(0, 0, 0);    //Represents colour of background
        //backgroundColor = new Color(255, 255, 255);    //Represents colour of background
        
        fireworks = new ArrayList<>(NO_OF_PARTICLES);
//        p = new Particle[NO_OF_PARTICLES];
        
        //comment out below
//        for(int i=0; i<NO_OF_PARTICLES; i++){
//            //Math.Pi doesn't matter for now, calls methods random funct
//            //p[i] = new Particle(GAME_WIDTH/2, GAME_HEIGHT/2, 3, 00000, 0.2);
////            fireworks.add(new Particle(GAME_WIDTH/2, GAME_HEIGHT/2, 60.0, 0.3));
//            
//                                                               //speed, gravity
//            fireworks.add(new Particle(GAME_WIDTH/2, 100, 15.0, 10.0));
//        }
        
        
        //Read about graph representation
        int oDeg = 53;
        int deg = 360-oDeg;
        System.out.println("deg: "+oDeg + " (fake: "+deg+")");
        System.out.println("rad: "+Math.toRadians(deg));
                                            //angle, magnitude
        //Age
        float age = 8000f;
        //Position/velocity
        Vector2D position = new Vector2D(400,400);
        Vector2D velocity = new Vector2D(0, -100);
        Vector2D acceleration = new Vector2D(0, 75);
        float damp = 1.0f;
        //Rotation
        float initRot = 45.0f;   //rot += rotVel;
        float initRotVel = 45.0f;
        float initRotDamp = 1.0f; //no dampening
        //Scale
        float initScale = 1.0f;
        float initScaleVel = 0.2f;
        float initScaleAcc = -0.1f;
        float initScaleMax = 2.0f;
        
        particle = new Particle(age, position, velocity, acceleration, damp,
        initRot, initRotVel, initRotDamp,
        initScale, initScaleVel, initScaleAcc, initScaleMax);
        
        //Load listeners
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
    }

    //METHODS
    
    /*
     Is called after our JPanel has been added to the JFrame component.
    */
    @Override
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        float deltaTime;
        long timeTaken;
        long frameCount = 0;
        long totalTime = 0;
        long waitTime;
        long targetTime = 1000 / FPS;
        
        //To test independent speed
        long start2 = 0;
        long timeMillis2 = 0;
        long timeMillis3 = 0;
        long timeMillis4 = 0;

        running = true;

        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //GAME LOOP
        while (running) {
            deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            
            //dt = 0.016 (60fps) dt = 0.032 (30fps)
//            //lock time so physics doesn't go crazy
//            if(deltaTime > 0.013){
//                deltaTime = 0.013f;
//            }
            
//            System.out.println("Delta Time: "+deltaTime+"s");
            //start2 = System.nanoTime();
            gameUpdate(deltaTime);
            //timeMillis2 = (System.nanoTime() - start2) / 1000000;

            //start2 = System.nanoTime();
            gameRender(g);
            //timeMillis3 = (System.nanoTime() - start2) / 1000000;

            //start2 = System.nanoTime();
            gameDraw();
            //timeMillis4 = (System.nanoTime() - start2) / 1000000;
            
//            gameUpdate();
//            gameRender(g);
//            gameDraw();
            
            //How long it took to run
            timeTaken = (System.nanoTime() - startTime) / 1000000;
            //              16ms - targetTime
            waitTime = targetTime - timeTaken;

            //System.out.println(timeTaken);
            if (waitTime < 0) {
                //I get a negative value at the beg
                System.out.println(counter++ + " - NEGATIVE: " + waitTime);
                System.out.println("targetTime = "+ targetTime);
                System.out.println("timeTaken = " + timeTaken+"\n");
            }
            
//            //Speed TEST methods
//            if(frameCount >= 58) {
//                //Test the time taken to run
//                System.out.println("Update time: " + timeMillis2);
//                System.out.println("Render time: " + timeMillis3);
//                System.out.println("Draw time: " + timeMillis4);
//                System.out.println("------------------------------------------");
//            }
            
            try {
                //System.out.println("Sleeping for: " + waitTime);
                //thread.sleep(waitTime);
                Thread.sleep(waitTime);
            } catch (Exception ex) {

            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;

            //If the current frame == 60  we calculate the average frame count
            if (frameCount >= FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println("Average fps: " + averageFPS);
            }
        }

    }

    private void gameUpdate(float deltaTime) {
        
        //********** Do updates HERE **********
        
//        for(Particle p: fireworks){
//            p.gameUpdate(deltaTime);
//        }
//        System.out.println("");
        
        particle.gameUpdate(deltaTime);
        
//        //Check for collisions
//        if(Collides(head.get(0), food)){
//            food.drawNewFood();
//            addBody();
//        }
    }

    private void gameRender(Graphics2D g) {
        //Clear screen
        g.setColor(backgroundColor);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        
        //********** Do drawings HERE **********
        
//        for(Particle p: fireworks){
//            p.gameRender(g);
//        }
        
        particle.gameRender(g);
        
        //Draw number line
        g.setColor(Color.WHITE);
        for(int i=50; i<GAME_WIDTH; i+=50){
            g.drawString(Integer.toString(i), i, GAME_HEIGHT - 40);
            g.drawString(Integer.toString(i), 0+40, GAME_HEIGHT-i);
        }
//        for(int i=GAME_HEIGHT; i>50; i-=50){
//        }
        g.drawString("Meters (1px = 1m)", GAME_WIDTH/2 - 150, GAME_HEIGHT-17);
        //Draw text information
        g.setColor(Color.RED);
        g.drawString("FPS:" + averageFPS, 25, 25);    
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }
    
    private boolean Collides(GameObject one, GameObject two){
        //does first object collides with second?
        return one.getHitbox().intersects(two.getHitbox());
    }
    
    
    
    //Handle Input ** Inner Class
    private class TAdapter extends KeyAdapter{

        //When a key is pressed, let the CRAFT class deal with it.
        @Override
        public void keyPressed(KeyEvent e) {
            //Handle player from world movement
            
//            ship.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
//            ship.keyReleased(e);
        }
    }
    
    private class MAdapter implements MouseListener{


        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("CLICKED");
            
            //Clicked in one position
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("PRESSED");
            
//            test.mousePressed(e);
            //transition.mousePressed(e);
//            dragon.mousePressed(e);
            for(int i=0; i<fireworks.size(); i++){
                fireworks.get(i).mousePressed(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("RELEASED");
            particle.mouseReleased(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println("ENTERED");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //System.out.println("EXITED");
        }
        
    }
    
    //Getters and Setters
    public Color getColor(){
        return backgroundColor;
    }
    

}
