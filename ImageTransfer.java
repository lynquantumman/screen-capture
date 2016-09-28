package screecapture;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ImageTransfer implements Transferable{
	private Image image;
	public ImageTransfer(Image image){
		this.image = image;
	}
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		if(!DataFlavor.imageFlavor.equals(flavor)){
			throw new UnsupportedFlavorException(flavor);
		}
		return image;
	}
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{DataFlavor.imageFlavor};
	}
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		
		return DataFlavor.imageFlavor.equals(flavor);
	}
}
