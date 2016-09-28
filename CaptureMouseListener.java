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
		//记下鼠标位置
		start.move(e.getPoint().x, e.getPoint().y);
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		//记下鼠标位置
		end.move(e.getPoint().x, e.getPoint().y);
		Robot rbt;
		try {
			rbt = new Robot();
//生成所要截图的矩形
			Rectangle rect = new Rectangle(Math.min(start.x, end.x), 
											Math.min(start.y, end.y),
											(Math.abs(end.x - start.x)), 
											(Math.abs(end.y - start.y))
											);
//截图，产生BufferedImage类型的图形，BufferedImage是Image的子类
			BufferedImage bi = rbt.createScreenCapture(rect);
//利用BufferedImage类型变量来初始化dpPart
			dpPart.setImage(bi);
//在鼠标监听器的mouseReleased函数中实现对主JFrame中DrawPanel的加载
		} catch(IllegalArgumentException iae){
//			经常的，双击结束会触发该异常，所以不进行处理
//			System.out.println(iae.getMessage());
			
			
		}catch (AWTException e1) {
			// TODO Auto-generated catch block
			System.out.println("============AWTException");
			e1.printStackTrace();
		}
		
	}
}
