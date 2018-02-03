package com.czy.chat.model;

import android.text.SpannableString;

import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.ext.message.TIMMessageDraft;

import java.nio.charset.Charset;

/**
 * 作者：叶应是叶
 * 时间：2017/11/29 20:53
 * 说明：文本消息
 */
public class TextMessage extends BaseMessage {

    public TextMessage(TIMMessage message) {
        super(message);
    }

    public TextMessage(TIMMessageDraft draft) {
        super(getDraft(draft));
    }

    public TextMessage(String content) {
        super(getMessage(content));
    }

    private static TIMMessage getMessage(String content) {
        TIMTextElem textElem = new TIMTextElem();
        textElem.setText(content);
        TIMMessage message = new TIMMessage();
        message.addElement(textElem);
        return message;
    }

    private static TIMMessage getDraft(TIMMessageDraft draft) {
        TIMMessage message = new TIMMessage();
        for (TIMElem elem : draft.getElems()) {
            message.addElement(elem);
        }
        return message;
    }

    @Override
    public SpannableString getMessageSummary() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.getElementCount(); ++i) {
            switch (message.getElement(i).getType()) {
                case Face:
                    TIMFaceElem faceElem = (TIMFaceElem) message.getElement(i);
                    byte[] data = faceElem.getData();
                    if (data != null) {
                        result.append(new String(data, Charset.forName("UTF-8")));
                    }
                    break;
                case Text:
                    TIMTextElem textElem = (TIMTextElem) message.getElement(i);
                    result.append(textElem.getText());
                    break;
            }
        }
        return new SpannableString(result);
    }

}