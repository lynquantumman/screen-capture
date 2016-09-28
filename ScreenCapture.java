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
最小化到托盘需要用到SystemTray TrayIcon
TrayIcon(Image image, String tooltip, PopupMenu popup)
*/

	SystemTray systemTray;
	TrayIcon ti;
	Image iconImage;
	MenuItem mn = new MenuItem("打开主面板");;
	MenuItem exit = new MenuItem("退出");
	PopupMenu popupMenu = new PopupMenu();
//	以下是截图所用到的对象
	DrawPanel dpFull = new DrawPanel();
	DrawPanel dpPart = new DrawPanel();
	Image screenCaptureImage;
	JButton jbCaptureScreen = new JButton("捕捉屏幕");
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
		ti = new TrayIcon(iconImage,"java托盘", popupMenu);
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

//		ActionListener 中的actionPerformed在最下面
		jbCaptureScreen.addActionListener(this);

//添加全局事件监听，按下Alt+A时，截屏
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {  
            @Override  
            public void eventDispatched(AWTEvent event) {  
                if(((KeyEvent)event).isAltDown()==true && KeyEvent.VK_A==((KeyEvent)event).getKeyCode()){  
                	getFullScreenAndCapture();
                }  
            }  
        }, AWTEvent.KEY_EVENT_MASK);  
		
//最小化至托盘的事件监听
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
//		将本类以JFrame的形式传递进去
		Robot rbt;
		try {
			rbt = new Robot();
			
			Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
//			截图，产生BufferedImage类型的图形，BufferedImage是Image的子类
			
			BufferedImage biScreen = rbt.createScreenCapture(screen);
//			利用BufferedImage类型变量来初始化dpFull
			dpFull.setImage(biScreen);
			dpFull.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
//			在鼠标监听器的mouseReleased函数中实现对主JFrame中DrawPanel的加载
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
//		++++++++++++需要修改
		CaptureMouseListener cpl = new CaptureMouseListener(jfFullScreen, dpPart, start, end);
		jfFullScreen.addMouseListener(cpl);
	}
	
}
