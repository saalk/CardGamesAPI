package nl.knikit.cardgames.dao.common;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;

import java.io.Serializable;
import java.util.List;

@Slf4j
// @SuppressWarnings("unchecked")
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
    public List<T> findAll(String column, String direction) {
        if (column == null || direction == null) {
            return getCurrentSession().createQuery("from " + clazz.getName()).list();
        } else {
            return getCurrentSession().createQuery("from " + clazz.getName() + " order by " + column + " " + direction ).list();
        }
    }

    @Override
    public final void create(final T entity) {
        String message = String.format("Entity to create in DAO: %s", entity.toString());
        log.info(message);

        Preconditions.checkNotNull(entity);
        try {
            // getCurrentSession().persist(entity);
            getCurrentSession().saveOrUpdate(entity);
        } catch (Exception e) {
            String errorMessage = String.format("Entity create error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }
    }

    @Override
    public final T update(final T entity) {
        String message = String.format("Entity to update in DAO: %s", entity.toString());
        log.info(message);

        Preconditions.checkNotNull(entity);
        try {
            return (T) getCurrentSession().merge(entity);
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }

    }

    // the deletes
    @Override
    public final void deleteOne(final T entity) {
        String message = String.format("Entity to delete in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        try {
            getCurrentSession().delete(entity);
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }
    }

    @Override
    @Modifying
    public final void deleteAll(final T entity) {
        String message = String.format("Entity to delete in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        try {
            getCurrentSession().createQuery("DELETE FROM "+ entity.toString()).executeUpdate();
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }
    }

    @Override
    @Modifying
    public final void deleteAllByWhereClause(final T entity, final String column, final String inputValue) {
        String idMessage = String.format("Entity to delete all DAO all by where clause in DAO: %s", entity.toString());
        log.info(idMessage);
        try {
            getCurrentSession().createQuery("DELETE c FROM " + entity.toString() + " c WHERE c."+column+" IS :value")
            .setParameter(""+column, inputValue);
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete all error: %s in DAO by where clause: %s IS $s", e, column, inputValue);
            log.error(errorMessage);
            throw e;
        }
    }

    @Override
    public final void deleteAllByIds(final T entity, final List<String> ids) {

        try {

            for (String id : ids ) {

                String idMessage = String.format("Entity to delete in DAO by list of ids has id: %s", id);
                log.info(idMessage);
                final T oneEntity = findOne(Integer.parseInt(id));

                String message = String.format("Entity to delete by id in DAO: %s", entity.toString());
                log.info(message);

                Preconditions.checkState(oneEntity != null);

                getCurrentSession().delete(oneEntity);
            }
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete error: %s in DAO by list of ids: %s", e, ids);
            log.error(errorMessage);
            throw e;
        }
    }

    // private method
    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}