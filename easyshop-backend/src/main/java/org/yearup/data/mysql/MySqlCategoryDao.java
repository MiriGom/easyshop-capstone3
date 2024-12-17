package org.yearup.data.mysql;

import org.apache.ibatis.executor.ExecutorException;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()) {
                int categoryId = resultSet.getInt( "category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                categoryList.add(new Category(categoryId, name, description));

            }
        }catch (SQLException e) {

        }
        return categoryList;
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        Category category= null;

        String name;
        String description;
        try(Connection connection = MySqlCategoryDao.getConnection()) {

            PreparedStatement ps = connection.prepareStatement("SELECT category_id, name, description FROM categories WHERE category_id = ?;");
            ps.setInt(1, categoryId);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("name");
                description = resultSet.getString("description");
                category = new Category(categoryId, name, description);
            }
        }catch (SQLException e) {

        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try (Connection connection = MySqlCategoryDao.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO categories (category_id, name, description,
                    VALUE(?,?,?);
                    """);
            ps.setInt(1, category.getCategoryId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try (Connection connection = MySqlCategoryDao.getConnection()){
            PreparedStatement ps = connection.prepareStatement("""
                    
                    """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}