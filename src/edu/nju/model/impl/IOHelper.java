package edu.nju.model.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IOHelper {
	public ArrayList<String> dataRead=new ArrayList<String>();
	public ArrayList<String> dataWrite=new ArrayList<String>();
	
	public static final int LINE=4; 
	
	public void read() throws IOException {
		File recordFile=new File("save.dat");
		try {
			BufferedReader br=new BufferedReader(new FileReader(recordFile));
			String line=null; 
			while((line=br.readLine())!=null){   
				 dataRead.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			System.out.println("IOHelper文件读入发生异常");
		}
	}
	
	public void write() throws IOException{
		File recordFile=new File("save.dat");
		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(recordFile));
			for (int i = 0; i <dataWrite.size(); i++) {
				bw.write(dataWrite.get(i)+"\n");
			}
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			System.out.println("IOHelper文件写入发生异常");
		}
	}
}
