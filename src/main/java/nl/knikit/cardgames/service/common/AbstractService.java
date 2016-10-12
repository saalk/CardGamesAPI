package nl.knikit.cardgames.service.common;

import nl.knikit.cardgames.dao.common.IOperations;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractService<T extends Serializable> implements IOperations<T> {

    @Override
    public T findOne(final int id) {
        return getDao().findOne(id);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public void create(final T entity) {
        getDao().create(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    public void delete(final T entity) {
        getDao().delete(entity);
    }

    @Override
    public void deleteAllByWhereClause(final T entity, final String whereClause)
    { getDao().deleteAllByWhereClause(entity, whereClause);};

    @Override
    public void deleteAllByIds(final T entity, final List<String> ids) {
        getDao().deleteAllByIds(entity, ids);
    }
    protected abstract IOperations<T> getDao();

}
