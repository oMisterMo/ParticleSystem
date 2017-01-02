
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 07-Nov-2016, 02:18:49.
 *
 * @author Mo
 */
public class myColor extends Color{
    
    float red;
    float green;
    float blue;
    float alpha;
    
    public myColor(float r, float g, float b, float a) {
        super(r, g, b, a);
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }
    
}
