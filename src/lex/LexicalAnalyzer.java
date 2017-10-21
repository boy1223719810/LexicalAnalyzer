package lex;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class LexicalAnalyzer {
	public static void main(String[] args){
		LexicalAnalyzer lex = new LexicalAnalyzer("a.c");
		ArrayList<Token> tokens = lex.getTokenList();
		lex.output(tokens);
	}
	
	/*
	 * �Լ�ʵ�ֵ�ɨ��ͻ�����
	 */
	private Scan scan;
	
	/**
	 * �ʷ���������Ĺ��췽�� 
	 * @param filename �ļ�λ�ú��ļ���
	 */
	public LexicalAnalyzer(String filename){
		this.scan = new Scan(filename);
	}

	/**
	 * ���ʷ��������������ļ���
	 * @param list �ʷ����������TokenList
	 * @throws FileNotFoundException 
	 */
	@SuppressWarnings("resource")
	public void output(ArrayList<Token> list){

		for(int i = 0;i < list.size();i++){
			String str = "( "+list.get(i).type+","+list.get(i).value+" )";
			System.out.println(str);

		}
	}
	
	/**
	 * ͨ���﷨�������Token����
	 * @return Token����
	 */
	public ArrayList<Token> getTokenList(){
		ArrayList<Token> result = new ArrayList<Token>();
		int index = 0;
		while(index < scan.getLength()){
			Token token = analyze(index);
			result.add(token);
			index = scan.getIndex();
		}
		this.scan.retract(scan.getLength()-1);
		return result;
	}
	
	/*
	 * �ؼ���
	 */
	private String[] keyword ={ //�ؼ���
			"if","else","for","int","double","char","long","switch","void","while","struct","break",
		"case","float","enum","register","typedef","return","union","const",
		"extern","short","unsigned","continue","signed",
		"default","goto","sizeof","volatile","do","static","auto"
	};

	/**
	 * ��ĳһλ�ý��дʷ�����
	 * @param index ��ĸ��ʼ��λ��
	 * @return ����Token
	 */
	private boolean flag = false;//�����б�˫����֮����ַ���
	private Token analyze(int index){
		int length = scan.getLength();
		int type = -1;
		String value = "";
		while(index < length){
			char ch = scan.getNextChar();
			index++;
			if(isDigit(ch)){//�ж��Ƿ�Ϊһ������
				if(Type.isCalc(type)){
					scan.retract(1);
					break;
				}
				if(value == ""){
					value = new Character(ch).toString();
					type = Type.NUM;
				} else {
					value += new Character(ch).toString();
				}
				
			} else if (isLetter(ch)){
				if(Type.isCalc(type)){
					scan.retract(1);
					break;
				}
				if(flag){
					value = scan.getStringInQuotation(index);
					type = Type.ID;
					scan.move(value.length()-1);
					return new Token(type,value);
				}
				if(type == Type.ID){
					value += new Character(ch).toString();
					continue;
				}
				String str = scan.getTestString(index);
				String val = null;
				if(str.startsWith("include")){
					val = "include";
					type = Type.INCLUDE;
				} else {
					for(int i = 0;i < keyword.length;i++){
						if(str.startsWith(keyword[i])){
							val = keyword[i];
							type = i;
							break;
						}
					}
				}
				if(val == null){
					type = Type.ID;
					if(value == ""){
						value = new Character(ch).toString();
					} else {
						value += new Character(ch).toString();
					}
				} else {
					value = val;
					scan.move(value.length()-1);
					return new Token(type,value);
				}
				
			} else {
				if(type == Type.NUM || type == Type.ID){
					scan.retract(1);
					return new Token(type,value);
				}
				switch(ch){
				case '='://==,=
					if(type == -1){
						type = Type.ASSIGN;
						value = "=";
					} else if(type == Type.LT){//C
						type = Type.LE;
						value = "<=";
					} else if(type == Type.GT){//>=
						type = Type.GE;
						value = ">=";
					} else if(type == Type.ASSIGN){//==
						type = Type.EQUAL;
						value = "==";
					} else if(type == Type.NOT){//!=
						type = Type.NE;
						value = "!=";
					} else if(type == Type.ADD){//+=
						type = Type.INCREASEBY;
						value = "+=";
					} else if(type == Type.SUB){//-=
						type = Type.DECREASEBY;
						value = "-=";
					} else if(type == Type.DIV){///=
						type = Type.DIVBY;
						value = "/=";
					} else if(type == Type.MUL){//*=
						type = Type.MULBY;
						value = "*=";
					}
					break;
				case '+':
					if(type == -1){
						type = Type.ADD;
						value = "+";
					} else if(type == Type.ADD){//++
						type = Type.INCREASE;
						value = "++";
					} 
					break;
				case '-':
					if(type == -1){
						type = Type.SUB;
						value = "-";
					} else if(type == Type.SUB){//--
						type = Type.DECREASE;
						value = "--";
					}
					break;
				case '*':
					if(type == -1){
						type = Type.MUL;
						value = "*";
					}
					break;
				case '/':
					if(type == -1){
						type = Type.DIV;
						value = "/";
					}
					break;
				case '<':
					if(type == -1){
						type = Type.LT;
						value = "<";
					}
					break;
				case '>':
					if(type == -1){
						type = Type.GT;
						value = ">";
					}
					break;
				case '!':
					if(type == -1){
						type = Type.NOT;
						value = "!";
					}
					break;
				case '|':
					if(type == -1){
						type = Type.OR_1;
						value = "|";
					} else if(type == Type.OR_1){
						type = Type.OR_2;
						value = "||";
					}
					break;
				case '&':
					if(type == -1){
						type = Type.AND_1;
						value = "&";
					} else if(type == Type.AND_1){
						type = Type.AND_2;
						value = "&&";
					}
					break;
				case ';':
					if(type == -1){
						type = Type.SEMICOLON;
						value = ";";
					}
					break;
				case '{':
					if(type == -1){
						type = Type.BRACE_L;
						value = "{";
					}
					break;
				case '}':
					if(type == -1){
						type = Type.BRACE_R;
						value = "}";
					}
					break;
				case '[':
					if(type == -1){
						type = Type.BRACKET_L;
						value = "[";
					}
					break;
				case ']':
					if(type == -1){
						type = Type.BRACKET_R;
						value = "]";
					}
					break;
				case '(':
					if(type == -1){
						type = Type.PARENTHESIS_L;
						value = "(";
					}
					break;
				case ')':
					if(type == -1){
						type = Type.PARENTHESIS_R;
						value = ")";
					}
					break;

				case ',':
					if(type == -1){
						type = Type.COMMA;
						value = ",";
					}
					break;

				case '"':
					if(flag == false){
						flag = true;//����������Ե�˫�����еĵ�һ��
					} else {
						flag = false;
					}

					break;
				default:
					break;
				}
				if(!Type.isCalc(type)){
					break;
				}
			}
		}
		if(value.length()>1){
			scan.move(value.length()-1);
		}
		Token token = new Token(type,value);
		return token;
	}
	
	private boolean isDigit(char c){
		if((c<='9'&&c>='0')||c=='.'){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isLetter(char c){
		if((c>='a'&&c<='z')||c=='_'||(c>='A'&&c<='Z')){
			return true;
		} else {
			return false;
		}
	}
	
}
