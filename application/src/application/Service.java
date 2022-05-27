package application;

import com.mathworks.engine.MatlabEngine;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Anna
 */
public class Service {
    public static ActionListener actionOkButton(ContainerRoadDrawer panel) {
        return  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String stringPointX = panel.getRoadDrawer().getStringPointX();
                String stringPointY = panel.getRoadDrawer().getStringPointY();
                String simulator = (String) panel.getComboSimulator().getSelectedItem();
                List<Point2D> points = panel.getRoadDrawer().getPoints();
                if (points.size()<=1){
                    JOptionPane.showMessageDialog(panel.getRoadDrawer(),"Insufficient number of points","Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (points.size()>1){
                    
                    //----------------BEAMNG----------------
                    
                    if (simulator.equals("BeamNG")){ 
                        try{
                            String maxSpeed = (String) panel.getRoadDrawer().getMaxSpeed().getSelectedItem();
                            panel.getRoadDrawer().setSpeed(Integer.parseInt(maxSpeed));
                            boolean valid = panel.getRoadDrawer().checkPoints(maxSpeed); // CONTROLLO CHE IL TEST SIA VALIDO
                            if (!valid){
                                return;
                            }
                            ProcessBuilder builder = new ProcessBuilder("python", "C:\\Users\\kikki\\PycharmProjects\\progetto\\competition.py","--module-name", "rs_anglelength.run_al", "--class-name", "AngleLengthGenerator", "--time-budget", "10000", "--executor", "beamng", "--map-size", "500");//, "--visualize-tests");
                            Process process = builder.start();
                            BufferedReader readerE = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                            String lines = null;
                            while ((lines=readerE.readLine())!=null){
                                System.out.println("Error : "+lines);
                            }
                            
                        } catch(Exception e){
                            System.out.println("Errore:");
                            e.printStackTrace();
                        }
                    }
                    
                    //----------------MATLAB----------------
                    
                    else if (simulator.equals("MATLAB")){
                        try{
                            String speed = (String) panel.getRoadDrawer().getSpeedFromCombo().getSelectedItem();
                            panel.getRoadDrawer().setSpeed(Integer.parseInt(speed));
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
                            System.out.println(stringPointX+"\n"+stringPointY);
                            panel.getRoadDrawer().writePointsOnFile(stringPointX, stringPointY, speed); // scrivo i punti *scelti* (NON interpolati - da aggiungere) sul file chosenPoint.txt
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

