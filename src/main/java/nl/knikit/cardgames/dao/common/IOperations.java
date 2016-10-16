package nl.knikit.cardgames.dao.common;

import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {

    T findOne(final int id);

    List<T> findAll(String column, String direction);

    List<T> findAllByFkId(final T entity, final String column, final String value);

    void create(final T entity);

    T update(final T entity);

    void deleteOne(final T entity);

    void deleteAll(final T entity);

    void deleteAllByIds(final T entity, final List<String> ids);
}
