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

import common.Vector2D;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Incomplete implementation of the Effect class from John Piles book,
 * 2D graphic programming for games.
 *
 * @version 0.1.0
 * @author Mohammed Ibrahim
 */
public class Effect extends GameObject {

    public BufferedImage texture;

    public Vector2D origin;
    public float originRadius;

    public float effectDuration;
    public int newParticleAmount;
    public float burstFrequency;  //ms
    public float burstCountdown;  //ms

    public Effect() {
        effectDuration = 10000;
        newParticleAmount = 1;
        burstFrequency = 16;
        burstCountdown = burstFrequency;
    }

    private void createParticle() {
        System.out.println("CREATING");

    }

    @Override
    void gameUpdate(float deltaTime) {
        effectDuration -= deltaTime;
        burstCountdown -= deltaTime;

        if ((burstCountdown <= 0) && (effectDuration >= 0)) {
            for (int i = 0; i < newParticleAmount; i++) {
                createParticle();
                burstCountdown = burstFrequency;
            }
        }

//        //Loop through particles and update
//        for (int i = particles.size() - 1; i >= 0; i--) {
//            Particle p = particles.get(i);
//            p.gameUpdate(deltaTime);
//
//            if (p.isDead()) {
//                particles.remove(p);
//            }
//        }
    }

    @Override
    void gameRender(Graphics2D g) {
    }

}
