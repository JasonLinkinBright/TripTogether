package com.jason.trip.custom.view.object;

/**
 * Version V1.0 <Trip客户端>
 * ClassName:RObject
 * Description: #话题#的存储基对象,满足扩展
 * Created by Jason on 16/11/22.
 */

public class RObject {

    private String objectRule = "#";// 默认匹配规则
    private String objectText;// 话题文本

    public String getObjectRule() {
        return objectRule;
    }

    public void setObjectRule(String objectRule) {
        this.objectRule = objectRule;
    }

    public String getObjectText() {
        return objectText;
    }

    public void setObjectText(String objectText) {
        this.objectText = objectText;
    }
}
