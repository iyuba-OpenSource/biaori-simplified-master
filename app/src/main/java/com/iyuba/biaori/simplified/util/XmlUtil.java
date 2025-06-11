package com.iyuba.biaori.simplified.util;

import com.iyuba.biaori.simplified.model.bean.textbook.UpdateCollectBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class XmlUtil {


    public static UpdateCollectBean parseXMLWithPull(String xml) {

        UpdateCollectBean updateCollectBean = new UpdateCollectBean();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xml));
            int type = xmlPullParser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                String node = xmlPullParser.getName();
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("msg".equals(node)) {//是否是name的节点
                            String msg = xmlPullParser.nextText();//获取节点的具体内容
                            updateCollectBean.setMsg(msg);
                        } else if ("result".equals(node)) {
                            String result = xmlPullParser.nextText();
                            updateCollectBean.setResult(Integer.parseInt(result));
                        } else if ("type".equals(node)) {
                            String typeStr = xmlPullParser.nextText();
                            updateCollectBean.setType(typeStr);
                        } else if ("topic".equals(node)) {

                            String topic = xmlPullParser.nextText();
                            updateCollectBean.setTopic(topic);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //当读取完Book节点时，输出一下获取的内容
                        if ("Book".equals(node)) {

                        }
                        break;
                }
                type = xmlPullParser.next();
            }//while
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return updateCollectBean;
    }
}
