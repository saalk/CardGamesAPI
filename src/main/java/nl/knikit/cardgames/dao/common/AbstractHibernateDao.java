package nl.knikit.cardgames.dao.common;

import com.google.common.base.Preconditions;

import nl.knikit.cardgames.model.Player;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

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
    public final List<T> findAllByFkId(final T entity, final String column, final String inputValue) {
        String idMessage = String.format("Entity to select all DAO all by where clause in DAO: %s", entity.toString());
        log.info(idMessage);

        // JPA Criteria API Queries
        // - cb=queryBuilder,
        // - q=query,
        // - c=class
        // - p=parameter
        // - gt, lt, eq is <, > or =

        // clazz.getClass() gets the runtime type of clazz
        // class.getName() gets the package + name of the class

        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root<T> rt  = cq.from(entity.getClass());
        cq.select(rt);

        if (inputValue == "true" || inputValue == "false") {
            ParameterExpression<Boolean> b = cb.parameter(Boolean.class, inputValue);
            cq.where(cb.equal(rt.get(""+column), b));
        } else {
            try{
                int num = Integer.parseInt(inputValue);
                // is an integer!
                ParameterExpression<Integer> i = cb.parameter(Integer.class, inputValue);
                cq.where(cb.equal(rt.get("" + column), i));
            } catch (NumberFormatException e) {
                // not an integer!
                ParameterExpression<String> s = cb.parameter(String.class, inputValue);
                cq.where(cb.equal(rt.get("" + column), s));
            }
        }

        try {
            return getCurrentSession().createQuery(cq).list();
        } catch (Exception e) {
            String errorMessage = String.format("Entity to select all error: %s in DAO by where clause column: %s value: $s", e, column, inputValue);
            log.error(errorMessage);
            throw e;
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