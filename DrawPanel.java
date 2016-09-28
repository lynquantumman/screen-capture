package screecapture;

import java.awt.Graphics;
import java.awt.Image;
//import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawPanel extends JPanel{
	private static final long serialVersionUID = 3055097146797017956L;
	Image image;
	//��Image��������ʼ��
	public DrawPanel(Image image){
//		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.image = image;
	}
	
	public DrawPanel(){
//		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		try {
			image = ImageIO.read(new File("E:/mathsoft/eclipse/icon.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Image getImage(){
		return image;
	}
	
	public void setImage(Image image){
		this.image = image;
		repaint();
	}
	//paintComponent��������Ҫ�������������У���Ϊjvm������Ҫ��ʱ����øú���
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		//ע�������this��panel�������ʵ����ImageObserver�ӿ�
		System.out.println(g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),this));
		System.out.println("paintComponent...");
	}
}
