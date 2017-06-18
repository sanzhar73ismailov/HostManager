package modelHost;

import entity.HostDictionary;
import java.util.List;
import java.util.Properties;

public interface Dao {

    <T extends HostDictionary> T getObject(int id, T obj) throws DaoException;

    <T extends HostDictionary> int insertObject(T obj) throws DaoException;

    <T extends HostDictionary> int removeObject(int id, T obj) throws DaoException;

    <T extends HostDictionary> int removeObjects(Properties properties, T obj) throws DaoException;

    <T extends HostDictionary> int updateObject(T obj) throws DaoException;

    <T extends HostDictionary> int updatePropertyOfObject(int id, T obj, String setString) throws DaoException;

    <T extends HostDictionary> List<T> getObjects(Properties properties, T obj) throws DaoException;

    <T extends HostDictionary> List<T> getObjects(Properties properties, T obj, String orderBy) throws DaoException;

    <T extends HostDictionary> List<T> getObjects(Properties properties, T obj, String ordeby, int limit) throws DaoException;

    int getMaxResultVersion(String instrument, String sid, String testCode) throws DaoException;

    int getMaxResultVersion(String instrument, String sid) throws DaoException;

    int delete(String tableName, Properties properties) throws DaoException;
}
