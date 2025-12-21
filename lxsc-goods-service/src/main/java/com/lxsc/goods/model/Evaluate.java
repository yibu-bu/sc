package com.lxsc.goods.model;

import java.util.List;

public class Evaluate {

    private Long id;

    private String content;

    private Integer score;

    private Long userId;

    private Long goodsId;

    private String time;

    private String nickName;

    private String headPortrait;

    // 评价的图片集合
    private List<EvaluateImg> evaluateImgList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public List<EvaluateImg> getEvaluateImgList() {
        return evaluateImgList;
    }

    public void setEvaluateImgList(List<EvaluateImg> evaluateImgList) {
        this.evaluateImgList = evaluateImgList;
    }

}