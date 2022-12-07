package fangkuai;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;



public class Snippet {
	public static Bitmap scaleImageByProportion(Bitmap src,float proportion){
			AffineTransform trans = new AffineTransform();
		    trans.scale(proportion, proportion);
		    BufferedImage img = new BufferedImage((int)(src.getWidth()*proportion), (int)(src.getHeight()*proportion), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = (Graphics2D) img.getGraphics();
			RenderingHints qualityHints = new  RenderingHints(RenderingHints.KEY_ANTIALIASING,              
			  		RenderingHints.VALUE_ANTIALIAS_ON); 
			qualityHints.put(RenderingHints.KEY_RENDERING,               
			  		RenderingHints.VALUE_RENDER_QUALITY); 
			g.setRenderingHints(qualityHints);
			g.drawImage(src.getImage(), trans, null);
			return new Bitmap(img);
		}
}

