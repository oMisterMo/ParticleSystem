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

/**
 * Game screen which ties together all classes.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class GamePanel extends JPanel implements Runnable {

    //GAMES WIDTH & HEIGHT
    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 600;

    private Thread thread;
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;

    private final int FPS = 60; //60 fps limit
    private long averageFPS;

    //GAME VARIABLES HERE-------------------------------------------------------
    private Color backgroundColor;
    private Particle particle;

    private Fireworks fireworkSystem;
    private float elapsed = 0;

    /**
     * Default constructor.
     */
    public GamePanel() {
        super();
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setFocusable(true);
        //System.out.println(requestFocusInWindow());
        requestFocus(); //-> platform dependant

        //initialise varialbes
        init();
    }

    private void init() {
        backgroundColor = new Color(0, 0, 0);
        fireworkSystem = new Fireworks();

        //Load listeners
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
    }

    //METHODS
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
        long lastTime = System.currentTimeMillis();
        long timeTaken = 0;
        long frameCount = 0;
        long totalTime = 0;
        long waitTime;
        double targetTime = 1000d / FPS;   //16.67ms per frame
        System.out.println("targetTime: " + targetTime);

        running = true;
        image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        //GAME LOOP
        while (running) {
            /*Update and render*/

            long current = System.currentTimeMillis();
            float deltaTime = (current - lastTime) / 1000f; //ms -> seconds
            //Cap deltaTime
            if (deltaTime > 0.1f) {
                deltaTime = 0.1f;
                System.out.println("Capped dt: " + deltaTime);
            }
            elapsed += deltaTime;
            //real time passed since the last game update
//            System.out.printf("dt %f : elapsed %f\n", deltaTime, elapsed);
            gameUpdate(deltaTime);
            gameRender(g);
            gameDraw();
            lastTime = current;

            /*Calculate sleep time*/
            waitTime = (long) ((targetTime + current - System.currentTimeMillis()));
            try {
                //System.out.println("Sleeping for: " + waitTime);
                //thread.sleep(waitTime);
                Thread.sleep(waitTime);
            } catch (Exception ex) {

            }
            totalTime += System.currentTimeMillis() - current;
            frameCount++;

            /*Debug*/
            //Calculate average fps
            if (frameCount >= FPS) {
                averageFPS = 1000 / ((totalTime / frameCount));
                frameCount = 0;
                totalTime = 0;
                System.out.println("Average fps: " + averageFPS);
            }
            //Print negative wait time
            if (waitTime < 0) {
                //I get a negative value at the beg
                System.out.println("NEGATIVE: " + waitTime);
                System.out.println("timeTaken = "
                        + (System.currentTimeMillis() - current));
            }
        }
    }

    private void gameUpdate(float deltaTime) {
        //********** Do updates HERE **********
        fireworkSystem.update(deltaTime);
    }

    private void gameRender(Graphics2D g) {
        //Clear screen
        g.setColor(backgroundColor);
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        //********** Do drawings HERE **********
        fireworkSystem.draw(g);

        //Draw text information
        g.setColor(Color.RED);
        g.drawString("FPS:" + averageFPS, 10, 20);
    }

    private void gameDraw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    //Inner Class
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            //Handle player from world movement
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class MAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("CLICKED");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //System.out.println("PRESSED");
            fireworkSystem.mousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("RELEASED");
//            particle.mouseReleased(e);
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
}
