package fangkuai;

import java.awt.image.BufferedImage;

//static�����ࣻ�෽�� ʵ������
public class Cell {
	
	private int row;//��
	private int col;//��
	private BufferedImage bgImage;//ͼƬ
	
	//get setͨ����ȡ����˽�е���Ͷ������÷�װ��get set����:shift+alt+s
	public int getRow() {
		return row;//��ȡ˽�еĶ���
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
	   this.bgImage=bgImage;//���캯��
	   
	}
	
	public void moveDown() {
		row++;//����
		
	}

	public void moveLeft() {
		col--;//����
		
	}	
	
	public void moveRight() {
		col++;//����
		
	}
	
}
