package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anna
 */
public class Test {
    private String name;
    private String simulator;
    private Integer speed;
    private List<Point2D> points = new ArrayList<>();
    private List<Point2D> interpolated = new ArrayList<>();
    private String stringPointX = "";
    private String stringPointY = "";
    
    public Test (String n, String s, Integer speed, List<Point2D> points, List<Point2D> interpolated, String sPX, String sPY){
        this.name = n;
        this.simulator = s;
        this.speed = speed;
        this.points = points;
        this.interpolated = interpolated;
        this.stringPointX = sPX;
        this.stringPointY = sPY;
    }
    
    public Test(String s,Integer speed){
        this.name = "";
        this.simulator = s;
        this.speed = speed;
        this.points = new ArrayList<>();
        this.interpolated = new ArrayList<>();
        this.stringPointX = "";
        this.stringPointY = "";
    }
    
    public Test(){
        this.name = "";
        this.simulator = "";
        this.speed = 0;
        this.points = new ArrayList<>();
        this.interpolated = new ArrayList<>();
        this.stringPointX = "";
        this.stringPointY = "";
    }
    
    public String getStringXinterpolated(){
        String res = "";
        for (Point2D p : interpolated){
            res = res + p.getX()+" ";
        }
        res = res.substring(0, res.length()-1);
        return res;
    }
    
    public String getStringYinterpolated(){
        String res = "";
        for (Point2D p : interpolated){
            res = res + p.getY()+" ";
        }
        res = res.substring(0, res.length()-1);
        return res;
    }
    
    public void addToStringPointX(double x){
        this.stringPointX = stringPointX + x +",";
    }
    
    public void addToStringPointY(double y){
        this.stringPointY = stringPointY + y +",";
    }
    
    public void clearStringPointX(){
        this.stringPointX = "";
    }
    
    public void clearStringPointY(){
        this.stringPointY = "";
    }

    public String getName() {
        return name;
    }

    public String getSimulator() {
        return simulator;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSimulator(String simulator) {
        this.simulator = simulator;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
    
    public List<Point2D> getPoints(){
        return this.points;
    } 
    
    public void setPoints(List<Point2D> points) {
        this.points = points;
    }
 
    public void setSpeed(int speed){
        this.speed = speed;
    }

    public List<Point2D> getInterpolated() {
        return interpolated;
    }

    public void setInterpolated(List<Point2D> interpolated) {
        this.interpolated = interpolated;
    }

    public String getStringPointX() {
        return stringPointX;
    }

    public void setStringPointX(String stringPointX) {
        this.stringPointX = stringPointX;
    }

    public String getStringPointY() {
        return stringPointY;
    }

    public void setStringPointY(String stringPointY) {
        this.stringPointY = stringPointY;
    }
    
    
}
