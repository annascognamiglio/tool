package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Anna
 */
public class RoadDrawer extends JPanel{
    
    private List<Point2D> points = new ArrayList<>();
    private List<Point2D> interpolated = new ArrayList<>();
    private int speed = 50;
    private String stringPointX = "";
    private String stringPointY = "";
    
    public RoadDrawer(){
        setBackground(new Color(23,99,8));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                System.out.println("Nuovo punto cliccato: "+e.getX() + ", " + e.getY());
                Point2D p = new Point2D.Double(e.getX(),e.getY());
                points.add(p);
                stringPointX = stringPointX + p.getX()+",";
                stringPointY = stringPointY + p.getY()+",";
                repaint();
            }
        });
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
        
    public List<Point2D> getPoints(){
        return this.points;
    } 
    
    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    public void setPoints(List<Point2D> p){
        this.points = p;
        repaint();
    }
    
    public void setInterpolatedPoints(List<Point2D> p){
        this.interpolated = p;
    }
    
    public int getSpeed(){
        return this.speed;
    }
    
    public List<Point2D> getInterpolatedPoints(){
        return this.interpolated;
    }
    
    public String getStringPointX(){
        return this.stringPointX;
    }
    
    public String getStringPointY(){
        return this.stringPointY;
    }
    
    public void setStringPointX(String stringPointX){
        this.stringPointX = stringPointX;
    }
    
    public void setStringPointY(String stringPointY){
        this.stringPointY = stringPointY;
    }
    
    public void clearStringPointX(){
        this.stringPointX = "";
    }
    
    public void clearStringPointY(){
        this.stringPointY = "";
    }
    
    public void interpolatePoints (){ //passo a python i punti scelti (attraverso le stringhe di punti) e ottengo quelli interpolati
        try {
            interpolated.clear();
            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\createSpline.py", stringPointX.substring(0, stringPointX.length()-1), stringPointY.substring(0, stringPointY.length()-1));
            Process process = builder.start();
            BufferedReader readerI = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String lines = null;
            while ((lines=readerI.readLine())!=null){
                String[] newpoints = lines.split(",");
                String xnew = newpoints[0];
                String ynew = newpoints[1];
                Point2D p = new Point2D.Double(
                                    Double.parseDouble(xnew),
                                    Double.parseDouble(ynew)
                                );
                interpolated.add(p);
                
            }
            BufferedReader readerE = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            lines = null;
            while ((lines=readerE.readLine())!=null){
                System.out.println("Error : "+lines);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getSpeedFromUser (String message){
        String speed = JOptionPane.showInputDialog(message);
        if (speed==null) return 0;
        try {
            Integer.parseInt(speed);
            if (Integer.parseInt(speed)<0) return getSpeedFromUser(message);
            return Integer.parseInt(speed);        
        }
        catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this,"Speed invalid","Speed invalid",JOptionPane.ERROR_MESSAGE);
        }
        return getSpeedFromUser(message);
    }
    
    public boolean checkPoints(String speed){ // validità del test
        String res = null;
        boolean valid = false;
        try {
            String lines = null;
            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\validate_test.py", stringPointX.substring(0, stringPointX.length()-1), stringPointY.substring(0, stringPointY.length()-1));
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            res=reader.readLine();
            BufferedReader readerE = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((lines=readerE.readLine())!=null){
                System.out.println("Error : "+lines);
            }
            if (res.equals("Test seems valid")){
                JOptionPane.showMessageDialog(this, res);
                if(speed!=null){
                    writePointsOnFile(this.stringPointX.substring(0, this.stringPointX.length()-1), this.stringPointY.substring(0, stringPointY.length()-1), speed);
                } //se il test è valido scrive i punti scelti sul file chosenPoint.txt
                valid = true;
            }
            else JOptionPane.showMessageDialog(this,res,"Test invalid",JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valid;
    }
    
    public void writePointsOnFile(String x, String y, String speed) throws Exception{
        FileWriter myWriter = new FileWriter("chosenPoint.txt");
        myWriter.write(x);
        myWriter.write("\n");
        myWriter.write(y);
        myWriter.write("\n");
        myWriter.write(speed);
        myWriter.close();
    }
    
    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.setColor(Color.gray);
        for (Point2D p : points){
                g2.fillOval((int)p.getX(),(int)p.getY(),5,5);
            }
        if (points.size()>3){
            try {
                interpolatePoints();
                for (int i=1; i<interpolated.size(); i++){
                Line2D line = new Line2D.Double(interpolated.get(i-1),interpolated.get(i));
                g2.setColor(Color.gray);
                g2.setStroke(new BasicStroke(10));
                g2.draw(line);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.yellow);
                g2.draw(line);
                }
            } catch (Exception ex) {
                Logger.getLogger(RoadDrawer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
