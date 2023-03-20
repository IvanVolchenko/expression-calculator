package com.epam.rd.servlets.expressioncalc.MathExpressions;

import java.util.ArrayList;
import java.util.Map;

public interface Expression {

    String preparation(String [] strings, String expression);

    String mainAction(Map <Character,String> table, String expression);

    void checkError(Map<Character,String> table , String expression2);

    void bracketAction (StringBuffer string);

    void calculation(StringBuffer string, int a, int b);

    int sum(ArrayList<String> expression, int i);

    int sub(ArrayList<String> expression, int i);

    int multi(ArrayList <String> expression, int i);

    int division (ArrayList <String> expression, int i);


}
