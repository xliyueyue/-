package fangkuai;

import java.awt.image.BufferedImage;

//static修饰类；类方法 实例方法
public class Cell {
	
	private int row;//行
	private int col;//列
	private BufferedImage bgImage;//图片
	
	//get set通过获取此类私有的类和对象，利用封装的get set方法:shift+alt+s
	public int getRow() {
		return row;//获取私有的对象
	}
	
	public void setRow(int row) {
		this.row = row;//
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public BufferedImage getBgImage() {
		return bgImage;
	}

	public void setBgImage(BufferedImage bgImage) {
		this.bgImage = bgImage;
	}
	public Cell() {
		
	}

	public Cell(int row,int col,BufferedImage bgImage) {
		this.row=row;
		this.col=col;
	   this.bgImage=bgImage;//构造函数
	   
	}
	
	public void moveDown() {
		row++;//下移
		
	}

	public void moveLeft() {
		col--;//左移
		
	}	
	
	public void moveRight() {
		col++;//右移
		
	}
	
}
