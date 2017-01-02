/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesystem;

/**
 * 07-Nov-2016, 01:50:14.
 *
 * @author Mo
 */
public class Helper {

    float value;
    float min;
    float max;
    
    public Helper(){
        value = 0f;
        min = 0f;
        max = 0f;
    }
    public static float Clamp(float val, float min, float max){
        if(val < min){
            return min;
        }else if(val > max){
            return max;
        }
        //We have a value within the range
        return val;
    }
}
