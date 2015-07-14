/*
 *
 * TODO To manage menu action
 */
package edu.nju.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import edu.nju.controller.impl.MenuControllerImpl;
import edu.nju.controller.impl.SettingControllerImpl;
import edu.nju.controller.service.MenuControllerService;
import edu.nju.controller.service.SettingControllerService;
import edu.nju.model.data.MyStaticData;
import edu.nju.model.impl.ChessBoardModelImpl;
import edu.nju.model.impl.GameModelImpl;
import edu.nju.model.impl.ParameterModelImpl;
import edu.nju.model.impl.StatisticModelImpl;
import edu.nju.network.client.ClientInHandlerImpl;
import edu.nju.network.client.ClientServiceImpl;
import edu.nju.network.host.HostInHandlerImpl;
import edu.nju.network.host.HostServiceImpl;
import edu.nju.network.modelProxy.GameModelProxy;
import edu.nju.view.CustomDialog;
import edu.nju.view.MainFrame;
import edu.nju.view.RecordDialog;
 

public class MenuListener implements ActionListener{

	private MainFrame ui;
	MenuControllerService menuController = new MenuControllerImpl();
	SettingControllerService settingController = new SettingControllerImpl();
	
	public MenuListener(MainFrame ui){
		this.ui = ui;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ui.getMenuItem("start")) {//生成游戏，默认生成简单游戏
			settingController.setEasyGameLevel();
			//MyStaticData.gameTotal[0]++;
			//MyStaticData.isRun=0;
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("easy")) {//生成简单游戏
			settingController.setEasyGameLevel();
			//MyStaticData.gameTotal[0]++;
			//MyStaticData.isRun=0;
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("hard")) {//生成中等游戏
			settingController.setHardGameLevel();
			//MyStaticData.gameTotal[1]++;
			//MyStaticData.isRun=1;
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("hell")) {//生成大型游戏
			settingController.setHellGameLevel();
			//MyStaticData.gameTotal[2]++;
			//MyStaticData.isRun=2;
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("custom")) {//生成定制游戏，需要向controller传递棋盘的高、宽和雷数
			CustomDialog cd=new CustomDialog(ui.getMainFrame());
			cd.show();
			
			settingController.setCustomizedGameLevel(cd.getHeight(),cd.getWidth(),cd.getMineNumber());
			menuController.startGame();
			
		} else if (e.getSource() == ui.getMenuItem("exit")) {
			System.exit(0);
		} else if (e.getSource() == ui.getMenuItem("record")) {//统计胜率信息
			RecordDialog rd=new RecordDialog(ui.getMainFrame());
			rd.show();
			
		}else if(e.getSource() == ui.getMenuItem("host")){//注册成为主机
			HostServiceImpl host = new HostServiceImpl();
			HostInHandlerImpl hostH = new HostInHandlerImpl();
			
			GameModelImpl game = new GameModelImpl(new StatisticModelImpl(),new ChessBoardModelImpl(new ParameterModelImpl()));
			game.addObserver(host);
			if(host.init(hostH)){
				System.out.println("Connecting!!!");
				game.startGame();
			}
		}else if(e.getSource() == ui.getMenuItem("client")){//注册成为客户端
			ClientServiceImpl client = new ClientServiceImpl();
			ClientInHandlerImpl clientH = new ClientInHandlerImpl();
			
			GameModelProxy gameProxy = new GameModelProxy(client);
			clientH.addObserver(gameProxy);
			
			client.init("127.0.0.1", clientH);
		}
	}


}