package com.qx.framelib.view.inputFilter;

/**
 * Created by ZhaoWei on 2016/11/9.
 */

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import com.qx.framelib.utlis.ToastUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 过滤Emoji表情
 * Created by tutu on 2016/5/31.
 */
public class EmojiFilter implements InputFilter {

    private static Set<String> filterSet = null;
    private static Set<Scope> scopeSet = null;

    /**
     * 区间类模型
     */
    private class Scope {
        int start;
        int end;

        @Override
        public boolean equals(Object o) {
            if (o instanceof Scope) {
                Scope scope = (Scope) o;
                if (scope.start == start && scope.end == end) {
                    return true;
                }
            }
            return super.equals(o);
        }
    }

    private static void addUnicodeRangeToSet(Set<String> set, int start, int end) {
        if (set == null) {
            return;
        }
        if (start > end) {
            return;
        }


        for (int i = start; i <= end; i++) {
            filterSet.add(new String(new int[]{i}, 0, 1));
        }
    }

    private static void addUnicodeRangeToSet(Set<String> set, int code) {
        if (set == null) {
            return;
        }
        filterSet.add(new String(new int[]{code}, 0, 1));
    }

    static {
        filterSet = new HashSet<String>();
        scopeSet = new HashSet<>();

        // See http://apps.timwhitlock.info/emoji/tables/unicode

        // 1. Emoticons ( 1F601 - 1F64F )
        addUnicodeRangeToSet(filterSet, 0x1F601, 0X1F64F);

        // 2. Dingbats ( 2702 - 27B0 )
        addUnicodeRangeToSet(filterSet, 0x2702, 0X27B0);

        // 3. Transport and map symbols ( 1F680 - 1F6C0 )
        addUnicodeRangeToSet(filterSet, 0X1F680, 0X1F6C0);

        // 4. Enclosed characters ( 24C2 - 1F251 )
        addUnicodeRangeToSet(filterSet, 0X24C2);
        addUnicodeRangeToSet(filterSet, 0X1F170, 0X1F251);

        // 6a. Additional emoticons ( 1F600 - 1F636 )
        addUnicodeRangeToSet(filterSet, 0X1F600, 0X1F636);

        // 6b. Additional transport and map symbols ( 1F681 - 1F6C5 )
        addUnicodeRangeToSet(filterSet, 0X1F681, 0X1F6C5);

        // 6c. Other additional symbols ( 1F30D - 1F567 )
        addUnicodeRangeToSet(filterSet, 0X1F30D, 0X1F567);

        // 5. Uncategorized
        addUnicodeRangeToSet(filterSet, 0X1F004);
        addUnicodeRangeToSet(filterSet, 0X1F0CF);
        // 与6c. Other additional symbols ( 1F30D - 1F567 )重复
        // 去掉重复部分虽然不去掉HashSet也不会重复，原范围（0X1F300 - 0X1F5FF）
        addUnicodeRangeToSet(filterSet, 0X1F300, 0X1F30D);
        addUnicodeRangeToSet(filterSet, 0X1F5FB, 0X1F5FF);
        addUnicodeRangeToSet(filterSet, 0X00A9);
        addUnicodeRangeToSet(filterSet, 0X00AE);
        addUnicodeRangeToSet(filterSet, 0X0023);
        //阿拉伯数字0-9，配合0X20E3使用
        //addUnicodeRangeToSet(filterSet, 0X0030, 0X0039);
        // 过滤掉203C开始后的2XXX 段落
        //addUnicodeRangeToSet(filterSet, 0X203C, 0X24C2);
        addUnicodeRangeToSet(filterSet, 0X203C);
        addUnicodeRangeToSet(filterSet, 0X2049);
        //严格验证的话需要判断前面是否是数字
        //Android上显示和数字分开可以不判断
        addUnicodeRangeToSet(filterSet, 0X20E3);
        addUnicodeRangeToSet(filterSet, 0X2122);
        addUnicodeRangeToSet(filterSet, 0X2139);
        addUnicodeRangeToSet(filterSet, 0X2194, 0X2199);
        addUnicodeRangeToSet(filterSet, 0X21A9, 0X21AA);
        addUnicodeRangeToSet(filterSet, 0X231A, 0X231B);
        addUnicodeRangeToSet(filterSet, 0X23E9, 0X23EC);
        addUnicodeRangeToSet(filterSet, 0X23F0);
        addUnicodeRangeToSet(filterSet, 0X23F3);
        addUnicodeRangeToSet(filterSet, 0X25AA, 0X25AB);
        addUnicodeRangeToSet(filterSet, 0X25FB, 0X25FE);
        //TODO： 26XX 太杂全部过滤
        addUnicodeRangeToSet(filterSet, 0X2600, 0X26FE);
        addUnicodeRangeToSet(filterSet, 0X2934, 0X2935);
        addUnicodeRangeToSet(filterSet, 0X2B05, 0X2B07);
        addUnicodeRangeToSet(filterSet, 0X2B1B, 0X2B1C);
        addUnicodeRangeToSet(filterSet, 0X2B50);
        addUnicodeRangeToSet(filterSet, 0X2B55);
        addUnicodeRangeToSet(filterSet, 0X3030);
        addUnicodeRangeToSet(filterSet, 0X303D);
        addUnicodeRangeToSet(filterSet, 0X3297);
        addUnicodeRangeToSet(filterSet, 0X3299);
    }

    public EmojiFilter() {
        super();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {

        int len = source.length();
        // check black-list set
        for (int i = 0; i < len; i++) {
            Log.e("filter", Integer.toHexString(source.charAt(i)));
        }
        Log.e("filter",source.toString() + " length： " + source.toString().length() +
                " ；bytes length： " + source.toString().getBytes().length);
//        Iterator<String> iterator = filterSet.iterator();
//        while (iterator.hasNext()) {
//            String filter = iterator.next();
//            if (filter.equals(source.toString())) {
//                LogUtil.e(filter + " length： " + filter.length() +
//                        " ；bytes length： " + filter.getBytes().length);
//                for (int i= 0; i < source.length(); i++){
//                    LogUtil.e(Integer.toHexString(source.charAt(i)));
//                }
//                return "";
//            }
//        }
        if (filterSet.contains(source.toString())) {
            ToastUtils.getInstance().show("不支持Emoji表情");
            return "";
        }

        if(len>=2){
            if (containsEmoji(source.toString())) {
                ToastUtils.getInstance().show("不支持Emoji表情");
//               resetText = true;
//                            Toast.makeText(mContext, "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                //是表情符号就将文本还原为输入表情符号之前的内容
//               setText(inputAfterText);
//               CharSequence text = getText();
//               if (text instanceof Spannable) {
//                   Spannable spanText = (Spannable) text;
//                   Selection.setSelection(spanText, text.length());
//               }  ToastUtils.getInstance().show("不支持Emoji表情");
                return "";
            }
        }

        return source;
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

}