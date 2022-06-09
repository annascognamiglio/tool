package application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author Anna
 */
public class ViewOutput extends JFrame {

    public ViewOutput() {
        initComponents();
    }
                     
    private void initComponents() {

        ContainerJPanel = new JPanel();
        jPanelBeamNG = new ChartPanel(null);
        jPanelMatlab = new ChartPanel(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelBeamNG.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelBeamNGLayout = new javax.swing.GroupLayout(jPanelBeamNG);
        jPanelBeamNG.setLayout(jPanelBeamNGLayout);
        jPanelBeamNGLayout.setHorizontalGroup(
            jPanelBeamNGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 778, Short.MAX_VALUE)
        );
        jPanelBeamNGLayout.setVerticalGroup(
            jPanelBeamNGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanelMatlab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelMatlab.setPreferredSize(new java.awt.Dimension(778, 300));

        javax.swing.GroupLayout jPanelMatlabLayout = new javax.swing.GroupLayout(jPanelMatlab);
        jPanelMatlab.setLayout(jPanelMatlabLayout);
        jPanelMatlabLayout.setHorizontalGroup(
            jPanelMatlabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelMatlabLayout.setVerticalGroup(
            jPanelMatlabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ContainerJPanelLayout = new javax.swing.GroupLayout(ContainerJPanel);
        ContainerJPanel.setLayout(ContainerJPanelLayout);
        ContainerJPanelLayout.setHorizontalGroup(
            ContainerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBeamNG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelMatlab, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
        );
        ContainerJPanelLayout.setVerticalGroup(
            ContainerJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContainerJPanelLayout.createSequentialGroup()
                .addComponent(jPanelBeamNG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelMatlab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContainerJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ContainerJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOutput().setVisible(true);
            }
        });
    }
                   
    private JPanel ContainerJPanel;
    private ChartPanel jPanelBeamNG;
    private ChartPanel jPanelMatlab;
}
