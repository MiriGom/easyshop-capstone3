package org.yearup.data.mysql;

import org.apache.ibatis.executor.ExecutorException;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categoryList = new ArrayList<>();
        //InteliJ recommended to make ".getConnection()" static
        try(Connection connection = MySqlCategoryDao.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM categories;");
            ps.executeQuery();

            ResultSet row = ps.getResultSet();
            while (row.next()) {
                Category category = mapRow(row);
                categoryList.add(category);
                /* Miriam's version of above code
                int categoryId = row.getInt( "category_id");
                String name = row.getString("name");
                String description = row.getString("description");
                categoryList.add(new Category(categoryId, name, description));
                */
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id


        String name;
        String description;
        try(Connection connection = MySqlCategoryDao.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("SELECT category_id, name, description FROM categories WHERE category_id = ?;");
            ps.setInt(1, categoryId);

            ResultSet row = ps.executeQuery();

            if(row.next()){
                Category category = mapRow(row);
                return category;
            }
            /*
            while (row.next()) {
                name = row.getString("name");
                description = row.getString("description");
                category = new Category(categoryId, name, description);
            }

             */
        }catch (SQLException e) {

        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try (Connection connection = MySqlCategoryDao.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO categories (name, description)
                    VALUE(?,?);
                    """, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int row = rs.getInt(1);
            category.setCategoryId(row);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try (Connection connection = MySqlCategoryDao.getConnection()){
            PreparedStatement ps = connection.prepareStatement("""
                    UPDATE categories
                    SET category_id = ?, name = ?, description = ?;
                    WHERE category_id = ?;
                    """);
            ps.setInt(1, category.getCategoryId());
            ps.setString(1, category.getName());
            ps.setString(3, category.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try (Connection connection = MySqlCategoryDao.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    DELETE
                    FROM categories
                    WHERE category_id = ?;
                    """);
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");
/*
        Category category = new Category();

           category.setCategoryId(categoryId);
           category.setName(name);
           category.setDescription(description);
*/
        return new Category(categoryId, name, description);
    }

}
