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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Anna
 */
public class RoadDrawer extends JPanel{
    
    private Test test = new Test();
    private Boolean interpolatedS = false;
    
    public RoadDrawer(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                Point2D p = new Point2D.Double(e.getX(),e.getY());
                interpolatedS = false;
                test.getPoints().add(p);
                test.addToStringPointX(p.getX());
                test.addToStringPointY(p.getY());
                repaint();
            }
        });
    }
    public void setPoints(List<Point2D> p){
        this.test.setPoints(new ArrayList<>(p));
        repaint();
    }
    
    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Boolean getInterpolatedS() {
        return interpolatedS;
    }

    public void setInterpolatedS(Boolean interpolatedS) {
        this.interpolatedS = interpolatedS;
    }
    
    
    
    public void interpolatePoints (){ //passo a python i punti scelti (attraverso le stringhe di punti) e ottengo quelli interpolati
        try {
            test.getInterpolated().clear();
            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\createSpline.py", test.getStringPointX().substring(0, test.getStringPointX().length()-1), test.getStringPointY().substring(0, test.getStringPointY().length()-1));
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
                test.getInterpolated().add(p);
                
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

    
    public boolean checkPoints(String speed){ // validità del test
        String res = null;
        boolean valid = false;
        try {
            String lines = null;
            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\validate_test.py", test.getStringPointX().substring(0, test.getStringPointX().length()-1), test.getStringPointY().substring(0, test.getStringPointY().length()-1));
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
                    writePointsOnFile(this.test.getStringPointX().substring(0, this.test.getStringPointX().length()-1), this.test.getStringPointY().substring(0, test.getStringPointY().length()-1), speed);
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
        BufferedImage background = null;
        try {
            background = ImageIO.read(new File("C:\\Users\\kikki\\Documents\\Tesi\\prato.jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(RoadDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.setColor(Color.gray);
        for (Point2D p : test.getPoints()){
                g2.fillOval((int)p.getX(),(int)p.getY(),5,5);
            }
        if (test.getPoints().size()>3){
            try {
                if (!interpolatedS){
                    interpolatePoints();
                }
                for (int i=1; i<test.getInterpolated().size(); i++){
                    Line2D line = new Line2D.Double(test.getInterpolated().get(i-1),test.getInterpolated().get(i));
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
