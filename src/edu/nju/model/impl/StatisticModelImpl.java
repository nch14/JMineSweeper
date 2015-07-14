package edu.nju.model.impl;

import edu.nju.model.data.StatisticData;
import edu.nju.model.po.StatisticPO;
import edu.nju.model.service.StatisticModelService;
import edu.nju.model.state.GameResultState;

public class StatisticModelImpl extends BaseModel implements StatisticModelService{
	
	private StatisticData statisticDao;
	
	public StatisticModelImpl(){
		//初始化Dao
		statisticDao=new StatisticData();
		
		StatisticPO std=new StatisticPO();
		statisticDao.saveStatistic(std);
	}

	@Override
	public void recordStatistic(GameResultState result, int time) {
		// TODO Auto-generated method stub
		statisticDao.getStatistic().setSum(statisticDao.getStatistic().getSum()+1);//总场数加一
		if(result.equals(GameResultState.SUCCESS)){
			statisticDao.getStatistic().setWins(statisticDao.getStatistic().getWins()+1);//获胜数加一
		}
		statisticDao.getStatistic().setWinrate(statisticDao.getStatistic().getWins()/statisticDao.getStatistic().getSum());
	}

	@Override
	public void showStatistics() {
		// TODO Auto-generated method stub
		
	}

}
