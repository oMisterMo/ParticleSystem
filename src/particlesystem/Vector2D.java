/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particlesystem;

/**
 * Change to floats!!!!
 * 
 * 21-Jul-2016, 19:05:58.
 *
 * @author Mo
 */
public class Vector2D {

    public final static double PI = 3.14;
    
    //Magnitude
    protected double x;
    protected double y;

//    //Direction or angle
//    protected float angle = 0f;

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }
    
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    /*Getters & Setters*/
    
    /**
     * If we have hypotenuse and angle, we can find x and y components of Vector
     * 
     * @param angle 
     */
    public void setAngle(double angle) {
        double length = this.getLength();
        this.x = Math.cos(angle) * length;
        this.y = Math.sin(angle) * length;
    }

    /**
     * Can find angle if we know x and y components of Vector
     * 
     * @return 
     */
    public double getAngle() {
        //System.out.println("degrees:"+ (Math.atan2(y, x) * 180)/Math.PI);
        return Math.atan2(this.y, this.x);
    }

    public void setLength(double length) {
        double angle = this.getAngle();
        this.x = Math.cos(angle) * length;
        this.y = Math.sin(angle) * length;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * Quicker to remove square root for distance comparison
     * 
     * @return 
     */
    public double lengthSqr(){
        return this.x * this.x + this.y * this.y;
    }
    
    /**
     * Unit vector has a length of 1
     * 
     * @return unit vector
     */
    public Vector2D normalize(){
        Vector2D nomarlize = new Vector2D();
        double length = getLength();
        nomarlize.x = this.x/length;
        nomarlize.y = this.y/length;
        return nomarlize;
    }

    
    
    
    
    
    /**
     * NEW CODE
     * 
     * @param one
     * @param two
     * @return 
     */
    public Vector2D add(double one, double two){
        this.x += one;
        this.y += two;
        return this;
    }
    
    /* Returns a new vector so we can override methos 
    e.g. 
    v3 = v1 + v2 
        instead of
    v3 = v1.add(v2)
    */
    public Vector2D add(Vector2D v2) {
//        x += v2.x;
//        y += v2.y;
        Vector2D v3 = new Vector2D(x + v2.x, y + v2.y);
        return v3;
    }
    

    public Vector2D sub(Vector2D v2) {
//        x -= v2.x;
//        y -= v2.y;
        Vector2D v3 = new Vector2D(x - v2.x, y - v2.y);
        return v3;
    }

    public Vector2D mult(float val) {
//        x *= val;
//        y *= val;
        Vector2D v3 = new Vector2D(x * val, y * val);
        return v3;
    }
    
    public Vector2D div(float val){
        Vector2D v3 = new Vector2D(x / val, y / val);
        return v3;
    }

    public void addToo(Vector2D two){
        this.x += two.x;
        this.y += two.y;
    }
    
    public void subFrom(Vector2D two){
        this.x -= two.x;
        this.y -= two.y;
    }
    
    public void multBy(float scale){
        this.x *= scale;
        this.y *= scale;
    }
    
    public void divBy(float scale){
        this.x /= scale;
        this.y /= scale;
    }
    
    public void addToo(double vx, double vy){
        this.x += vx;
        this.y += vy;
    }
    
    //P to I: P = I - P (destination - source)
    public void subFrom(double vx, double vy){
        this.x -= vx;
        this.y -= vy;
    }
    
    
    public static double toRad(int degree){
        
        return 0;
    }
    
    public static int toDeg(double radian){
        
        return 0;
    }
    
    @Override
    public String toString(){
        return "x: " + this.x+ " y: " + this.y;
    }
}
