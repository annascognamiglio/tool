package application;

import java.awt.EventQueue;

/**
 *
 * @author Anna
 */
public class App {
    
    public static void main(String [] args){
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                ContainerRoadDrawer drawer = new ContainerRoadDrawer();
                drawer.setVisible(true);
                drawer.getOkButton().addActionListener(Service.actionOkButton(drawer));
                drawer.getResetItem().addActionListener(Service.actionReset(drawer));
                drawer.getOpenItem().addActionListener(Service.actionOpen(drawer));
                drawer.getSaveItem().addActionListener(Service.actionSave(drawer));
            }
        });
    }
}
