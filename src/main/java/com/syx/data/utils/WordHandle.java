package com.syx.data.utils;

import com.hankcs.hanlp.HanLP;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.StringHolder;

import java.util.*;

/**
 * @author Msater Zg
 *         ansj文本处理类（提取关键词）
 *         hanlp关键词提取（两种不同的实现方式）
 */
public class WordHandle {
    /**
     * 定义关键词的个数
     */
    private static final int WORD_IMPORTANT_NUMBER = 25;

    /**
     * 根据标题和内容提取关键词
     *
     * @param title
     * @param content
     */
    private List wordImportantExtractAnsj(String title, String content) {
        KeyWordComputer keyWordComputer = new KeyWordComputer(WORD_IMPORTANT_NUMBER);
        Collection<Keyword> result = keyWordComputer.computeArticleTfidf(title, content);
        Iterator<Keyword> keywordIterator = result.iterator();
        List list = new ArrayList();
        while (keywordIterator.hasNext()) {
            Keyword keyword = keywordIterator.next();
            list.add(keyword.getName());
        }
        return list;
    }

    /**
     * 用hanpl分词技术完成分词
     *
     * @param title
     * @param content
     * @return
     */
    private List<String> wordImportantExtractHanpl(String title, String content) {
        List<String> keywordList = HanLP.extractKeyword(title + content, WORD_IMPORTANT_NUMBER);
        return keywordList;
    }

    private List getEqualWordList(List<String> firstList, List<String> secondList) {
        List equalWordList = new ArrayList();
        for (int i = 0, firstLen = firstList.size(); i < firstLen; i++) {
            String wordName = firstList.get(i);
            for (int j = 0, secondLen = secondList.size(); j < secondLen; j++) {
                String secondWordName = secondList.get(j);
                if (wordName.equals(secondWordName)) {
                    equalWordList.add(wordName);
                }
            }
        }
        return equalWordList;
    }


    private List<Double> getSpaceVector(List<String> wordName, String content) {
        // 返回空间向量
        List<Double> vector = new ArrayList<>();
        Result resultNpl = ToAnalysis.parse(content);
        int number = resultNpl.size();
        for (int i = 0, wordNameLen = wordName.size(); i < wordNameLen; i++) {
            String wordNameItem = wordName.get(i);
            int count = StringUtils.countMatches(content, wordNameItem);
            vector.add(Double.valueOf(count) / number);
        }
        return vector;
    }

    private List<String> mergeList(List<String> firstList, List<String> secondList) {
        List<String> returnList = new ArrayList<>();
        int firstListLen = firstList.size();
        int secondListLen = secondList.size();
        if (firstListLen > secondListLen) {
            returnList.addAll(secondList);
            returnList.addAll(firstList.subList(0, secondListLen));
        } else {
            returnList.addAll(firstList);
            returnList.addAll(secondList.subList(0, secondListLen));
        }
        return returnList;
    }

    /**
     * 两个文本关键词的提取，计算if-idf的值，运用相对词频
     *
     * @param contentOneMap
     * @param contentTwoMap
     */
    public boolean getWordFrequency(Map<String, String> contentOneMap, Map<String, String> contentTwoMap) {
        boolean flag = false;
        String titleFirst = contentOneMap.get("title");
        String contentFirst = contentOneMap.get("content");
        String titleSecond = contentTwoMap.get("title");
        String contentSecond = contentTwoMap.get("content");
        List ansjFirst = wordImportantExtractAnsj(titleFirst, contentFirst);
        List ansjSecond = wordImportantExtractAnsj(titleSecond, contentSecond);
        List hanplFirst = wordImportantExtractHanpl(titleFirst, contentFirst);
        List hanplSecond = wordImportantExtractHanpl(titleSecond, contentSecond);
        List<String> wordEqualFirst = getEqualWordList(ansjFirst, hanplFirst);
        List<String> wordEqualSecond = getEqualWordList(ansjSecond, hanplSecond);
        List<String> allImportantWord = mergeList(wordEqualFirst, wordEqualSecond);
        // 两个空间向量
        List<Double> firstDouble = getSpaceVector(allImportantWord, contentFirst);
        List<Double> secondDouble = getSpaceVector(allImportantWord, contentSecond);
        VsmCalculate vsmCalculate = new VsmCalculate();
        flag = vsmCalculate.calculateSpaceSemblance(firstDouble, secondDouble);
        return flag;
    }
}
