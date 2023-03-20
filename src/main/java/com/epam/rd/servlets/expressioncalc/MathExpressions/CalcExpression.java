package com.epam.rd.servlets.expressioncalc.MathExpressions;


import java.util.*;

public class CalcExpression implements Expression{

    /**
     * Prepares data for calculations and creates
     * a correct parameter map
     */
    @Override
    public String preparation(String[] strings, String expression) {
        Map <Character,String> table = new HashMap<>();
        for (int i=1; i<strings.length;i++) {
            Character character = strings[i].charAt(0);
            String str = strings[i].substring(2);
            table.put(character,str);
        }
        for (Map.Entry<Character, String> entry : table.entrySet()) {
            Character character = entry.getValue().charAt(0);
            if (entry.getValue().length()==1 && !Character.isDigit(entry.getValue().charAt(0))) {
                entry.setValue(table.get(character));
            }
        }

        return mainAction(table,expression);
    }

    /**
     * Submits the expression for validation
     * and inserts data instead of variables.
     * Submits expression for the brackets checking
     */
    @Override
    public String mainAction(Map <Character,String> table,String expression) {
       String exprWithoutSpace = expression.replaceAll("\\s", "");
       checkError(table,exprWithoutSpace);
       ArrayList <String> proba = new ArrayList<>();
       String newWithSpace = exprWithoutSpace.replaceAll("[+]"," + ")
               .replaceAll("[-]"," - ").replaceAll("[*]"," * ")
                       .replaceAll("[/]"," / ");
       proba.add(newWithSpace);

        for (Map.Entry<Character, String> entry : table.entrySet()) {
            proba.set(0,proba.get(0).replaceAll(String.valueOf(entry.getKey()),entry.getValue()));

        }
        StringBuffer myResult = new StringBuffer(proba.get(0));
        bracketAction(myResult);

        return myResult.toString();
    }

    /**
     * Checks the expression for validation.
     */
    @Override
    public void checkError(Map <Character,String> table, String expression2) {
        ArrayList <Character> symbolsMath = new ArrayList<>();
        symbolsMath.add('+');
        symbolsMath.add('-');
        symbolsMath.add('=');
        symbolsMath.add('*');
        symbolsMath.add('/');

        int countFirst = 0;
        int countSecond = 0;
        for (int i=0; i<expression2.length()-1;i++){
            if (expression2.charAt(i)=='(') {
                if (expression2.charAt(i+1)==')') throw new NumberFormatException("expression isn't correct");
                countFirst++;
            }
            if (expression2.charAt(i)==')') {
                if (expression2.charAt(i+1)=='(') throw new NumberFormatException("expression isn't correct");
                countSecond++;
            }
            if (expression2.charAt(i)=='/'){
                if (expression2.charAt(i+1)=='0') throw new NumberFormatException("expression isn't correct");
                if (table.containsKey(expression2.charAt(i+1)) && Objects.equals(table.get(expression2.charAt(i + 1)), "0")) // ? нужно ли
                    throw new NumberFormatException("expression isn't correct");
            }
            if (symbolsMath.contains(expression2.charAt(i)) && expression2.charAt(i+1)==')')
                throw new NumberFormatException("expression isn't correct");

            if (symbolsMath.contains(expression2.charAt(i)) && symbolsMath.contains(expression2.charAt(i+1)))
                throw new NumberFormatException("expression isn't correct");
        }
        if (expression2.charAt(expression2.length()-1)==')') countSecond++;
        if (countSecond != countFirst) throw new NumberFormatException("expression isn't correct");

        int positionFirst= expression2.lastIndexOf('(');
        int positionSecond =expression2.lastIndexOf(')');

        if (positionFirst>positionSecond) throw new NumberFormatException("expression isn't correct");
    }

    /**
     * Checks the expression for the brackets.
     * Submits expression for calculation.
     */
    @Override
    public void bracketAction(StringBuffer string) {
        int firstPosition = 0; // of bracket
        int secondPosition = 0; // of bracket
        boolean test = true;
        while (test){
            for (int i=0; i<string.length(); i++) {
                if (string.charAt(i)==')') secondPosition=i;
                if (string.charAt(i)=='(') firstPosition = i;

                if (secondPosition!=0){
                    calculation(string,firstPosition,secondPosition);
                    secondPosition=0;
                    firstPosition=0;
                    i=0;
                    break;
                }
                if (i==string.length()-1) test=false;
            }
        }
        calculation(string,0,string.length());
    }

    /**
     * Makes Calculation
     */
    @Override
    public void calculation(StringBuffer string, int firstPosition, int secondPosition) {

        String rrr = string.toString().substring(firstPosition,secondPosition).replaceAll("[(]","")
                .replaceAll("[)]","");
        ArrayList <String> expression = new ArrayList<>(List.of(rrr.split("\\s")));

        int partOfChanges = 0;
        boolean go = true;
        while (go){
           if (expression.size()==1) go=false;
            int sizeOfExpression = expression.size()-1;
            for (int i=1; i<sizeOfExpression; i++) {
                if (Objects.equals(expression.get(i), "*")){
                    partOfChanges += multi(expression,i);
                    sizeOfExpression = expression.size()-1;
                    break;
                }
                if (Objects.equals(expression.get(i), "/")){
                    partOfChanges += division(expression,i);
                    sizeOfExpression = expression.size()-1;
                    break;
                }
            }
            for (int i=1; i<sizeOfExpression; i++) {
                if (Objects.equals(expression.get(i), "+")){
                    partOfChanges += sum(expression,i);
                    sizeOfExpression = expression.size()-1;
                    break;
                }
                if (Objects.equals(expression.get(i), "-")){
                    partOfChanges += sub(expression,i);
                    sizeOfExpression = expression.size()-1;
                    break;
                }
            }
        }
        string.replace(firstPosition,secondPosition+1,expression.get(0));
    }

    @Override
    public int sum(ArrayList <String> expression, int i) {
        int result = Integer.parseInt(expression.get(i - 1)) + Integer.parseInt(expression.get(i + 1));
        expression.set(i, String.valueOf(Integer.parseInt(expression.get(i - 1)) + Integer.parseInt(expression.get(i + 1))));
        expression.remove(i+1);
        expression.remove(i-1);
        return result;
    }

    @Override
    public int sub(ArrayList<String> expression, int i) {
        int result = Integer.parseInt(expression.get(i - 1)) - Integer.parseInt(expression.get(i + 1));
        expression.set(i, String.valueOf(Integer.parseInt(expression.get(i - 1)) - Integer.parseInt(expression.get(i + 1))));
        expression.remove(i+1);
        expression.remove(i-1);
        return result;
    }


    @Override
    public int multi(ArrayList <String> expression, int i) {
        int result = Integer.parseInt(expression.get(i - 1)) * Integer.parseInt(expression.get(i + 1));
        expression.set(i, String.valueOf(Integer.parseInt(expression.get(i - 1)) * Integer.parseInt(expression.get(i + 1))));
        expression.remove(i+1);
        expression.remove(i-1);
        return result;
    }

    @Override
    public int division(ArrayList<String> expression, int i) {
        int result = Integer.parseInt(expression.get(i - 1)) / Integer.parseInt(expression.get(i + 1));
        expression.set(i, String.valueOf(Integer.parseInt(expression.get(i - 1)) / Integer.parseInt(expression.get(i + 1))));
        expression.remove(i+1);
        expression.remove(i-1);
        return result;
    }

}
