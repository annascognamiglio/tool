package application;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
        removeButton = new JButton();
        runButton = new JButton();
        scrollPaneTable = new JScrollPane();
        testsTable = new JTable();
        menu = new JMenu();
        menuBar = new JMenuBar();
        save = new JMenuItem();
        reset = new JMenuItem();
        resultFolder = new JMenuItem();
        open = new JMenu();
        export = new JMenuItem();
        loadingPanel = new javax.swing.JPanel();
        loadingText = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        roadDrawer.setPreferredSize(new Dimension(500,500));
        
        List<String> listCombo = new ArrayList<>();
        listCombo.add("BeamNG");
        listCombo.add("MATLAB");
        listCombo.add("All");
        comboSimulator.setModel(new DefaultComboBoxModel(listCombo.toArray()));
        speedField.setText("Insert speed");
        okButton.setText("Ok");
        addButton.setText("+");
        removeButton.setText("-");
        menu.setText("Menu");
        menuBar.add(menu);
        setJMenuBar(menuBar);
        export.setText("Export as OpenDrive");
        save.setText("Save");
        resultFolder.setText("Result Folder");
        runButton.setText("Run");
        reset.setText("Reset");
        open.setText("Open");
        menu.add(open);
        menu.add(save);
        menu.add(reset);
        menu.add(export);
        menu.add(resultFolder);
        getSavedFile();
        
        loadingText.setText("Checking validity test");
        loadingPanel.setVisible(false);
        
        testsTable.setModel(new TestsTableModel());
        scrollPaneTable.setViewportView(testsTable);
        
        javax.swing.GroupLayout loadingPanelLayout = new javax.swing.GroupLayout(loadingPanel);
        loadingPanel.setLayout(loadingPanelLayout);
        loadingPanelLayout.setHorizontalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadingText)
                .addGap(75, 75, 75)
                .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        loadingPanelLayout.setVerticalGroup(
            loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loadingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loadingText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout roadDrawerLayout = new javax.swing.GroupLayout(roadDrawer);
        roadDrawer.setLayout(roadDrawerLayout);
        roadDrawerLayout.setHorizontalGroup(
            roadDrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loadingPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        roadDrawerLayout.setVerticalGroup(
            roadDrawerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roadDrawerLayout.createSequentialGroup()
                .addComponent(loadingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(runButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(roadDrawer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboSimulator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(speedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton)
                    .addComponent(addButton)
                    .addComponent(removeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runButton))
                    .addComponent(roadDrawer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();

    }
    
    private JButton okButton, addButton, removeButton, runButton;
    private RoadDrawer roadDrawer;
    private JComboBox<String> comboSimulator;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem save, reset, open, resultFolder, export;
    private JScrollPane scrollPaneTable;
    private JTextField speedField;
    private JTable testsTable;
    private JProgressBar progressBar;
    private JPanel loadingPanel;
    private JLabel loadingText;

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JPanel getLoadingPanel() {
        return loadingPanel;
    }

    public void setLoadingPanel(JPanel loadingPanel) {
        this.loadingPanel = loadingPanel;
    }
    
    public JMenuItem getExportItem(){
        return this.export;
    }
    
    public JMenuItem getSaveItem(){
        return this.save;
    }
    
    public JMenuItem getResetItem(){
        return this.reset;
    }
    
    public JMenuItem getResultFolderItem(){
        return this.resultFolder;
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
    
    public JButton getRunButton(){
        return this.runButton;
    }
    
    public JButton getRemoveButton(){
        return this.removeButton;
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
   
