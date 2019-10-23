package com.ais.sys.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Quote {
    private Long id;

    private String quote;

    private String keyWord;

    private String author;

    private Date createDate;

    private String createBy;

    private Date lastUpdateDate;

    private String lastUpdateBy;

    public Quote(Long id, String quote, String keyWord, String author, Date createDate, String createBy, Date lastUpdateDate, String lastUpdateBy) {
        this.id = id;
        this.quote = quote;
        this.keyWord = keyWord;
        this.author = author;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdateDate = lastUpdateDate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Quote() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote == null ? null : quote.trim();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy == null ? null : lastUpdateBy.trim();
    }
}