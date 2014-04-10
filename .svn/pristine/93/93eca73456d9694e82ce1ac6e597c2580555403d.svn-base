package com.wcs.tih.filenet.helper.ce;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.JoinComparison;
import com.filenet.api.constants.JoinOperator;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.wcs.tih.filenet.helper.ce.util.EngineCollectionUtils;
import com.wcs.tih.filenet.helper.vo.RankVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Search {
	private Logger logger = Logger.getLogger(super.getClass());
	private ObjectStore os;
	private SearchSQL sql;
	private SearchScope search;
	private PropertyFilter filter;
	private int pageSize;
	private boolean continuable;

	public Search() {
		this.pageSize = 100;
		this.continuable = true;
	}

	public Search setRowSql(String className, boolean includeSubclasses, String searchExpr) {
		this.sql = new SearchSQL();
		this.sql.setSelectList("*");
		this.sql.setFromClauseInitialValue(className, null, includeSubclasses);
		if (!(StringUtils.isEmpty(searchExpr))) {
			this.sql.setContainsRestriction(className, searchExpr);
		}
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setRowSqlWithFullText(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords,
			boolean isFullText) {
		this.sql = new SearchSQL();
		String alias = "d";
		this.sql.setSelectList(alias + ".*, cs.Rank");
		this.sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText)
			this.sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject",
					includeSubclasses);
		if (!(StringUtils.isEmpty(whereClause))) {
			this.sql.setWhereClause(whereClause);
		}
		if (!(StringUtils.isEmpty(orderByClause))) {
			this.sql.setOrderByClause(orderByClause);
		}

		if (maxRecords != -1)
			this.sql.setMaxRecords(maxRecords);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause) {
		this.sql = new SearchSQL();
		String alias = "d";
		this.sql.setSelectList(alias + ".*");
		this.sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (!(StringUtils.isEmpty(whereClause))) {
			this.sql.setWhereClause(whereClause);
		}
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords) {
		this.sql = new SearchSQL();
		String alias = "d";
		this.sql.setSelectList(alias + ".*");
		this.sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (!(StringUtils.isEmpty(whereClause))) {
			this.sql.setWhereClause(whereClause);
		}
		if (!(StringUtils.isEmpty(orderByClause))) {
			this.sql.setOrderByClause(orderByClause);
		}

		if (maxRecords != -1)
			this.sql.setMaxRecords(maxRecords);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String selectClause, String className, boolean includeSubclasses, String whereClause, String orderByClause,
			int maxRecords) {
		this.sql = new SearchSQL();
		String alias = "d";
		this.sql.setSelectList(selectClause);
		this.sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (!(StringUtils.isEmpty(whereClause))) {
			this.sql.setWhereClause(whereClause);
		}
		if (!(StringUtils.isEmpty(orderByClause))) {
			this.sql.setOrderByClause(orderByClause);
		}

		if (maxRecords != -1)
			this.sql.setMaxRecords(maxRecords);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setObjectSql(String className, boolean includeSubclasses, String whereClause, String orderByClause, int maxRecords,
			boolean isFullText) {
		this.sql = new SearchSQL();
		String alias = "d";
		this.sql.setSelectList(alias + ".*");
		this.sql.setFromClauseInitialValue(className, alias, includeSubclasses);
		if (isFullText)
			this.sql.setFromClauseAdditionalJoin(JoinOperator.INNER, "ContentSearch", "cs", "d.This", JoinComparison.EQUAL, "cs.QueriedObject",
					includeSubclasses);
		if (!(StringUtils.isEmpty(whereClause))) {
			this.sql.setWhereClause(whereClause);
		}
		if (!(StringUtils.isEmpty(orderByClause))) {
			this.sql.setOrderByClause(orderByClause);
		}

		if (maxRecords != -1)
			this.sql.setMaxRecords(maxRecords);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("sql: " + this.sql.toString());
		}
		return this;
	}

	public Search setScope(ObjectStore os) {
		this.os = os;
		this.search = new SearchScope(os);
		PropertyFilter filter = new PropertyFilter();
		filter.setMaxRecursion(1);
		filter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
		return this;
	}

	public Search setMaxRecords(int maxRecords) {
		this.sql.setMaxRecords(maxRecords);
		return this;
	}

	public Search setPropertyFilter() {
		this.filter = new PropertyFilter();
		this.filter.setMaxRecursion(1);
		this.filter.addIncludeType(new FilterElement(null, null, null, FilteredPropertyType.ANY, null));
		return this;
	}

	public IndependentObjectSet fetchIndependentObjectSet() {
		return this.search.fetchObjects(this.sql, Integer.valueOf(this.pageSize), this.filter, Boolean.valueOf(this.continuable));
	}

	public List<Document> fetchObjects() {
		IndependentObjectSet objects = this.search.fetchObjects(this.sql, Integer.valueOf(this.pageSize), this.filter,
				Boolean.valueOf(this.continuable));
		List<Document> documents = new ArrayList<Document>();
		for (Document doc : EngineCollectionUtils.c(objects, Document.class)) {
			documents.add(doc);
		}
		return documents;
	}

	public List<Folder> fetchFolders() {
		IndependentObjectSet objects = this.search.fetchObjects(this.sql, Integer.valueOf(this.pageSize), this.filter,
				Boolean.valueOf(this.continuable));
		List<Folder> folders = new ArrayList<Folder>();
		for (Folder folder : EngineCollectionUtils.c(objects, Folder.class)) {
			folders.add(folder);
		}
		return folders;
	}

	public List<Document> fetchRows() {
		RepositoryRowSet rowSet = this.search.fetchRows(this.sql, Integer.valueOf(this.pageSize), this.filter, Boolean.valueOf(this.continuable));
		List<Document> documentList = new ArrayList<Document>();
		for (RepositoryRow row : EngineCollectionUtils.c(rowSet, RepositoryRow.class)) {
			Id docId = row.getProperties().get("Id").getIdValue();
			Document document = Factory.Document.fetchInstance(this.os, docId, null);
			documentList.add(document);
		}
		return documentList;
	}

	public List<RankVo> fetchRowsWithRank() {
		RepositoryRowSet rowSet = this.search.fetchRows(this.sql, Integer.valueOf(this.pageSize), this.filter, Boolean.valueOf(this.continuable));
		List<RankVo> documentList = new ArrayList<RankVo>();
		RankVo rankVo = null;
		for (RepositoryRow row : EngineCollectionUtils.c(rowSet, RepositoryRow.class)) {
			Id docId = row.getProperties().get("Id").getIdValue();
			Document document = Factory.Document.fetchInstance(this.os, docId, null);
			double rank = row.getProperties().getFloat64Value("Rank").doubleValue();
			rankVo = new RankVo();
			rankVo.setDocument(document);
			rankVo.setRank(rank);
			documentList.add(rankVo);
		}
		return documentList;
	}
}