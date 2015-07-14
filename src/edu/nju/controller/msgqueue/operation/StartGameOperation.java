package edu.nju.controller.msgqueue.operation;

import edu.nju.controller.msgqueue.OperationQueue;
import edu.nju.model.data.MyStaticData;
import edu.nju.model.service.GameModelService;

public class StartGameOperation extends MineOperation{

	
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		GameModelService game = OperationQueue.getGameModel();
		OperationQueue.isRunning=true;
		game.startGame();
	}

}
