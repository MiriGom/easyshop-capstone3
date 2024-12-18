package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCart extends MySqlDaoBase implements ShoppingCartDao {
    public MySqlShoppingCart(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public ShoppingCart getByUserId(int userId) {
        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    SELECT *
                    FROM shopping_cart
                    WHERE user_id = ?;"
                    """);
            ps.setInt(1, userId);
            ResultSet row = ps.executeQuery();
            if (row.next()) {
                return mapRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void addProductToCart(int userId, int productId) {
        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    INSERT INTO shopping_cart (user_id, product_id)
                    VALUES(?, ?);
                    """);
            ps.setInt(1,userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean updateProductQuantity(int userId, int productId, int quantity) {
        try(Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement("""
                    UPDATE shopping_cart
                    SET quantity = ?
                    WHERE user_id = ?
                    AND product id = ?;
                    """);
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean clearCart(int userId) {
        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement("""
                    DELETE FROM shopping_cart
                    WHERE user_id = ?
                    """);
            ps.setInt(1, userId);
            int rowsdeleted = ps.executeUpdate();
            return rowsdeleted > 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private ShoppingCart mapRow(ResultSet row) throws SQLException
    {
        ShoppingCart cart = new ShoppingCart();
        return cart;
    }
}
