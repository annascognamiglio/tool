package application;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
                String path = "C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\Result";
                panel.getRoadDrawer().getTest().setSimulator(panel.getComboSimulator().getSelectedItem().toString());
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
                Test t = panel.getRoadDrawer().getTest();
                try {
                    t.executeTest(path);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
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
    
        public static ActionListener actionResultFolder(ContainerRoadDrawer panel){
        return new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                try {
                    Desktop.getDesktop().open(new File("C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\Result"));
                } catch (IOException ex) {
                    Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
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
                String name = JOptionPane.showInputDialog(null, "Test name:","Add", JOptionPane.INFORMATION_MESSAGE);
                if (name==null) return;
                String simulator = (String) panel.getComboSimulator().getSelectedItem();
                List<Point2D> points = new ArrayList<>(panel.getRoadDrawer().getTest().getPoints());
                List<Point2D> interpolated = new ArrayList<>(panel.getRoadDrawer().getTest().getInterpolated());
                String stringPointX = panel.getRoadDrawer().getTest().getStringPointX();
                String stringPointY = panel.getRoadDrawer().getTest().getStringPointY();                
                Test t = new Test(name,simulator,speed,points,interpolated,stringPointX,stringPointY);
                if (!t.checkValidity()){
                    return;
                }
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
                    try {
                        t.executeTest(path);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                    }
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


