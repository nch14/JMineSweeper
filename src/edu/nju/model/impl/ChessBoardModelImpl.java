package edu.nju.model.impl;

import java.util.ArrayList;
import java.util.List;

import edu.nju.model.po.BlockPO;
import edu.nju.model.service.ChessBoardModelService;
import edu.nju.model.service.GameModelService;
import edu.nju.model.service.ParameterModelService;
import edu.nju.model.state.BlockState;
import edu.nju.model.state.GameResultState;
import edu.nju.model.state.GameState;
import edu.nju.model.vo.BlockVO;

public class ChessBoardModelImpl extends BaseModel implements ChessBoardModelService{
	
	private GameModelService gameModel;
	private ParameterModelService parameterModel;
	
	private BlockPO[][] blockMatrix;

	
	public ChessBoardModelImpl(ParameterModelService parameterModel){
		this.parameterModel = parameterModel;
	}

	@Override
	public boolean initialize(int width, int height, int mineNum) {
		// TODO Auto-generated method stub
		/********************简单示例初始化方法，待完善********************/
		blockMatrix = new BlockPO[width][height];
		setBlock(mineNum);
		
		
		this.parameterModel.setMineNum(mineNum);
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		
		this.printBlockMatrix();
		
		return false;
	}

	@Override
	public boolean excavate(int x, int y) {
		// TODO Auto-generated method stub
		/********************简单示例挖开方法，待完善********************/
		if(blockMatrix == null)
			return false;
		
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		BlockPO block = blockMatrix[x][y];
		
		//已经被标记的方块是不可以被挖开的
		if (block.getState()!=BlockState.FLAG){
			block.setState(BlockState.CLICK);
			blocks.add(block);
			GameState gameState = GameState.RUN;
			if (block.isMine()) {
				gameState = GameState.OVER;
				this.gameModel.gameOver(GameResultState.FAIL);
				
				int width = blockMatrix.length;
				int height = blockMatrix[0].length;
				
				for(int i=0;i<width;i++){
					for(int j=0;j<height;j++){
						blocks.add(blockMatrix[i][j]);
					}
				}
				
			}
			super.updateChange(new UpdateMessage("excute", this.getDisplayList(
					blocks, gameState)));
		}
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		return true;
	}
	
	@Override
	public boolean mark(int x, int y) {
		// TODO Auto-generated method stub
		/********************简单示例标记方法，待完善********************/
		if(blockMatrix == null)
			return false;
		
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		BlockPO block = blockMatrix[x][y];
		 
		BlockState state = block.getState();
		if(state == BlockState.UNCLICK){
			block.setState(BlockState.FLAG);
			this.parameterModel.minusMineNum();
			if(block.isMine()){
				ParameterModelImpl.unMarkLeft-=1;
			}	
		}
		else if(state == BlockState.FLAG){
			block.setState(BlockState.UNCLICK);
			this.parameterModel.addMineNum();
			if(block.isMine()){
				ParameterModelImpl.unMarkLeft+=1;
			}
		}
		
		if(ParameterModelImpl.unMarkLeft==0){
			this.gameModel.gameOver(GameResultState.SUCCESS);
		}
		
		blocks.add(block);	
		super.updateChange(new UpdateMessage("excute",this.getDisplayList(blocks, GameState.RUN)));
		/***********请在删除上述内容的情况下，完成自己的内容****************/
		
		return true;
	}

