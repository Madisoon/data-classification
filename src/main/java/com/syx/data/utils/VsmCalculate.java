package com.syx.data.utils;

import java.util.List;

/**
 * @author Msater Zg
 *         VSM算法类
 */
public class VsmCalculate {

    private static final double SIMILARITY_BOUNDS = 0.85;

    /**
     * 计算向量空间模型
     *
     * @param listOne
     * @param listTwo
     * @return boolean
     */
    public boolean calculateSpaceSemblance(List<Double> listOne, List<Double> listTwo) {
        // 分子
        double sum = 0;
        // 分母
        double sumOne = 0;
        double sumTwo = 0;
        for (int i = 0, listOneLen = listOne.size(); i < listOneLen; i++) {
            double listOneValue = listOne.get(i);
            double listTwoValue = listTwo.get(i);
            sum += listOneValue * listTwoValue;
            sumOne += listOneValue * listOneValue;
            sumTwo += listTwoValue * listTwoValue;
        }
        double result = sum / (Math.sqrt(sumOne) * Math.sqrt(sumTwo));
        return similarityJudgment(result);
    }

    public boolean similarityJudgment(double result) {
        System.out.println(result);
        boolean flag = false;
        if (result > SIMILARITY_BOUNDS) {
            flag = true;
        }
        return flag;
    }
}
