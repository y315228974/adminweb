package com.lz.adminweb.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 计算工具类
 * @author chenpinjia
 * @date 2020/5/13
 */
public class ArithUtils {

    /**
     *  默认除法运算精度
     */
    private static final int DEF_SCALE = 10;

    private ArithUtils() {

    }

    /**
     * 精确计算 加法
     * @param v1
     * @param v2
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:07
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 精确计算 加法
     * @param vs 多个参与计算的值
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:11
     */
    public static double add(double... vs) {
        BigDecimal result = BigDecimal.ZERO;
        for (Double v : vs) {
            BigDecimal b2 = new BigDecimal(String.valueOf(v));
            result = result.add(b2);
        }
        return result.doubleValue();
    }


    /**
     * 精确计算 减法
     * @param v1 被减数
     * @param v2 减数
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:15
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确计算 乘法
     * @param v1
     * @param v2
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:17
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 精确计算 除法
     * 默认四舍五入保留两位小数
     * @param v1 被除数
     * @param v2 除数
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:22
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确计算 除法
     * @param v1 被除数
     * @param v2 除数
     * @param scale 保留位数
     * @param rule 进似值规则（BigDecimal）
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:28
     */
    public static double div(double v1, double v2, int scale, int rule) {
        BigDecimal b1 = new BigDecimal(String.valueOf(v1));
        BigDecimal b2 = new BigDecimal(String.valueOf(v2));
        return b1.divide(b2, scale, rule).doubleValue();
    }

    /**
     * 四舍五入
     * @param v 被处理数
     * @param scale 保留位数
     * @return double
     * @author chenpinjia
     * @date 2020/5/13 17:30
     */
    public static double round(double v, int scale) {
        BigDecimal b = new BigDecimal(String.valueOf(v));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 运算符计算
     * @param a1
     * @param a2
     * @param operator
     * @return java.math.BigDecimal
     * @author chenpinjia
     * @date 2020/6/17 10:45
     */
    private static BigDecimal operatorCal(BigDecimal a1, BigDecimal a2, char operator)  {
        switch (operator) {
            case '+':
                return a1.add(a2);
            case '-':
                return a1.subtract(a2);
            case '*':
                return a1.multiply(a2);
            case '/':
                return a1.divide(a2,20,BigDecimal.ROUND_HALF_UP);
            default:
                throw new IllegalArgumentException("illegal operator!");
        }

    }

    /**
     * 字符优先级确定
     * @param s
     * @return int
     * @author chenpinjia
     * @date 2020/6/17 11:03
     */
    private static int getPriority(String s) {
        if(s==null) {
            return 0;
        } else if(s.equals("(")) {
            return 1;
        } else if(s.equals("+") || s.equals("-")) {
            return 2;
        } else if(s.equals("*") || s.equals("/")) {
            return 3;
        }else {
            throw new IllegalArgumentException("illegal operator!");
        }
    }
    /**
     * 公式计算
     * @param formula 公式
     * @param valueMap 值map
     * @return java.math.BigDecimal
     * @author chenpinjia
     * @date 2020/6/17 10:45
     */
    public static BigDecimal formulaCal(String formula, Map<String, ?> valueMap) throws Exception{
        Stack<BigDecimal> number = new Stack<>();
        Stack<String> operator = new Stack<>();
        operator.push(null);


        final Pattern p = Pattern.compile("[^+\\-*/()]+|[+\\-*/()]");
        Matcher m = p.matcher(formula);
        //公式开始是否为减号号（第一个）
        boolean isBeginWithSubOperatorFlag = false;
        //是否为第一次循环
        boolean isFirst = true;
        while (m.find()){
            String temp = m.group();
            //以减号为开头，即下个数值为负数
            if (isFirst && temp.equals("-")){
                isBeginWithSubOperatorFlag = true;
                isFirst = false;
                continue;
            }
            isFirst = false;


            if(!temp.matches("[+\\-*/()]")) {
                BigDecimal val = null;
                //遇到变量或常量压入数字栈
                if(temp.matches("(?<!\\d)-?\\d+(\\.\\d+)?")) {
                    val = new BigDecimal(temp);
                }else{
                    String valStr = String.valueOf(ObjectUtils.defaultIfNull(valueMap.get(temp), "0"));
                    val = new BigDecimal(valStr);
                }
                //减号开头取反数
                if (isBeginWithSubOperatorFlag){
                    val = val.negate();
                    isBeginWithSubOperatorFlag = false;
                }
                number.push(val);
                continue;
            }


            //遇到符号
            if(temp.equals("(")) {
                //遇到左括号，直接入符号栈
                operator.push(temp);
                continue;
            }
            if(temp.equals(")")){
                //遇到右括号，"符号栈弹栈取栈顶符号b，数字栈弹栈取栈顶数字a1，数字栈弹栈取栈顶数字a2，计算a2 b a1 ,将结果压入数字栈"，重复引号步骤至取栈顶为左括号，将左括号弹出
                String b = null;
                while(!(b = operator.pop()).equals("(")) {
                    BigDecimal a1 = number.pop();
                    BigDecimal a2 = number.pop();
                    number.push(operatorCal(a2, a1, b.charAt(0)));
                }
                continue;
            }
            //遇到运算符，满足该运算符的优先级大于栈顶元素的优先级压栈；否则计算后压栈
            while(getPriority(temp) <= getPriority(operator.peek())) {
                BigDecimal a1 = number.pop();
                BigDecimal a2 = number.pop();
                String b = operator.pop();
                number.push(operatorCal(a2, a1, b.charAt(0)));
            }
            operator.push(temp);
        }

        while(operator.peek()!=null) {//遍历结束后，符号栈数字栈依次弹栈计算，并将结果压入数字栈
            BigDecimal a1 = number.pop();
            BigDecimal a2 = number.pop();
            String b = operator.pop();
            number.push(operatorCal(a2, a1, b.charAt(0)));
        }
        //科学计数，保留2位小数
        return number.pop().setScale(2,BigDecimal.ROUND_HALF_UP);

    }
    
}
