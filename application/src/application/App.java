package application;
import javax.swing.UIManager.*;
import java.awt.EventQueue;
import javax.swing.UIManager;

/**
 *
 * @author Anna
 */
public class App {
    
    public static void main(String [] args){

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                ContainerRoadDrawer drawer = new ContainerRoadDrawer();
                drawer.setVisible(true);
                drawer.getOkButton().addActionListener(Service.actionOkButton(drawer));
                drawer.getResetItem().addActionListener(Service.actionReset(drawer));
                drawer.getOpenItem().addActionListener(Service.actionOpen(drawer));
                drawer.getSaveItem().addActionListener(Service.actionSave(drawer));
                drawer.getAddButton().addActionListener(Service.actionAddButton(drawer));
                drawer.getTestsTable().addMouseListener(Service.actionTestsTable(drawer));
            }
        });
    }
}
