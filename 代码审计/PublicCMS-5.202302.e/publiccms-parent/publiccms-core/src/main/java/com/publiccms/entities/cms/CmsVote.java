package com.publiccms.entities.cms;
// Generated 2020-3-26 11:25:55 by Hibernate Tools 6.0.0-SNAPSHOT

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * CmsVote generated by hbm2java
 */
@Entity
@Table(name = "cms_vote")
@DynamicUpdate
public class CmsVote implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    @JsonIgnore
    @GeneratorColumn(title = "站点", condition = true)
    private short siteId;
    /**
     * start date<p>
     * 开始日期
     */
    @GeneratorColumn(title = "开始日期", condition = true, order = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    /**
     * end date<p>
     * 结束日期
     */
    @GeneratorColumn(title = "结束日期", condition = true, order = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    /**
     * votes<p>
     * 投票次数
     */
    @GeneratorColumn(title = "投票次数", order = true)
    private int votes;
    /**
     * title<p>
     * 标题
     */
    @GeneratorColumn(title = "标题", condition = true, like = true)
    private String title;
    /**
     * description<p>
     * 描述
     */
    @GeneratorColumn(title = "描述")
    private String description;
    /**
     * create date<p>
     * 创建日期
     */
    @GeneratorColumn(title = "创建日期", order = true)
    private Date createDate;
    @JsonIgnore
    @GeneratorColumn(title = "已禁用", condition = true)
    private boolean disabled;

    public CmsVote() {
    }

    public CmsVote(short siteId, Date startDate, Date endDate, int votes, String title, Date createDate, boolean disabled) {
        this.siteId = siteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.votes = votes;
        this.title = title;
        this.createDate = createDate;
        this.disabled = disabled;
    }

    public CmsVote(short siteId, Date startDate, Date endDate, int votes, String title, String description, Date createDate, boolean disabled) {
        this.siteId = siteId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.votes = votes;
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.disabled = disabled;
    }

    @Id
    @GeneratedValue(generator = "cmsGenerator")
    @GenericGenerator(name = "cmsGenerator", strategy = CmsUpgrader.IDENTIFIER_GENERATOR)

    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "site_id", nullable = false)
    public short getSiteId() {
        return this.siteId;
    }

    public void setSiteId(short siteId) {
        this.siteId = siteId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date", nullable = false, length = 19)
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date", length = 19)
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "votes", nullable = false)
    public int getVotes() {
        return this.votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", length = 300)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, length = 19)
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "disabled", nullable = false)
    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
