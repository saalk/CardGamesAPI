package nl.knikit.cardgames.dao.common;

import com.google.common.base.Preconditions;

import nl.knikit.cardgames.model.Game;
import nl.knikit.cardgames.model.GameType;
import nl.knikit.cardgames.model.state.CardGameStateMachine;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class GenericHibernateDao<T extends Serializable> extends AbstractHibernateDao<T> implements IGenericDao<T> {
	
	//
}