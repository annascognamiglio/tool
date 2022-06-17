package application;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
        speedField = new JTextField();
        okButton = new JButton();
        addButton = new JButton();
        scrollPaneTable = new JScrollPane();
        testsTable = new JTable();
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
        speedField.setText("Insert speed");
        okButton.setText("Ok");
        addButton.setText("Add");
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
        
        testsTable.setModel(new TestsTableModel());
        scrollPaneTable.setViewportView(testsTable);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPaneTable, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(comboSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(roadDrawer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(okButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roadDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollPaneTable))
                .addContainerGap())
        );

        pack();

    }
    
    private JButton okButton, addButton;
    private RoadDrawer roadDrawer;
    private JComboBox<String> comboSimulator;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem save, reset, open;
    private JScrollPane scrollPaneTable;
    private JTextField speedField;
    private JTable testsTable;
    
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
    
    public JButton getAddButton(){
        return this.addButton;
    }
    
    public RoadDrawer getRoadDrawer(){
        return this.roadDrawer;
    }
    
    public JTextField getSpeedField(){
        return this.speedField;
    }
    
    public JTable getTestsTable(){
        return this.testsTable;
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
   
