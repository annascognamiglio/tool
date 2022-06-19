package application;

import com.mathworks.engine.MatlabEngine;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Anna
 */
public class Service {
    
    public static void createAndSaveChart(List<Double> oob) throws IOException{
        XYSeries series = new XYSeries("oob");
        DefaultXYDataset ds = new DefaultXYDataset();
        double[] time = new double[oob.size()];
        double[][] data = new double[oob.size()][oob.size()];
        for (int i = 0; i<oob.size();i++){
            double sec = ((double) (i))/1000.0;
            series.add(sec,oob.get(i));
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ChartPanel cp = new ChartPanel(chart);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        frame.getContentPane().add(cp);
        ChartUtilities.saveChartAsPNG(new File("C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\Result\\chartBeamNG.png"), chart, 600, 400);
    }
    
    public static ActionListener actionOkButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean flag = false;
                String stringPointX = panel.getRoadDrawer().getTest().getStringPointX();
                String stringPointY = panel.getRoadDrawer().getTest().getStringPointY();
                String simulator = (String) panel.getComboSimulator().getSelectedItem();
                List<Double> oob = new ArrayList<>();
                List<Point2D> points = panel.getRoadDrawer().getTest().getPoints();
                if (points.size()<=1){
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Insufficient number of points","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (points.size()>1){
                    
                    //----------------BEAMNG----------------
                    
                    if (simulator.equals("BeamNG")){ 
                        try{
                            Integer speed = 0;
                            try{
                                speed = Integer.parseInt(panel.getSpeedField().getText());
                                panel.getRoadDrawer().getTest().setSpeed(speed);
                            }
                            catch(NumberFormatException e){
                                JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be a number!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (speed<=0) {
                                JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be greater than zero!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            boolean valid = panel.getRoadDrawer().checkPoints(Integer.toString(speed)); // CONTROLLO CHE IL TEST SIA VALIDO
                            if (!valid){
                                return;
                            }
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
                                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Car drove out of the lane!");
                                }
                                if (lines.contains("Connessione in corso interrotta forzatamente dall'host remoto")){
                                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Connessione in corso interrotta forzatamente dall'host remoto!");
                                }
                            }
                            
                            createAndSaveChart(oob);
                            
                            
                        } catch(Exception e){
                            System.out.println("Errore:");
                            e.printStackTrace();
                        }
                    }
                    
                    //----------------MATLAB----------------
                    
                    else if (simulator.equals("MATLAB")){
                        try{
                            Integer speed = 0;
                            try{
                                speed = Integer.parseInt(panel.getSpeedField().getText());
                                panel.getRoadDrawer().getTest().setSpeed(speed);
                            }
                            catch(NumberFormatException e){
                                JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be a number!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (speed<=0) {
                                JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be greater than zero!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            System.out.println("MATLAB");
                            boolean valid = panel.getRoadDrawer().checkPoints(null); // CONTROLLO CHE IL TEST SIA VALIDO
                            if (!valid){
                                return;
                            }
                            MatlabEngine eng = MatlabEngine.startMatlab();
                            eng.eval("cd 'C:\\Users\\kikki\\Documents\\MATLAB\\Examples\\R2022a\\autonomous_control\\LaneKeepingAssistWithLaneDetectionExample'");
                            StringWriter writerO = new StringWriter();
                            StringWriter writerE = new StringWriter();
                            stringPointX = panel.getRoadDrawer().getTest().getStringXinterpolated();
                            stringPointY = panel.getRoadDrawer().getTest().getStringYinterpolated();
                            panel.getRoadDrawer().writePointsOnFile(stringPointX, stringPointY, panel.getRoadDrawer().getTest().getSpeed().toString()); // scrivo i punti *scelti* (NON interpolati - da aggiungere) sul file chosenPoint.txt
                            eng.eval("runScenario");
                            
                            
                           
                            
                        } catch (Exception e){
                            System.out.println("Errore:");
                            e.printStackTrace();
                        }
                        

                    }
                }
            }
        };
    }
    public static ActionListener actionReset(ContainerRoadDrawer panel){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                RoadDrawer roadDrawer = panel.getRoadDrawer();
                roadDrawer.getTest().getPoints().clear();
                roadDrawer.getTest().getInterpolated().clear();
                roadDrawer.getTest().clearStringPointX();
                roadDrawer.getTest().clearStringPointY();
                roadDrawer.repaint();
            }
        };
    }
    public static ActionListener actionSave(ContainerRoadDrawer panel){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                JSONObject json = new JSONObject();
                JSONArray chosenPoints = new JSONArray();
                JSONArray interpolatedPoints = new JSONArray();
                for (Point2D point : panel.getRoadDrawer().getTest().getPoints()){
                    JSONObject p = new JSONObject();
                    p.put("x", String.valueOf(point.getX()));
                    p.put("y", String.valueOf(point.getY()));
                    chosenPoints.add(p);
                }
                for (Point2D point : panel.getRoadDrawer().getTest().getInterpolated()){
                    JSONObject p = new JSONObject();
                    p.put("x", String.valueOf(point.getX()));
                    p.put("y", String.valueOf(point.getY()));
                    interpolatedPoints.add(p);
                }
                json.put("points", chosenPoints);
                json.put("interpolated", interpolatedPoints);
                json.put("stringX",panel.getRoadDrawer().getTest().getStringPointX());
                json.put("stringY",panel.getRoadDrawer().getTest().getStringPointY());
                String name = JOptionPane.showInputDialog(null, "Nome del file:","Save", JOptionPane.INFORMATION_MESSAGE);
                if (name==null) return;
                try {
                    FileWriter file = new FileWriter("C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\savedFiles\\"+name+".json");
                    file.write(json.toJSONString());
                    file.close();
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Saved");
                    panel.getSavedFile();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        };
    }
    public static ActionListener actionOpen(ContainerRoadDrawer panel){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                List<Point2D> newPointsList = new ArrayList<>();
                List<Point2D> newInterpolatedList = new ArrayList<>();
                String fileName = actionEvent.getActionCommand();
                JSONParser jsonParser = new JSONParser();
                try (FileReader reader = new FileReader("C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\savedFiles\\"+fileName)){
                    Object obj = jsonParser.parse(reader);
                    JSONObject json = (JSONObject) obj;
                    JSONArray points = (JSONArray) json.get("points");
                    for (JSONObject p : (Iterable<JSONObject>)points){
                        double x = Double.valueOf((String) p.get("x"));
                        double y = Double.valueOf((String) p.get("y"));
                        Point2D newPoint = new Point2D.Double(x,y);
                        newPointsList.add(newPoint);
                    }
                    JSONArray interpolated = (JSONArray) json.get("interpolated");
                    for (JSONObject p : (Iterable<JSONObject>)interpolated){
                        double x = Double.valueOf((String) p.get("x"));
                        double y = Double.valueOf((String) p.get("y"));
                        Point2D newPoint = new Point2D.Double(x,y);
                        newInterpolatedList.add(newPoint);
                    }
                    Object stringX = json.get("stringX");
                    Object stringY = json.get("stringY");
                    panel.getRoadDrawer().getTest().setPoints(newPointsList);
                    panel.getRoadDrawer().getTest().setInterpolated(newInterpolatedList);
                    panel.getRoadDrawer().getTest().setStringPointX(stringX.toString());
                    panel.getRoadDrawer().getTest().setStringPointY(stringY.toString());
                    panel.getRoadDrawer().setInterpolatedS(true);
                    panel.getRootPane().repaint();
                           
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
    
    public static ActionListener actionAddButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Integer speed = 0;
                try{
                    speed = Integer.parseInt(panel.getSpeedField().getText());
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be a number!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (speed<=0) {
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Speed must be greater than zero!","Invalid speed",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean valid = panel.getRoadDrawer().checkPoints(Integer.toString(speed)); // CONTROLLO CHE IL TEST SIA VALIDO
                if (!valid){
                    return;
                }
                String name = JOptionPane.showInputDialog(null, "Test name:","Add", JOptionPane.INFORMATION_MESSAGE);
                if (name==null) return;
                String simulator = (String) panel.getComboSimulator().getSelectedItem();
                List<Point2D> points = new ArrayList<>(panel.getRoadDrawer().getTest().getPoints());
                List<Point2D> interpolated = new ArrayList<>(panel.getRoadDrawer().getTest().getInterpolated());
                String stringPointX = panel.getRoadDrawer().getTest().getStringPointX();
                String stringPointY = panel.getRoadDrawer().getTest().getStringPointY();                
                Test t = new Test(name,simulator,speed,points,interpolated,stringPointX,stringPointY);
                ((TestsTableModel)panel.getTestsTable().getModel()).getTestsGroup().add(t);
                ((TestsTableModel)panel.getTestsTable().getModel()).fireTableDataChanged();
            }
            
        };
    }
    
    public static ActionListener actionRemoveButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (panel.getTestsTable().getSelectedRow()!=-1){
                    ((TestsTableModel)panel.getTestsTable().getModel()).getTestsGroup().remove(panel.getTestsTable().getSelectedRow());
                    ((TestsTableModel)panel.getTestsTable().getModel()).fireTableDataChanged();
                    RoadDrawer roadDrawer = panel.getRoadDrawer();
                    roadDrawer.getTest().getPoints().clear();
                    roadDrawer.getTest().getInterpolated().clear();
                    roadDrawer.getTest().clearStringPointX();
                    roadDrawer.getTest().clearStringPointY();
                    roadDrawer.repaint();
                }
            }
            
        };
    }
    
        public static ActionListener actionRunButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                LocalDateTime dateTime = LocalDateTime.now();
                String timeStamp = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss").format(dateTime);
                String path = "C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\Result\\"+timeStamp;
                ArrayList<Test> testList = ((TestsTableModel)panel.getTestsTable().getModel()).getTestsGroup();
                new File(path).mkdirs();
                for (Test t : testList){
                    //executeTest(t.getName(),path);
                }
            }
            
        };
    }
    
    public static MouseListener actionTestsTable(ContainerRoadDrawer panel){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event) {
                JTable table = panel.getTestsTable();
                int row = table.rowAtPoint(event.getPoint());
                int col = table.columnAtPoint(event.getPoint());
                Test t = ((TestsTableModel)panel.getTestsTable().getModel()).getTestsGroup().get(row);
                panel.getRoadDrawer().setPoints(t.getPoints());
                panel.getSpeedField().setText(t.getSpeed().toString());
                panel.getRoadDrawer().getTest().setInterpolated(t.getInterpolated());
                panel.getRoadDrawer().setInterpolatedS(true);
                panel.getComboSimulator().setSelectedItem(t.getSimulator());
                panel.getRoadDrawer().getTest().setStringPointX(t.getStringPointX());
                panel.getRoadDrawer().getTest().setStringPointY(t.getStringPointY());
                panel.getRoadDrawer().repaint();
            }
        };
    }
    
    public static FocusListener actionSpeedField(ContainerRoadDrawer panel){
        return new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                if(panel.getSpeedField().getText().equals("Insert speed")){
                    panel.getSpeedField().setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e){
                if (panel.getSpeedField().getText().isEmpty()){
                    panel.getSpeedField().setText("Insert speed");
                }
            }
        };
    }
}


