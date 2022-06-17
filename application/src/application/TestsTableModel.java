package application;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Anna
 */
public class TestsTableModel extends AbstractTableModel{

    public ArrayList<Test> getTestsGroup() {
        return testsGroup;
    }

    private ArrayList<Test> testsGroup;


    public TestsTableModel() {
        this.testsGroup = new ArrayList<Test>();
    }
    
    @Override
    public int getRowCount() {return testsGroup.size();}
    @Override
    public int getColumnCount( ){
        return 1;
    }

    @Override
    public String getColumnName(int i) {
        switch (i){
            case 0 : {return "Test Name";}
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int i,int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Test test = testsGroup.get(i);
        switch (i1) {
            case 0 : {return test.getName();}
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int r, int c) {
        Test test = testsGroup.get(r);
        switch (c){
            case 0 : { test.setName((String) value);break;}
        }
        this.fireTableCellUpdated(r,c);
    }

}