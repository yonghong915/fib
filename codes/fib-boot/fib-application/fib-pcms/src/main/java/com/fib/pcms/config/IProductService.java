package com.fib.pcms.config;

import java.util.List;

public interface IProductService {
	void getProduct();

	List listProducts();

	int countProducts();

	int saveProduct();

	int removeProduct();

	int updateProduct();
}
