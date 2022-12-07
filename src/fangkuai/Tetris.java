package fangkuai;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris extends JPanel{
	int num;//消除的行数
	private Tetromino nextone;//下一个下落 对象
	private Tetromino tetromino;//当前下落对象
	private static final int ROWS=20;//行数
	private static final int COLS=10;//列数
	private int score =0;//分数
	private int lines=0;//行数
	private int level=5;//等级
	private Cell[][] wall=new Cell[ROWS][COLS];//墙
	private boolean STATE=true;//状态
	private int BGI =0;//图片的序号
	public static final int CELL_SIZE=26;//26元素点
	public static BufferedImage Z;//由4个格子组成的图片的形状
	public static BufferedImage S;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage O;
	public static BufferedImage I;
	public static BufferedImage T;
	public static BufferedImage[] bgi =new BufferedImage[4];//背静图片组
	public static BufferedImage pause;//暂停
	public static BufferedImage tetris;
	public static BufferedImage gameover;//游戏结束
	public static BufferedImage lgg;
	static{
	try {
		Z =ImageIO.read(Tetris.class.getResource("Z.png"));
		S =ImageIO.read(Tetris.class.getResource("S.png"));
		J =ImageIO.read(Tetris.class.getResource("J.png"));
		T =ImageIO.read(Tetris.class.getResource("T.png"));
		O =ImageIO.read(Tetris.class.getResource("O.png"));
		I =ImageIO.read(Tetris.class.getResource("I.png"));
		L =ImageIO.read(Tetris.class.getResource("L.png"));
		//使用io流，获取图片，注意：静态,记住加花括号和分号
		pause =ImageIO.read(Tetris.class.getResource("pause.png"));
		tetris=ImageIO.read(Tetris.class.getResource("tetris1.png"));
		gameover = ImageIO.read(Tetris.class.getResource("gameover.png"));
		lgg=ImageIO.read(Tetris.class.getResource("lgg.png"));
		 for (int i=0;i<bgi.length;i++) {
			 String name="bgi0"+i+".jpg";
			 bgi[i]=ImageIO.read(Tetris.class.getResource(name));
		 }
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();//自动获取的
	}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame =new JFrame();
		Tetris tetris =new Tetris();//创建对象
		frame.add(tetris);//添加对象
		frame.setSize(525,600);//大小
		frame.setLocationRelativeTo(null);//居中
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//保证后台关闭
		frame.setVisible(true);//可视化
		tetris.action();//完成移动操作
		//System.out.println(Z);
	}

	private void action() {
		// TODO Auto-generated method stub
		tetromino = Tetromino.ranShape();//当前图片
		nextone = Tetromino.ranShape();//下一张图片
		KeyListener kl =new KeyAdapter() {//匿名name类，接口，类
			public void keyPressed(KeyEvent e) {//方法
				int k=e.getKeyCode();//变量代指按键和键的释放
				keyMoveAction(k);//使用变量k对击键事件进行操作
				repaint();//重新加载和获取画面
			}
		};//匿名name类 记住加分号
		this.addKeyListener(kl);//将k1加入对键的监听
		this.setFocusable(true);//将此组件的可聚焦状态设置为确定的值
		this.requestFocus();//请求此组件获取输入焦点，并且该组件的顶级祖先成为关注窗口
		Timer timer =new Timer();//定时装置
		TimerTask task = new TimerTask() {
			int moveIndex =0;
			//int bgiIndex =0;
			int speed = 5 * level;
			@Override
			public void run() {
				if(STATE) {
					if(moveIndex%speed==0) {
						moveDownAction();
						moveIndex=0;//延缓了下降的速度
					}
				}
				moveIndex++;
				//bgiIndex++;
				repaint();
			}
		};//匿名name类
		
		timer.schedule(task, 10, 20);//任务 延迟 时期
	}
	public void testAction() {//无用
		for(int i=0;i<tetromino.cells.length ;i++) {
			System.out.println(tetromino.cells[i].getRow()+" "+tetromino.cells[i].getCol());
		}
	}
			private void keyMoveAction(int k) {
				// TODO Auto-generated method stub
				switch(k) {
				case KeyEvent.VK_RIGHT://KeyEvent击键事件，给对应键盘对应的方法
				moveRightAction();
				break;
				case KeyEvent.VK_LEFT:
				moveLeftAction();
				break;
				case KeyEvent.VK_DOWN:
				moveDownAction();
				break;
				case KeyEvent.VK_UP:
				spinCellAction();
				break;
				case KeyEvent.VK_I:
				moveInitAction();
				break;
				case KeyEvent.VK_P://暂停
				STATE=false;
				break;
				case KeyEvent.VK_C://重启
				STATE=true;
				break;
				case KeyEvent.VK_E://退出
				System.exit(0);
				break;
				}
			}
			protected void moveInitAction() {
				//重新开始，状态为false，重新设置墙，
				//设置上一图案和下一图案，并且把分数，消除的行数和等级清零
				// TODO Auto-generated method stub
				STATE =false;
				wall=new Cell[ROWS][COLS];
				tetromino =Tetromino.ranShape();
				nextone = Tetromino.ranShape();
				score =0;
				lines=0;
				level=0;
			}
			protected void spinCellAction() {
				/*按上键实现旋转，先使得每个方块获得旋转的方法，if没有方块，则什么都没有
				 * 遍历方块，然后获取每一方块的行和列，超出了范围，就什么也不返回
				 * 范围：不超过墙的行 列，不为空；最后在获取旋转之后的方块
				 * */
				// TODO Auto-generated method stub
				Cell[] nCells=tetromino.spin();
				if(nCells==null)
					return;
				for(int i=0;i<nCells.length;i++) {
					int nRow =nCells[i].getRow();
					int nCol= nCells[i].getCol();
					if(nRow<0 ||nRow>=ROWS||nCol<0||nCol>=COLS||wall[nRow][nCol]!=null)
						return;
							}
				tetromino.cells=nCells;
			}
			protected void moveDownAction() {
				/*向下移动，if图案不为空，
				 * 不下落，获取下落的方法
				 * */
				// TODO Auto-generated method stub
				if(tetromino ==null)
					return;
				if(!isBottom()) {
					tetromino.moveDown();
				}
			}
			protected void moveLeftAction() {
				//左移：可以左移和不下落，再调用左移的方法
				// TODO Auto-generated method stub
				if(canLeftMove()&&!isBottom()) {
					tetromino.moveLeft();
				}
			}
			private boolean canLeftMove() {
				/*能否左移：图案存在，通过数组获取每一个小方块，再遍历每一个小方块
				 * 利用空白的构造方法获取小方块，行，列，
				 * if在左面，墙对应的方块的位置左面有方块，则false
				 */
				// TODO Auto-generated method stub
				if(tetromino==null)
				return false;
				Cell[] cells=tetromino.cells;
				for(int i=0;i<cells.length;i++) {
					Cell c=cells[i];
					int row =c.getRow();
					int col=c.getCol();
					if(col==0||wall[row][col-1]!=null)
						return false;
				}
				return true;
			}
			int numIndex =0;
			int guogan =0;
			



			protected void moveRightAction() {
				//右移+不向下落
				// TODO Auto-generated method stub
				if(canRightMove()&&!isBottom()) {
					tetromino.moveRight();}
				}



			private boolean isBottom() {//不下落
				/*是否下落：确定图案存在，
				 * for获取图案，对应的小方块，行，列，false
				 * if下一行不为空 或者下一行是最后一行，
				 * for再次遍历小方块，列，行，墙获取nextone的每一个小方块
				 * 
				 * removeLine()判断是否满行消除,切换背景，获取当前图案和下一个图案，true
				 */
				// TODO Auto-generated method stub
				if(tetromino==null)
				return false;
				Cell[] cells=tetromino.cells;
				for(int i=0;i<cells.length;i++) {
					Cell c=cells[i];
					int col=c.getCol();
					int row=c.getRow();
					if((row+1)==ROWS||wall[row+1][col]!=null) {
						//下落，下落至最后一行或者墙的下一行不为空
						for(int j=0;j<cells.length;j++) {
							Cell cell=cells[i];
							int col1=cell.getCol();
							int row1=cell.getRow();
							wall[row1][col1]=cell;
						}
						removeLine();
						BGI=(BGI==3)?0:BGI+1;
						tetromino=nextone;
						nextone=Tetromino.ranShape();
						return true;
					}
				}
				return false;
			}
			 
			public boolean isGameOver() {
				//游戏结束，遍历列，第0行不为空，则false
				for(int col=0;col<COLS;col++) {
					if(wall[0][col]!=null)
						return true;
				}
				return false;
			}


			private void removeLine() {
				/*定义flag rowstart num 换行
				 * for遍历行 for遍历列 if墙为空，flag=false
				 * if（flag）遍历列。。。。。
				 * 分数加10，行加1，遍历全部向下移
				 */
				// TODO Auto-generated method stub
				boolean flag =true;
				int rowStart =20;
				num =0;
				for(int row=0;row<ROWS;row++) {
					for(int col=0;col<COLS;col++) {
						if(wall[row][col]==null) {
							flag=false;
							break;
						}
					}
					if(flag) {
						num++;
						for(int col=0;col<COLS;col++) {
							wall[row][col]=null;
						}
						rowStart = row;
						score+=10;
						lines+=1;
						level=lines%10==0?level==1?level:level-1:level;
						for(int row1=rowStart;row1>0;row--) {
							for(int col1=0;col1<COLS;col1++) {
								wall[row1][col1]=wall[row-1][col1];
							}
						}
					}
					else {
						flag=true;
					}
				}
			}



			private boolean canRightMove() {
				/*右移：确保图案存在；遍历数组，for遍历小方块，行，列
				 * if(下一个列为最后一列；方块的行和下一列)false
				 */
				// TODO Auto-generated method stub
				if (tetromino ==null)
					return false;
				Cell[] cells=tetromino.cells;
				for(int i=0;i<cells.length;i++) {
				Cell c=cells[i];
				int row=c.getRow();
				int col=c.getCol();
				if((col+1)==COLS||wall[row][col+1]!=null)
					return false;
			}
			return true;

			}
			public void paint(Graphics g) {
				g.drawImage(bgi[BGI], 0, 0,null);
				g.drawImage(tetris, 0, 0, null);
				g.translate(15, 15);
				if(num==1) {//一次性消除的行数
					g.drawImage(lgg, numIndex++, (int)Math.random()*34, null);
					Font font =new Font("宋体",Font.BOLD,18);
					Graphics2D g2= (Graphics2D) g;
					g2.setColor(Color.cyan);
					g2.setColor(Color.blue);
					g2.setFont(font);
					g2.drawString("加油", 0, guogan++);
				}
				paintWall(g);
				paintTetromino(g);
				paintNextone(g);
				paintTabs(g);
				paintGamePause(g);
				paintGameOver(g);
				
				
				
				
			}



			private void paintGameOver(Graphics g) {
				// TODO Auto-generated method stub
				if(isGameOver()) {
					tetromino=null;
					g.drawImage(gameover, -15, -15, null);
					Color color =new Color(0,71,157);
					g.setColor(color);
					Font font =new Font(Font.SERIF,Font.BOLD,30);
					g.setFont(font);
					g.drawString(""+score, 260, 207);
					g.drawString(""+lines, 260, 253);
					g.drawString(""+level, 260, 300);
					STATE=false;
				}
			}



			private void paintGamePause(Graphics g) {
				// TODO Auto-generated method stub
				if(!STATE&&!isGameOver()) {
					g.drawImage(pause, -15, -15, null);
				}
			}



			private void paintTabs(Graphics g) {
				// TODO Auto-generated method stub
				int x=410;
				int y=160;
				Color color = new Color(240,234,34);
				g.setColor(color);
				Font f=new Font(Font.SERIF,Font.BOLD,30);
				g.setFont(f);
				g.drawString(""+score, x, y);
				y+=56;
				g.drawString(""+lines, x, y);
				y+=56;
				g.drawString(""+level, x, y);
			
			}



			private void paintNextone(Graphics g) {
				// TODO Auto-generated method stub
				if(nextone==null)
					return;
				Cell[] cells=nextone.cells;
				for(int i=0;i<cells.length;i++) {
					Cell c=cells[i];
					int row=c.getRow();
					int col=c.getCol()+9;
					int x=col*CELL_SIZE;
					int y=row*CELL_SIZE;
					g.drawImage(c.getBgImage(), x, y, null);
					
				}
			}



			private void paintTetromino(Graphics g) {
				// TODO Auto-generated method stub
				if(tetromino==null)
					return;
				Cell[] cells = tetromino.cells;
				for(int i=0;i<cells.length;i++) {
					Cell c=cells[i];
					int col=c.getCol();
					int row=c.getRow();
					int x=col*CELL_SIZE;
					int y=row*CELL_SIZE;
					g.drawImage(c.getBgImage(), x, y, null);
				}
			}



			private void paintWall(Graphics g) {
				// TODO Auto-generated method stub
				for(int row=0;row<ROWS;row++) {
					for(int col=0;col<COLS;col++) {
						Cell cell= wall[row][col];
						int rows=row*CELL_SIZE;
						int cols=col*CELL_SIZE;
						if(cell==null) {
							
						}else {
							g.drawImage(cell.getBgImage(), cols, rows, null);
						}
					}
					
				}
			}
			
		}
	



	