package edu.nju.model.data;

import java.io.IOException;

import edu.nju.model.impl.IOHelper;

public class MyStaticData {
	//0;1;2
	public static int[] gameTotal;	
	public static int[] winTotal;	
	public static String[] winTime;
	
	public static int isRun=-1;

	
	public MyStaticData(){
		gameTotal=new int[IOHelper.LINE];
		winTotal=new int[IOHelper.LINE];
		winTime=new String[IOHelper.LINE];
		
		IOHelper ih=new IOHelper();
		try {
			ih.read();
			String[] elem;
			for(int i=0;i<IOHelper.LINE;i++){
				String aLine=ih.dataRead.get(i);
				elem=aLine.split(";");
				winTime[i] = elem[0];
				gameTotal[i]=Integer.parseInt(elem[2]);
				winTotal[i]=Integer.parseInt(elem[3]);
			}
			
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("读取文件失败");
			
		}
		
		
	}
	public static void reFlashData(){
		double[] rate=new double[IOHelper.LINE];
		for(int i=0;i<IOHelper.LINE;i++){
			try {
				rate[i] = (winTotal[i] *100/ gameTotal[i]);
				//System.out.println("shenglve"+rate[i]);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				rate[i]=0;
			}
		}
		IOHelper ih=new IOHelper();
		for(int j=0;j<IOHelper.LINE;j++){
			ih.dataWrite.add(winTime[j]+";"+rate[j]+";"+gameTotal[j]+";"+winTotal[j]);
		}
		try {
			ih.write();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			System.out.println("写入发生错误");
		}
	}
}