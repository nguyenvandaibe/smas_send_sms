package vn.com.viettel.dataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmasMysqlConnectionPool {

    private final Logger log = Logger.getLogger("SMS_SENDER");

    private static final SmasMysqlConnectionPool instance = new SmasMysqlConnectionPool();

    private ComboPooledDataSource cpds;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final static String CLASS_NAME = SmasMysqlConnectionPool.class.getName();

    /**
     * Singleton instance for SmasMysqlConnectionPool. Remember to call one
     * (and only one) initC3P0 method before using its getConnection() method
     *
     * @return Singleton instance for SmasMysqlConnectionPool
     */
    public static SmasMysqlConnectionPool getInstance() {
        return instance;
    }

    /**
     * Prevent outsider from calling construction method.
     */
    private SmasMysqlConnectionPool() {

    }

    /**
     * Initialize C3P0 database connection pooling. Note that, this function
     * only need call one and just one time.
     *
     * @throws PropertyVetoException will be throw when critical error occurred.
     * It's the time to stop our system.
     */
    public void initC3P0() throws PropertyVetoException, Exception {

        Date eventDate = CommonUtils.getDateNow();
        if (initialized.getAndSet(false)) {
            log.info("Khong khoi tao duoc initC3P0");
            return;
        }

        cpds = new ComboPooledDataSource();

        Properties sysProperties = new Properties();
        InputStream is = null;

        try {
            String pathFile =GlobalConstant.SMAS_CORE_CONFIG_PATH;
            is = new FileInputStream(pathFile);

            // Load all sms core system proferties
            sysProperties.load(is);
            Set<String> keys = sysProperties.stringPropertyNames();

            for (String key : keys) {
                if (key.startsWith("c3p0")) {
                    BeanUtils.setProperty(cpds, key.substring("c3p0.".length()), sysProperties.getProperty(key));
                    //Neu la key CSDL thi thuc hien ma hoa
                    /*if (Parameters.Encrypt && (key.contains(C3P0_DRIVER_CLASS)
                            || key.contains(C3P0_JDBC_URL)
                            || key.contains(C3P0_USER)
                            || key.contains(C3P0_PASSWORD))) {
                        String value = PassTranformer.decrypt(sysProperties.getProperty(key));
                        BeanUtils.setProperty(cpds, key.substring("c3p0.".length()), value);
                    } else {

                    }*/
                }
            }

            LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "initC3P0", eventDate, "Null", "- C3P0 loaded with: " + cpds.getProperties());
            // Check database driver
            cpds.setMinPoolSize(Parameters.CONNECTION_MINPOOLSIZE);
            cpds.setAcquireIncrement(Parameters.CONNECTION_ACQUIREINCREMENT);
            cpds.setMaxPoolSize(Parameters.CONNECTION_MAXPOOLSIZE);
            cpds.setMaxStatements(Parameters.CONNECTION_MAXSTATEMENTS);

            cpds.setCheckoutTimeout(Parameters.CONNECTION_TIMEOUT);

            Class.forName(cpds.getDriverClass());

        } catch (IOException e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "initC3P0", CommonUtils.getDateNow(), "Load database property configuration file error. Please check the C3P0 configuration part.",e);

            //TODO process with critial error
        } catch (ClassNotFoundException e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "initC3P0", CommonUtils.getDateNow(), "No JDBC driver found, please check the C3P0 configuration part.", e);
            //TODO process with critial error
        } catch (IllegalAccessException | InvocationTargetException e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "initC3P0", CommonUtils.getDateNow(), "Invalid properties in the C3P0 configuration part.", e);
            //TODO process with critial error
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.ErrorExt(log, CLASS_NAME, "initC3P0", CommonUtils.getDateNow(), "Invalid properties in the C3P0 configuration part.", e);
                    //Ignore
                }
            }
        }
    }

    public Connection getConnection(Logger logMM) {
        Connection cnn = null;
        try {
            cnn = cpds.getConnection();

        } catch (SQLException ex) {
            LogUtil.ErrorExt(log, CLASS_NAME, "getConnection", CommonUtils.getDateNow(), "NULL", ex);
            logMM.error("SMAS_TKD Can not connect Mysql DB");
            logMM.error("TCP/IP Error not connect to Mysql DB");
        }
        return cnn;
    }
}
