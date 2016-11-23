package nl.knikit.cardgames.dao.common;

import com.google.common.base.Preconditions;

import nl.knikit.cardgames.model.CardGameType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

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
    public final T findOneWithString(final String id) {return getCurrentSession().get(clazz, id);
    }

    @Override
    public final T findOne(final int id) {return getCurrentSession().get(clazz, id);
    }

    @Override
    public List<T> findAll(final String column, final String direction) {
        if (column == null || direction == null) {
            return getCurrentSession().createQuery("from " + clazz.getName()).list();
        } else {
            return getCurrentSession().createQuery("from " + clazz.getName() + " order by " + column + " " + direction ).list();
        }
    }

    @Override
    public final List<T> findAllWhere(final String column, final String inputValue) {
        String idMessage = String.format("findAll where dao entity: %s", clazz.getName().getClass());
        log.info(idMessage);

        // JPA Criteria API Query builder logic:
        // - cb=queryBuilder,
        // - q=query,
        // - c=class
        // - p=parameter
        // - gt, ge, lt, le, eq, like, ilike (case insensitive like) for operand

        // clazz.getClass() gets the runtime type of clazz
        // class.getName() gets the package + name of the class

        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(clazz);
        Root<T> rt  = cq.from(clazz.getName().getClass());
        cq.select(rt);

        switch (inputValue) {
            case "HIGHLOW":
                cq.where(cb.equal(rt.get(column), CardGameType.HIGHLOW));
                idMessage = String.format("findAllWhere dao inputValue is ENUM: %s", inputValue);
                break;
            case "BLACKJACK":
                cq.where(cb.equal(rt.get(column), CardGameType.BLACKJACK));
                idMessage = String.format("findAllWhere dao inputValue is ENUM: %s", inputValue);
                break;
            case "true":
                cq.where(cb.equal(rt.get(column), Boolean.TRUE));
                idMessage = String.format("findAllWhere dao inputValue is true boolean: %s", inputValue);
                break;
            case "false":
                cq.where(cb.equal(rt.get(column), Boolean.FALSE));
                idMessage = String.format("findAllWhere dao inputValue is false boolean: %s", inputValue);
                break;
            default:
                try{
                    int num = Integer.parseInt(inputValue);

                    // is an integer!
                    cq.where(cb.equal(rt.get(column), num));
                    idMessage = String.format("findAllWhere dao inputValue is integer: %d", num);
                } catch (NumberFormatException e) {

                    // not an integer!
                    cq.where(cb.equal(rt.get(column), inputValue));
                    idMessage = String.format("findAllWhere dao inputValue is string: %s", inputValue);
                }
        }
        log.info(idMessage);

        try {
            return getCurrentSession().createQuery(cq).list();
        } catch (Exception e) {
            String errorMessage = String.format("Error findAllWhere, column: %s inputValue: %s query: %s error: %s", column, inputValue, cq ,e);
            log.error(errorMessage);
            throw e;
        }
    }

    @Override
    public final T create(final T entity) {
        String message = String.format("Entity to create in DAO: %s", entity.toString());
        log.info(message);

        Preconditions.checkNotNull(entity);
        try {
            // getCurrentSession().persist(entity);
            getCurrentSession().saveOrUpdate(clazz.getName(), entity);
        } catch (Exception e) {
            String errorMessage = String.format("Entity create error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }
        return entity;
    }

    @Override
    public final T update(final T entity) {
        String message = String.format("Entity to update in DAO: %s", entity.toString());
        log.info(message);

        Preconditions.checkNotNull(entity);
        try {
            // getCurrentSession().merge(entity);
            getCurrentSession().update(clazz.getName(), entity);
        } catch (Exception e) {
            String errorMessage = String.format("Entity to update error: %s in DAO by entity: %s", e, entity);
            log.error(errorMessage);
            throw e;
        }
        return entity;
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
        String message = String.format("Entity to delete all in DAO: %s", entity.toString());
        log.info(message);
        Preconditions.checkNotNull(entity);
        try {
            getCurrentSession().createQuery("DELETE FROM "+ entity.toString()).executeUpdate();
        } catch (Exception e) {
            String errorMessage = String.format("Entity to delete all error: %s in DAO by entity: %s", e, entity);
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