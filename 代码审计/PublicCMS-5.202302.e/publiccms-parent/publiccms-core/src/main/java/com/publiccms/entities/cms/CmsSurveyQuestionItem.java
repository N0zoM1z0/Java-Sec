package com.publiccms.entities.cms;
// Generated 2020-7-1 18:53:08 by Hibernate Tools 6.0.0-SNAPSHOT

import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * CmsSurveyQuestionItem generated by hbm2java
 */
@Entity
@Table(name = "cms_survey_question_item")
@DynamicUpdate
public class CmsSurveyQuestionItem implements java.io.Serializable {

    /**
    *
    */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    /**
     * question id<p>
     * 问题id
     */
    @GeneratorColumn(title = "问题", condition = true)
    private long questionId;
    /**
     * votes<p>
     * 票数
     */
    @GeneratorColumn(title = "投票次数", order = true)
    private int votes;
    /**
     * title<p>
     * 标题
     */
    @GeneratorColumn(title = "标题")
    private String title;
    /**
     * sort<p>
     * 排序
     */
    @GeneratorColumn(title = "排序", order = true)
    private int sort;

    public CmsSurveyQuestionItem() {
    }

    public CmsSurveyQuestionItem(long questionId, int votes, String title, int sort) {
        this.questionId = questionId;
        this.votes = votes;
        this.title = title;
        this.sort = sort;
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

    @Column(name = "question_id", nullable = false)
    public long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
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
    
    @Column(name="sort", nullable=false)
    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