	@Override
	public boolean quickExcavate(int x, int y) {
		// TODO Auto-generated method stub
		/***********请在此处完成快速挖开方法实现****************/
		if(blockMatrix == null)
			return false;
		
		List<BlockPO> blocks = new ArrayList<BlockPO>();
		BlockPO block = blockMatrix[x][y];
		 
		//只有在这个方块已经被挖开的情况下，才可能实现快速挖开
		BlockState state = block.getState();
		if(state.equals(BlockState.CLICK)){
			
			//方块上的数字等于周围被标记的雷数时
			int mineDoubleClicked=block.getMineNum();

			//做一次遍历,得到周围雷格的情况
			int xMine=x-1;
			int yMine=y-1;
			ArrayList<BlockPO> flaged=new ArrayList<BlockPO>();
			ArrayList<BlockPO> clicked=new ArrayList<BlockPO>();
			ArrayList<BlockPO> unClicked=new ArrayList<BlockPO>();
			
			for(int i=0;i<3;i++){
				
				for(int j=0;j<3;j++){
					try {
						BlockPO aBlock=blockMatrix[xMine][yMine];
						if(aBlock.getState().equals(BlockState.FLAG)){
							flaged.add(aBlock);
						}else{
							if(aBlock.getState().equals(BlockState.CLICK)){
								clicked.add(aBlock);
							}else{
								unClicked.add(aBlock);
							}
						}
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
					}
					xMine++;
				}
				yMine++;
				xMine-=3;
			}
			
			
			if(flaged.size()==mineDoubleClicked){				
				for(int i=0;i<unClicked.size();i++){
					BlockPO quickBlock=unClicked.get(i);
					if (quickBlock.getState()!=BlockState.FLAG){
						quickBlock.setState(BlockState.CLICK);
						blocks.add(quickBlock);
						GameState gameState = GameState.RUN;
						if (quickBlock.isMine()) {
							gameState = GameState.OVER;
							this.gameModel.gameOver(GameResultState.FAIL);
						}
						super.updateChange(new UpdateMessage("excute", this.getDisplayList(
								blocks, gameState)));
					}
					if(quickBlock.getMineNum()==0){						
						quickExcavate(quickBlock.getX(),quickBlock.getY());
					}
				}			
				return true;
			}else{
				return false;//如果在该雷格都没有被挖开的情况下，不会执行快速挖开操作，所以返回值为false
			}
	}
		return false;
		}

	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 初始化BlockMatrix中的Block，并随机设置mineNum颗雷
	 * 同时可以为每个Block设定附近的雷数
	 * @param mineNum
	 * @return
	 */
	private boolean setBlock(int mineNum){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		//初始化
		ArrayList<BlockPO> helpForSetMine=new ArrayList<BlockPO>();
		for(int i = 0 ; i<width; i++){
			for (int j = 0 ; j< height; j++){
				blockMatrix[i][j] = new BlockPO(i,j);
				helpForSetMine.add(blockMatrix[i][j]);
			}
		}				
		if(width==48&&height==48){
			int[][] leeRuqi=LeeRuqi();
			for(int i=0;i<48;i++){
				for(int j=0;j<48;j++){
					if(leeRuqi[i][j]==1){
						blockMatrix[i][j].setMine(true);
					}
				}
			}
			
			
			
			
			
		}else{
			//布雷
			while(mineNum>0){
				int number=(int)(Math.random()*helpForSetMine.size());
				helpForSetMine.get(number).setMine(true);
				addMineNum(helpForSetMine.get(number).getX(),helpForSetMine.get(number).getY());
				mineNum--;
				helpForSetMine.remove(number);
			}
			helpForSetMine=null;//用完了就释放资源			
		}
		return true;
	}	
	/**
	 * 示例方法，可选择是否保留该形式
	 * 
	 * 将(i,j)位置附近的Block雷数加1
	 * @param i
	 * @param j
	 */
	private void addMineNum(int i, int j){
		int width = blockMatrix.length;
		int height = blockMatrix[0].length;
		
		int tempI = i-1;		
		
		for(;tempI<=i+1;tempI++){
			int tempJ = j-1;
			for(;tempJ<=j+1;tempJ++){
				if((tempI>-1&&tempI<width)&&(tempJ>-1&&tempJ<height)){
					blockMatrix[tempI][tempJ].addMine();
				}
			}
		}
		
	}
	
	/**
	 * 将逻辑对象转化为显示对象
	 * @param blocks
	 * @param gameState
	 * @return
	 */
	private List<BlockVO> getDisplayList(List<BlockPO> blocks, GameState gameState){
		List<BlockVO> result = new ArrayList<BlockVO>();
		for(BlockPO block : blocks){
			if(block != null){
				BlockVO displayBlock = block.getDisplayBlock(gameState);
				if(displayBlock.getState() != null)
				result.add(displayBlock);
			}
		}
		return result;
	}

	@Override
	public void setGameModel(GameModelService gameModel) {
		// TODO Auto-generated method stub
		this.gameModel = gameModel;
	}
	
