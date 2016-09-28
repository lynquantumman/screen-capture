package screecapture;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
//first we need to finish the SystemTray function
//Then we need to let this program to have capture screen function
// let the combination alt + a have this funciton

public class ScreenCapture extends JFrame implements ActionListener{
/**
	 * 
	 */
	private static final long serialVersionUID = 5168336004003909743L;
/**
Robot rbt; This will be used in screen capture
rbt.createScreenCapture();
��С����������Ҫ�õ�SystemTray TrayIcon
TrayIcon(Image image, String tooltip, PopupMenu popup)
*/

	SystemTray systemTray;
	TrayIcon ti;
	Image iconImage;
	MenuItem mn = new MenuItem("�������");;
	MenuItem exit = new MenuItem("�˳�");
	PopupMenu popupMenu = new PopupMenu();
//	�����ǽ�ͼ���õ��Ķ���
	DrawPanel dpFull = new DrawPanel();
	DrawPanel dpPart = new DrawPanel();
	Image screenCaptureImage;
	JButton jbCaptureScreen = new JButton("��׽��Ļ");
	JPanel upButtons = new JPanel();
	Point start = new Point(0,0);
	Point end = new Point(0,0);
	BufferedImage bi;
	CaptureMouseListener cpl;
	public ScreenCapture(){
		try {
			iconImage = ImageIO.read(new File("E:/mathsoft/eclipse/icon.jpg"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new RuntimeException();
		}
		//Please notice the PopupMenu
        popupMenu.add(mn); 
        popupMenu.add(exit);  
		ti = new TrayIcon(iconImage,"java����", popupMenu);
		ti.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){
					setVisible(true);
					systemTray.remove(ti);
					setExtendedState(JFrame.NORMAL);
				}
			}
		});
		systemTray = SystemTray.getSystemTray();
		

		
		setBounds(10,10,50,50);
		
		upButtons.add(jbCaptureScreen);
		add(upButtons, BorderLayout.NORTH);
		add(dpPart, BorderLayout.CENTER);
		setVisible(true);

		mn.addActionListener(this);
		exit.addActionListener(this);

//		ActionListener �е�actionPerformed��������
		jbCaptureScreen.addActionListener(this);

//���ȫ���¼�����������Alt+Aʱ������
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {  
            @Override  
            public void eventDispatched(AWTEvent event) {  
                if(((KeyEvent)event).isAltDown()==true && KeyEvent.VK_A==((KeyEvent)event).getKeyCode()){  
                	getFullScreenAndCapture();
                }  
            }  
        }, AWTEvent.KEY_EVENT_MASK);  
		
//��С�������̵��¼�����
		addWindowListener(
			new WindowAdapter(){
				@Override
			 	public void windowIconified(WindowEvent we){
					try {
						setVisible(false);
						systemTray.add(ti);
					} catch (AWTException awte) {
						awte.printStackTrace();
					}
				}
			}
		);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==mn){
			setVisible(true);
			systemTray.remove(ti);
		}
		
		if(ae.getSource()==exit){
			dispose();
			System.exit(0);
		}
		
		if(ae.getSource()==jbCaptureScreen){
			getFullScreenAndCapture();
		}

	}
	
	private void getFullScreenAndCapture(){
//		��������JFrame����ʽ���ݽ�ȥ
		Robot rbt;
		try {
			rbt = new Robot();
			
			Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//			��ͼ������BufferedImage���͵�ͼ�Σ�BufferedImage��Image������
			
			BufferedImage biScreen = rbt.createScreenCapture(screen);
//			����BufferedImage���ͱ�������ʼ��dpFull
			dpFull.setImage(biScreen);
			dpFull.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
//			������������mouseReleased������ʵ�ֶ���JFrame��DrawPanel�ļ���
		}
		catch (AWTException e1) {
			// TODO Auto-generated catch block
			System.out.println("============AWTException");
			e1.printStackTrace();
		}
		JFrame jfFullScreen = new JFrame();
		jfFullScreen.setUndecorated(true);
		jfFullScreen.add(dpFull);
		GraphicsEnvironment ge = 
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if(gd.isFullScreenSupported())
			gd.setFullScreenWindow(jfFullScreen);
//		++++++++++++��Ҫ�޸�
		CaptureMouseListener cpl = new CaptureMouseListener(jfFullScreen, dpPart, start, end);
		jfFullScreen.addMouseListener(cpl);
	}
	
}
