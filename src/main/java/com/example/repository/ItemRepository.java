package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * itemsテーブルを操作するリポジトリ.
 * 
 * @author kenta_ichiyoshi
 *
 */
@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Item> ITEM_ROW_MAPPER = new BeanPropertyRowMapper<>(Item.class);
	/** itemsとcategoriesをjoinしたitemオブジェクトを生成するローマッパ */
	private static final RowMapper<Item> ITEM_JOIN_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setItemId(rs.getInt("i_item_id"));
		item.setName(rs.getString("i_name"));
		item.setCondition(rs.getInt("i_condition"));
		item.setCategoryId(rs.getInt("i_category_id"));
		item.setBrand(rs.getString("i_brand"));
		item.setPrice(rs.getDouble("i_price"));
		item.setShipping(rs.getInt("i_shipping"));
		item.setDescription(rs.getString("i_description"));
		return item;
	};

	/**
	 * 全件検索する(itemId昇順)
	 * 
	 * @return 全商品情報
	 */
	public List<Item> findAll(Integer offset) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT item_id,name,condition,category_id,brand,price,shipping,description");
		sql.append(" FROM items");
		sql.append(" ORDER BY item_id LIMIT 30 OFFSET :offset;");

		SqlParameterSource param = new MapSqlParameterSource().addValue("offset", offset);

		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);

		return itemList;
	}

	/**
	 * 全商品数をカウントする.
	 * 
	 * @return カウント数
	 */
	public Integer countItems() {
		String sql = "SELECT COUNT(*) FROM items";

		SqlParameterSource param = new MapSqlParameterSource();

		Integer count = template.queryForObject(sql, param, Integer.class);

		return count;
	}

	/**
	 * 商品を検索検索する.
	 * 
	 * @param name   検索したい商品名
	 * @param id     検索したいカテゴリ親id
	 * @param brand  検索したいブランド名
	 * @param offset offset
	 * @return 検索された商品
	 */
	public List<Item> findByForm(String name, Integer id, String brand, Integer offset) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT i.item_id i_item_id,i.name i_name,i.condition i_condition");
		sql.append(",i.category_id i_category_id,i.brand i_brand,i.price i_price");
		sql.append(",i.shipping i_shipping,i.description i_description");
		sql.append(" FROM treepaths t LEFT OUTER JOIN items i");
		sql.append(" ON t.child_id=i.category_id");
		sql.append(" WHERE i.name ILIKE :name");
		if (id != null) {
			sql.append(" AND t.parent_id = :id");

		}
		sql.append(" AND i.brand ILIKE :brand");
		sql.append(" GROUP BY i.item_id,i.name,i.condition,i.category_id,i.brand,i.price,i.shipping,i.description");
		sql.append(" ORDER BY item_id LIMIT 30 OFFSET :offset");

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%").addValue("id", id)
				.addValue("brand", "%" + brand + "%").addValue("offset", offset);

		List<Item> itemList = template.query(sql.toString(), param, ITEM_JOIN_ROW_MAPPER);

		return itemList;
	}

	/**
	 * 検索結果の商品数をカウントする.
	 * 
	 * @param name  検索したい商品名
	 * @param id    検索したいカテゴリ親id
	 * @param brand 検索したいブランド名
	 * @return 検索結果数
	 */
	public Integer countByForm(String name, Integer id, String brand) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM treepaths AS t");
		sql.append(" LEFT OUTER JOIN items AS i");
		sql.append(" ON t.child_id = i.category_id");
		sql.append(" LEFT OUTER JOIN categories AS c");
		sql.append(" ON i.category_id = c.category_id");
		sql.append(" WHERE i.name ILIKE :name AND i.brand ILIKE :brand AND");
		if (id == null) {
			sql.append(" t.parent_id IN (SELECT MIN(parent_id) FROM treepaths");
			sql.append(" GROUP BY child_id)");
		} else {
			sql.append(" t.parent_id = :id");
		}

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%")
				.addValue("brand", "%" + brand + "%").addValue("id", id);

		Integer count = template.queryForObject(sql.toString(), param, Integer.class);

		return count;
	}

	/**
	 * 主キー検索する.
	 * 
	 * @param itemId 商品id
	 * @return 商品詳細情報
	 */
	public Item load(Integer itemId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT item_id,name,condition,category_id,brand,price,shipping,description");
		sql.append(" FROM items WHERE item_id = :itemId");

		SqlParameterSource param = new MapSqlParameterSource("itemId", itemId);

		return template.queryForObject(sql.toString(), param, ITEM_ROW_MAPPER);
	}

	/**
	 * 現商品登録数を確認する.
	 * 
	 * @return DBに登録されている商品数
	 */
	public Integer checkItemId() {
		String sql = "SELECT MAX(item_id) from items;";

		SqlParameterSource param = new MapSqlParameterSource();

		Integer itemId = template.queryForObject(sql, param, Integer.class);

		return itemId;
	}

	/**
	 * 商品を追加する.
	 * 
	 * @param item 追加されたitem情報
	 */
	public synchronized void insertItem(Item item) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERt INTO items (item_id,name,condition,category_id,brand,price,shipping,description)");
		sql.append(" VALUES (:itemId,:name,:condition,:categoryId,:brand,:price,:shipping,:description)");

		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", item.getItemId())
				.addValue("name", item.getName()).addValue("condition", item.getCondition())
				.addValue("categoryId", item.getCategoryId()).addValue("brand", item.getBrand())
				.addValue("price", item.getPrice()).addValue("shipping", item.getShipping())
				.addValue("description", item.getDescription());

		template.update(sql.toString(), param);

	}

	/**
	 * 商品情報を更新する.
	 * 
	 * @param item 更新されたitem情報
	 */
	public void updateItem(Item item) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE items SET name=:name,condition=:condition,category_id=:categoryId");
		sql.append(",brand=:brand,price=:price,shipping=:shipping,description=:description");
		sql.append(" WHERE item_id = :itemId");

		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", item.getItemId())
				.addValue("name", item.getName()).addValue("condition", item.getCondition())
				.addValue("categoryId", item.getCategoryId()).addValue("brand", item.getBrand())
				.addValue("price", item.getPrice()).addValue("shipping", item.getShipping())
				.addValue("description", item.getDescription());

		template.update(sql.toString(), param);
	}

	/** indexを作成. */
	public void createIndexForItemId() {
		String sql = "CREATE UNIQUE INDEX idx_item_id ON items(item_id)";
		template.update(sql, new MapSqlParameterSource());
	}

	/** indexを削除. */
	public void deleteIndexForItemId() {
		String sql = "DROP INDEX IF EXISTS idx_item_id";
		template.update(sql, new MapSqlParameterSource());

	}

}
