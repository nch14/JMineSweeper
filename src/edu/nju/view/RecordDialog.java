/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.nju.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import edu.nju.model.impl.IOHelper;

public class RecordDialog {

	/**
	 *  
	 */
	public RecordDialog(JFrame parent) {
		super();
		
		String[] timeLeastTemp=new String[IOHelper.LINE];
		double[] rate=new double[IOHelper.LINE];
		IOHelper ih=new IOHelper();
		try {
			ih.read();
			String[] elem;
			for(int i=0;i<IOHelper.LINE;i++){
				String aLine=ih.dataRead.get(i);
				elem=aLine.split(";");
				timeLeastTemp[i]=elem[0];
				rate[i]=Double.parseDouble(elem[1]);
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("记录表格读取文件失败");
			timeLeastTemp= new String[]{"Unknow","Unknow","Unknow","Unknow"};
			rate = new double[]{0,0,0,0};
		}
		this.timeLeast =timeLeastTemp;
		this.rakeForUser = rate;
		
		initialization(parent);
	}

	public boolean show(String[] names, double[] score) {
		clear = false;
		this.timeLeast = names;
		this.rakeForUser = score;
		dialog.setVisible(true);
		return clear;
	}
	
	public void show(){
		//dialog.setVisible(true);
		show(timeLeast,rakeForUser);
	}

	private void initialization(JFrame parent) {

		dialog = new JDialog(parent, "record", true);

		okBtn = new JButton("ok");
		okBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		okBtn.setBounds(60, 180, 70, 23);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});

		clearBtn = new JButton("clear");
		clearBtn.setFont(new Font("Monospaced", Font.PLAIN, 12));
		clearBtn.setBounds(160,180, 70, 23);
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear = true;
				IOHelper ih=new IOHelper();
				for (int i = 0; i<IOHelper.LINE;i++) {
					timeLeast[i] = "Unknow";
					rakeForUser[i] = 0;
					ih.dataWrite.add(timeLeast[i]+";"+rakeForUser[i]+";"+"0"+";"+"0");//最后这个两个0分别为历史总场数，历史总胜场数
				}
				try {
					ih.write();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					System.out.println("Clear数据时写入发生错误");
				}
				textPanel.repaint();
			}
		});

		line = new JSeparator();
		line.setBounds(20, 160, 240, 4);

		panel = new JPanel();
		panel.setLayout(null);

		textPanel = new DescribeTextPanel();
		panel.add(textPanel);

		panel.add(okBtn);
		panel.add(clearBtn);
		//panel.add(line);

		dialog.setContentPane(panel);
		dialog.setBounds(parent.getLocation().x + 50,
				parent.getLocation().y + 50, 300, 260);

		clear = false;

	}

	private class DescribeTextPanel extends JPanel {

		DescribeTextPanel() {
			super();
			setBounds(0, 0, 350, 200);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setFont(new Font("Monospaced", Font.PLAIN, 12));
			int length = timeLeast.length;
			g.drawString("游戏类型", 20, 30 );
			g.drawString("历史胜率",100, 30 );
			g.drawString("最好成绩（秒）", 180, 30 );
			for (int i = 0; i <IOHelper.LINE; i++) {
				g.drawString(rank[i], 30, 30 * (i + 2));
				g.drawString(String.valueOf(rakeForUser[i])+"%",125, 30 * (i + 2));
				g.drawString(timeLeast[i], 190, 30 * (i + 2));
			}
		}
	}

	private final String[] rank = { "Easy", "Hard", "Hell","Custom"};
  	private JDialog dialog;

	private JPanel panel;

	private JButton okBtn;

	private JButton clearBtn;

	private JSeparator line;

	private String timeLeast[];

	private double rakeForUser[];

	private JPanel textPanel;

	boolean clear;
}