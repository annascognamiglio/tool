package application;

import com.mathworks.engine.MatlabEngine;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
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
    
    public static void createChart(List<Double> oob){
        XYSeries series = new XYSeries("oob");
        DefaultXYDataset ds = new DefaultXYDataset();
        double[] time = new double[oob.size()];
        double[][] data = new double[oob.size()][oob.size()];
        for (int i = 0; i<oob.size();i++){
            series.add(i,oob.get(i));
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
        /*ds.addSeries("oob", data);
        System.out.println("LENGTH: "+data.length);
        ds.addSeries("oob", data);
        JFreeChart chart = ChartFactory.createXYLineChart("Test Chart", "x", "y", ds, PlotOrientation.VERTICAL, true, true, false);*/
        JFrame frame = new JFrame("Charts");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        ChartPanel cp = new ChartPanel(chart);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        frame.getContentPane().add(cp);
    }
    
    public static ActionListener actionOkButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean flag = false;
                String stringPointX = panel.getRoadDrawer().getStringPointX();
                String stringPointY = panel.getRoadDrawer().getStringPointY();
                String simulator = (String) panel.getComboSimulator().getSelectedItem();
                List<Double> oob = new ArrayList<>();
                List<Point2D> points = panel.getRoadDrawer().getPoints();
                if (points.size()<=1){
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Insufficient number of points","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (points.size()>1){
                    
                    //----------------BEAMNG----------------
                    
                    if (simulator.equals("BeamNG")){ 
                        try{
                            int maxSpeed = panel.getRoadDrawer().getSpeedFromUser("Please insert max speed:");
                            boolean valid = panel.getRoadDrawer().checkPoints(Integer.toString(maxSpeed)); // CONTROLLO CHE IL TEST SIA VALIDO
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
                            }
                            
                            createChart(oob);
                            
                            
                        } catch(Exception e){
                            System.out.println("Errore:");
                            e.printStackTrace();
                        }
                    }
                    
                    //----------------MATLAB----------------
                    
                    else if (simulator.equals("MATLAB")){
                        try{
                            // String speed = (String) panel.getRoadDrawer().getSpeedFromCombo().getSelectedItem();
                            // panel.getRoadDrawer().setSpeed(Integer.parseInt(speed));
                            int speed = panel.getRoadDrawer().getSpeedFromUser("Please insert speed:");
                            System.out.println("MATLAB");
                            boolean valid = panel.getRoadDrawer().checkPoints(null); // CONTROLLO CHE IL TEST SIA VALIDO
                            if (!valid){
                                return;
                            }
                            MatlabEngine eng = MatlabEngine.startMatlab();
                            eng.eval("cd 'C:\\Users\\kikki\\Documents\\MATLAB\\Examples\\R2022a\\autonomous_control\\LaneKeepingAssistWithLaneDetectionExample'");
                            StringWriter writerO = new StringWriter();
                            StringWriter writerE = new StringWriter();
                            stringPointX = panel.getRoadDrawer().getStringXinterpolated();
                            stringPointY = panel.getRoadDrawer().getStringYinterpolated();
                            panel.getRoadDrawer().writePointsOnFile(stringPointX, stringPointY, Integer.toString(speed)); // scrivo i punti *scelti* (NON interpolati - da aggiungere) sul file chosenPoint.txt
                            Future<Void> fLoad = eng.evalAsync("open_system('LKATestBenchExample')");
                            while (!fLoad.isDone()){
                                System.out.println("Opening Simulink model...");
                                Thread.sleep(10000);
                            }
                            eng.evalAsync("set_param('LKATestBenchExample/Safe Lateral Offset','Value','2')");
                            Future<Void> fSim = eng.evalAsync("sim('LKATestBenchExample'");
                            while (!fSim.isDone()) {
                                System.out.println("Running Simulation...");
                                Thread.sleep(10000);
                            }
                            System.out.println(writerO.toString());
                           
                            
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
                roadDrawer.getPoints().clear();
                roadDrawer.clearStringPointX();
                roadDrawer.clearStringPointY();
                System.out.println(roadDrawer.getPoints().size());
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
                for (Point2D point : panel.getRoadDrawer().getPoints()){
                    JSONObject p = new JSONObject();
                    p.put("x", String.valueOf(point.getX()));
                    p.put("y", String.valueOf(point.getY()));
                    chosenPoints.add(p);
                }
                json.put("points", chosenPoints);
                json.put("stringX",panel.getRoadDrawer().getStringPointX());
                json.put("stringY",panel.getRoadDrawer().getStringPointY());
                String name = JOptionPane.showInputDialog("Nome del file:");
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
                List<Point2D> newInterpolatedPointsList = new ArrayList<>();
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
                    Object stringX = json.get("stringX");
                    Object stringY = json.get("stringY");
                    panel.getRoadDrawer().setPoints(newPointsList);
                    panel.getRoadDrawer().setStringPointX(stringX.toString());
                    panel.getRoadDrawer().setStringPointY(stringY.toString());
                           
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}


