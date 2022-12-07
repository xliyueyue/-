package fangkuai;

import java.util.Random;

public class Tetromino extends Cell{
		 
		//由四个方块组成的图案
		protected Cell[] cells=new Cell[4];
		
		//方法重写：调用父类的方法，同时细化这个方法,遍历，每个人方块调用继承的方法
		public void moveDown() {
			for (int i=0;i<cells.length;i++) {
				cells[i].moveDown();
			}
		}
		
		public void moveLeft() {
			for(int i=0;i<cells.length;i++) {
				cells[i].moveLeft();
			}
		}
		
		public void moveRright() {
			for (int i=0;i<cells.length;i++) {
				cells[i].moveRight();
			}
		}
		
		public static Tetromino ranShape() {//返回值类型 方法名 方法体
			Random random =new Random();//类名 对象名 创建对象
			int index = random.nextInt(7);//调用这个API方法（0-6），参数
			switch (index) {
			case 0:
				return new J();//调用了Cell的午无参构造函数
			case 1:
				return new L();
			case 2:
				return new O();
			case 3:
				return new Z();
			case 4:
				return new S();
			case 5:
				return new I();
			case 6:
				return new T();
			}
			return null;
		}
		
		/*旋转:如果是图案是O型，则跳过
		 * 获取每一个小方块，还有旋转中心（编号为2的方块）
		 * 遍历，获取每一个方块的行，列，
		 * 得到旋转之后的图案（行，列，图），再返回图案
		 */
		public Cell[] spin() {
			
			if(this.getClass().equals(new O().getClass())){
				return null;
				
			}
			
			Cell[] iCells=new Cell[4];
			int iRow =this.cells[2].getRow();
			int iCol =this.cells[2].getCol();
			for(int i=0;i<this.cells.length;i++) {
				int nRow=this.cells[i].getRow();
				int nCol=this.cells[i].getCol();
				iCells[i]=new Cell(iRow-iCol+nCol,iCol+iRow-nRow,this.cells[i].getBgImage());
				
			}
					return iCells;
					
		}
		
}
		class J extends Tetromino{//继承，搭建的方块的各个形状
			public J() {
				cells[0]=new Cell(2,5,Tetris.J);
				cells[1]=new Cell(0,6,Tetris.J);
				cells[2]=new Cell(1,6,Tetris.J);
				cells[3]=new Cell(2,6,Tetris.J);	
			}
		}
		
		class L extends Tetromino{
			public L() {
				cells[0]=new Cell(2,6,Tetris.L);
				cells[1]=new Cell(0,5,Tetris.L);
				cells[2]=new Cell(1,5,Tetris.L);
				cells[3]=new Cell(2,5,Tetris.L);	
			}
		}
		
		class O extends Tetromino{
			public O() {
				cells[0]=new Cell(0,5,Tetris.O);
				cells[1]=new Cell(0,6,Tetris.O);
				cells[2]=new Cell(1,5,Tetris.O);
				cells[3]=new Cell(1,6,Tetris.O);	
			}
		}
		
		class Z extends Tetromino{
			public Z() {
				cells[0]=new Cell(0,5,Tetris.Z);
				cells[1]=new Cell(0,6,Tetris.Z);
				cells[2]=new Cell(1,6,Tetris.Z);
				cells[3]=new Cell(1,7,Tetris.Z);	
			}
		}
		
		class S extends Tetromino{
			public S() {
				cells[0]=new Cell(0,5,Tetris.S);
				cells[1]=new Cell(0,6,Tetris.S);
				cells[2]=new Cell(1,5,Tetris.S);
				cells[3]=new Cell(1,4,Tetris.S);	
			}
		}
		
		class I extends Tetromino{
			public I() {
				cells[0]=new Cell(0,5,Tetris.I);
				cells[1]=new Cell(1,5,Tetris.I);
				cells[2]=new Cell(2,5,Tetris.I);
				cells[3]=new Cell(3,5,Tetris.I);	
			}
		}
		
		class T extends Tetromino{
			public T() {
				cells[0]=new Cell(0,5,Tetris.T);
				cells[1]=new Cell(0,6,Tetris.T);
				cells[2]=new Cell(0,7,Tetris.T);
				cells[3]=new Cell(1,6,Tetris.T);	
			}
		}
