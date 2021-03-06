package com.zhouwei.anything.xxorm;


import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.Map;

import static android.R.attr.version;

/**
 * Created by zhouwei on 2017/12/20.
 */

public class XMLParser {
    private static final String TAG = XMLParser.class.getSimpleName();

    public static XMLParser getInstance() {
        return XMLParserHolder.parser;
    }

    private XMLParser() {
    }

    private static class XMLParserHolder {
        public static XMLParser parser = new XMLParser();
    }

    /**
     * @param inputStream
     * @param currentVersion
     * @return 返回值false表示数据库信息没有变化
     * 返回值true表示数据库信息变化了
     */
    public int parserDBConfig(InputStream inputStream, int currentVersion) {
        int res = 0;
        DBConfig dbConfig = null;
        DBInfo dbInfo = null;
        Table table = null;
        Property property = null;
        Map<String, String> tableDbNameDic = null;

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            int next = parser.getEventType();

            while (next != XmlPullParser.END_DOCUMENT) {
                switch (next) {
                    case XmlPullParser.START_DOCUMENT:
                        dbConfig = DBConfig.getInstance();
                        tableDbNameDic = DBEngine.getInstance().getTableDBNameDic();
                        break;
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        //AppLog.i("AAAA", "tagName: " + tagName);

                        if ("dbs".equalsIgnoreCase(tagName)) {
                            int version = Integer.parseInt(parser.getAttributeValue(null, "version"));
                            if (version == currentVersion) {
                                return version;
                            } else {
                                res = version;
                            }
                        } else if ("db".equalsIgnoreCase(tagName)) {
                            dbInfo = new DBInfo();
                        } else if ("dbName".equalsIgnoreCase(tagName)) {
                            String text = parseText(parser);
                            dbInfo.setDbName(text);
                        } else if ("version".equalsIgnoreCase(tagName)) {
                            String text = parseText(parser);
                            dbInfo.setVersion(Integer.parseInt(text));
                        } else if ("cursorFactory".equalsIgnoreCase(tagName)) {
                            String text = parseText(parser);
                            String type = parser.getAttributeValue(null, "type");
                            dbInfo.setCursorFactoryClassName(type);
                        } else if ("databaseErrorHandler".equalsIgnoreCase(tagName)) {
                            String type = parser.getAttributeValue(null, "type");
                            dbInfo.setDatabaseErrorHandlerClassName(type);
                        } else if ("mapping".equalsIgnoreCase(tagName)) {
                            table = new Table();
                            String beanName = parser.getAttributeValue(null, "beanName");
                            table.setBeanName(beanName);
                            tableDbNameDic.put(table.getTableName(), dbInfo.getDbName());
                        } else if ("property".equalsIgnoreCase(tagName)) {
                            property = new Property();

                            String primaryKey = parser.getAttributeValue(null, "primaryKey");
                            boolean isPrimaryKey = "true".equalsIgnoreCase(primaryKey);
                            property.setPrimaryKey(isPrimaryKey);

                            String autoincre = parser.getAttributeValue(null, "autoincre");
                            boolean isAutoicre = "true".equalsIgnoreCase(autoincre);
                            property.setAutoincre(isAutoicre);
                        } else if ("name".equalsIgnoreCase(tagName)) {
                            String text = parseText(parser);
                            property.setPropertyName(text);
                        } else if ("type".equalsIgnoreCase(tagName)) {
                            String text = parseText(parser);
                            property.setPropertyType(text);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String tn = parser.getName();
                        if ("db".equalsIgnoreCase(tn)) {
                            dbConfig.putDBInfo(dbInfo);
                            Log.i("BBBB", "dbInfo: " + dbInfo);
                            dbInfo = null;
                        } else if ("mapping".equalsIgnoreCase(tn)) {
                            dbInfo.putTable(table);
                            table = null;
                        } else if ("property".equalsIgnoreCase(tn)) {
                            table.putProperty(property);
                            property = null;
                        }
                        break;
                    default:
                        break;
                }

                next = parser.next();
            }

            inputStream.close();
        } catch (Exception e) {
            res = currentVersion;
            e.printStackTrace();
            Log.i(TAG, "e: " + e.getMessage());
        }

        return res;
    }

    private static String parseText(XmlPullParser parser) {
        String text = "";
        try {
            int type = parser.next();
            if (type == XmlPullParser.TEXT) {
                text = parser.getText().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }
}
