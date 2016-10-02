package nl.knikit.cardgames.dao.common;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public abstract class AbstractHibernateDao<T extends Serializable> implements IOperations<T> {
    private Class<T> clazz;

    @Autowired
    private SessionFactory sessionFactory;

    // API

    protected final void setClazz(final Class<T> clazzToSet) {
        clazz = Preconditions.checkNotNull(clazzToSet);
    }

    @Override
    public final T findOne(final int id) {return (T) getCurrentSession().get(clazz, id);
    }

    @Override
    public final List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    @Override
    public final void create(final T entity) {
        String message = String.format("Entity to create in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        // getCurrentSession().persist(entity);
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public final T update(final T entity) {
        String message = String.format("Entity to update in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        return (T) getCurrentSession().merge(entity);
    }

    @Override
    public final void delete(final T entity) {
        String message = String.format("Entity to delete in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        getCurrentSession().delete(entity);
    }

    @Override
    public final void deleteById(final int entityId) {
        String idMessage = String.format("Entity to delete id in DAO: %s", entityId);
        log.info(idMessage);
        final T entity = findOne(entityId);
        String message = String.format("Entity to delete by id in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkState(entity != null);
        try {
            delete(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}