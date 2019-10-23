package com.ais.sys.models;

public class OneNoteTask {
    private Integer id;

    private String keyWord;

    private Integer status;

    public OneNoteTask(Integer id, String keyWord, Integer status) {
        this.id = id;
        this.keyWord = keyWord;
        this.status = status;
    }

    public OneNoteTask() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}