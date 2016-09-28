package screecapture;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class CaptureMouseListener extends MouseAdapter{
	
	Point start;
	Point end;
	DrawPanel dpPart;
	JFrame jf;
	Image imageSaved;
	public CaptureMouseListener(JFrame jf,DrawPanel dpPart, Point start, Point end){
		this.jf = jf;
		this.dpPart = dpPart;
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			jf.setVisible(false);
			jf.dispose();
		}
		imageSaved = dpPart.getImage();
		ImageTransfer imgTransfer = new ImageTransfer(imageSaved);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgTransfer, null);
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		//�������λ��
		start.move(e.getPoint().x, e.getPoint().y);
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		//�������λ��
		end.move(e.getPoint().x, e.getPoint().y);
		Robot rbt;
		try {
			rbt = new Robot();
//������Ҫ��ͼ�ľ���
			Rectangle rect = new Rectangle(Math.min(start.x, end.x), 
											Math.min(start.y, end.y),
											(Math.abs(end.x - start.x)), 
											(Math.abs(end.y - start.y))
											);
//��ͼ������BufferedImage���͵�ͼ�Σ�BufferedImage��Image������
			BufferedImage bi = rbt.createScreenCapture(rect);
//����BufferedImage���ͱ�������ʼ��dpPart
			dpPart.setImage(bi);
//������������mouseReleased������ʵ�ֶ���JFrame��DrawPanel�ļ���
		} catch(IllegalArgumentException iae){
//			�����ģ�˫�������ᴥ�����쳣�����Բ����д���
//			System.out.println(iae.getMessage());
			
			
		}catch (AWTException e1) {
			// TODO Auto-generated catch block
			System.out.println("============AWTException");
			e1.printStackTrace();
		}
		
	}
}
