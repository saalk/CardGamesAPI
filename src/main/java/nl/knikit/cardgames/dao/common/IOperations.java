package nl.knikit.cardgames.dao.common;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {

    T findOneWithString(final String id);

    T findOne(final int id);

    List<T> findAll(String column, String direction);

    List<T> findAllWhere(final String column, final String inputValue);

    void create(final T entity);

    T update(final T entity);

    void deleteOne(final T entity);

    void deleteAll(final T entity);

    void deleteAllByIds(final T entity, final List<String> ids);
}
