package lex;

public class Type {
//	运算符
	public static final int ASSIGN = 33; // =
	public static final int LT = 34;   //  <
	public static final int GT = 35;//  >

	public static final int ADD = 36;  // +
	public static final int SUB = 37;  // -
	public static final int MUL = 38;//*
	public static final int DIV = 39; //  /
//数字
	public static final int ID = 40;
	public static final int NUM = 41;

	public static final int INCREASEBY = 42;//  +=
	public static final int DECREASEBY = 43;//  -=
	public static final int MULBY = 44;//   *=
	public static final int DIVBY = 45;//  /=

	public static final int LE = 46;// <=
	public static final int GE = 47;// >=
	public static final int NE = 48;// !=
	public static final int EQUAL = 49;//  ==

	public static final int INCREASE = 50;//++
	public static final int DECREASE = 51;//--
//符号
	public static final int OR_1 = 52;// |
	public static final int OR_2 = 53;//  ||
	public static final int AND_1 = 54;//  &
	public static final int AND_2 = 55;//   &&
	public static final int NOT = 56;//  !
	public static final int COMMA = 57;//,
	public static final int SEMICOLON = 58;//;
	public static final int BRACE_L = 59;// {
	public static final int BRACE_R = 60;//  }
	public static final int BRACKET_L = 61;// [
	public static final int BRACKET_R = 62;// ]
	public static final int PARENTHESIS_L = 63;// (
	public static final int PARENTHESIS_R = 64;// )
	public static final int INCLUDE = 65;
	/**
	 * 判断是否为计算型的符号
	 * @param type
	 * @return
	 */
	public static boolean isCalc(int type){
		if(type == Type.ASSIGN || type == Type.ADD || type == Type.SUB || type == Type.DIV ||
				type == Type.MUL ||type == Type.LT || type == Type.GT || type == Type.OR_1 ||
				type == Type.AND_1 || type == Type.NOT){
			return true;
		} else {
			return false;
		}
	}
}
