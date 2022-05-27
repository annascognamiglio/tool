package application;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

/**
 *
 * @author Anna
 */
public class ContainerRoadDrawer extends JFrame{
    public ContainerRoadDrawer() {
        initComponents();
    }

    private void initComponents() {
        roadDrawer = new RoadDrawer();
        comboSimulator = new JComboBox<>();
        okButton = new JButton();
        menu = new JMenu();
        menuBar = new JMenuBar();
        save = new JMenuItem();
        reset = new JMenuItem();
        open = new JMenu();
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        roadDrawer.setPreferredSize(new Dimension(500,500));
        
        List<String> listCombo = new ArrayList<>();
        listCombo.add("BeamNG");
        listCombo.add("MATLAB");
        comboSimulator.setModel(new DefaultComboBoxModel(listCombo.toArray()));
        
        okButton.setText("Ok");
        menu.setText("Menu");
        menuBar.add(menu);
        setJMenuBar(menuBar);
        save.setText("Save");
        reset.setText("Reset");
        open.setText("Open");
        menu.add(open);
        menu.add(save);
        menu.add(reset);
        getSavedFile();
        
        
        GroupLayout panelLayout = new GroupLayout(roadDrawer);
        roadDrawer.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,0,Short.MAX_VALUE));
        panelLayout.setVerticalGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,257,Short.MAX_VALUE));
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(comboSimulator, 0, 319, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton))
            .addComponent(roadDrawer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(roadDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pack();

    }
    
    private JButton okButton;
    private RoadDrawer roadDrawer;
    private JComboBox<String> comboSimulator;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem save, reset, open;
    
    public JMenuItem getSaveItem(){
        return this.save;
    }
    
    public JMenuItem getResetItem(){
        return this.reset;
    }
    
    public JMenuItem getOpenItem(){
        return this.open;
    }
    
    public JComboBox getComboSimulator(){
        return this.comboSimulator;
    }
    
    public JButton getOkButton(){
        return this.okButton;
    }
    
    public RoadDrawer getRoadDrawer(){
        return this.roadDrawer;
    }
    
    public void getSavedFile(){
        File f = new File("C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\savedFiles");
                if (f.exists()){
                    open.removeAll();
                    File[] listFiles = f.listFiles();
                    for (File file : listFiles){
                    if (file.getAbsolutePath().endsWith("json")){
                        JMenuItem m = new JMenuItem(file.getName());
                        open.add(m);
                        m.addActionListener(Service.actionOpen(this));
                    }
                }
                }
    }
}
   
