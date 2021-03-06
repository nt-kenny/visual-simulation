package diagram;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class DistributionDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 8385402169634329188L;
    Object _value;
    Property _property;
    private JLabel[] _labels;
    JTextField[] _inputs;
    JPanel[] _panes;
    String _type;

    PropertiesTableData[] data;
    PropertiesTableReader _pReader;
    PropertiesSetting _pSetting;
    JPanel middlePane;
    JComponent basicPane;
    TableTextField _txtField;

    public DistributionDialog(JFrame owner, Property property, TableTextField txtField) {
        super(owner, "Distribution Setting", true);

        _type = new String(property.getType());
        _property = property;
        _value = property.getValue();
        _txtField = txtField;

        // create dialog framework
        setResizable(false);
        JPanel mainPane = new JPanel();
//		mainPane.setPreferredSize(new Dimension(400,300));
        JTabbedPane tabbedPane = new JTabbedPane();
        basicPane = new JPanel();
        JComponent advancedPane = new JPanel();
        tabbedPane.addTab("Basic", basicPane);
        tabbedPane.addTab("Advanced", advancedPane);
        tabbedPane.setSelectedIndex(0);
        mainPane.setLayout(new BorderLayout());
        mainPane.add(tabbedPane, BorderLayout.CENTER);
        JPanel buttonPane = new JPanel();
        JButton cancelButton = new JButton("Cancel");
        JButton setButton = new JButton("Set");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
        setButton.addActionListener(new DistributionDialogActionListener(this));
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(setButton);
        mainPane.add(buttonPane, BorderLayout.SOUTH);
        getContentPane().add(mainPane);
        basicPane.setLayout(new BoxLayout(basicPane, BoxLayout.Y_AXIS));

        // create distribution combo box

        _pReader = new PropertiesTableReader();
        _pSetting = new PropertiesSetting();
        String[] typeAry = _pReader.getType(_type);
        String[] choiceAry = new String[typeAry.length];

        for (int j = 0; j < typeAry.length; j++) {
            StringTokenizer st = new StringTokenizer(typeAry[j], ".");
            int count = st.countTokens();
            while (count != 1) {
                st.nextToken();
                count--;
            }

            choiceAry[j] = st.nextToken();
        }
        JComboBox distributionList = new JComboBox(choiceAry);
        distributionList.setEditable(false);

        if (_property.getValue() != null) {
            String className = (_property.getValue().getClass().getName());
            String classNameOnly = className.substring((className.lastIndexOf(".") + 1));
            for (int i = 0; i < distributionList.getItemCount(); i++) {
                System.err.println("(String)distributionList.getItemAt(i)" + (String) distributionList.getItemAt(i));
                if (((String) distributionList.getItemAt(i)).equals(classNameOnly)) {
                    distributionList.setSelectedIndex(i);
                }
            }
        } else {
            distributionList.setSelectedIndex(0);
        }

        distributionList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                String itemName = (String) cb.getSelectedItem();
                basicPane.remove(middlePane);
                refreshPane(itemName);
            }
        });

        JLabel distributionLabel = new JLabel("Distribution:");

        JPanel topPane = new JPanel();
        topPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPane.add(distributionLabel);
        topPane.add(distributionList);
        basicPane.add(topPane);

        JLabel label3 = new JLabel("Sample n:");
        JTextField txt3 = new JTextField(5);
        JButton editButton = new JButton("Show chart");

        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPane.add(label3);
        bottomPane.add(txt3);
        bottomPane.add(editButton);

        basicPane.add(bottomPane);

        refreshPane((String) distributionList.getSelectedItem());

        basicPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLocation(400, 400);
        pack();
        Object o;

        try {
            if (_property.getValue() == null) {
                _value = Class.forName(_type).newInstance(); // This is the
                                                             // object created
            } else {
                _value = _property.getValue();
            }
        } catch (Exception cnf) {
            System.err.println("In DistributionDialog:" + cnf);
        }

    }

    public Object getPropertyValue() {
        return _value;
    }

    private void refreshPane(String selectedItem) {
        _type = _type.substring(0, _type.lastIndexOf(".")) + "." + selectedItem;
        data = _pReader.getPropertiesTableData(_type);
        _labels = new JLabel[data.length];
        _panes = new JPanel[data.length];
        _inputs = new JTextField[data.length];
        middlePane = new JPanel();
        middlePane.setLayout(new BoxLayout(middlePane, BoxLayout.Y_AXIS));
        for (int i = 0; i < data.length; i++) {
            if (!(data[i].getName().equalsIgnoreCase("null"))) {
                _labels[i] = new JLabel(data[i].getName() + ":");
                _panes[i] = new JPanel();

                if (_pSetting.get(_value, data[i].getGetMethod()) == null) {
                    _inputs[i] = new JTextField(5);
                    this.requestFocus();
                } else {
                    _inputs[i] = new JTextField(_pSetting.get(_value, data[i].getGetMethod()).toString());
                    _inputs[i].setColumns(5);
                }

                _panes[i].setLayout(new FlowLayout(FlowLayout.LEFT));
                _panes[i].add(_labels[i]);
                _panes[i].add(_inputs[i]);
                middlePane.add(_panes[i]);
            }

        }
        basicPane.add(middlePane);
        pack();
        try {
            _value = Class.forName(_type).newInstance(); // This is the object
                                                         // created
        } catch (Exception cnf) {
            System.err.println("In DistributionDialog:" + cnf);
        }
    }
}

class DistributionDialogActionListener implements ActionListener {

    private DistributionDialog _dialog;

    public DistributionDialogActionListener(DistributionDialog dialog) {
        _dialog = dialog;
    }

    public void actionPerformed(ActionEvent ae) {

        PropertiesTableData[] data = _dialog._pReader.getPropertiesTableData(_dialog._type);
        if (!(data[0].getName().equalsIgnoreCase("null"))) {
            for (int i = 0; i < data.length; i++) {
                try {

                    Class[] para = new Class[1];
                    para[0] = _dialog._inputs[i].getText().getClass();

                    Object[] para2 = new Object[1];
                    para2[0] = _dialog._inputs[i].getText();

                    _dialog._pSetting.set(_dialog._value, data[i].getSetMethod(), Class.forName(data[i].getType()).getConstructor(para).newInstance(para2));
                } catch (Exception e) {
                    System.out.println("Event - " + e);
                }
            }
        }
        _dialog._txtField.setText(_dialog._type.substring(_dialog._type.lastIndexOf(".") + 1));
        _dialog.setVisible(false);
    }
}
