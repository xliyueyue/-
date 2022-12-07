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
	int num;//����������
	private Tetromino nextone;//��һ������ ����
	private Tetromino tetromino;//��ǰ�������
	private static final int ROWS=20;//����
	private static final int COLS=10;//����
	private int score =0;//����
	private int lines=0;//����
	private int level=5;//�ȼ�
	private Cell[][] wall=new Cell[ROWS][COLS];//ǽ
	private boolean STATE=true;//״̬
	private int BGI =0;//ͼƬ�����
	public static final int CELL_SIZE=26;//26Ԫ�ص�
	public static BufferedImage Z;//��4��������ɵ�ͼƬ����״
	public static BufferedImage S;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage O;
	public static BufferedImage I;
	public static BufferedImage T;
	public static BufferedImage[] bgi =new BufferedImage[4];//����ͼƬ��
	public static BufferedImage pause;//��ͣ
	public static BufferedImage tetris;
	public static BufferedImage gameover;//��Ϸ����
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
		//ʹ��io������ȡͼƬ��ע�⣺��̬,��ס�ӻ����źͷֺ�
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
		e.printStackTrace();//�Զ���ȡ��
	}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame =new JFrame();
		Tetris tetris =new Tetris();//��������
		frame.add(tetris);//��Ӷ���
		frame.setSize(525,600);//��С
		frame.setLocationRelativeTo(null);//����
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//��֤��̨�ر�
		frame.setVisible(true);//���ӻ�
		tetris.action();//����ƶ�����
		//System.out.println(Z);
	}

	private void action() {
		// TODO Auto-generated method stub
		tetromino = Tetromino.ranShape();//��ǰͼƬ
		nextone = Tetromino.ranShape();//��һ��ͼƬ
		KeyListener kl =new KeyAdapter() {//����name�࣬�ӿڣ���
			public void keyPressed(KeyEvent e) {//����
				int k=e.getKeyCode();//������ָ�����ͼ����ͷ�
				keyMoveAction(k);//ʹ�ñ���k�Ի����¼����в���
				repaint();//���¼��غͻ�ȡ����
			}
		};//����name�� ��ס�ӷֺ�
		this.addKeyListener(kl);//��k1����Լ��ļ���
		this.setFocusable(true);//��������Ŀɾ۽�״̬����Ϊȷ����ֵ
		this.requestFocus();//����������ȡ���뽹�㣬���Ҹ�����Ķ������ȳ�Ϊ��ע����
		Timer timer =new Timer();//��ʱװ��
		TimerTask task = new TimerTask() {
			int moveIndex =0;
			//int bgiIndex =0;
			int speed = 5 * level;
			@Override
			public void run() {
				if(STATE) {
					if(moveIndex%speed==0) {
						moveDownAction();
						moveIndex=0;//�ӻ����½����ٶ�
					}
				}
				moveIndex++;
				//bgiIndex++;
				repaint();
			}
		};//����name��
		
		timer.schedule(task, 10, 20);//���� �ӳ� ʱ��
	}
	public void testAction() {//����
		for(int i=0;i<tetromino.cells.length ;i++) {
			System.out.println(tetromino.cells[i].getRow()+" "+tetromino.cells[i].getCol());
		}
	}
			private void keyMoveAction(int k) {
				// TODO Auto-generated method stub
				switch(k) {
				case KeyEvent.VK_RIGHT://KeyEvent�����¼�������Ӧ���̶�Ӧ�ķ���
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
				case KeyEvent.VK_P://��ͣ
				STATE=false;
				break;
				case KeyEvent.VK_C://����
				STATE=true;
				break;
				case KeyEvent.VK_E://�˳�
				System.exit(0);
				break;
				}
			}
			protected void moveInitAction() {
				//���¿�ʼ��״̬Ϊfalse����������ǽ��
				//������һͼ������һͼ�������Ұѷ����������������͵ȼ�����
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
				/*���ϼ�ʵ����ת����ʹ��ÿ����������ת�ķ�����ifû�з��飬��ʲô��û��
				 * �������飬Ȼ���ȡÿһ������к��У������˷�Χ����ʲôҲ������
				 * ��Χ��������ǽ���� �У���Ϊ�գ�����ڻ�ȡ��ת֮��ķ���
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
				/*�����ƶ���ifͼ����Ϊ�գ�
				 * �����䣬��ȡ����ķ���
				 * */
				// TODO Auto-generated method stub
				if(tetromino ==null)
					return;
				if(!isBottom()) {
					tetromino.moveDown();
				}
			}
			protected void moveLeftAction() {
				//���ƣ��������ƺͲ����䣬�ٵ������Ƶķ���
				// TODO Auto-generated method stub
				if(canLeftMove()&&!isBottom()) {
					tetromino.moveLeft();
				}
			}
			private boolean canLeftMove() {
				/*�ܷ����ƣ�ͼ�����ڣ�ͨ�������ȡÿһ��С���飬�ٱ���ÿһ��С����
				 * ���ÿհ׵Ĺ��췽����ȡС���飬�У��У�
				 * if�����棬ǽ��Ӧ�ķ����λ�������з��飬��false
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
				//����+��������
				// TODO Auto-generated method stub
				if(canRightMove()&&!isBottom()) {
					tetromino.moveRight();}
				}



			private boolean isBottom() {//������
				/*�Ƿ����䣺ȷ��ͼ�����ڣ�
				 * for��ȡͼ������Ӧ��С���飬�У��У�false
				 * if��һ�в�Ϊ�� ������һ�������һ�У�
				 * for�ٴα���С���飬�У��У�ǽ��ȡnextone��ÿһ��С����
				 * 
				 * removeLine()�ж��Ƿ���������,�л���������ȡ��ǰͼ������һ��ͼ����true
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
						//���䣬���������һ�л���ǽ����һ�в�Ϊ��
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
				//��Ϸ�����������У���0�в�Ϊ�գ���false
				for(int col=0;col<COLS;col++) {
					if(wall[0][col]!=null)
						return true;
				}
				return false;
			}


			private void removeLine() {
				/*����flag rowstart num ����
				 * for������ for������ ifǽΪ�գ�flag=false
				 * if��flag�������С���������
				 * ������10���м�1������ȫ��������
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
				/*���ƣ�ȷ��ͼ�����ڣ��������飬for����С���飬�У���
				 * if(��һ����Ϊ���һ�У�������к���һ��)false
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
				if(num==1) {//һ��������������
					g.drawImage(lgg, numIndex++, (int)Math.random()*34, null);
					Font font =new Font("����",Font.BOLD,18);
					Graphics2D g2= (Graphics2D) g;
					g2.setColor(Color.cyan);
					g2.setColor(Color.blue);
					g2.setFont(font);
					g2.drawString("����", 0, guogan++);
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
	



	