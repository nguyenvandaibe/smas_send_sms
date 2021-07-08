/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: doanhcdm $
 */
package vn.com.viettel.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class StringUtil.
 */
public final class StringUtil {

    /**
     * The Constant EMPTY.
     */
    public static final String EMPTY = "";

    /**
     * The Constant REPLACE_CHARS.
     */
    private static final String REPLACE_CHARS = "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêế�?ểễệiíìỉĩịuúùủũụưứừửữựoóò�?õ�?ôốồổỗộơớ�?ởỡợyýỳỷỹỵ";

    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern
            .compile("[,.?;:\'\"`~!@#$%&*()^<>{}\\[\\]\\\\/ ]");

    private static final Pattern PATTERN_NONE_WORD = Pattern
            .compile("([^a-zA-Z0-9 ])");

    /**
     * The codau.
     */
    static String unicode[] = {"à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ",
        "ẩ", "ẫ", "ă", "ằ", "ắ", "ặ", "ẳ", "ẵ", "è", "é", "ẹ", "ẻ", "ẽ",
        "ê", "ề", "ế", "ệ", "ể", "ễ", "ì", "í", "ị", "ỉ", "ĩ", "ò", "ó",
        "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ",
        "ở", "ỡ", "ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ", "ứ", "ự", "ử", "ữ",
        "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ", "À", "Á", "Ạ", "Ả", "Ã", "Â", "Ầ",
        "Ấ", "Ậ", "Ẩ", "Ẫ", "Ă", "Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ", "È", "É", "Ẹ",
        "Ẻ", "Ẽ", "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ", "Ì", "Í", "Ị", "Ỉ", "Ĩ",
        "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ơ", "Ờ",
        "Ớ", "Ợ", "Ở", "Ỡ", "Ù", "Ú", "Ụ", "Ủ", "Ũ", "Ư", "Ừ", "Ứ", "Ự",
        "Ử", "Ữ", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "Đ", "ê", "ù", "à"};

    /**
     * The codau.
     */
    static String MAX_CO_DAU[] = {"à", "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ",
        "ẩ", "ẫ", "ă", "ằ", "ắ", "ặ", "ẳ", "ẵ", "è", "é", "ẹ", "ẻ", "ẽ",
        "ê", "ề", "ế", "ệ", "ể", "ễ", "ì", "í", "ị", "ỉ", "ĩ", "ò", "ó",
        "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ", "ờ", "ớ", "ợ",
        "ở", "ỡ", "ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ", "ứ", "ự", "ử", "ữ",
        "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ", "À", "Á", "Ạ", "Ả", "Ã", "Â", "Ầ",
        "Ấ", "Ậ", "Ẩ", "Ẫ", "Ă", "Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ", "È", "É", "Ẹ",
        "Ẻ", "Ẽ", "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ", "Ì", "Í", "Ị", "Ỉ", "Ĩ",
        "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ", "Ơ", "Ờ",
        "Ớ", "Ợ", "Ở", "Ỡ", "Ù", "Ú", "Ụ", "Ủ", "Ũ", "Ư", "Ừ", "Ứ", "Ự",
        "Ử", "Ữ", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "Đ", "ê", "ù", "à",
        "á", "ạ", "ả", "ã", "â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă",
        "ằ", "ắ", "ặ", "ẳ", "ẵ", "è", "é", "ẹ", "ẻ", "ẽ", "ê",
        "ề", "ế", "ệ", "ể", "ễ", "ì", "í", "ị", "ỉ", "ĩ", "ò",
        "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ", "ơ",
        "ờ", "ớ", "ợ", "ở", "ỡ", "ù", "ú", "ụ", "ủ", "ũ", "ư",
        "ừ", "ứ", "ự", "ử", "ữ", "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ",
        "À", "Á", "Ạ", "Ả", "Ã", "Â", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ",
        "Ă", "Ằ", "Ắ", "Ặ", "Ẳ", "Ẵ", "È", "É", "Ẹ", "Ẻ", "Ẽ",
        "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ", "Ì", "Í", "Ị", "Ỉ", "Ĩ",
        "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ", "Ổ", "Ỗ",
        "Ơ", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ", "Ù", "Ú", "Ụ", "Ủ", "Ũ",
        "Ư", "Ừ", "Ứ", "Ự", "Ử", "Ữ", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ",
        "Đ"};

    /**
     * The khongdau.
     */
    static String english[] = {"a", "a", "a", "a", "a", "a", "a", "a", "a",
        "a", "a", "a", "a", "a", "a", "a", "a", "e", "e", "e", "e", "e",
        "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o", "o",
        "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
        "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u",
        "y", "y", "y", "y", "y", "d", "A", "A", "A", "A", "A", "A", "A",
        "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "E", "E", "E",
        "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I", "I",
        "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O",
        "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U", "U",
        "U", "U", "Y", "Y", "Y", "Y", "Y", "D", "e", "u", "a"};

