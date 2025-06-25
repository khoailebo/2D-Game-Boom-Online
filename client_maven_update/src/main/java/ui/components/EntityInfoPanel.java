package ui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import dtos.ModelEntityInfo;
import main.GamePanel;
import net.miginfocom.swing.MigLayout;
public class EntityInfoPanel extends JPanel {
    ModelEntityInfo Model_data;
    public static String []ComponentConstrains = new String[]{"cell 0 0, top","cell 2 1, bottom","cell 2 0,top","cell 0 1,bottom"};
    public void setModel_data(ModelEntityInfo model_data) {
        Model_data = model_data;
        SpeedLabel.setText(Integer.toString(model_data.getSpeed()));
        LifeLabel.setText(Integer.toString(model_data.getLife()));
        BoomSizeLabel.setText(Integer.toString(model_data.getBoomSize()));
        MaxBoomLabel.setText(Integer.toString(model_data.getMaxBoom()));
        ImageIconLabel.setIcon(model_data.getImage());
    }
    public void updateState(ModelEntityInfo model_data)
    {
        Model_data = model_data;
        SpeedLabel.setText(Integer.toString(model_data.getSpeed()));
        LifeLabel.setText(Integer.toString(model_data.getLife()));
        BoomSizeLabel.setText(Integer.toString(model_data.getBoomSize()));
        MaxBoomLabel.setText(Integer.toString(model_data.getMaxBoom()));
        repaint();
    }
    Label ImageIconLabel;
    Label SpeedLabel;
    Label LifeLabel;
    Label MaxBoomLabel;
    Label BoomSizeLabel;
    public EntityInfoPanel(){
        initComponents();
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g);
    }
    public void initComponents(){
        setOpaque(false);
        setDoubleBuffered(true);
        setLayout(new MigLayout("fill","[50%,center][50%,center]"));
        setPreferredSize(new Dimension(GamePanel.SideBarWidth,150));
        setBackground(Color.white);
        ImageIconLabel = new Label("",0);
        ImageIconLabel.setIcon(new ImageIcon(getClass().getResource("/player/boy_down_1.png")));
        

        SpeedLabel = new Label("1", 20f);
        SpeedLabel.setIcon(new ImageIcon(getClass().getResource("/obj/speed_icon.png")));
        SpeedLabel.setIconTextGap(20);

        MaxBoomLabel = new Label("1", 20f);
        MaxBoomLabel.setIcon(new ImageIcon(getClass().getResource("/obj/maxboom_icon.png")));
        MaxBoomLabel.setIconTextGap(20);

        LifeLabel = new Label("1", 20f);
        LifeLabel.setIcon(new ImageIcon(getClass().getResource("/obj/life_icon.png")));
        LifeLabel.setIconTextGap(20);


        BoomSizeLabel = new Label("1", 20f);
        BoomSizeLabel.setIcon(new ImageIcon(getClass().getResource("/obj/boomsize_icon.png")));
        BoomSizeLabel.setIconTextGap(20);


        add(ImageIconLabel,"span 1 4");
        add(SpeedLabel,"wrap,growx");
        add(MaxBoomLabel,"wrap,growx");
        add(LifeLabel,"wrap,growx");
        add(BoomSizeLabel,"wrap,growx");
    }
}
