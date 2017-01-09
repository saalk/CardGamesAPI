package nl.knikit.cardgames.service.common;

import nl.knikit.cardgames.dao.common.AttributesHashMap;
import nl.knikit.cardgames.dao.common.IOperations;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

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
    public List<T> findAllWhere(final String column, final String inputValue)
    { return getDao().findAllWhere(column, inputValue);}
    
    @Override
    public <SK> List<T> findAllByAttributes(AttributesHashMap<T> attributes, SingularAttribute<T, SK> order) {
        return getDao().findAllByAttributes(attributes, order);
    }
    
    @Override
    public T create(final T entity) {
        return getDao().create(entity);
    }
    
    @Override
    public T createDefaultGame(final T entity) {
        return getDao().createDefaultGame(entity);
    }

    @Override
    public T update(final T entity) {
        return getDao().update(entity);
    }
    
    @Override
    public T updateStateInGame(final T entity) {
        return getDao().updateStateInGame(entity);
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
