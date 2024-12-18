package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);

    void addProductToCart(int userId, int productId);

    boolean updateProductQuantity(int userId, int productId, int quantity);

    boolean clearCart(int userId);
    // add additional method signatures here
}