    /**
     * The khongdau.
     */
    static String MAX_KHONG_DAU[] = {"a", "a", "a", "a", "a", "a", "a", "a",
        "a", "a", "a", "a", "a", "a", "a", "a", "a", "e", "e", "e", "e",
        "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o",
        "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
        "o", "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u",
        "u", "y", "y", "y", "y", "y", "d", "A", "A", "A", "A", "A", "A",
        "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "E", "E",
        "E", "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I",
        "I", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O",
        "O", "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U",
        "U", "U", "U", "Y", "Y", "Y", "Y", "Y", "D", "e", "u", "a",
        "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
        "a", "a", "a", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
        "e", "i", "i", "i", "i", "i", "o", "o", "o", "o", "o", "o", "o",
        "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u",
        "u", "u", "u", "u", "u", "u", "u", "u", "y", "y", "y", "y", "y",
        "d", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
        "A", "A", "A", "A", "A", "E", "E", "E", "E", "E", "E", "E", "E",
        "E", "E", "E", "I", "I", "I", "I", "I", "O", "O", "O", "O", "O",
        "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "U",
        "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "Y", "Y", "Y",
        "Y", "Y", "D"};

    /**
     * The codau tokhongdau.
     */
    static HashMap<String, String> unicodetoEnglishLanguageHashMap = null;

    /**
     * Inits the.
     */
    public static void init() {
        unicodetoEnglishLanguageHashMap = new HashMap<>();
        for (int i = 0; i < unicode.length; i++) {
            unicodetoEnglishLanguageHashMap.put(MAX_CO_DAU[i], MAX_KHONG_DAU[i]);

        }

    }

    /**
     * Codau2khongdau.
     *
     * @param input the input
     * @return the string
     */
    public static String replaceUnicodetoEnglishLanguage(String input) {
        if (unicodetoEnglishLanguageHashMap == null) {
            init();
        }
        input = input.trim();

        for (int i = 0; i < unicode.length; i++) {
            input = input.replace(unicode[i], english[i]);
        }
        return input;
    }

    /**
     * Format text.
     *
     * @param data the data
     *
     * @return the string
     */
    public static String formatText(String data) {
        // String str =
        // "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêế�?ểễệiíìỉĩịuúùủũụưứừửữựoóò�?õ�?ôốồổỗộơớ�?ởỡợyýỳỷỹỵ";

        for (int i = 1; i < 6; i++) {
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 0),
                    REPLACE_CHARS.charAt(0));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 1),
                    REPLACE_CHARS.charAt(6));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 2),
                    REPLACE_CHARS.charAt(12));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 3),
                    REPLACE_CHARS.charAt(18));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 4),
                    REPLACE_CHARS.charAt(24));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 5),
                    REPLACE_CHARS.charAt(30));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 6),
                    REPLACE_CHARS.charAt(36));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 7),
                    REPLACE_CHARS.charAt(42));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 8),
                    REPLACE_CHARS.charAt(48));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 9),
                    REPLACE_CHARS.charAt(54));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 10),
                    REPLACE_CHARS.charAt(60));
            data = data.replace(REPLACE_CHARS.charAt(i + 6 * 11),
                    REPLACE_CHARS.charAt(66));
        }

        return data;
    }

    /**
     * Checks if is null or empty.
     *
     * @param s the s
     *
     * @return true, if is null or empty
     */
    public static boolean isNullOrEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * Remove special charactor from string
     *
     * @param text
     * @param byString
     * @return
     */
    public static String removeSpecialChars(String text, String byString) {
        final String[] chars = new String[]{",", ".", "/", "!", "@", "#",
            "$", "%", "^", "&", "*", "'", "\"", ";", "-", "_", "(", ")",
            ":", "|", "[", "]", "~", "+", "{", "}", "?", "\\", "<", ">"};
        if (StringUtil.isNullOrEmpty(text)) {
            return text;
        }

        for (String char1 : chars) {
            if (text.contains(char1)) {
                text = text.replace(char1, byString);
            }
        }
        return text;
    }

    public static String replaceSpecialChar(final String strSource) {
        if (isNullOrEmpty(strSource)) {
            return "";
        }
        return SPECIAL_CHAR_PATTERN.matcher(strSource).replaceAll("");
    }

    /**
     * Escape string ('\n' -> '\\n').
     *
     * @param source the source
     * @return the string
     */
    public String escape(String source) {
        return null;
    }

    public static String escapeSpecialCharacter(String source) {
        return SPECIAL_CHAR_PATTERN.matcher(source).replaceAll("\\\\$1");
    }

    public static String escapeNonWordCharacter(String source) {
        return PATTERN_NONE_WORD.matcher(source).replaceAll("\\\\$1");
    }

    public static String removeNewlineChar(String content) {
        return content.replaceAll("\\r\\n|\\r|\\n", " ");
    }

    public static String removeSpecialChar(String content) {
        content = content.replaceAll(Character.toString((char) 12), ""); // FORM
        // FEED
        content = content.replaceAll("[\\^|\\{|\\}|\\]|\\[|\\~|€|–|—]", "");
        content = content.replaceAll("\\\\", "");// \
        content = content.replaceAll(Character.toString((char) 124), "");// |

        content = replaceUnicodetoEnglishLanguage(content);

        return content;
    }

    /**
     * Tao cau truc gui lai ma loi
     * @param mobile: So dien thoai gui sms bi loi
     * @param errorMsg: Ma loi
     * @return 
     */
    public static String createMassage(String mobile, String errorMsg) {
        //String msg = AppId + "_Ip: " + LogUtil.getIp() + "_Noi dung loi: " + errorMsg;
        String msg = "Loi gui tin nhan: So dien thoai: " + mobile + ", Noi dung: " + errorMsg;
        return msg;
    }

}
