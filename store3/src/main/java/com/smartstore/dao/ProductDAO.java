package com.smartstore.dao;

import com.smartstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    public void save(Product p) {
        String sql = "INSERT INTO products(name, category, brand, price, cost_price, stock, battery, camera, storage, rating, shelf_life_days, reorder_level, min_order_qty, last_stock_in) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                p.getName(),
                p.getCategory(),
                p.getBrand(),
                p.getPrice(),
                p.getCostPrice() != null ? p.getCostPrice() : 0,
                p.getStock(),
                p.getBattery(),
                p.getCamera(),
                p.getStorage(),
                p.getRating(),
                p.getShelfLifeDays() != null ? p.getShelfLifeDays() : 365,
                p.getReorderLevel() != null ? p.getReorderLevel() : 5,
                p.getMinOrderQty() != null ? p.getMinOrderQty() : 1,
                p.getLastStockIn());
    }

    public void update(Product p) {
        String sql = "UPDATE products SET name=?, category=?, brand=?, price=?, cost_price=?, stock=?, battery=?, camera=?, storage=?, rating=?, shelf_life_days=?, reorder_level=?, min_order_qty=?, last_stock_in=?, updated_at=NOW() " +
                     "WHERE id=?";
        jdbcTemplate.update(sql,
                p.getName(),
                p.getCategory(),
                p.getBrand(),
                p.getPrice(),
                p.getCostPrice() != null ? p.getCostPrice() : 0,
                p.getStock(),
                p.getBattery(),
                p.getCamera(),
                p.getStorage(),
                p.getRating(),
                p.getShelfLifeDays() != null ? p.getShelfLifeDays() : 365,
                p.getReorderLevel() != null ? p.getReorderLevel() : 5,
                p.getMinOrderQty() != null ? p.getMinOrderQty() : 1,
                p.getLastStockIn(),
                p.getId());
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM products WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public void updateStock(Integer id, Integer stock) {
        String sql = "UPDATE products SET stock=?, last_stock_in=NOW() WHERE id=?";
        jdbcTemplate.update(sql, stock, id);
    }

    public Product findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id=?";
        List<Product> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public Integer countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer countLowStock(int threshold) {
        String sql = "SELECT COUNT(*) FROM products WHERE stock < ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, threshold);
    }

    public List<Map<String, Object>> getProductWiseSalesComparison(String startDate, String endDate) {
        String sql = "SELECT p.id, p.name, p.category, SUM(o.quantity) as total_quantity, SUM(o.total) as total_revenue, COUNT(o.id) as order_count " +
                     "FROM orders o JOIN products p ON o.product_id = p.id " +
                     "WHERE o.date BETWEEN ? AND ? " +
                     "GROUP BY p.id, p.name, p.category " +
                     "ORDER BY total_revenue DESC";
        return jdbcTemplate.queryForList(sql, startDate + " 00:00:00", endDate + " 23:59:59");
    }

    public List<Map<String, Object>> getCategoryWiseSalesHeatmap(String period) {
        String sql = "SELECT p.category, DATE_FORMAT(o.date, ?) as time_period, SUM(o.total) as revenue, SUM(o.quantity) as quantity " +
                     "FROM orders o JOIN products p ON o.product_id = p.id " +
                     "GROUP BY p.category, time_period " +
                     "ORDER BY time_period DESC, revenue DESC";
        return jdbcTemplate.queryForList(sql, period);
    }

    public List<Map<String, Object>> getInventoryAgingReport() {
        String sql = "SELECT id, name, category, stock, DATEDIFF(NOW(), last_stock_in) as days_in_stock, " +
                     "CASE WHEN DATEDIFF(NOW(), last_stock_in) > 90 THEN 'OLD' " +
                     "WHEN DATEDIFF(NOW(), last_stock_in) > 30 THEN 'AGING' " +
                     "ELSE 'FRESH' END as aging_status " +
                     "FROM products WHERE stock > 0 " +
                     "ORDER BY days_in_stock DESC";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getProfitabilityReport() {
        String sql = "SELECT p.id, p.name, p.price, p.cost_price, (p.price - p.cost_price) as profit_per_unit, " +
                     "ROUND(((p.price - p.cost_price) / p.price * 100), 2) as margin_percentage, " +
                     "SUM(o.quantity) as units_sold, " +
                     "SUM(o.total) as total_revenue, " +
                     "SUM(o.quantity) * (p.price - p.cost_price) as total_profit " +
                     "FROM products p LEFT JOIN orders o ON p.id = o.product_id " +
                     "GROUP BY p.id, p.name, p.price, p.cost_price " +
                     "ORDER BY total_profit DESC";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getExpiringStockReport(Integer daysThreshold) {
        String sql = "SELECT id, name, category, stock, shelf_life_days, last_stock_in, " +
                     "DATE_ADD(last_stock_in, INTERVAL shelf_life_days DAY) as expiry_date, " +
                     "DATEDIFF(DATE_ADD(last_stock_in, INTERVAL shelf_life_days DAY), NOW()) as days_until_expiry " +
                     "FROM products WHERE stock > 0 AND DATEDIFF(DATE_ADD(last_stock_in, INTERVAL shelf_life_days DAY), NOW()) < ? " +
                     "ORDER BY days_until_expiry ASC";
        return jdbcTemplate.queryForList(sql, daysThreshold);
    }

    public List<Map<String, Object>> getYesterdayVsTodaySales() {
        String sql = "SELECT DATE(date) as sale_date, SUM(total) as daily_revenue, COUNT(id) as order_count, " +
                     "SUM(quantity) as total_quantity " +
                     "FROM orders WHERE DATE(date) IN (CURDATE(), DATE_SUB(CURDATE(), INTERVAL 1 DAY)) " +
                     "GROUP BY DATE(date) ORDER BY sale_date DESC";
        return jdbcTemplate.queryForList(sql);
    }

    public Double getTurnoverValue() {
        String sql = "SELECT SUM(total) FROM orders WHERE date >= DATE_SUB(NOW(), INTERVAL 30 DAY)";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result != null ? result : 0.0;
    }

    public List<Map<String, Object>> getVolumeWiseSalesBreakdown() {
        String sql = "SELECT CASE WHEN quantity BETWEEN 1 AND 10 THEN '1-10 units' " +
                     "WHEN quantity BETWEEN 11 AND 50 THEN '11-50 units' " +
                     "WHEN quantity BETWEEN 51 AND 100 THEN '51-100 units' " +
                     "ELSE '100+ units' END as volume_category, " +
                     "COUNT(id) as order_count, SUM(quantity) as total_quantity, SUM(total) as revenue " +
                     "FROM orders GROUP BY volume_category ORDER BY volume_category";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Product> getProductsRequiringReorder() {
        String sql = "SELECT * FROM products WHERE stock <= reorder_level ORDER BY stock ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    public Double averageMovingForecast(Integer productId, Integer days) {
        String sql = "SELECT AVG(daily_sales) FROM ( " +
                     "SELECT SUM(quantity) as daily_sales FROM orders WHERE product_id = ? AND date >= DATE_SUB(NOW(), INTERVAL ? DAY) " +
                     "GROUP BY DATE(date) " +
                     ") as daily_avg";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, productId, days);
        return result != null ? result : 0.0;
    }
}
