package com.wcs.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.Validate;

/**
 * JPA Service 基类.
 * <p/>
 * 扩展功能包括分页查询,按属性过滤条件列表查询. 可直接使用,也可以扩展EntityService
 * 
 * @author chris
 */
public class EntityService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityService() {
    }

    // ----------------------------- 基本 CRUD 操作方法 ------------------------------//

    /**
     * 保存新增的对象.
     */
    public <T> T create(final T entity) {
        Validate.notNull(entity, "entity不能为空");
        entityManager.persist(entity);
        logger.debug("create-create entity: {}", entity);
        return entity;
    }

    public boolean isManaged(Object entity) {
        return entityManager.contains(entity);
    }

    /**
     * 保存修改的对象.
     */
    public <T> T update(final T entity) {
        Validate.notNull(entity, "entity不能为空");
        entityManager.merge(entity);
         logger.debug("update entity: {}", entity);
        return entity;
    }

    /**
     * 删除对象.
     * 
     * @param entity 对象必须是session中的对象或含id属性的transient对象.
     */
    public <T> void delete(final T entity) {
        Validate.notNull(entity, "entity不能为空");
        entityManager.remove(entity);
        logger.debug("delete entity: {}", entity);
    }

    /**
     * 按id删除对象.
     */

    public void delete(Class<?> entityClass, final Serializable id) {
        Validate.notNull(id, "id不能为空");
        delete(findUnique(entityClass, id));
        logger.debug("delete entity {},id is {}", entityClass.getSimpleName(), id);
    }

    /**
     * 按id获取对象.
     */
    public <T> T findUnique(Class<T> entityClass, final Serializable id) {
        Validate.notNull(id, "id不能为空");
        return (T) entityManager.find(entityClass, id);
    }

    /**
     * 获取全部对象
     * 
     * @param entityClass 实体类的类型
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> entityClass) {
        return this.findAll(entityClass, null, null);
    }

    /**
     * 获取全部对象, 支持按属性行序.
     * 
     * @param entityClass
     * @param orderByProperty
     * @param isAsc
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> entityClass, String orderByProperty, Boolean isAsc) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
        Root<T> entityRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(entityRoot);

        if (orderByProperty != null) {
            if (isAsc) {
                criteriaQuery.orderBy(builder.asc(entityRoot.get(orderByProperty)));
            } else {
                criteriaQuery.orderBy(builder.desc(entityRoot.get(orderByProperty)));
            }
        }

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    // --------------------------------- 条件查询方法 -----------------------------------//

    /**
     * 按属性查找唯一对象, 匹配方式为相等.
     */
    public <T> T findUnique(Class<T> entityClass, final String propertyName, final Object value) {
        Validate.hasText(propertyName, "propertyName不能为空");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);

        Root<T> entityRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(entityRoot);
        criteriaQuery.where(builder.equal(entityRoot.get(propertyName), value));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    /**
     * 按JPQL查询唯一对象.
     * 
     * @param values 数量可变的参数,按顺序绑定.
     */
    public <X> X findUnique(final String jpql, final Object... values) {
        return (X) createQuery(jpql, values).getSingleResult();
    }

    /**
     * 按JPQL查询唯一对象.
     * 
     * @param values 命名参数,按名称绑定.
     */
    public <X> X findUnique(final String jpql, final Map<String, ?> values) {
        return (X) createQuery(jpql, values).getSingleResult();
    }

    /**
     * 按属性查找对象列表, 匹配方式为相等.
     */
    public <T> List<T> findList(Class<T> entityClass, final String propertyName, final Object value) {
        Validate.hasText(propertyName, "propertyName不能为空");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);

        Root<T> entityRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(entityRoot);
        criteriaQuery.where(builder.equal(entityRoot.get(propertyName), value));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * 按JPQL查询对象列表.
     * 
     * @param values 数量可变的参数,按顺序绑定.
     */
    public <X> List<X> findList(final String jpql, final Object... values) {
        return createQuery(jpql, values).getResultList();
    }

    /**
     * 按JPQL查询对象列表.
     * 
     * @param values 命名参数,按名称绑定.
     */
    public <X> List<X> findList(final String jpql, final Map<String, ?> values) {
        return createQuery(jpql, values).getResultList();
    }

    // ------------------------------ createQuery 方法集合 -------------------------//

    /**
     * 根据查询JPQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     * 
     * @param values 数量可变的参数,按顺序绑定.
     */

    public Query createQuery(final String queryString, final Object... values) {
        Validate.hasText(queryString, "queryString不能为空");
        Query query = entityManager.createQuery(queryString);

        if (values != null && values.length > 0) {
            for (int i = 1; i <= values.length; i++) {
                query.setParameter(i, values[i - 1]);
            }
        }
        return query;
    }

    /**
     * 根据查询JPQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
     * 
     * @param queryString jpql 查询语句
     * @param values 命名参数,按名称绑定.
     * @return 返回 javax.persistence.Query 对象.
     */
    public Query createQuery(final String queryString, final Map<String, ?> values) {
        Validate.hasText(queryString, "queryString不能为空");
        Query query = entityManager.createQuery(queryString);
        for (Map.Entry<String, ?> kv : values.entrySet()) {
            query.setParameter(kv.getKey(), kv.getValue());
        }
        return query;
    }

    // ---------------------------- execute 及其他方法 ------------------------------ //

    /**
     * 执行JPQL进行批量修改/删除操作.
     * 
     * @param values 数量可变的参数,按顺序绑定.
     * @return 更新记录数.
     */

    public int batchExecute(final String jpql, final Object... values) {
        return createQuery(jpql, values).executeUpdate();
    }

    /**
     * 执行JPQL进行批量修改/删除操作.
     * 
     * @param values 命名参数,按名称绑定.
     * @return 更新记录数.
     */

    public int batchExecute(final String jpql, final Map<String, ?> values) {
        return createQuery(jpql, values).executeUpdate();
    }

}
