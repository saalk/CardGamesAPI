package nl.knikit.cardgames.service.common;

import nl.knikit.cardgames.dao.common.IOperations;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    @Override
    public T findOneWithString(final String id) {
        return getDao().findOneWithString(id);
    }

    @Override
    public T findOne(final int id) {
        return getDao().findOne(id);
    }

    @Override
    public List<T> findAll(String column, String direction) {
        return getDao().findAll(column, direction);
    }

    @Override
    public List<T> findAllWhere(final T entity, final String column, final String inputValue)
    { return getDao().findAllWhere(entity, column, inputValue);}

    @Override
    public void create(final T entity) {
        getDao().create(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    public void deleteOne(final T entity) {
        getDao().deleteOne(entity);
    }

    @Override
    public void deleteAll(final T entity) {
        getDao().deleteAll(entity);
    }

    @Override
    public void deleteAllByIds(final T entity, final List<String> ids) {
        getDao().deleteAllByIds(entity, ids);
    }
    protected abstract IOperations<T> getDao();

}
