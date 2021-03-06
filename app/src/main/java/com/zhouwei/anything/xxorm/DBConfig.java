package com.zhouwei.anything.xxorm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouwei on 2017/12/21.
 */

public class DBConfig {
    private static HashMap<String, DBInfo> dbs = new HashMap<>();

    // dbName,DBInfo
    public Map<String, DBInfo> getDbs() {
        return dbs;
    }

    public DBInfo getDBInfo(String dbName) {
        return dbs.get(dbName);
    }

    public void putDBInfo(DBInfo dbInfo) {
        dbs.put(dbInfo.getDbName(), dbInfo);
    }

    public static DBConfig getInstance() {
        return DBConfigHolder.instance;
    }

    private DBConfig() {
    }

    private static class DBConfigHolder {
        public static DBConfig instance = new DBConfig();
    }

    public int parserDBConfig(InputStream inputStream, int currentVersion) {
        return XMLParser.getInstance().parserDBConfig(inputStream, currentVersion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, DBInfo> en : dbs.entrySet()) {
            sb.append("dbName: " + en.getKey() + " ");
            DBInfo info = en.getValue();
            sb.append("DBInfo: " + info + " ");
        }
        return sb.toString();
    }
}
