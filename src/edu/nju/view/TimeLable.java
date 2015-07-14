package edu.nju.view;

import javax.swing.JLabel;




public class TimeLable extends JLabel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2006328075940911593L;
	public static double gameTime=0;
	public static boolean gameRun=false;
	@Override
		public void run() {
			// TODO 自动生成的方法存根
			gameTime=0;
			gameRun=true;
			while(TimeLable.gameRun){			
				gameTime+=0.1;	
				this.setText((int)gameTime+"");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
		
	}

