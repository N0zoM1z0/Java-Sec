package com.publiccms.common.servlet;

import static com.publiccms.common.constants.Constants.DEFAULT_CHARSET_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.publiccms.common.base.AbstractCmsUpgrader;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.database.CmsUpgrader;
import com.publiccms.common.tools.DatabaseUtils;
import com.publiccms.common.tools.VerificationUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * InstallServlet
 *
 */
public class InstallServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public static final String STEP_CHECKDATABASE = "checkDataBase";
    /**
     *
     */
    public static final String STEP_DATABASECONFIG = "dataBaseConfig";
    /**
     *
     */
    public static final String STEP_INITDATABASE = "initDatabase";
    /**
     *
     */
    public static final String STEP_UPDATE = "update";
    /**
     *
     */
    public static final String STEP_START = "start";

    private final Log log = LogFactory.getLog(getClass());

    private Configuration freemarkerConfiguration;
    private String startStep;
    private String fromVersion;
    private AbstractCmsUpgrader cmsUpgrader;

    /**
     * @param startStep
     * @param fromVersion
     */
    public InstallServlet(String startStep, String fromVersion) {
        this.startStep = startStep;
        this.fromVersion = fromVersion;
        this.cmsUpgrader = new CmsUpgrader();
        this.freemarkerConfiguration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        freemarkerConfiguration.setClassForTemplateLoading(getClass(), "/initialization/template/");
        freemarkerConfiguration.setDefaultEncoding(DEFAULT_CHARSET_NAME);
        freemarkerConfiguration.setNumberFormat("#");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (CmsVersion.isInitialized()) {
            response.sendRedirect(request.getContextPath());
        } else {
            String step = request.getParameter("step");
            Map<String, Object> map = new HashMap<>();

            // 记录当前版本号
            map.put("currentVersion", CmsVersion.getVersion());
            map.put("defaultPort", cmsUpgrader.getDefaultPort());
            map.put("dataFilePath", CommonConstants.CMS_FILEPATH);

            if (null == step) {
                step = startStep;
            }

            // 2017-06-19 修改connection为局部变量，避免出现connection没有关闭的漏洞
            if (null != step) {
                map.put("versions", cmsUpgrader.getVersionList());
                map.put("fromVersion", fromVersion);
                switch (step) {
                case STEP_DATABASECONFIG:
                    configDatabase(request, map);
                    break;
                case STEP_CHECKDATABASE:
                    checkDatabse(request, map);
                    break;
                case STEP_INITDATABASE:
                    try {
                        initDatabase(request.getParameter("useSimple"), request.getParameter("username"),
                                request.getParameter("password"), request.getParameter("siteurl"), map);
                        startCMS(map);
                    } catch (Exception e) {
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_UPDATE:
                    try {
                        upgradeDatabase(request.getParameter("from_version"), map);
                        startCMS(map);
                    } catch (Exception e) {
                        map.put("message", "failed");
                        map.put("error", e.getMessage());
                    }
                    break;
                case STEP_START:
                    try {
                        start();
                        step = "startSuccess";
                    } catch (IOException e) {
                        map.put("message", "failed");
                        map.put("error", e.getMessage());
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
            render(step, request.getLocale(), map, response);
        }
    }

    private void start() throws FileNotFoundException, IOException {
        CmsVersion.setInitialized(true);
        CmsDataSource.initDefaultDataSource();
        File file = new File(CommonConstants.CMS_FILEPATH + CommonConstants.INSTALL_LOCK_FILENAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(CmsVersion.getVersion().getBytes(CommonConstants.DEFAULT_CHARSET));
        }
        log.info(new StringBuilder("PublicCMS ").append(CmsVersion.getVersion()).append(" started!"));
    }

    /**
     * 配置数据库
     */
    private void configDatabase(HttpServletRequest request, Map<String, Object> map) {
        try {
            Properties dbconfig = PropertiesLoaderUtils.loadAllProperties(CmsDataSource.DATABASE_CONFIG_TEMPLATE);
            String host = request.getParameter("host");
            String port = request.getParameter("port");
            String database = request.getParameter("database");
            String timeZone = request.getParameter("timeZone");
            cmsUpgrader.setDataBaseUrl(dbconfig, host, port, database, timeZone);
            dbconfig.setProperty("jdbc.username", request.getParameter("username"));
            dbconfig.setProperty("jdbc.encryptPassword", VerificationUtils
                    .base64Encode(VerificationUtils.encrypt(request.getParameter("password"), CommonConstants.ENCRYPT_KEY)));
            String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
            File file = new File(databaseConfiFile);
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                dbconfig.store(outputStream, null);
            }
            try (Connection connection = DatabaseUtils.getConnection(databaseConfiFile)) {
                map.put("siteurl", getSiteUrl(request));
                map.put("usersql", new File(CommonConstants.CMS_FILEPATH + "/publiccms.sql").exists());
                map.put("message", "success");
            }
        } catch (Exception e) {
            startStep = null;
            map.put("error", e.getMessage());
        }
    }

    /**
     * 检查数据库
     *
     * @param map
     */
    private void checkDatabse(HttpServletRequest request, Map<String, Object> map) {
        String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
        if (null != fromVersion && cmsUpgrader.getOldDatabaseConfigVersionList().contains(fromVersion)) {
            startStep = null;
        } else {
            try (Connection connection = DatabaseUtils.getConnection(databaseConfiFile)) {
                map.put("message", "success");
                map.put("siteurl", getSiteUrl(request));
                map.put("usersql", new File(CommonConstants.CMS_FILEPATH + "/publiccms.sql").exists());
            } catch (Exception e) {
                startStep = null;
                map.put("error", e.getMessage());
            }
        }
    }

    private static String getSiteUrl(HttpServletRequest request) {
        return "//" + request.getServerName()
                + ("https".equals(request.getScheme()) && 443 == request.getServerPort() || 80 == request.getServerPort() ? ("")
                        : (":" + request.getServerPort()))
                + request.getContextPath();
    }

    /**
     * 初始化数据库
     *
     * @param useSimple
     * @param username
     * @param password
     * @param map
     * @throws IOException
     */
    private void initDatabase(String useSimple, String username, String password, String siteurl, Map<String, Object> map)
            throws Exception {
        String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
        try (Connection connection = DatabaseUtils.getConnection(databaseConfiFile)) {
            try {
                map.put("history", install(connection, username, password, siteurl, null != useSimple));
                map.put("message", "success");
            } catch (Exception e) {
                e.printStackTrace();
                map.put("message", "failed");
                map.put("error", e.getMessage());
            }
        }
    }

    private String install(Connection connection, String username, String password, String siteurl, boolean useSimple)
            throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(new PrintWriter(stringWriter));
        runner.setAutoCommit(true);
        try (InputStream inputStream = InstallServlet.class.getResourceAsStream("/initialization/sql/initDatabase.sql")) {
            runner.runScript(new InputStreamReader(inputStream, CommonConstants.DEFAULT_CHARSET));
        }
        cmsUpgrader.setPassword(connection, username, password);
        cmsUpgrader.setSiteUrl(connection, siteurl);
        if (useSimple) {
            File file = new File(CommonConstants.CMS_FILEPATH + "/publiccms.sql");
            if (file.exists()) {
                try (InputStream simpleInputStream = new FileInputStream(file)) {
                    runner.runScript(new InputStreamReader(simpleInputStream, CommonConstants.DEFAULT_CHARSET));
                }
            }
        }
        return stringWriter.toString();
    }

    /**
     * 升级数据库
     */
    private void upgradeDatabase(String version, Map<String, Object> map) throws Exception {
        if (cmsUpgrader.getVersionList().contains(version)) {
            String databaseConfiFile = CommonConstants.CMS_FILEPATH + CmsDataSource.DATABASE_CONFIG_FILENAME;
            try (Connection connection = DatabaseUtils.getConnection(databaseConfiFile)) {
                StringWriter stringWriter = new StringWriter();
                try {
                    cmsUpgrader.update(stringWriter, connection, version);
                    map.put("history", stringWriter.toString());
                    map.put("message", "success");
                } catch (Exception e) {
                    fromVersion = cmsUpgrader.getVersion();
                    map.put("history", stringWriter.toString());
                    throw e;
                }
            }
        }
    }

    /**
     * 启动CMS
     */
    private void startCMS(Map<String, Object> map) {
        try {
            start();
        } catch (Exception e) {
            CmsVersion.setInitialized(false);
            e.printStackTrace();
            map.put("error", e.getMessage());
        }
    }

    private void render(String step, Locale locale, Map<String, Object> model, HttpServletResponse response) {
        if (!response.isCommitted()) {
            try {
                Template template = freemarkerConfiguration.getTemplate(null == step ? "index.html" : step + ".html", locale);
                response.setCharacterEncoding(DEFAULT_CHARSET_NAME);
                response.setContentType("text/html");
                template.process(model, response.getWriter());
            } catch (TemplateException | IOException e) {
            }
        }
    }
}
