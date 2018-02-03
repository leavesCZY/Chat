package com.czy.ui.input.utils;

import android.support.v4.util.ArrayMap;

import com.czy.ui.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiUtils {

    public static final int EMOJI_TYPE_CLASSICS = 0x0001;

    public static final List<Integer> emojiTypeList = new ArrayList<>();

    public static ArrayMap<String, Integer> classicsEmoji;

    static {
        emojiTypeList.add(EMOJI_TYPE_CLASSICS);
        classicsEmoji = new ArrayMap<>();
        classicsEmoji.put("[emoji0]", R.drawable.smiley_0);
        classicsEmoji.put("[emoji1]", R.drawable.smiley_1);
        classicsEmoji.put("[emoji2]", R.drawable.smiley_2);
        classicsEmoji.put("[emoji3]", R.drawable.smiley_3);
        classicsEmoji.put("[emoji4]", R.drawable.smiley_4);
        classicsEmoji.put("[emoji5]", R.drawable.smiley_5);
        classicsEmoji.put("[emoji6]", R.drawable.smiley_6);
        classicsEmoji.put("[emoji7]", R.drawable.smiley_7);
        classicsEmoji.put("[emoji8]", R.drawable.smiley_8);
        classicsEmoji.put("[emoji9]", R.drawable.smiley_9);
        classicsEmoji.put("[emoji10]", R.drawable.smiley_10);
        classicsEmoji.put("[emoji11]", R.drawable.smiley_11);
        classicsEmoji.put("[emoji12]", R.drawable.smiley_12);
        classicsEmoji.put("[emoji13]", R.drawable.smiley_13);
        classicsEmoji.put("[emoji14]", R.drawable.smiley_14);
        classicsEmoji.put("[emoji15]", R.drawable.smiley_15);
        classicsEmoji.put("[emoji16]", R.drawable.smiley_16);
        classicsEmoji.put("[emoji17]", R.drawable.smiley_17);
        classicsEmoji.put("[emoji18]", R.drawable.smiley_18);
        classicsEmoji.put("[emoji19]", R.drawable.smiley_19);
        classicsEmoji.put("[emoji20]", R.drawable.smiley_20);
        classicsEmoji.put("[emoji21]", R.drawable.smiley_21);
        classicsEmoji.put("[emoji22]", R.drawable.smiley_22);
        classicsEmoji.put("[emoji23]", R.drawable.smiley_23);
        classicsEmoji.put("[emoji24]", R.drawable.smiley_24);
        classicsEmoji.put("[emoji25]", R.drawable.smiley_25);
        classicsEmoji.put("[emoji26]", R.drawable.smiley_26);
        classicsEmoji.put("[emoji27]", R.drawable.smiley_27);
        classicsEmoji.put("[emoji28]", R.drawable.smiley_28);
        classicsEmoji.put("[emoji29]", R.drawable.smiley_29);
        classicsEmoji.put("[emoji30]", R.drawable.smiley_30);
        classicsEmoji.put("[emoji31]", R.drawable.smiley_31);
        classicsEmoji.put("[emoji32]", R.drawable.smiley_32);
        classicsEmoji.put("[emoji33]", R.drawable.smiley_33);
        classicsEmoji.put("[emoji34]", R.drawable.smiley_34);
        classicsEmoji.put("[emoji35]", R.drawable.smiley_35);
        classicsEmoji.put("[emoji36]", R.drawable.smiley_36);
        classicsEmoji.put("[emoji37]", R.drawable.smiley_37);
        classicsEmoji.put("[emoji38]", R.drawable.smiley_38);
        classicsEmoji.put("[emoji39]", R.drawable.smiley_39);
        classicsEmoji.put("[emoji40]", R.drawable.smiley_40);
        classicsEmoji.put("[emoji41]", R.drawable.smiley_41);
        classicsEmoji.put("[emoji42]", R.drawable.smiley_42);
        classicsEmoji.put("[emoji43]", R.drawable.smiley_43);
        classicsEmoji.put("[emoji44]", R.drawable.smiley_44);
        classicsEmoji.put("[emoji45]", R.drawable.smiley_45);
        classicsEmoji.put("[emoji46]", R.drawable.smiley_46);
        classicsEmoji.put("[emoji47]", R.drawable.smiley_47);
        classicsEmoji.put("[emoji48]", R.drawable.smiley_48);
        classicsEmoji.put("[emoji49]", R.drawable.smiley_49);
        classicsEmoji.put("[emoji50]", R.drawable.smiley_50);
        classicsEmoji.put("[emoji51]", R.drawable.smiley_51);
        classicsEmoji.put("[emoji52]", R.drawable.smiley_52);
        classicsEmoji.put("[emoji53]", R.drawable.smiley_53);
        classicsEmoji.put("[emoji54]", R.drawable.smiley_54);
        classicsEmoji.put("[emoji55]", R.drawable.smiley_55);
        classicsEmoji.put("[emoji56]", R.drawable.smiley_56);
        classicsEmoji.put("[emoji57]", R.drawable.smiley_57);
        classicsEmoji.put("[emoji58]", R.drawable.smiley_58);
        classicsEmoji.put("[emoji59]", R.drawable.smiley_59);
        classicsEmoji.put("[emoji60]", R.drawable.smiley_60);
        classicsEmoji.put("[emoji61]", R.drawable.smiley_61);
        classicsEmoji.put("[emoji62]", R.drawable.smiley_62);
        classicsEmoji.put("[emoji63]", R.drawable.smiley_63);
        classicsEmoji.put("[emoji64]", R.drawable.smiley_64);
        classicsEmoji.put("[emoji65]", R.drawable.smiley_65);
        classicsEmoji.put("[emoji66]", R.drawable.smiley_66);
        classicsEmoji.put("[emoji67]", R.drawable.smiley_67);
        classicsEmoji.put("[emoji68]", R.drawable.smiley_68);
        classicsEmoji.put("[emoji69]", R.drawable.smiley_69);
        classicsEmoji.put("[emoji70]", R.drawable.smiley_70);
        classicsEmoji.put("[emoji71]", R.drawable.smiley_71);
        classicsEmoji.put("[emoji72]", R.drawable.smiley_72);
        classicsEmoji.put("[emoji73]", R.drawable.smiley_73);
        classicsEmoji.put("[emoji74]", R.drawable.smiley_74);
        classicsEmoji.put("[emoji75]", R.drawable.smiley_75);
        classicsEmoji.put("[emoji76]", R.drawable.smiley_76);
        classicsEmoji.put("[emoji77]", R.drawable.smiley_77);
        classicsEmoji.put("[emoji78]", R.drawable.smiley_78);
        classicsEmoji.put("[emoji79]", R.drawable.smiley_79);
        classicsEmoji.put("[emoji80]", R.drawable.smiley_80);
        classicsEmoji.put("[emoji81]", R.drawable.smiley_81);
        classicsEmoji.put("[emoji82]", R.drawable.smiley_82);
        classicsEmoji.put("[emoji83]", R.drawable.smiley_83);
        classicsEmoji.put("[emoji84]", R.drawable.smiley_84);
        classicsEmoji.put("[emoji85]", R.drawable.smiley_85);
        classicsEmoji.put("[emoji86]", R.drawable.smiley_86);
        classicsEmoji.put("[emoji87]", R.drawable.smiley_87);
        classicsEmoji.put("[emoji88]", R.drawable.smiley_88);
        classicsEmoji.put("[emoji89]", R.drawable.smiley_89);
    }

    public static int getEmojiId(int emojiType, String emojiName) {
        Integer emojiId = null;
        switch (emojiType) {
            case EMOJI_TYPE_CLASSICS:
                emojiId = classicsEmoji.get(emojiName);
                break;
            default:
                break;
        }
        return emojiId == null ? -1 : emojiId;
    }

    public static ArrayMap<String, Integer> getEmojiMap(int emojiType) {
        ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
        switch (emojiType) {
            case EMOJI_TYPE_CLASSICS:
                arrayMap = classicsEmoji;
                break;
        }
        return arrayMap;
    }

}
