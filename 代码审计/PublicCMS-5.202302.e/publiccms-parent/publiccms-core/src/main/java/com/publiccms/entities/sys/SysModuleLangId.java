package com.publiccms.entities.sys;
// Generated 2018-6-5 18:18:57 by Hibernate Tools 5.1.6.Final

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import com.publiccms.common.generator.annotation.GeneratorColumn;

/**
 * SysModuleLangId generated by hbm2java
 */
@Embeddable
public class SysModuleLangId implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * module id<p>
     * 模块id
     */
    @GeneratorColumn(title = "模块", condition = true)
    private String moduleId;
    /**
     * lang<p>
     * 语言
     */
    @GeneratorColumn(title = "语言", condition = true)
    private String lang;

    public SysModuleLangId() {
    }

    public SysModuleLangId(String moduleId, String lang) {
        this.moduleId = moduleId;
        this.lang = lang;
    }

    @Column(name = "module_id", nullable = false, length = 30)
    public String getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Column(name = "lang", nullable = false, length = 20)
    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof SysModuleLangId))
            return false;
        SysModuleLangId castOther = (SysModuleLangId) other;

        return ((this.getModuleId() == castOther.getModuleId()) || (this.getModuleId() != null && castOther.getModuleId() != null
                && this.getModuleId().equals(castOther.getModuleId())))
                && ((this.getLang() == castOther.getLang())
                        || (this.getLang() != null && castOther.getLang() != null && this.getLang().equals(castOther.getLang())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + (getModuleId() == null ? 0 : this.getModuleId().hashCode());
        result = 37 * result + (getLang() == null ? 0 : this.getLang().hashCode());
        return result;
    }

}
