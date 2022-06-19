package application;

import com.mathworks.engine.MatlabEngine;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
    private Boolean valid = false;
    
    public Test (String n, String s, Integer speed, List<Point2D> points, List<Point2D> interpolated, String sPX, String sPY){
        this.name = n;
        this.simulator = s;
        this.speed = speed;
        this.points = points;
        this.interpolated = interpolated;
        this.stringPointX = sPX;
        this.stringPointY = sPY;
        valid = false;
    }
    
    public Test(){
        this.name = "";
        this.simulator = "";
        this.speed = 0;
        this.points = new ArrayList<>();
        this.interpolated = new ArrayList<>();
        this.stringPointX = "";
        this.stringPointY = "";
        valid = false;
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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
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

    public Boolean checkValidity(){
        Boolean valid = false;
        String res = null;
            try {
                String lines = null;
                ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\validate_test.py", this.stringPointX.substring(0, this.stringPointX.length()-1), this.stringPointY.substring(0, this.stringPointY.length()-1));
                Process process = builder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                res=reader.readLine();
                BufferedReader readerE = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((lines=readerE.readLine())!=null){
                    System.out.println("Error : "+lines);
                }
                if (res.equals("Test seems valid")){
                    JOptionPane.showMessageDialog(null, res);
                    if(speed!=null){
                        writePointsOnFile(this.stringPointX.substring(0, this.stringPointX.length()-1), this.stringPointY.substring(0, this.stringPointY.length()-1), speed.toString());
                    } //se il test è valido scrive i punti scelti sul file chosenPoint.txt
                    this.valid = true;
                }
                else JOptionPane.showMessageDialog(null,res,"Test invalid",JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.valid; 
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
    
    public void writeNameOnFile(String path) throws Exception{
        FileWriter myWriter = new FileWriter("testName.txt");
        myWriter.write(path);
        myWriter.close();
    }
    
    public void executeTest(String path) throws InterruptedException{
        if (points.size()<=3){
            JOptionPane.showMessageDialog(null,"Insufficient number of points","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!this.valid){
            if(!checkValidity()){
            return;
            }
        }
        switch (this.simulator) {
            case "BeamNG":
                executeTestBeamNG(path);
                break;
            case "MATLAB":
                executeTestMATLAB(path);
                break;
            case "All":
                executeTestBeamNG(path);            
                executeTestMATLAB(path);
                break;
            default:
                break;
        }
    }
    
    
    
    public void executeTestBeamNG(String path){
        List<Double> oob = new ArrayList<>();
        Boolean flag = false;
        try{
            writePointsOnFile(this.stringPointX.substring(0, this.stringPointX.length()-1), this.stringPointY.substring(0, this.stringPointY.length()-1), speed.toString());
            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\competition.py","--module-name", "rs_anglelength.run_al", "--class-name", "AngleLengthGenerator", "--time-budget", "10000", "--executor", "beamng", "--map-size", "500");//, "--visualize-tests");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader readerE = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String lines = null;
            while((lines=reader.readLine())!=null){
                System.out.println("Output: "+lines);
                if(lines.equals("end")){
                    flag = false;
                }
                if (flag){
                    oob.add(Double.parseDouble(lines));
                }
                if (lines.equals("oob")){
                    flag = true;
                }
            }
            while ((lines=readerE.readLine())!=null){
                System.out.println("Error : "+lines);
                if (lines.contains("AssertionError: Car drove out of the lane")){
                    JOptionPane.showMessageDialog(null,"Car drove out of the lane!");
                }
                if (lines.contains("Connessione in corso interrotta forzatamente dall'host remoto")){
                    JOptionPane.showMessageDialog(null,"Connessione in corso interrotta forzatamente dall'host remoto!");
                }
            }
            createAndSaveChart(oob,path);
            } catch(Exception e){
                System.out.println("Errore:");
                e.printStackTrace();
            }
    }
    
    public void executeTestMATLAB(String path){
        try{
            MatlabEngine eng = MatlabEngine.startMatlab();
            eng.eval("cd 'C:\\Users\\kikki\\Documents\\MATLAB\\Examples\\R2022a\\autonomous_control\\LaneKeepingAssistWithLaneDetectionExample'");
            StringWriter writerO = new StringWriter();
            StringWriter writerE = new StringWriter();
            String xInterpolated = this.getStringXinterpolated();
            String yInterpolated = this.getStringYinterpolated();
            writePointsOnFile(xInterpolated, yInterpolated, speed.toString()); // scrivo i punti *scelti* (NON interpolati - da aggiungere) sul file chosenPoint.txt
            if (this.getName().equals("")){
                LocalDateTime dateTime = LocalDateTime.now();
                String timeStamp = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss").format(dateTime);
                writeNameOnFile(path+"\\"+timeStamp+"_MATLAB.png");
            }
            else {
                writeNameOnFile(path+"\\"+this.getName()+"_MATLAB.png");
            }
            eng.eval("runScenario");
        }catch (Exception e){
            System.out.println("Errore:");
            e.printStackTrace();
        }
    }
    
    public void createAndSaveChart(List<Double> oob, String path) throws IOException{
        XYSeries series = new XYSeries("oob");
        DefaultXYDataset ds = new DefaultXYDataset();
        double[] time = new double[oob.size()];
        double[][] data = new double[oob.size()][oob.size()];
        for (int i = 0; i<oob.size();i++){
            double sec = ((double) (i))/100.0;
            series.add(sec,oob.get(i)*100);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createScatterPlot(
        "Simulation result",
        "Time (sec)",
        "Out of Bound (%)",
        dataset,
        PlotOrientation.VERTICAL,
        false,
        false,
        false
        );
        JFrame frame = new JFrame("Charts");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        ChartPanel cp = new ChartPanel(chart);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        frame.getContentPane().add(cp);
        if (this.getName().equals("")){
            LocalDateTime dateTime = LocalDateTime.now();
            String timeStamp = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss").format(dateTime);
            ChartUtilities.saveChartAsPNG(new File(path+"\\"+timeStamp+"_BeamNG.png"), chart, 600, 400);
        }
        else {
            ChartUtilities.saveChartAsPNG(new File(path+"\\"+this.getName()+"_BeamNG.png"), chart, 600, 400);
        }
        
    }
    
}
