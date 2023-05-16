package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Category;


@Repository
public class CategoryRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Category>CATEGORY_ROW_MAPPER = (rs,i)->{
		Category category = new Category();
		category.setId(rs.getInt("category_id"));
		category.setName(rs.getString("name"));
		category.setDepth(rs.getInt("depth"));
		return category;
	};
	
	
	private static final RowMapper<Category>CATEGORY_JOIN_ROW_MAPPER = (rs,i)->{
		Category category = new Category();
		category.setId(rs.getInt("c_category_id"));
		category.setName(rs.getString("c_name"));
		category.setDepth(rs.getInt("c_depth"));
		return category;
	};

//	/**
//	 * カテゴリ総称を検索する.
//	 * 
//	 * @param nameAll カテゴリ総称(Men/Tops/T-shirts)
//	 * @return 検索されたカテゴリ情報
//	 */
//	public Category findByNameAll(String nameAll) {
//		String sql = "SELECT category_id,name,name_all,depth FROM categories WHERE name_all = :nameAll ;";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("nameAll", nameAll);
//		List<Category>categoryList = template.query(sql, param,CATEGORY_ROW_MAPPER);
//		if(categoryList.size() == 0) {
//			return null;
//		}
//		return categoryList.get(0);
//	}
	
	/**
	 * 階層でカテゴリ情報を検索する.
	 * 
	 * @param depth　階層 0:親カテゴリー　1:子カテゴリー　2:孫カテゴリー
	 * @return　階層に紐付くカテゴリ情報
	 */
	public List<Category>findByDepth(Integer depth){
		String sql ="SELECT category_id,name,depth FROM categories WHERE depth = :depth ORDER BY name;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("depth", depth);
		List<Category>categoryList = template.query(sql, param,CATEGORY_ROW_MAPPER);
		if(categoryList.size() == 0) {
			return null;
		}
		return categoryList;
	}
	
	/**
	 * 親子孫のカテゴリ名を検索する.
	 * 
	 * @param parentId 親id
	 * @param depth 階層
	 * @return　検索されたカテゴリ名
	 */
	public List<Category>findByParentIdAndDepth(Integer parentId,Integer depth){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c.category_id c_category_id,c.name c_name,c.depth c_depth");
		sql.append(" FROM treepaths t LEFT OUTER JOIN categories c");
		sql.append(" ON c.category_id=t.child_id WHERE t.parent_id = :parentId AND c.depth = :depth");
		sql.append(" GROUP BY c.category_id,c.name,c.depth");
		sql.append(" ORDER BY c.name;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId).addValue("depth", depth);
		List<Category>categoryList = template.query(sql.toString(), param,CATEGORY_JOIN_ROW_MAPPER);
		return categoryList;
	}
	public List<Category>pickUpParentIdAndDepth(Integer parentId,Integer depth){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT category_id,name,depth");
		sql.append(" FROM categories WHERE depth = :depth");
		sql.append(" AND category_id IN(SELECT child_id FROM treepaths WHERE parent_id = :parentId");
		sql.append(" ORDER BY name;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("parentId", parentId).addValue("depth", depth);
		List<Category>categoryList = template.query(sql.toString(), param,CATEGORY_ROW_MAPPER);
		return categoryList;
	}
	
	
	
	/**
	 * 子,孫カテゴリを検索する.
	 * 
	 * @param childId 子id
	 * @return 検索された子,孫カテゴリ名
	 */
	public List<Category>findByChildId(Integer childId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c.category_id c_category_id,c.name c_name,c.depth c_depth");
		sql.append(" FROM treepaths t LEFT OUTER JOIN categories c");
		sql.append(" ON c.category_id = t.parent_id WHERE t.child_id =:childId ");
		sql.append(" GROUP BY c.category_id,c.name,c.depth ");
		sql.append(" ORDER BY c.depth;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("childId", childId);
		List<Category>categoryList = template.query(sql.toString(), param,CATEGORY_JOIN_ROW_MAPPER);
		return categoryList;
		
	}

}
