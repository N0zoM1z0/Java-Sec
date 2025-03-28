package com.publiccms.logic.component.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.views.pojo.entities.ConfigInfo;
import com.publiccms.views.pojo.entities.SysConfig;

/**
 *
 * ConfigComponent 配置组件
 *
 */
@Component
public class ConfigComponent implements SiteCache {
    @Resource
    private SysConfigDataService service;
    @Autowired(required = false)
    private List<Config> configPluginList;

    @Resource
    private SiteComponent siteComponent;
    private CacheEntity<Short, Map<String, Map<String, String>>> cache;

    /**
     * @param siteId
     * @param code
     * @param locale
     * @return config
     */
    public ConfigInfo getConfig(short siteId, String code, Locale locale) {
        Map<String, SysConfig> map = getMap(siteId);
        SysConfig entity = map.get(code);
        ConfigInfo configInfo = null;
        if (null != entity) {
            configInfo = new ConfigInfo(entity.getCode(), entity.getDescription());
            configInfo.setCustomed(true);
        }
        if (CommonUtils.notEmpty(configPluginList)) {
            for (Config configPlugin : configPluginList) {
                String configCode = configPlugin.getCode(siteId);
                if (null != configCode && configCode.equals(code)) {
                    configInfo = new ConfigInfo(code, configPlugin.getCodeDescription(locale));
                }
            }
        }
        return configInfo;
    }

    /**
     * @param siteId
     * @return config list
     */
    public Set<String> getExportableConfigCodeList(short siteId) {
        Set<String> configCodeSet = new HashSet<>();
        if (CommonUtils.notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String code = config.getCode(siteId, false);
                if (CommonUtils.notEmpty(code) && config.exportable()) {
                    configCodeSet.add(code);
                }
            }
        }
        for (Entry<String, SysConfig> entry : getMap(siteId).entrySet()) {
            configCodeSet.add(entry.getKey());
        }
        return configCodeSet;
    }

    /**
     * @param siteId
     * @param locale
     * @param showAll
     * @return config list
     */
    public List<ConfigInfo> getConfigList(short siteId, Locale locale, boolean showAll) {
        List<ConfigInfo> configList = new ArrayList<>();
        Set<String> configCodeSet = new HashSet<>();
        if (CommonUtils.notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String code = config.getCode(siteId, showAll);
                if (CommonUtils.notEmpty(code) && configCodeSet.add(code)) {
                    configList.add(new ConfigInfo(code, config.getCodeDescription(locale)));
                }
            }
        }
        for (Entry<String, SysConfig> entry : getMap(siteId).entrySet()) {
            if (configCodeSet.add(entry.getKey())) {
                ConfigInfo configInfo = new ConfigInfo(entry.getKey(), entry.getValue().getDescription());
                configInfo.setCustomed(true);
                configList.add(configInfo);
            }
        }
        return configList;
    }

    /**
     * @param site
     * @param code
     * @param customed
     * @param locale
     * @return field list
     */
    public List<SysExtendField> getFieldList(SysSite site, String code, Boolean customed, Locale locale) {
        List<SysExtendField> fieldList = new ArrayList<>();
        if ((null == customed || !customed) && CommonUtils.notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String configCode = config.getCode(site.getId());
                if (null != configCode && configCode.equals(code)) {
                    List<SysExtendField> extendFieldList = config.getExtendFieldList(site, locale);
                    if (null != extendFieldList) {
                        fieldList.addAll(extendFieldList);
                    }
                }
            }
        }
        if (null == customed || customed) {
            SysConfig sysConfig = getMap(site.getId()).get(code);
            if (null != sysConfig && CommonUtils.notEmpty(sysConfig.getExtendList())) {
                fieldList.addAll(sysConfig.getExtendList());
            }
        }
        return fieldList;
    }

    /**
     * @param siteId
     * @param code
     * @return config data
     */
    public Map<String, String> getConfigData(short siteId, String code) {
        Map<String, Map<String, String>> siteMap = cache.get(siteId);
        if (null == siteMap) {
            siteMap = new HashMap<>();
        }
        Map<String, String> configMap = siteMap.get(code);
        if (null == configMap) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(siteId, code));
            if (null != entity && CommonUtils.notEmpty(entity.getData())) {
                configMap = ExtendUtils.getExtendMap(entity.getData());
            } else {
                configMap = new HashMap<>();
            }
            siteMap.put(code, configMap);
            cache.put(siteId, siteMap);
        }
        return configMap;
    }

    public static int getInt(String value, int defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static long getLong(String value, long defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static boolean getBoolean(String value, boolean defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            return "true".equalsIgnoreCase(value);
        }
    }

    /**
     * @param siteId
     * @return config map
     */
    public Map<String, SysConfig> getMap(short siteId) {
        Map<String, SysConfig> modelMap;
        File file = new File(siteComponent.getConfigFilePath(siteId));
        if (CommonUtils.notEmpty(file)) {
            try {
                modelMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                        .constructMapLikeType(HashMap.class, String.class, SysConfig.class));
            } catch (IOException | ClassCastException e) {
                modelMap = new HashMap<>();
            }
        } else {
            modelMap = new HashMap<>();
        }
        return modelMap;
    }

    /**
     * 保存配置
     *
     * @param siteId
     * @param modelMap
     * @return whether to save successfully
     */
    public boolean save(short siteId, Map<String, SysConfig> modelMap) {
        File file = new File(siteComponent.getConfigFilePath(siteId));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            CommonConstants.objectMapper.writeValue(outputStream, modelMap);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * @param siteId
     * @param code
     */
    public void removeCache(short siteId, String code) {
        Map<String, Map<String, String>> map = cache.get(siteId);
        if (CommonUtils.notEmpty(map)) {
            map.remove(code);
        }
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        cache = cacheEntityFactory.createCacheEntity("config");
    }

}
