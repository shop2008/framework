package com.wxxr.mobile.dao;

import java.util.Collection;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.wxxr.mobile.dao.query.Query;
import com.wxxr.mobile.dao.query.QueryBuilder;

public interface IObjectDAO<T, K> {

	AbstractDaoSession getSession();

	String getTablename();

	Property[] getProperties();

	Property getPkProperty();

	String[] getAllColumns();

	String[] getPkColumns();

	String[] getNonPkColumns();

	/**
	 * Loads and entity for the given PK.
	 * 
	 * @param key
	 *            a PK value or null
	 * @return The entity or null, if no entity matched the PK value
	 */
	T load(K key);

	T loadByRowId(long rowId);

	/** Loads all available entities from the database. */
	List<T> loadAll();

	/** Detaches an entity from the identity scope (session). Subsequent query results won't return this object. */
	boolean detach(T entity);

	/**
	 * Inserts the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	void insertInTx(Iterable<T> entities);

	/**
	 * Inserts the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	void insertInTx(T... entities);

	/**
	 * Inserts the given entities in the database using a transaction. The given entities will become tracked if the PK
	 * is set.
	 * 
	 * @param entities
	 *            The entities to insert.
	 * @param setPrimaryKey
	 *            if true, the PKs of the given will be set after the insert; pass false to improve performance.
	 */
	void insertInTx(Iterable<T> entities, boolean setPrimaryKey);

	/**
	 * Inserts or replaces the given entities in the database using a transaction. The given entities will become
	 * tracked if the PK is set.
	 * 
	 * @param entities
	 *            The entities to insert.
	 * @param setPrimaryKey
	 *            if true, the PKs of the given will be set after the insert; pass false to improve performance.
	 */
	void insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey);

	/**
	 * Inserts or replaces the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	void insertOrReplaceInTx(Iterable<T> entities);

	/**
	 * Inserts or replaces the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	void insertOrReplaceInTx(T... entities);

	/**
	 * Insert an entity into the table associated with a concrete DAO.
	 * 
	 * @return row ID of newly inserted entity
	 */
	long insert(T entity);

	/**
	 * Insert an entity into the table associated with a concrete DAO <b>without</b> setting key property. Warning: This
	 * may be faster, but the entity should not be used anymore. The entity also won't be attached to identy scope.
	 * 
	 * @return row ID of newly inserted entity
	 */
	long insertWithoutSettingPk(T entity);

	/**
	 * Insert an entity into the table associated with a concrete DAO.
	 * 
	 * @return row ID of newly inserted entity
	 */
	long insertOrReplace(T entity);

	/** A raw-style query where you can pass any WHERE clause and arguments. */
	List<T> queryRaw(String where, String... selectionArg);

	/**
	 * Creates a repeatable {@link Query} object based on the given raw SQL where you can pass any WHERE clause and
	 * arguments.
	 */
	Query<T> queryRawCreate(String where, Object... selectionArg);

	/**
	 * Creates a repeatable {@link Query} object based on the given raw SQL where you can pass any WHERE clause and
	 * arguments.
	 */
	Query<T> queryRawCreateListArgs(String where,
			Collection<Object> selectionArg);

	void deleteAll();

	/** Deletes the given entity from the database. Currently, only single value PK entities are supported. */
	void delete(T entity);

	/** Deletes an entity with the given PK from the database. Currently, only single value PK entities are supported. */
	void deleteByKey(K key);

	/**
	 * Deletes the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to delete.
	 */
	void deleteInTx(Iterable<T> entities);

	/**
	 * Deletes the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to delete.
	 */
	void deleteInTx(T... entities);

	/**
	 * Deletes all entities with the given keys in the database using a transaction.
	 * 
	 * @param keys
	 *            Keys of the entities to delete.
	 */
	void deleteByKeyInTx(Iterable<K> keys);

	/**
	 * Deletes all entities with the given keys in the database using a transaction.
	 * 
	 * @param keys
	 *            Keys of the entities to delete.
	 */
	void deleteByKeyInTx(K... keys);

	/** Resets all locally changed properties of the entity by reloading the values from the database. */
	void refresh(T entity);

	void update(T entity);

	QueryBuilder<T> queryBuilder();

	/**
	 * Updates the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to insert.
	 */
	void updateInTx(Iterable<T> entities);

	/**
	 * Updates the given entities in the database using a transaction.
	 * 
	 * @param entities
	 *            The entities to update.
	 */
	void updateInTx(T... entities);

	long count();

	/** Gets the SQLiteDatabase for custom database access. Not needed for greenDAO entities. */
	SQLiteDatabase getDatabase();

}