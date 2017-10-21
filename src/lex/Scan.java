package lex;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Scan {
	private static String inputPath = "";
	public String input;
	public int pointer;
	
	public Scan(String filename){
		File sourceFile = new File(Scan.inputPath+filename);
		ArrayList<Character> trans = new ArrayList<Character>();
		try {
			FileInputStream in = new FileInputStream(sourceFile);
			char ch1 = ' ';
			char ch2 = ' ';//��������֤�Ƿ�Ϊ�����ڽ�β����ע�ͽ�β
			while(in.available()>0){
				if(ch2 != ' '){
					ch1 = ch2;
				} else {
					ch1 = (char) in.read();
				}
				
				if(ch1 == '\''){//����ɾ���հ�ʱ�����������Ŀհ��ַ��޳�
					trans.add(ch1);
					trans.add((char)in.read());
					trans.add((char)in.read());
				} else if (ch1 == '\"'){//���⽫�ַ����еĿհ��޳�
					trans.add(ch1);
					while(in.available()>0){
						ch1 = (char)in.read();
						trans.add(ch1);
						if(ch1 == '\"'){
							break;
						}
					}
				} else if (ch1 == '/'){//�޳��ַ���
					ch2 = (char)in.read();
					if(ch2 == '/'){
						while(in.available() > 0){
							ch2 = (char)in.read();
							if(ch2 == '\n'){
								break;
							}
						}
						ch2 = ' ';
					} else if (ch2 == '*') {
						while(in.available() > 0){
							ch1 = (char)in.read();
							if(ch1 == '*'){
								ch2 = (char)in.read();
								if(ch2 == '/'){
									break;
								}
							}
						}
					} else {
						if(ch2 == ' '){
							while(ch2 == ' '){
								ch2 = (char)in.read();
							}
						}
						trans.add(ch1);
						trans.add(ch2);
						ch2 = ' ';
					}
				} else if(ch1 == ' '){
					if(trans.get(trans.size()-1) == ' '){
						continue;
					} else {
						//trans.add(' ');
					}
				} else {
					if((int)ch1 == 13 ||(int)ch1 == 10 ||(int)ch1 == 32){//ȥ������
						
					} else {
						trans.add(ch1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		char[] chStr = new char[trans.size()];
		for(int i = 0;i < trans.size();i++){
			chStr[i] = trans.get(i);
		}
		String result = new String(chStr);
		this.input = result;
		this.pointer = 0;
	}
	
	public char getNextChar(){
		if(pointer==input.length()){
			return (char)0;
		} else {
			return input.charAt(pointer++);
		}
	}
	
	//����n���ַ�
	public void retract(int n){
		for(int i = 0;i < n;i++){
			pointer--;
		}
	}
	
	public int getIndex(){
		return pointer;
	}
	
	public int getLength(){
		return this.input.length();
	}
	
	public String getTestString(int index){
		int temp = index;
		int len = 1;
		while(isLetterOrDigit(input.charAt(temp))&&(temp<=(input.length()-1))){
			temp++;
			len++;
		}
		String result = input.substring(index-1,index-1+len);
		return result;
	}
	
	private boolean isLetterOrDigit(char c){
		if(c=='_'||(c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')){
			return true;
		} else {
			return false;
		}
	}
	public void move(int n){
		for(int i = 0;i < n;i++){
			pointer++;
		}
	}
	
	public String getStringInQuotation(int index){
		int temp = index;
		while(input.charAt(temp-1)!='\"'){
			temp--;
		}
		StringBuilder sb = new StringBuilder();
		while(input.charAt(temp) != '\"'){
			sb.append(input.charAt(temp));
			temp++;
		}
		return sb.toString();
	}
	
}
