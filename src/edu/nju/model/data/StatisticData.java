package edu.nju.model.data;

import edu.nju.model.po.StatisticPO;

/**
 * 负责进行统计数据获取和记录操作
 * @author Wangy
 *
 */
public class StatisticData {
	StatisticPO aStatistic;
	public StatisticPO getStatistic(){
		return aStatistic;
	}
	
	public boolean saveStatistic(StatisticPO statistic){
		aStatistic=statistic;
		return false;
	}

}