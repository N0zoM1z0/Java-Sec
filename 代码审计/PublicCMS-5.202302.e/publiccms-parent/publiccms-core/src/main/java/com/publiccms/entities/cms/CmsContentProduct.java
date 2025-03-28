package com.publiccms.entities.cms;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * CmsContentProduct generated by hbm2java
 */
@Entity
@Table(name = "cms_content_product")
@DynamicUpdate
public class CmsContentProduct implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @GeneratorColumn(title = "ID")
    private Long id;
    @GeneratorColumn(title = "站点", condition = true)
    @JsonIgnore
    private short siteId;
    /**
     * content<p>
     * 内容
     */
    @GeneratorColumn(title = "内容", condition = true)
    private long contentId;
    /**
     * upload user id<p>
     * 上传用户id
     */
    @GeneratorColumn(title = "上传用户", condition = true)
    private long userId;
    /**
     * cover<p>
     * 封面图
     */
    @GeneratorColumn(title = "封面")
    private String cover;
    /**
     * title<p>
     * 标题
     */
    @GeneratorColumn(title = "标题")
    private String title;
    /**
     * price<p>
     * 价格
     */
    @GeneratorColumn(title = "价格", condition = true, order = true)
    private BigDecimal price;
    /**
     * min quantity<p>
     * 最小购买数量
     */
    @GeneratorColumn(title = "最小购买数量")
    private Integer minQuantity;
    /**
     * max quantity<p>
     * 最大购买数量
     */
    @GeneratorColumn(title = "最大购买数量")
    private Integer maxQuantity;
    /**
     * inventory<p>
     * 库存
     */
    @GeneratorColumn(title = "库存", order = true)
    private int inventory;
    /**
     * sales<p>
     * 销量
     */
    @GeneratorColumn(title = "销量", order = true)
    private int sales;

    public CmsContentProduct() {
    }

    public CmsContentProduct(short siteId, long contentId, long userId, String title, BigDecimal price, int inventory, int sales) {
        this.siteId = siteId;
        this.contentId = contentId;
        this.userId = userId;
        this.title = title;
        this.price = price;
        this.inventory = inventory;
        this.sales = sales;
    }

    public CmsContentProduct(short siteId, long contentId, long userId, String cover, String title, BigDecimal price, Integer minQuantity,
            Integer maxQuantity, int inventory, int sales) {
        this.siteId = siteId;
        this.contentId = contentId;
        this.userId = userId;
        this.cover = cover;
        this.title = title;
        this.price = price;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.inventory = inventory;
        this.sales = sales;
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

    @Column(name = "content_id", nullable = false)
    public long getContentId() {
        return this.contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "cover")
    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Column(name="title", nullable=false, length=100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="price", nullable=false, precision=10)
    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "min_quantity")
    public Integer getMinQuantity() {
        return this.minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    @Column(name = "max_quantity")
    public Integer getMaxQuantity() {
        return this.maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @Column(name = "inventory", nullable = false)
    public int getInventory() {
        return this.inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Column(name = "sales", nullable = false)
    public int getSales() {
        return this.sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

}
