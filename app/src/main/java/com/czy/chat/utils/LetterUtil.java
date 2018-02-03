package com.czy.chat.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import static net.sourceforge.pinyin4j.format.HanyuPinyinCaseType.UPPERCASE;
import static net.sourceforge.pinyin4j.format.HanyuPinyinToneType.WITHOUT_TONE;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:56
 * 说明：汉字转拼音
 */
public class LetterUtil {

    /**
     * 如果字符串的首字符为汉字，则返回该汉字的拼音大写首字母
     * 如果字符串的首字符为字母，也转化为大写字母返回
     * 其他情况均返回'#'
     *
     * @param name 字符串
     * @return 首字母
     */
    public static char getHeaderLetter(String name) {
        if (name != null && name.trim().length() != 0) {
            char[] letterArray = name.trim().toCharArray();
            char headChar = letterArray[0];
            //如果是大写字母则直接返回
            if (Character.isUpperCase(headChar)) {
                return headChar;
            } else if (Character.isLowerCase(headChar)) {
                return Character.toUpperCase(headChar);
            }
            if (String.valueOf(headChar).matches("[\\u4E00-\\u9FA5]+")) {
                HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
                hanYuPinOutputFormat.setCaseType(UPPERCASE);
                hanYuPinOutputFormat.setToneType(WITHOUT_TONE);
                try {
                    String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(headChar, hanYuPinOutputFormat);
                    if (stringArray != null && stringArray[0] != null) {
                        return stringArray[0].charAt(0);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    return '#';
                }
            }
        }
        return '#';
    }

    /**
     * 如果传入的字符为字母，则返回其大写字母形式
     * 如果是汉字，则返回其拼音的首字母大写形式
     * 否则返回 “#”
     *
     * @param letter Letter
     * @return Letter
     */
    static char getHeaderLetter(char letter) {
        if (Character.isUpperCase(letter)) {
            return letter;
        } else if (Character.isLowerCase(letter)) {
            return Character.toUpperCase(letter);
        }
        if (String.valueOf(letter).matches("[\\u4E00-\\u9FA5]+")) {
            HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
            hanYuPinOutputFormat.setCaseType(UPPERCASE);
            hanYuPinOutputFormat.setToneType(WITHOUT_TONE);
            try {
                String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(letter, hanYuPinOutputFormat);
                if (stringArray != null && stringArray[0] != null) {
                    return stringArray[0].charAt(0);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                return '#';
            }
        }
        return '#';
    }

}