	//控制台输出雷的设置情况，实际发布可删去
	private void printBlockMatrix(){
		for(BlockPO[] blocks : this.blockMatrix){
			for(BlockPO b :blocks){
				String p = b.getMineNum()+"";
				if(b.isMine())
					p="*";
				System.out.print(p);
			}
			System.out.println();
		}
	}
	
	public int[][] LeeRuqi(){
		int[][] she=new int[48][48];
		for(int i=0;i<48;i++){
			for(int j=0;j<48;j++){
				she[i][j]=0;
			}
		}
		
		//S
		she[7][6]=1;
		she[7][7]=1;
		she[7][8]=1;
		she[7][9]=1;
		she[7][10]=1;
		
		she[11][6]=1;
		she[11][7]=1;
		she[11][8]=1;
		she[11][9]=1;
		she[11][10]=1;
		
		she[15][6]=1;
		she[15][7]=1;
		she[15][8]=1;
		she[15][9]=1;
		she[15][10]=1;
		
		she[8][6]=1;
		she[9][6]=1;
		she[10][6]=1;
		
		she[12][10]=1;
		she[13][10]=1;
		she[14][10]=1;
		
		//H
		for(int i=7;i<16;i++){
			she[i][13]=1;
		}
		
		for(int i=11;i<16;i++){
			she[i][17]=1;
		}
		
		she[11][14]=1;
		she[11][15]=1;
		she[11][16]=1;
		
		//E
		for(int i=7;i<16;i++){
			she[i][20]=1;
		}
		
		for(int i=7;i<12;i++){
			she[i][24]=1;
		}
		
		she[7][21]=1;
		she[7][22]=1;
		she[7][23]=1;
		
		she[11][21]=1;
		she[11][22]=1;
		she[11][23]=1;
		
		she[15][21]=1;
		she[15][22]=1;
		she[15][23]=1;
		she[15][24]=1;
		
		//I
		for(int i=7;i<16;i++){
			she[i][27]=1;
		}
		
		she[8][27]=0;
		she[9][27]=0;
		
		//L
		for(int i=7;i<16;i++){
			she[i][30]=1;
		}
		
		//A
		for(int i=8;i<15;i++){
			she[i][34]=1;
		}
		
		for(int i=8;i<15;i++){
			she[i][39]=1;
		}
		
		for(int i=35;i<39;i++){
			she[7][i]=1;
		}
		
		for(int i=35;i<39;i++){
			she[15][i]=1;
		}
		
		she[8][35]=1;
		she[8][38]=1;
		
		she[14][35]=1;
		she[14][38]=1;
		
		she[14][40]=1;
		she[15][41]=1;
		
		//图形
		for(int i=9;i<39;i++){
			she[32][i]=1;
		}
		
		she[31][10]=1;
		she[30][11]=1;
		she[29][12]=1;
		she[28][13]=1;
		
		she[33][10]=1;
		she[34][11]=1;
		she[35][12]=1;
		she[36][13]=1;
		
		for(int i=19;i<22;i++){
			she[26][i]=1;
		}
		for(int i=27;i<30;i++){
			she[26][i]=1;
		}
		
		for(int i=18;i<23;i++){
			she[27][i]=1;
		}
		for(int i=26;i<31;i++){
			she[27][i]=1;
		}
		
		for(int i=17;i<32;i++){
			she[28][i]=1;
		}
		
		for(int i=16;i<33;i++){
			she[29][i]=1;
		}
		
		for(int i=16;i<33;i++){
			she[30][i]=1;
		}
		
		for(int i=17;i<32;i++){
			she[31][i]=1;
		}
		
		for(int i=19;i<30;i++){
			she[33][i]=1;
		}
		
		for(int i=20;i<29;i++){
			she[34][i]=1;
		}
		
		for(int i=21;i<28;i++){
			she[35][i]=1;
		}
		
		for(int i=22;i<27;i++){
			she[36][i]=1;
		}

		for(int i=23;i<26;i++){
			she[37][i]=1;
		}
		
		for(int i=24;i<25;i++){
			she[38][i]=1;
		}
		
		return she;
	}
}
